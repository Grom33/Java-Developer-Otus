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
		log.info("Request {}, uri: {}", request.getMethod(), request.getRequestURI());
		response.addHeader("Access-Control-Allow-Origin", "*");
		String[] path = request.getRequestURI().split("/");
		response.setContentType("application/json;charset=utf-8");
		try {
			switch (path.length) {
				case 3:
					response.getWriter().println(
							objectMapper.writeValueAsString(
									UnProxyHelper.get(
											service.readAll())));
					response.setStatus(HttpServletResponse.SC_OK);
					break;
				case 4:
					response.getWriter().println(
							objectMapper.writeValueAsString(
									UnProxyHelper.get(
											service.read(Long.parseLong(path[3])))));
					response.setStatus(HttpServletResponse.SC_OK);
					break;
				default:
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (Exception e) {
			response.getWriter().println(e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.info("Request {}, uri: {}", req.getMethod(), req.getRequestURI());
		processRequest(req, resp, 3, (path) -> {
			UserDataSet user = null;
			try {
				user = objectMapper.readValue(
						req.getParameter("user"), UserDataSet.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			service.save(user);
			resp.setStatus(HttpServletResponse.SC_OK);
			return null;
		});
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.info("Request {},  uri: {}", req.getMethod(), req.getRequestURI());
		processRequest(req, resp, 4, (path) -> {
			service.remove(Long.parseLong(path[3]));
			resp.setStatus(HttpServletResponse.SC_OK);
			return null;
		});
	}

	private void processRequest(HttpServletRequest req,
	                            HttpServletResponse resp,
	                            int pathLevel,
	                            Function<String[], Void> function) throws IOException {
		log.info("Process request {} req {}", req.getMethod(), req.getRequestURI());
		String[] path = req.getRequestURI().split("/");
		resp.setContentType("application/json;charset=utf-8");
		try {
			if (path.length == pathLevel) {
				function.apply(path);
			} else {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.getWriter().println(e);
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}


}
