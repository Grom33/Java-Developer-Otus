package ru.otus.gromov.messageSystem.messages;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import lombok.extern.slf4j.Slf4j;
import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.messageSystem.Address;
import ru.otus.gromov.messageSystem.MessageToDB;
import ru.otus.gromov.service.DBService;

import java.util.List;

@Slf4j
public class MsgGetAllUsers extends MessageToDB {

	public MsgGetAllUsers(Address from, Address to, long index) {
		super(from, to, index);
	}

	@Override
	public void exec(DBService dbService) {
		log.info("Send msg to DB");
		List<UserDataSet> users = dbService.readAll();
		dbService.getMS().sendMessage(new MsgGetAllUsersAnswer(getTo(), getFrom(), super.getIndex(), users));
	}
}
