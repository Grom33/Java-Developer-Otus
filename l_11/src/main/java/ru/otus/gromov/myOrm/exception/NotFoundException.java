package ru.otus.gromov.myOrm.exception;

public class NotFoundException extends RuntimeException {
	public NotFoundException(Object id) {
		super(String.format("Entity with id %s not found!", id));
	}
}
