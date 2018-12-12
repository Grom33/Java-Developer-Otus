package ru.otus.gromov.messageSystem;
/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class Address {
	private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
	private final String id;

	public Address() {
		id = String.valueOf(ID_GENERATOR.getAndIncrement());
	}

	public Address(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Address address = (Address) o;

		return id != null ? id.equals(address.id) : address.id == null;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

}
