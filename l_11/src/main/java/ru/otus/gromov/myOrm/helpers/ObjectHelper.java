package ru.otus.gromov.myOrm.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.gromov.myOrm.exception.InstantiateEntityException;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ObjectHelper {
	private static final String NO_ANNOTATIONS = "NoAnnotations";
	private static final boolean INIT = true;
	private static final boolean VALUES = false;
	private static final Logger log = LoggerFactory.getLogger(ObjectHelper.class);


	public static String getQuery(Object object) {
		String result = prepareInitQueryForObject(object) + prepareValuesQueryForObject(object);
		log.info("Prepared query: {}", result);
		return result;
	}


	private static String prepareInitQueryForObject(Object object) {
		return applaySqlHelperToObjectStructure(object,
				SQLQueryHelper::getInitQuery,
				SQLQueryHelper::getInitJoinTable,
				ObjectHelper::prepareInitQueryForObject,
				INIT);
	}

	private static String prepareValuesQueryForObject(Object object) {
		return applaySqlHelperToObjectStructure(object,
				SQLQueryHelper::getValueQuery,
				SQLQueryHelper::getValuesJoinTable,
				ObjectHelper::prepareValuesQueryForObject,
				VALUES);
	}

	private static String applaySqlHelperToObjectStructure(Object object,
	                                                       AppllaySqlHelper<Object, Collection<Field>> funcEntity,
	                                                       AppllayTableJoiner<Object, Object> funcJoin,
	                                                       Function<Object, String> funcChild,
	                                                       boolean init) {
		Map<String, Collection<Field>> objectStructure = ripObject(object);
		StringBuilder resultQuery = new StringBuilder();
		resultQuery.append(funcEntity.apply(object, objectStructure.get(NO_ANNOTATIONS))).append("\n");
		objectStructure.entrySet().stream()
				.filter((entry) -> !entry.getKey().equals(NO_ANNOTATIONS) & !entry.getKey().equals(ManyToOne.class.getSimpleName()))
				.forEach((entry) -> entry.getValue().forEach(field -> {
					Object childObject = ReflectionHelper.getFieldValueByField(object, field);
					if (childObject instanceof Collection) {
						if (init)
							childObject = Collections.singletonList(((Collection<Object>) childObject).iterator().next());
						appendQueryToResultQueryFromCollection(resultQuery, (Collection<Object>) childObject, object, funcJoin, funcChild);
					} else {
						appendQueryToResultQueryFromChilObject(resultQuery, childObject, object, funcJoin, funcChild);
					}
				}));
		return resultQuery.toString();
	}

	private static void appendQueryToResultQueryFromCollection(StringBuilder resultQuery,
	                                                           Collection<Object> collection,
	                                                           Object parentObject,
	                                                           AppllayTableJoiner<Object, Object> funcJoin,
	                                                           Function<Object, String> funcChild) {
		collection.forEach(
				(obj) -> appendQueryToResultQueryFromChilObject(
						resultQuery, obj, parentObject, funcJoin, funcChild));
	}

	private static void appendQueryToResultQueryFromChilObject(StringBuilder resultQuery,
	                                                           Object childObject,
	                                                           Object parentObject,
	                                                           AppllayTableJoiner<Object, Object> funcJoin,
	                                                           Function<Object, String> funcChild) {
		resultQuery.append(funcJoin.apply(parentObject, childObject)).append("\n")
				.append(funcChild.apply(childObject)).append("\n");
	}

	public static Map<String, Collection<Field>> ripObject(Object object) {
		return ripObject(object.getClass());

	}

	public static Map<String, Collection<Field>> ripObject(Class clazz) {
		log.info("Rip Class: {}", clazz);
		Map<String, Collection<Field>> objectStucture = new HashMap<>();
		List<Field> fields = ReflectionHelper.getFields(clazz);
		fields.forEach(field -> {
			String structureKey = NO_ANNOTATIONS;
			if (field.getDeclaredAnnotations() != null) {
				if (field.isAnnotationPresent(OneToOne.class)) {
					structureKey = OneToOne.class.getSimpleName();
				} else if (field.isAnnotationPresent(OneToMany.class)) {
					structureKey = OneToMany.class.getSimpleName();
				} else if (field.isAnnotationPresent(ManyToOne.class)) {
					structureKey = ManyToOne.class.getSimpleName();
				} else if (field.isAnnotationPresent(ManyToMany.class)) {
					structureKey = ManyToMany.class.getSimpleName();
				}
			}
			if (!objectStucture.containsKey(structureKey)) objectStucture.put(structureKey, new ArrayList<>());
			objectStucture.get(structureKey).add(field);
		});
		return objectStucture;
	}

	public static List<Field> getListWithChildObject(Class clazz) {
		return ObjectHelper.ripObject(clazz)
				.entrySet().stream()
				.filter(entry -> !NO_ANNOTATIONS.equals(entry.getKey()))
				.filter(entry -> !ManyToOne.class.getSimpleName().equals(entry.getKey()))
				.map(entry -> entry.getValue().iterator().next())
				.collect(Collectors.toList());
	}

	public static <T> T buildObject(Class<T> clazz, Map<Field, Object> objectFieldsWithValues) {

		T result = ReflectionHelper.instantiate(clazz);
		if (result == null) throw new InstantiateEntityException();
		objectFieldsWithValues.forEach(((field, object) -> ReflectionHelper.setFieldValueByField(result, field, object)));
		log.info("Built object: {}, from Class: {}", result, clazz);
		return result;
	}

	public static List<Field> getFieldListOfObject(Class clazz) {
		return ReflectionHelper.getFields(clazz).stream()
				.filter(ObjectHelper::isSkipedField)
				.collect(Collectors.toList());
	}

	public static void setFieldToObject(Object parentObject, Field field, Object loadedObj) {
		log.info("Set field: {} to Object {}, value: {}", field, parentObject, loadedObj);
		ReflectionHelper.setFieldValueByField(parentObject, field, loadedObj);
	}

	private static boolean isSkipedField(Field field) {
		if (Modifier.isTransient(field.getModifiers())) return false;
		if (field.getDeclaredAnnotations() != null) {
			if (field.isAnnotationPresent(OneToOne.class)) {
				return false;
			} else if (field.isAnnotationPresent(OneToMany.class)) {
				return false;
			} else return !field.isAnnotationPresent(ManyToOne.class);
		}
		return true;
	}

	@FunctionalInterface
	interface AppllaySqlHelper<Object, Collection> {
		public String apply(Object obj, Collection collection);
	}

	@FunctionalInterface
	interface AppllayTableJoiner<First, Second> {
		public String apply(Object first, Object Second);
	}
}
