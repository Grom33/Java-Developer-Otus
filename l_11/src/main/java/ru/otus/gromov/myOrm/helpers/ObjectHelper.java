package ru.otus.gromov.myOrm.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;

public class ObjectHelper {
	private static final String NO_ANNOTATIONS = "NoAnnotations";
	private static final Logger log = LoggerFactory.getLogger(ObjectHelper.class);

	public static String prepareInitQueryForObject(Object object) {
		return applaySqlHelperToObjectStructure(object,
				SQLQueryHelper::getInitQuery,
				SQLQueryHelper::getJoinTable,
				ObjectHelper::prepareInitQueryForObject);
	}

	public static String prepareValuesQueryForObject(Object object) {
		return applaySqlHelperToObjectStructure(object,
				SQLQueryHelper::getValueQuery,
				SQLQueryHelper::getJoinTable,
				ObjectHelper::prepareValuesQueryForObject);
	}

	private static String applaySqlHelperToObjectStructure(Object object,
	                                                       AppllaySqlHelper<Object, Collection<Field>> funcEntity,
	                                                       AppllayTableJoiner<Object, Object> funcJoin,
	                                                       Function<Object, String> funcChild) {
		Map<String, Collection<Field>> objectStucture = ripObject(object);
		StringBuilder resultQuery = new StringBuilder();
		resultQuery.append(funcEntity.apply(object, objectStucture.get(NO_ANNOTATIONS))).append("\n");
		objectStucture.forEach((key, value) -> {
			if (!key.equals(NO_ANNOTATIONS) & !key.equals(ManyToOne.class.getSimpleName()))
				value.forEach(field -> {
					Object childObject = ReflectionHelper.getFieldValueByField(object, field);
					if (childObject instanceof Collection) {
						Collection<Object> childObjectCollection = (Collection<Object>) childObject;
						if (childObjectCollection.size() > 0) {
							Object objectInCollection = childObjectCollection.iterator().next();
							resultQuery.append(funcJoin.apply(object, objectInCollection)).append("\n");
							resultQuery.append(funcChild.apply(objectInCollection)).append("\n");
						}
					} else {
						resultQuery.append(funcJoin.apply(object, childObject)).append("\n");
						resultQuery.append(funcChild.apply(childObject)).append("\n");
					}
				});
		});
		return resultQuery.toString();
	}


	private static Map<String, Collection<Field>> ripObject(Object object) {
		Map<String, Collection<Field>> objectStucture = new HashMap<>();
		List<Field> fields = ReflectionHelper.getFields(object);
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

	private static boolean isSkipedField(Field field) {
		boolean result = true;
		if (Modifier.isTransient(field.getModifiers())) return false;
		if (field.getDeclaredAnnotations() != null) {
			if (field.isAnnotationPresent(OneToOne.class)) {
				return false;
			} else if (field.isAnnotationPresent(OneToMany.class)) {
				return false;
			} else if (field.isAnnotationPresent(ManyToOne.class)) {
				return false;
			}
		}
		return result;
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
