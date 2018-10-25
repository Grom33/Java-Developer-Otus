package ru.otus.gromov.exception;

public class InstantiateEntityException extends RuntimeException {
	public InstantiateEntityException() {
		super("There is some problem with instantiating entity!");
	}
}
