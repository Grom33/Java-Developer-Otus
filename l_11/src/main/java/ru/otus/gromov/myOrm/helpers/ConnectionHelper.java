package ru.otus.gromov.myOrm.helpers;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class ConnectionHelper implements AutoCloseable {

	private final String user;
	private final String password;
	private Connection connection;

	public ConnectionHelper(String user, String password) {
		this.user = user;
		this.password = password;
	}

	private void initConnection() {
		try {
			Driver driver = (Driver) Class.forName("org.h2.Driver").getConstructor().newInstance();
			DriverManager.registerDriver(driver);

			String url = "jdbc:h2:" +               //db type
					"mem:" +                        //host name
					//"3306/" +                     //port
					"test2?" +                       //db name
					"user=" + user + "&" +          //login
					"password=" + password + "&" +  //password
					"useSSL=false;"; // +               //do not use Secure Sockets Layer
			//"DB_CLOSE_DELAY=-1";            //Keep data in memory after close connection

			connection = DriverManager.getConnection(url);
		} catch (SQLException |
				InstantiationException |
				InvocationTargetException |
				NoSuchMethodException |
				IllegalAccessException |
				ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) initConnection();
		return connection;
	}

	@Override
	public void close() throws Exception {
		connection.close();
	}
}
