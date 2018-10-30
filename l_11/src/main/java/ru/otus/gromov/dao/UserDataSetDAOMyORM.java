package ru.otus.gromov.dao;

import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.myOrm.domain.MyUserDataSet;
import ru.otus.gromov.myOrm.session.MySession;

import java.util.List;

public class UserDataSetDAOMyORM {
	private final MySession session;

	public UserDataSetDAOMyORM(MySession session) {
		this.session = session;
	}

	public UserDataSet load(long id){
		return (UserDataSet) session.load(id, UserDataSet.class);
	}

	public UserDataSet loadByName(String name){
		return (UserDataSet) session.load(1L, UserDataSet.class);
	}

	public void save(UserDataSet userDataSet){
		session.save(new MyUserDataSet(1,"Ivan",11,true), UserDataSet.class);
	}

	public List<UserDataSet> loadAll(){
		return session.loadAll(UserDataSet.class);
	}
}
