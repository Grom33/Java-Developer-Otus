package ru.otus.gromov.exception;

public class NotFoundException extends RuntimeException {
	public NotFoundException(long id) {
		super(String.format("Entity with id %s not found!", id));
	}
}
