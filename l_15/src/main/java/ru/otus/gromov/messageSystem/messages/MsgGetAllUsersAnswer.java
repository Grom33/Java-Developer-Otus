package ru.otus.gromov.messageSystem.messages;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import lombok.extern.slf4j.Slf4j;
import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.messageSystem.Address;
import ru.otus.gromov.messageSystem.MessageToFront;
import ru.otus.gromov.servlet.FrontendService;

import java.util.List;

@Slf4j
public class MsgGetAllUsersAnswer extends MessageToFront {
	private final List<UserDataSet> users;

	public MsgGetAllUsersAnswer(Address from, Address to, long index, List<UserDataSet> userDataSets) {
		super(from, to, index);
		this.users = userDataSets;
	}

	@Override
	public void exec(FrontendService frontendService) {
		log.info("Put answer to map");
		frontendService.addAnswer(super.getIndex(), users);
	}
}
