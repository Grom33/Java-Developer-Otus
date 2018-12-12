package ru.otus.gromov.messageSystem.messages;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import ru.otus.gromov.base.dataSets.UserDataSet;
import ru.otus.gromov.messageSystem.Address;
import ru.otus.gromov.messageSystem.MessageToFront;
import ru.otus.gromov.servlet.FrontendService;

public class MsgGetUserAnswer extends MessageToFront {
	private final UserDataSet user;

	public MsgGetUserAnswer(Address from, Address to, long index, UserDataSet user) {
		super(from, to, index);
		this.user = user;
	}


	@Override
	public void exec(FrontendService frontendService) {
		frontendService.addAnswer(super.getIndex(), user);
	}
}
