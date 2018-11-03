package ru.otus.gromov.servlet;

import com.google.gson.Gson;
import ru.otus.gromov.base.dataSets.AdressDataSet;
import ru.otus.gromov.base.dataSets.PhoneDataSet;
import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.helper.JsonHelper;
import ru.otus.gromov.service.DBService;
import ru.otus.gromov.service.DBServiceHibernateImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class UserServlet extends HttpServlet {
	private DBService service;
	private Gson gson;

	public UserServlet() {
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.service = new DBServiceHibernateImpl();
		this.gson = new Gson();
		UserDataSet TEST_DATASET_1 = new UserDataSet();
		UserDataSet TEST_DATASET_2 = new UserDataSet();
		UserDataSet TEST_DATASET_3 = new UserDataSet();
		TEST_DATASET_1.setName("Ivan Ivanovich");
		TEST_DATASET_1.setAdress(new AdressDataSet("Moscow, street street"));
		TEST_DATASET_1.setPhones(Collections.singletonList(new PhoneDataSet("903-940-59-34", TEST_DATASET_1)));
		service.save(TEST_DATASET_1);
		TEST_DATASET_2.setName("Petr Petrovich");
		TEST_DATASET_2.setAdress(new AdressDataSet("SPB, street street"));
		TEST_DATASET_2.setPhones(Collections.singletonList(new PhoneDataSet("911-220-59-34", TEST_DATASET_2)));
		service.save(TEST_DATASET_2);
		TEST_DATASET_3.setName("Sidr Sidorovich");
		TEST_DATASET_3.setAdress(new AdressDataSet("Minsk, street street"));
		TEST_DATASET_3.setPhones(Collections.singletonList(new PhoneDataSet("999-970-59-34", TEST_DATASET_3)));
		service.save(TEST_DATASET_3);
	}

	public void doGet(HttpServletRequest request,
	                  HttpServletResponse response) throws ServletException, IOException {
		String[] path = request.getRequestURI().split("/");
		response.setContentType("application/json;charset=utf-8");
		try {
			switch (path.length) {
				case 3:
					response.getWriter().println(JsonHelper.initializeAndUnproxy(service.readAll()));
					response.setStatus(HttpServletResponse.SC_OK);
					break;
				case 4:
					response.getWriter().println(JsonHelper.initializeAndUnproxy(service.read(Long.parseLong(path[3]))));
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
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//super.doPost(req, resp);
		String[] path = req.getRequestURI().split("/");
		resp.setContentType("application/json;charset=utf-8");
		try {
			if (path.length == 3) {
				System.out.println("!!!!"+req.getInputStream());
				//service.save(gson.fromJson(req.getParameter("user"), UserDataSet.class));
				resp.setStatus(HttpServletResponse.SC_OK);
			} else {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (Exception e) {
			resp.getWriter().println(e);
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doDelete(req, resp);
	}

}
