package ru.otus.gromov;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.gromov.servlet.UserServlet;

public class Application {
	private final static Logger log = LoggerFactory.getLogger(Application.class);
	private final static int PORT = 8090;
	private final static String PUBLIC_HTML = "frontend/public";
	private final static String REST_URL = "/rest/admin/*";

	public static void main(String[] args) throws Exception {
		log.info("Init server");
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setResourceBase(PUBLIC_HTML);
		log.info("Init server resource path: {}", PUBLIC_HTML);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.addServlet(UserServlet.class, REST_URL);
		log.info("Init server, servlet{} path: {}", UserServlet.class.getSimpleName(), REST_URL);
		Server server = new Server(PORT);
		server.setHandler(new HandlerList(resourceHandler, context));
		log.info("Init server, server port: {}", PORT);
		server.start();
		server.join();
	}
}
