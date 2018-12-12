package ru.otus.gromov.service;

import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.messageSystem.Addressee;

import java.util.List;

public interface DBService extends Addressee {
	String getLocalStatus();

	void save(UserDataSet dataSet);

	UserDataSet read(long id);

	UserDataSet readByName(String name);

	List<UserDataSet> readAll();

	void shutdown();

	void remove(long id);

	void update(UserDataSet user);

	void init();
}
