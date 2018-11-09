package ru.otus.gromov.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.helper.UnProxyHelper;
import ru.otus.gromov.service.DBService;
import ru.otus.gromov.service.DBServiceHibernateImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Function;

public class UserServlet extends HttpServlet {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private DBService service;
	private ObjectMapper objectMapper;

	public UserServlet() {
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		log.info("Init servlet {}", getClass());
		super.init(config);
		this.service = new DBServiceHibernateImpl();
		this.objectMapper = new ObjectMapper();
	}

	public void doGet(HttpServletRequest request,
	                  HttpServletResponse response) throws IOException {
		if (checkPath(request, response)) return;
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=utf-8");
		try {
			if (request.getParameter("id") == null || request.getParameter("id").isBlank()) {
				response.getWriter().println(
						objectMapper.writeValueAsString(
								UnProxyHelper.get(
										service.readAll())));
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				long userId = Long.parseLong(request.getParameter("id"));
				response.getWriter().println(
						objectMapper.writeValueAsString(
								UnProxyHelper.get(
										service.read(userId))));
				response.setStatus(HttpServletResponse.SC_OK);
			}
		} catch (Exception e) {
			response.getWriter().println(e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
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
			service.save(user);
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
				service.remove(id);
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
			if (req.getParameter("id") == null || req.getParameter("id").isBlank()) {
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
		if (!"/rest/admin/".equals(req.getRequestURI())) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return true;
		}
		return false;
	}


}
