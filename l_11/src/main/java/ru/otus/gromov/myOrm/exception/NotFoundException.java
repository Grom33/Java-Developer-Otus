package ru.otus.gromov.myOrm.exception;

public class NotFoundException extends MyOrmException {
	public NotFoundException(Object id) {
		super(String.format("Entity with id %s not found!", id));
	}
}
