package ru.otus.gromov.messageSystem;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

public interface Addressee {
	Address getAddress();

	MessageSystem getMS();
}
