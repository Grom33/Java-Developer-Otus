package ru.otus.gromov;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.security.Constraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.gromov.servlet.UserServlet;

import java.util.Collections;

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
		log.info("Init server, server port: {}", PORT);
		log.info("Init security...");
		LoginService loginService = new HashLoginService("Cred",
				"src/test/resources/cred.properties");
		server.addBean(loginService);
		ConstraintSecurityHandler security = new ConstraintSecurityHandler();
		server.setHandler(security);

		Constraint constraint = new Constraint();
		constraint.setName("auth");
		constraint.setAuthenticate(true);
		constraint.setRoles(new String[]{"user", "admin"});

		ConstraintMapping mapping = new ConstraintMapping();
		mapping.setPathSpec("/*");
		mapping.setConstraint(constraint);

		security.setConstraintMappings(Collections.singletonList(mapping));
		security.setAuthenticator(new BasicAuthenticator());
		security.setLoginService(loginService);

		HandlerList myHandler = new HandlerList(resourceHandler, context);
		security.setHandler(myHandler);
		log.info("Start server...");
		server.start();
		server.join();
	}
}
