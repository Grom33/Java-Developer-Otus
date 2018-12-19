package ru.gromov.l162.messages;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import lombok.Data;

@Data
public class TransferMessage {
	private long id;
	private MessageType command;
	private String addressee;
	private String sender;
	private String payload;
}
