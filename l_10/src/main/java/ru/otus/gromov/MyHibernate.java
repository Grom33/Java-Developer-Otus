package ru.otus.gromov;

import ru.otus.gromov.domain.DataSet;
import ru.otus.gromov.exception.WrongTypeException;
import ru.otus.gromov.reflection.ReflectionHelper;
import ru.otus.gromov.sql.ConnectionHelper;
import ru.otus.gromov.sql.SQLHelper;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;

import static ru.otus.gromov.sql.SQLHelper.buildQuery;

public class MyHibernate implements Executor {

	public MyHibernate() {
	}

	@Override
	public void save(Object object) {
		checkObjectType(object.getClass());
		saveObject(object);
	}

	@Override
	public Object load(long id, Class clazz) {
		checkObjectType(clazz);
		return loadObject(id, clazz);
	}

	public void saveObject(Object object) {
		SQLHelper.transactionalExecute(
				connection -> {
					try (PreparedStatement ps = connection.prepareStatement(
							buildQuery(object))) {
						System.out.println(buildQuery(object));
						System.out.println(ps.execute());
					}
					return null;
				},
				ConnectionHelper.getConnection("sa", "")
		);
	}

	public <T> T loadObject(long id, Class<T> clazz) {
		return SQLHelper.transactionalExecute(
				connection -> {
					T result;
					try (PreparedStatement ps = connection.prepareStatement(
							String.format("SELECT * FROM %s r WHERE r.id = ?",
									clazz.getSimpleName().toUpperCase()))) {
						ps.setString(1, String.valueOf(id));
						ResultSet rs = ps.executeQuery();
						if (!rs.next()) {
							throw new RuntimeException("UPS");
						}
						result = buildObject(rs, clazz);
					}
					return result;
				},
				ConnectionHelper.getConnection("sa", "")
		);
	}

	private <T> T buildObject(ResultSet rs, Class<T> clazz) {
		T result = ReflectionHelper.instantiate(clazz);
		if (result == null) throw new RuntimeException("Can't instantiate object!");
		List<Field> fields = ReflectionHelper.getFields(result);

		fields.forEach(field -> {
			try {
				ReflectionHelper.setFieldValueByField(result, field, rs.getObject(field.getName()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		return result;
	}


	private void checkObjectType(Class clazz) {
		if (!DataSet.class.isAssignableFrom(clazz)) throw new WrongTypeException();
	}
}
