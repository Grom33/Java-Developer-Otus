package ru.otus.gromov.messageSystem.messages;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import ru.otus.gromov.messageSystem.Address;
import ru.otus.gromov.messageSystem.MessageToDB;
import ru.otus.gromov.service.DBService;

public class MsgDeleteUser extends MessageToDB {
	private final long id;

	public MsgDeleteUser(Address from, Address to, long index, long id) {
		super(from, to, index);
		this.id = id;
	}

	@Override
	public void exec(DBService dbService) {
		dbService.remove(this.id);
	}
}
