package ru.otus.gromov.myOrm.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class ReflectionHelper {

	private ReflectionHelper() {
	}

	public static <T> List getFields(T t) {
		List<Field> fields = new ArrayList<>();
		Class clazz = t.getClass();
		while (clazz != Object.class) {
			fields.addAll(Arrays.stream(clazz.getDeclaredFields())
					.filter(field -> !Modifier.isStatic(field.getModifiers()))
					.filter(field -> !Modifier.isTransient(field.getModifiers()))
					.collect(Collectors.toList()));
			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	public static <T> T getFieldValueByField(Object object, Field field) {
		try {
			field.setAccessible(true);
			return (T) field.get(object);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			if (field != null) {
				field.setAccessible(false);
			}
		}
		return null;
	}

	public static <T> T instantiate(Class<T> type, Object... args) {
		try {
			if (args.length == 0) {
				return type.getDeclaredConstructor().newInstance();
			} else {
				Class<?>[] classes = toClasses(args);
				return type.getDeclaredConstructor(classes).newInstance(args);
			}
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void setFieldValueByField(Object object, Field field, Object value) {
		boolean isAccessible = true;
		try {
			isAccessible = field.canAccess(object);
			field.setAccessible(true);
			field.set(object, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			if (field != null && !isAccessible) {
				field.setAccessible(false);
			}
		}
	}

	static private Class<?>[] toClasses(Object[] args) {
		return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
	}
}
