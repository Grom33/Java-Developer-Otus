package ru.otus.l161.service;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gromov.l162.messages.MessageType;
import ru.gromov.l162.messages.TransferMessage;

import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class MessageFactory {
	private final String SENDER = "FRONT";
	private final AtomicLong ticket = new AtomicLong();


	public TransferMessage getMessage(String addressee, String payload, MessageType command) {
		log.info("Get blank message for {}", addressee);
		TransferMessage transferMessage = new TransferMessage();
		transferMessage.setSender(SENDER);
		transferMessage.setAddressee(addressee);
		transferMessage.setPayload(payload);
		transferMessage.setCommand(command);
		transferMessage.setId(getTicket());
		return transferMessage;
	}

	private long getTicket() {
		ticket.compareAndSet(Long.MAX_VALUE, 1L);
		return ticket.incrementAndGet();
	}

}
