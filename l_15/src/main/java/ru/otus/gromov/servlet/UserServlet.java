package ru.otus.gromov.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.helper.UnProxyHelper;
import ru.otus.gromov.messageSystem.Address;
import ru.otus.gromov.messageSystem.MessageSystem;
import ru.otus.gromov.messageSystem.messages.MsgDeleteUser;
import ru.otus.gromov.messageSystem.messages.MsgGetAllUsers;
import ru.otus.gromov.messageSystem.messages.MsgGetUser;
import ru.otus.gromov.messageSystem.MessageSystemContext;
import ru.otus.gromov.messageSystem.messages.MsgPutUser;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

@Slf4j
public class UserServlet extends HttpServlet implements FrontendService {

	private ObjectMapper objectMapper;

	@Autowired
	@Qualifier("frontEndAdress")
	private Address address;

	@Autowired
	private MessageSystemContext context;

	private final long TIME_OUT = 3000;
	private final Map<Long, Object> answers = new WeakHashMap<>();
	private final AtomicLong ticket = new AtomicLong();

	private final Object synch = new Object();

	@Override
	public void init(ServletConfig config) {
		log.info("Init servlet {}", getClass());
		objectMapper = new ObjectMapper();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		context.getMessageSystem().addAddressee(this);
		context.getMessageSystem().start();
	}

	public void doGet(HttpServletRequest request,
	                  HttpServletResponse response) throws IOException {
		if (checkPath(request, response)) return;
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		long answerIndex = getTicket();

		try {
			if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
				log.info("get all users");
				context.getMessageSystem().sendMessage(
						new MsgGetAllUsers(getAddress(), context.getDbAddress(), answerIndex));
			} else {

				long userId = Long.parseLong(request.getParameter("id"));
				log.info("get user with id:{}", userId);
				context.getMessageSystem().sendMessage(
						new MsgGetUser(getAddress(), context.getDbAddress(), answerIndex, userId));
			}
		} catch (Exception e) {
			response.getWriter().println(e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		long timeToStop = System.currentTimeMillis() + TIME_OUT;
		try {
			synchronized (synch) {
				while (!answers.containsKey(answerIndex)) {
					synch.wait();
				}
			}

			Object answer = answers.get(answerIndex);
			log.info("get answer {}", answer);
			response.getWriter().println(
					objectMapper.writeValueAsString(
							UnProxyHelper.get(answer)));
			answers.remove(answerIndex);
			response.setStatus(HttpServletResponse.SC_OK);
			return;

		} catch (InterruptedException e) {
			log.error(e.toString());
		}

		response.getWriter().println("Time out!");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (checkPath(req, resp)) return;
		processRequest(req, resp, (id) -> {
			UserDataSet user = null;
			try {
				user = objectMapper.readValue(
						req.getParameter("user"), UserDataSet.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			context.getMessageSystem().sendMessage(
					new MsgPutUser(getAddress(), context.getDbAddress(), 0, user));
			resp.setStatus(HttpServletResponse.SC_CREATED);
			return null;
		});
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.info("Request {},  uri: {}", req.getMethod(), req.getRequestURI());
		processRequest(req, resp, (id) -> {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			if (id != 0L) {
				context.getMessageSystem().sendMessage(
						new MsgDeleteUser(getAddress(), context.getDbAddress(), 0, id));
				resp.setStatus(HttpServletResponse.SC_OK);
			}
			return null;
		});
	}

	private void processRequest(HttpServletRequest req,
	                            HttpServletResponse resp,
	                            Function<Long, Void> function) throws IOException {
		log.info("Process request {} req {}", req.getMethod(), req.getRequestURI());
		resp.setContentType("application/json;charset=utf-8");
		try {
			if (req.getParameter("id") == null || req.getParameter("id").isEmpty()) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				function.apply(0L);
			} else {
				long userId = Long.parseLong(req.getParameter("id"));
				function.apply(userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.getWriter().println(e);
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private boolean checkPath(HttpServletRequest req, HttpServletResponse resp) {
		log.info("Request {}, uri: {}", req.getMethod(), req.getRequestURI());
		if (!"/MyApp/rest/admin/".equals(req.getRequestURI())) {
			log.debug("!!!!! " + req.getRequestURI());
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return true;
		}
		return false;
	}


	@Override
	public Address getAddress() {
		return address;
	}

	@Override
	public MessageSystem getMS() {
		return context.getMessageSystem();
	}

	private long getTicket() {
		ticket.compareAndSet(Long.MAX_VALUE, 1L);
		return ticket.incrementAndGet();
	}


	@Override
	public void addAnswer(long index, Object answer) {
		synchronized (synch) {
			log.info("Answer is back");
			answers.put(index, answer);
			synch.notify();
		}
	}
}
