package ru.otus.gromov.myOrm.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.gromov.myOrm.domain.DataSet;
import ru.otus.gromov.myOrm.MyOrm;
import ru.otus.gromov.myOrm.exception.DBIsNotInstantiatedException;
import ru.otus.gromov.myOrm.exception.InstantiateEntityException;
import ru.otus.gromov.myOrm.exception.NotFoundException;
import ru.otus.gromov.myOrm.exception.WrongTypeOfEntityException;
import ru.otus.gromov.myOrm.reflection.ReflectionHelper;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MySession implements AutoCloseable {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final MyOrm myOrm;
	private MyORMStatus status;

	public MySession(MyOrm myOrm) {
		this.myOrm = myOrm;
	}

	@Override
	public void close() {
		status = MyORMStatus.NOT_ACTIVE;
	}
	private void checkInitDB() {
		log.info("Check Helpers to initialised");
		if (myOrm.isClosed() || myOrm.getSqlHelper() == null)
			throw new DBIsNotInstantiatedException();
	}

	public void save(Object object) {
		log.info("Save object: {}", object);
		checkObjectType(object.getClass());
		checkInitDB();
		saveObject(object);
	}

	public Object load(long id, Class clazz) {
		log.info("Load object: id:{}, Class:{}", id, clazz);
		checkObjectType(clazz);
		checkInitDB();
		return loadObject(id, clazz);
	}

	public <T> List<T> loadAll(Class<T> Clazz){
		return null;
	}

	public void saveObject(Object object) {
		log.info("Try to save use SQL Helper: Object:{}", object);
		myOrm.getSqlHelper().transactionalExecute(
				connection -> {
					try (PreparedStatement ps = connection.prepareStatement(
							myOrm.getSqlHelper().buildQuery(object), Statement.RETURN_GENERATED_KEYS)) {
						ps.executeUpdate();
					}
					return null;
				}
		);
	}

	public <T> T loadObject(long id, Class<T> clazz) {
		log.info("Try to load object with SQLHelper, id {}, Class {}", id, clazz);
		return myOrm.getSqlHelper().transactionalExecute(
				connection -> {
					T result;
					String sqlQuery = String.format(
							"SELECT * FROM %s r WHERE r.id = ?",
							clazz.getSimpleName().toUpperCase());
					log.info("(Method loadObject) SQL query string: {}", sqlQuery);
					try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
						ps.setString(1, String.valueOf(id));
						ResultSet rs = ps.executeQuery();
						if (!rs.next()) {
							throw new NotFoundException(id);
						}
						result = buildObject(rs, clazz);
					}
					return result;
				}
		);
	}

	private <T> T buildObject(ResultSet rs, Class<T> clazz) {
		log.info("Try to build object, Class {}", clazz);
		T result = ReflectionHelper.instantiate(clazz);
		if (result == null) throw new InstantiateEntityException();
		List<Field> fields = ReflectionHelper.getFields(result);

		fields.forEach(field -> {
			try {
				ReflectionHelper.setFieldValueByField(result, field, rs.getObject(field.getName()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		log.info("Built object: {}", result);
		return result;
	}


	private void checkObjectType(Class clazz) {
		if (!DataSet.class.isAssignableFrom(clazz)) throw new WrongTypeOfEntityException();
	}
}
