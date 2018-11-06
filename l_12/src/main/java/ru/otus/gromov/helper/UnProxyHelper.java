package ru.otus.gromov.helper;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

public class UnProxyHelper {
	public static <T> T get(T entity) {
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