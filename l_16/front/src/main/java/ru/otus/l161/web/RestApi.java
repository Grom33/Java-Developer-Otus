package ru.otus.l161.web;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.gromov.l162.channel.SocketMsgWorker;
import ru.gromov.l162.messages.MessageType;
import ru.gromov.l162.messages.TransferMessage;
import ru.otus.l161.service.MessageFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@Slf4j
public class RestApi {

	@Autowired
	private SocketMsgWorker client;

	@Autowired
	private MessageFactory messageFactory;

	private final Object sync = new Object();
	private final Map<Long, Object> answers = new ConcurrentHashMap<>();


	@GetMapping("/api/users/{id}")
	public Object getUser(@PathVariable Long id) {
		log.info("Request to get user with id {}", id);
		TransferMessage message = messageFactory.getMessage("DB", id.toString(), MessageType.READ_BY_ID);
		client.send(message);
		return getAnswer(message.getId());
	}

	@GetMapping("/api/users/")
	public Object getAll()  {
		log.info("Request to get all users");
		TransferMessage message = messageFactory.getMessage("DB", "", MessageType.READ_ALL);
		client.send(message);
		return getAnswer(message.getId());
	}

	@GetMapping("/api/users/byname/{name}")
	public Object getUserByName(@PathVariable String name) {
		log.info("Request to get user with name {}", name);
		TransferMessage message = messageFactory.getMessage("DB", name, MessageType.READ_BY_NAME);
		client.send(message);
		return getAnswer(message.getId());
	}

	@DeleteMapping("/api/users/{id}")
	public void deleteUser(@PathVariable Long id) {
		log.info("Request to delete user with id {}", id);
		TransferMessage message = messageFactory.getMessage("DB", id.toString(), MessageType.DELETE);
		client.send(message);
	}

	@PostMapping(value = "/api/users/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addUser(@RequestBody String user)  {
		log.info("Request to add user: {}", user);
		TransferMessage message = messageFactory.getMessage("DB", user , MessageType.DELETE);
		client.send(message);
	}

	private Object getAnswer(long ticket) {
		log.info("try to get answer with ticket {}", ticket);
		synchronized (sync) {
			while (!answers.containsKey(ticket)) {
				try {
					sync.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Object answer = answers.get(ticket);
		log.info("Answer with ticket {} is {} ", ticket, answer);
		answers.remove(ticket);
		return answer;
	}

	public void addAnswer(long index, Object answer) {
		log.info("Answer with ticket {} arrived", index);
		synchronized (sync) {
			answers.put(index, answer);
			sync.notify();
		}
	}

}
