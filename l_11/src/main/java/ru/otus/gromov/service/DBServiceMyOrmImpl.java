package ru.otus.gromov.service;

import ru.otus.gromov.base.dataSets.UserDataSet;

import java.util.List;

class DBServiceMyOrmImpl implements DBService {
	@Override
	public String getLocalStatus() {
		return null;
	}

	@Override
	public void save(UserDataSet dataSet) {

	}

	@Override
	public UserDataSet read(long id) {
		return null;
	}

	@Override
	public UserDataSet readByName(String name) {
		return null;
	}

	@Override
	public List<UserDataSet> readAll() {
		return null;
	}

	@Override
	public void shutdown() {

	}
}
