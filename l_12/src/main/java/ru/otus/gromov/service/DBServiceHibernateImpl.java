package ru.otus.gromov.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.gromov.base.dataSets.AdressDataSet;
import ru.otus.gromov.base.dataSets.PhoneDataSet;
import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.dao.UserDataSetDAO;

import java.io.File;
import java.util.List;
import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final SessionFactory sessionFactory;

	public DBServiceHibernateImpl() {
		log.info("Init Hibernate config");
		Configuration configuration = new Configuration()
				.configure(new File("config/hibernate.cfg.xml"))
				.addAnnotatedClass(UserDataSet.class)
				.addAnnotatedClass(AdressDataSet.class)
				.addAnnotatedClass(PhoneDataSet.class);

		sessionFactory = createSessionFactory(configuration);
	}

	private static SessionFactory createSessionFactory(Configuration configuration) {
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
		builder.applySettings(configuration.getProperties());
		ServiceRegistry serviceRegistry = builder.build();
		return configuration.buildSessionFactory(serviceRegistry);
	}

	public String getLocalStatus() {
		return runInSession(session -> session.getTransaction().getStatus().name());
	}

	public void save(UserDataSet dataSet) {
		log.info("Save entity:  {}", dataSet);

		try (Session session = sessionFactory.openSession()) {
			UserDataSetDAO dao = new UserDataSetDAO(session);
			runInTransaction(session, (returnedDao) -> {
				returnedDao.save(dataSet);
				return null;
			});

		}
	}

	public UserDataSet read(long id) {
		log.info("Read entity with Id:  {}", id);
		return runInSession(session -> {
			UserDataSetDAO dao = new UserDataSetDAO(session);
			return dao.read(id);
		});
	}

	public UserDataSet readByName(String name) {
		log.info("Read entity by name:  {}", name);
		return runInSession(session -> {
			UserDataSetDAO dao = new UserDataSetDAO(session);
			return dao.readByName(name);
		});
	}

	public List<UserDataSet> readAll() {
		log.info("Read all entity:  {}");
		return runInSession(session -> {
			UserDataSetDAO dao = new UserDataSetDAO(session);
			return dao.readAll();
		});
	}

	public void shutdown() {
		log.info("Shutdown service");
		sessionFactory.close();
	}

	@Override
	public void remove(long id) {
		log.info("Remove entity with id:  {}", id);
		try (Session session = sessionFactory.openSession()) {
			runInTransaction(session, (dao) -> {
				dao.remove(id);
				return null;
			});
		}
	}

	@Override
	public void update(UserDataSet user) {
		log.info("Update entity:  {}", user);
		try (Session session = sessionFactory.openSession()) {
			runInTransaction(session, (dao) -> {
				dao.update(user);
				return null;
			});
			UserDataSetDAO dao = new UserDataSetDAO(session);
			dao.update(user);
		}
	}

	private <R> R runInSession(Function<Session, R> function) {
		try (Session session = sessionFactory.openSession()) {
			Transaction transaction = session.beginTransaction();
			R result = function.apply(session);
			transaction.commit();
			return result;
		}
	}

	private void runInTransaction(Session session, Function<UserDataSetDAO, Void> daoFunc) {
		Transaction transaction = session.getTransaction();
		transaction.begin();
		UserDataSetDAO dao = new UserDataSetDAO(session);
		daoFunc.apply(dao);
		session.flush();
		transaction.commit();
	}
}
