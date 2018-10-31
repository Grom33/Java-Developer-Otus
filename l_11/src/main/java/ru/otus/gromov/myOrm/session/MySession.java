package ru.otus.gromov.myOrm.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.gromov.base.dataSets.DataSet;
import ru.otus.gromov.myOrm.MyOrm;
import ru.otus.gromov.myOrm.exception.DBIsNotInstantiatedException;
import ru.otus.gromov.myOrm.exception.NotFoundException;
import ru.otus.gromov.myOrm.exception.WrongTypeOfEntityException;
import ru.otus.gromov.myOrm.helpers.ObjectHelper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.*;

public class MySession implements AutoCloseable {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final MyOrm myOrm;
	private MyORMStatus status;
	private Connection connection;

	public MySession(MyOrm myOrm) {
		this.myOrm = myOrm;
		connection = myOrm.getConnection();

	}

	public MyORMStatus getStatus() {
		return status;
	}

	@Override
	public void close() {
		status = MyORMStatus.NOT_ACTIVE;
		myOrm.updateStatus(this);
	}

	private void checkInitDB() {
		log.info("Check DB connection");
		if (myOrm.isClosed()) throw new DBIsNotInstantiatedException();
	}

	public void save(Object object, Class clazz) {
		log.info("Save object: {}", object);
		checkObjectType(object.getClass());
		checkInitDB();
		saveObject(object);
	}

