package ru.otus.gromov.messageSystem.messages;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.messageSystem.Address;
import ru.otus.gromov.messageSystem.MessageToDB;
import ru.otus.gromov.service.DBService;

public class MsgPutUser extends MessageToDB {
	private final UserDataSet user;

	public MsgPutUser(Address from, Address to, long index, UserDataSet user) {
		super(from, to, index);
		this.user = user;
	}

	@Override
	public void exec(DBService dbService) {
		dbService.save(user);
	}
}
