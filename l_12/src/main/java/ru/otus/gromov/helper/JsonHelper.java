package ru.otus.gromov.helper;

import com.google.gson.*;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import ru.otus.gromov.base.dataSets.UserDataSet;

import java.lang.reflect.Type;

public class JsonHelper {
	public static <T> T initializeAndUnproxy(T entity) {
		if (entity == null) {
			throw new
					NullPointerException("Entity passed for initialization is null");
		}
		Hibernate.initialize(entity);
		if (entity instanceof HibernateProxy) {
			entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
					.getImplementation();
		}
		return entity;
	}
}