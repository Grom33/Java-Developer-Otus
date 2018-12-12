package ru.otus.gromov.messageSystem;
/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */


import ru.otus.gromov.service.DBService;


public abstract class MessageToDB extends Message {

	public MessageToDB(Address from, Address to, long index) {
		super(from, to, index);
	}

	@Override
	public void exec(Addressee addressee) {
		if (addressee instanceof DBService) {
			exec((DBService) addressee);
		}
	}


	public abstract void exec(DBService dbService);
}
