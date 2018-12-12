package ru.otus.gromov.messageSystem.messages;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.messageSystem.Address;
import ru.otus.gromov.messageSystem.MessageToDB;
import ru.otus.gromov.service.DBService;

public class MsgGetUser extends MessageToDB {

	private final long id;

	public MsgGetUser(Address from, Address to, long index, long id) {
		super(from, to, index);
		this.id = id;
	}


	@Override
	public void exec(DBService dbService) {
		UserDataSet user = dbService.read(this.id);
		dbService.getMS().sendMessage(new MsgGetUserAnswer(getTo(), getFrom(), super.getIndex(), user));
	}


}
