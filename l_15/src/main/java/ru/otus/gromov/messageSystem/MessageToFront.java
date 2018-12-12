package ru.otus.gromov.messageSystem;
/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import ru.otus.gromov.servlet.FrontendService;

public abstract class MessageToFront extends Message {


	public MessageToFront(Address from, Address to, long index) {
		super(from, to, index);
	}

	@Override
	public void exec(Addressee addressee) {
		if (addressee instanceof FrontendService) {
			exec((FrontendService) addressee);
		}
	}

	public abstract void exec(FrontendService frontendService);

}
