package ru.otus.gromov.myOrm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.gromov.myOrm.exception.DBIsNotInstantiatedException;
import ru.otus.gromov.myOrm.session.MyORMStatus;
import ru.otus.gromov.myOrm.session.MySession;
import ru.otus.gromov.myOrm.sql.ConnectionHelper;
import ru.otus.gromov.myOrm.sql.SQLHelper;

import java.sql.*;


public class MyOrm {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private Connection connection;
	private SQLHelper sqlHelper;
	private MyORMStatus status;

	public MyOrm(ConnectionHelper connectionHelper) throws SQLException {
		connection = connectionHelper.getConnection();
		sqlHelper = new SQLHelper(connectionHelper);
		status = MyORMStatus.NOT_ACTIVE;
	}


	public boolean isClosed(){
		boolean result = true;
		try {
			result = connection.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}



	public MySession getSession() {

		return new MySession(this);
	}

	public String getStatus(){
		return status.toString();
	}

	public Connection getConnection() {
		return connection;
	}

	public SQLHelper getSqlHelper() {
		return sqlHelper;
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
