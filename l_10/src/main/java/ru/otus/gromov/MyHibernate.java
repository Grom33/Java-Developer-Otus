package ru.otus.gromov;

import ru.otus.gromov.domain.DataSet;
import ru.otus.gromov.exception.WrongTypeException;
import ru.otus.gromov.sql.SQLHelper;
import ru.otus.gromov.sql.SqlTransaction;

import java.sql.*;

import static ru.otus.gromov.sql.SQLHelper.buildQuery;

public class MyHibernate implements Executor {
	private Connection connection;

	public MyHibernate(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void save(Object object) {
		checkObjectType(object.getClass());
		saveObject(object);
	}

	@Override
	public Object load(long id, Class clazz) {
		checkObjectType(clazz);

		return null;
	}

	public void saveObject(Object object) {
		SQLHelper.transactionalExecute(
				connection -> {
					SQLHelper.doQuery(buildQuery(object), connection);
					return null;
				}
		);
	}

	public Object loadObject(long id, Class clazz) {
		/*String query = String.format("SELECT * FROM %s WHERE id=%s", clazz.getSimpleName(), id);
		System.out.println(query);
		ResultSet rs = execute(query);
		System.out.println(rs);*/

		SQLHelper.transactionalExecute(
				connection -> {
					try (PreparedStatement ps = connection.prepareStatement(
							String.format("SELECT * FROM %s WHERE id=%s",
									clazz.getSimpleName(),
									id))) {
						ps.setLong(1, id);
						ResultSet rs = ps.executeQuery();
						if (!rs.next()) {
							throw new RuntimeException("UPS");
						}
						System.out.println(rs.getString("name"));
					}
					return null;
				}
		);
		return null;
	}


	private ResultSet execute(String query) {
		try (Statement stmt = connection.createStatement()) {
			stmt.execute(query);
			ResultSet rs = stmt.getResultSet();
			System.out.println("!!!" + rs);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void checkObjectType(Class clazz) {
		if (!DataSet.class.isAssignableFrom(clazz)) throw new WrongTypeException();
	}

	public MyHibernate() {
	}
}
