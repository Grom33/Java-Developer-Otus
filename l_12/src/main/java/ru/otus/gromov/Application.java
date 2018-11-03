package ru.otus.gromov;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.gromov.servlet.UserServlet;

public class Application {

	private final static int PORT = 8090;
	private final static String PUBLIC_HTML = "public";

	public static void main(String[] args) throws Exception {
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setResourceBase(PUBLIC_HTML);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.addServlet(UserServlet.class, "/rest/admin/*");

		Server server = new Server(PORT);
		server.setHandler(new HandlerList(resourceHandler, context));

		server.start();
		server.join();
	}
}
