package ru.otus.l161.service;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gromov.l162.channel.SocketMsgWorker;
import ru.gromov.l162.messages.MessageType;
import ru.gromov.l162.messages.TransferMessage;
import ru.otus.l161.web.RestApi;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@NoArgsConstructor
public class ServiceListener {
	@Autowired
	SocketMsgWorker client;
	@Autowired
	RestApi restApi;


	@PostConstruct
	public void init() {
		log.info("Start listener");

		TransferMessage greeting = new TransferMessage();
		greeting.setCommand(MessageType.HANDSHAKE);
		greeting.setSender("FRONT");
		client.send(greeting);

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.submit(() -> {
			try {
				while (true) {
					final TransferMessage msg = client.take();
					restApi.addAnswer(msg.getId(),msg.getPayload());
					log.info("Message received:{} ", msg.toString());
				}
			} catch (InterruptedException e) {
				log.info(e.getMessage());
			}
		});


	}


}