	public Object load(String name, Class clazz) {
		log.info("Load object: name:{}, Class:{}", name, clazz);
		long id = 0;
		try {
			id = getObjectIdByName(name, clazz);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return load(id, clazz);

	}

	private long getObjectIdByName(String name, Class clazz) throws SQLException {
		log.info("Try to get object id by name {}, Class {}", name, clazz);
		return transactionalExecute(
				connection -> {
					long result;
					try (PreparedStatement ps = connection.prepareStatement(String.format(
							"SELECT * FROM %s r WHERE r.NAME = ?",
							clazz.getSimpleName().toUpperCase()))) {
						ResultSet rs = execute(name, ps);
						result = rs.getLong("id");
					}
					return result;
				});
	}


	public Object load(long id, Class clazz) {
		log.info("Load object: id:{}, Class:{}", id, clazz);
		checkObjectType(clazz);
		checkInitDB();

		Object parentObject = null;
		try {
			parentObject = loadObject(id, clazz);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		List<Field> objectStructure = ObjectHelper.getListWithChildObject(clazz);
		if (objectStructure != null & objectStructure.size() > 0) {
			for (Field field : objectStructure) {
				try {
					Object loadedObj = null;
					log.info("Verify field {} is {} ", field.getType(), (Collection.class.isAssignableFrom(field.getType())));
					if (Collection.class.isAssignableFrom(field.getType())) {
						ParameterizedType objectInCollectionType = (ParameterizedType) field.getGenericType();
						Class<?> classOfObjectInCollection = (Class<?>) objectInCollectionType.getActualTypeArguments()[0];
						log.info("!!! try  {} ", classOfObjectInCollection);
						List<Long> childId = loadChildId(id, classOfObjectInCollection, clazz);
						List<Object> childCollection = new ArrayList<>();
						childId.forEach((i) -> childCollection.add(load(i, classOfObjectInCollection)));
						loadedObj = childCollection;
					} else {
						loadedObj = load(getIdChildObject(id, clazz, field.getType()), field.getType());
					}
					ObjectHelper.setFieldToObject(parentObject, field, loadedObj);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return parentObject;
	}

	private long getIdChildObject(long id, Class parentClass, Class childClass) throws SQLException {
		log.info("Try to load child ID, parent id {}, parent Class {}, child Class {}", id, parentClass, childClass);
		return transactionalExecute(
				connection -> {
					Long result;
					String sqlQuery = String.format(
							"SELECT * FROM %s r WHERE r.%s = ?",
							parentClass.getSimpleName() + "_" + childClass.getSimpleName(),
							parentClass.getSimpleName() + "_id");
					log.info("(Method getIdChildObject) SQL query string: {}", sqlQuery);
					try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
						ResultSet rs = execute(id, ps);
						result = rs.getLong(childClass.getSimpleName() + "_id");
					}
					return result;
				}
		);
	}


	private List<Long> loadChildId(long id, Class<?> childClass, Class clazz) throws SQLException {
		log.info("Try to load child ID, parent id {}, parent Class {}, child Class {}", id, clazz, childClass);
		return transactionalExecute(
				connection -> {
					List<Long> result;
					String sqlQuery = String.format(
							"SELECT * FROM %s r WHERE r.%s = ?",
							clazz.getSimpleName() + "_" + childClass.getSimpleName(),
							clazz.getSimpleName() + "_id");
					log.info("(Method loadChildId) SQL query string: {}", sqlQuery);
					try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
						ResultSet rs = execute(id, ps);
						List<Long> childIdList = new ArrayList<>();
						do {
							childIdList.add(rs.getLong(childClass.getSimpleName() + "_id"));
						} while (rs.next());

						result = childIdList;
					}
					return result;
				}
		);
	}

	public <T> List<T> loadAll(Class<T> clazz) {
		log.info("Try to get all object Class {}", clazz);
		List<Long> listOfObjectId = new ArrayList<>();
		try {
			listOfObjectId = transactionalExecute(
					connection -> {
						List<Long> result = new ArrayList<>();
						try (PreparedStatement ps = connection.prepareStatement(String.format(
								"SELECT * FROM %s r",
								clazz.getSimpleName().toUpperCase()))) {
							ResultSet rs = ps.executeQuery();
							if (!rs.next()) {
								throw new NotFoundException(0);
							}
							do {
								result.add(rs.getLong("id"));
							} while (rs.next());
						}
						return result;
					});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<T> listOfObject = new ArrayList<>();
		listOfObjectId.forEach(i -> {
			listOfObject.add((T) load(i, clazz));
		});
		return listOfObject;
	}

	private void saveObject(Object object) {
		log.info("Try to save use SQL Helper: Object:{}", object);
		try {
			transactionalExecute(
					connection -> {
						try (PreparedStatement ps = connection.prepareStatement(
								ObjectHelper.getQuery(object), Statement.RETURN_GENERATED_KEYS)) {
							ps.executeUpdate();
						}
						return null;
					}
			);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private <T> T loadObject(Object id, Class<T> clazz) throws SQLException {
		log.info("Try to load object with SQLQueryHelper, id {}, Class {}", id, clazz);
		return executeTransaction(id, clazz, String.format(
				"SELECT * FROM %s r WHERE r.id = ?",
				clazz.getSimpleName().toUpperCase()));
	}

	private <T> T loadObject(String name, Class<T> clazz) throws SQLException {
		log.info("Try to load object with SQLQueryHelper, name {}, Class {}", name, clazz);
		return executeTransaction(name, clazz, String.format(
				"SELECT * FROM %s r WHERE r.name = ?",
				clazz.getSimpleName().toUpperCase()));
	}

	private <T> T executeTransaction(Object key, Class<T> clazz, String sqlQuery) throws SQLException {
		return transactionalExecute(
				connection -> {
					T result;
					log.info("(Method loadObject) SQL query string: {}", sqlQuery);
					try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
						ResultSet rs = execute(key, ps);
						result = buildObject(rs, clazz);
					}
					return result;
				}
		);
	}

	private ResultSet execute(Object id, PreparedStatement ps) throws SQLException {
		ps.setString(1, String.valueOf(id));
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			throw new NotFoundException(id);
		}
		return rs;
	}

	private <T> T buildObject(ResultSet rs, Class<T> clazz) {
		log.info("Try to build object, Class {}", clazz);
		List<Field> fields = ObjectHelper.getFieldListOfObject(clazz);
		Map<Field, Object> fieldsWithValues = new HashMap<>();
		fields.forEach(field -> {
			try {
				fieldsWithValues.put(field, rs.getObject(field.getName()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		T result = ObjectHelper.buildObject(clazz, fieldsWithValues);
		log.info("Built object: {}", result);
		return result;
	}


	private void checkObjectType(Class clazz) {
		if (!DataSet.class.isAssignableFrom(clazz)) throw new WrongTypeOfEntityException();
	}

	private <T> T transactionalExecute(SqlTransaction<T> executor) throws SQLException {
		log.info("Begin execute transactional SQL operation.");
		try {
			log.info("Open transaction.");
			T res = executor.execute(connection);
			connection.commit();
			log.info("Commit transaction.");
			return res;
		} catch (SQLException e) {
			log.info("Rollback transaction.");
			connection.rollback();
			throw new RuntimeException(e);
		}

	}

}
