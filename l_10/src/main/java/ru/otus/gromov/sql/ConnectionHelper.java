package ru.otus.gromov.sql;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class ConnectionHelper {

	private final String user;
	private final String password;

	public ConnectionHelper(String user, String password) {
		this.user = user;
		this.password = password;
	}

	public Connection getConnection() {
		try {
			Driver driver = (Driver) Class.forName("org.h2.Driver").getConstructor().newInstance();
			DriverManager.registerDriver(driver);

			String url = "jdbc:h2:" +               //db type
					"mem:" +                        //host name
					//"3306/" +                     //port
					"test?" +                       //db name
					"user=" + user + "&" +          //login
					"password=" + password + "&" +  //password
					"useSSL=false;" +               //do not use Secure Sockets Layer
					"DB_CLOSE_DELAY=-1";            //Keep data in memory after close connection

			return DriverManager.getConnection(url);
		} catch (SQLException |
				InstantiationException |
				InvocationTargetException |
				NoSuchMethodException |
				IllegalAccessException |
				ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
