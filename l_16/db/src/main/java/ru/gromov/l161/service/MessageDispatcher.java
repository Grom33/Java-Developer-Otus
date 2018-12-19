package ru.gromov.l161.service;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.gromov.l161.base.dataSets.UserDataSet;
import ru.gromov.l161.helper.UnProxyHelper;
import ru.gromov.l162.channel.SocketMsgWorker;
import ru.gromov.l162.messages.MessageType;
import ru.gromov.l162.messages.TransferMessage;

import java.io.IOException;
@Slf4j
public class MessageDispatcher {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final DBService dbservice;


	public MessageDispatcher(DBService dbservice) {
		this.dbservice = dbservice;
	}

	public void dispatch(TransferMessage message, SocketMsgWorker client) throws IOException {
		log.info("Dispatch message {}", message.getId());
		resolveRequest(message, client);
	}

	private void resolveRequest(TransferMessage message, SocketMsgWorker client) throws IOException {
		switch (message.getCommand()) {
			case READ_BY_ID:
				readById(message, client);
				break;
			case READ_BY_NAME:
				readByName(message, client);
				break;
			case READ_ALL:
				getAll(message, client);
				break;
			case CREATE:
				create(message);
				break;
			case DELETE:
				delete(message);
				break;
			default:
				break;
		}
	}

	private void delete(TransferMessage message) {
		long id = Long.parseLong(message.getPayload());
		log.info("Delete user  {}",id);
		dbservice.remove(id);
	}

	private void create(TransferMessage message) throws IOException {
		log.info("Create user  {}", message);
		if (message.getPayload().length() > 0) {
			UserDataSet userDataSet = MAPPER.readValue(
					message.getPayload(), UserDataSet.class);
			dbservice.save(userDataSet);
		}
	}

	private void getAll(TransferMessage message, SocketMsgWorker client) throws JsonProcessingException {
		log.info("Get all users");
		TransferMessage newMsg = getMessage(message);
		newMsg.setPayload(
				MAPPER.writeValueAsString(
						UnProxyHelper.get(dbservice.readAll())));
		client.send(newMsg);
	}

	private void readByName(TransferMessage message, SocketMsgWorker client) throws JsonProcessingException {

		TransferMessage newMsg = getMessage(message);
		String name = message.getPayload();
		log.info("Get user with name {}", name);
		UserDataSet user = dbservice.readByName(name);
		if (user == null) {
			newMsg.setPayload(null);
		} else {
			newMsg.setPayload(
					MAPPER.writeValueAsString(
							UnProxyHelper.get(user)));
		}
		client.send(newMsg);
	}

	private void readById(TransferMessage message, SocketMsgWorker client) throws JsonProcessingException {
		TransferMessage newMsg = getMessage(message);
		long id = Long.parseLong(message.getPayload());
		log.info("Get user with id {}", id);
		newMsg.setPayload(
				MAPPER.writeValueAsString(
						UnProxyHelper.get(dbservice.read(id))));
		client.send(newMsg);
	}

	private TransferMessage getMessage(TransferMessage message) {
		log.info("Get blank message");
		TransferMessage newMsg = new TransferMessage();
		newMsg.setAddressee(message.getSender());
		newMsg.setSender("DB");
		newMsg.setCommand(MessageType.ANSWER);
		newMsg.setId(message.getId());
		return newMsg;
	}
}
