package ru.otus.gromov.myOrm.exception;

public class InstantiateEntityException extends MyOrmException {
	public InstantiateEntityException() {
		super("There is some problem with instantiating entity!");
	}
}
