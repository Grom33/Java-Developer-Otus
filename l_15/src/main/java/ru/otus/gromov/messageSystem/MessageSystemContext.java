package ru.otus.gromov.messageSystem;
/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class MessageSystemContext {

	private final MessageSystem messageSystem;
	private final Address frontAddress;
	private final Address dbAddress;

	@Autowired
	public MessageSystemContext(MessageSystem messageSystem, Address frontAddress, Address dbAddress) {
		this.messageSystem = messageSystem;
		this.frontAddress = frontAddress;
		this.dbAddress = dbAddress;
	}
}
