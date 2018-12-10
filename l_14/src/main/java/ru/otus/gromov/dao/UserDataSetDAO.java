package ru.otus.gromov.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.otus.gromov.base.dataSets.UserDataSet;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDataSetDAO {
	private Session session;

	public UserDataSetDAO(Session session) {
		this.session = session;
	}

	public void save(UserDataSet dataSet) {
		if (dataSet.getId() != 0L) {
			session.update(dataSet);
		} else {
			session.save(dataSet);
		}

	}

	public UserDataSet read(long id) {
		return session.load(UserDataSet.class, id);
	}

	public UserDataSet readByName(String name) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
		Root<UserDataSet> from = criteria.from(UserDataSet.class);
		criteria.where(builder.equal(from.get("name"), name));
		Query<UserDataSet> query = session.createQuery(criteria);
		return query.uniqueResult();
	}

	public List<UserDataSet> readAll() {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
		criteria.from(UserDataSet.class);
		return session.createQuery(criteria).list();
	}

	public void remove(long id) {
		System.out.println("!!!!  delete user " + id);
		session.delete(read(id));
	}

	public void update(UserDataSet dataSet) {
		session.update(dataSet);
	}
}
