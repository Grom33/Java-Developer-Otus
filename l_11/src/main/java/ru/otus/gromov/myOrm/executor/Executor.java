package ru.otus.gromov.myOrm.executor;

public interface Executor<T> {

	void save(T object);

	T load(long id, Class<T> clazz);

}
