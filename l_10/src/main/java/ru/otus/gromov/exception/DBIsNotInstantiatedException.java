package ru.otus.gromov.exception;

public class DBIsNotInstantiatedException extends RuntimeException{
	public DBIsNotInstantiatedException() {
		super("Connection to DB in myHibernate is not instantiated!");
	}
}