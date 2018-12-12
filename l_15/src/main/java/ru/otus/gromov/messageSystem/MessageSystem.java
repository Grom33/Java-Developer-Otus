package ru.otus.gromov.messageSystem;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class MessageSystem {

	private static final int DEFAULT_STEP_TIME = 10;

	private final List<Thread> workers;
	private final Map<Address, LinkedBlockingQueue<Message>> messagesMap;
	private final Map<Address, Addressee> addresseeMap;

	public MessageSystem() {
		workers = new ArrayList<>();
		messagesMap = new HashMap<>();
		addresseeMap = new HashMap<>();
	}

	public void addAddressee(Addressee addressee) {
		addresseeMap.put(addressee.getAddress(), addressee);
		messagesMap.put(addressee.getAddress(), new LinkedBlockingQueue<>());
	}

	public void sendMessage(Message message) {
		log.info("send message {}", message);
		log.info("Adressee {}", message.getTo());
		messagesMap.get(message.getTo()).add(message);
	}

	public void start() {
		for (Map.Entry<Address, Addressee> entry : addresseeMap.entrySet()) {
			String name = "MS-worker-" + entry.getKey().getId();
			log.info("prepare thread {}", name);
			Thread thread = new Thread(() -> {
				LinkedBlockingQueue<Message> queue = messagesMap.get(entry.getKey());
				while (true) {
					try {
						Message message = queue.take();
						log.info("try to send message {}", message);
						message.exec(entry.getValue());
					} catch (InterruptedException e) {
						log.info("Thread interrupted. Finishing: " + name);
						return;
					}
				}
			});
			thread.setName(name);
			thread.start();
			workers.add(thread);
		}
	}

	public void dispose() {
		workers.forEach(Thread::interrupt);
	}
}
