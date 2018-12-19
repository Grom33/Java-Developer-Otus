package ru.gromov.l161.helper;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.stream.Collectors;

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

	public static <T> List<T> get(List<T> list) {
		return list.stream().map(UnProxyHelper::get).collect(Collectors.toList());
	}
}