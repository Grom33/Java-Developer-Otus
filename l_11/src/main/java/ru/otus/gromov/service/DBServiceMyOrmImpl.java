package ru.otus.gromov.service;

import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.dao.UserDataSetDAOMyORM;
import ru.otus.gromov.myOrm.MyOrm;
import ru.otus.gromov.myOrm.session.MySession;
import ru.otus.gromov.myOrm.helpers.ConnectionHelper;

import java.sql.SQLException;
import java.util.List;

class DBServiceMyOrmImpl implements DBService {

	private final MyOrm myOrm;


	public DBServiceMyOrmImpl() throws SQLException {
		this.myOrm = new MyOrm(new ConnectionHelper("sa", ""));
	}

	@Override
	public String getLocalStatus() {
		return myOrm.getStatus();
	}

	@Override
	public void save(UserDataSet dataSet){
		try(MySession session = myOrm.getSession()) {
			UserDataSetDAOMyORM dao = new UserDataSetDAOMyORM(session);
			dao.save(dataSet);
		}
	}

	@Override
	public UserDataSet read(long id) {
		try(MySession session = myOrm.getSession()) {
			UserDataSetDAOMyORM dao = new UserDataSetDAOMyORM(session);
			return dao.load(id);
		}

	}

	@Override
	public UserDataSet readByName(String name){
		try(MySession session = myOrm.getSession()) {
			UserDataSetDAOMyORM dao = new UserDataSetDAOMyORM(session);
			return dao.loadByName(name);
		}
	}

	@Override
	public List<UserDataSet> readAll() {
		try(MySession session = myOrm.getSession()) {
			UserDataSetDAOMyORM dao = new UserDataSetDAOMyORM(session);
			return dao.loadAll();
		}
	}


	@Override
	public void shutdown() {
		myOrm.close();
	}

}