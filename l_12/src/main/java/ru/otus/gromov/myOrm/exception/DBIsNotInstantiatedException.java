package ru.otus.gromov.myOrm.exception;

public class DBIsNotInstantiatedException extends MyOrmException {
	public DBIsNotInstantiatedException() {
		super("Connection to DB in myHibernate is not instantiated!");
	}
}