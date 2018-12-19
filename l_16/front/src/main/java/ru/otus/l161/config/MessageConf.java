package ru.otus.l161.config;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.gromov.l162.channel.SocketMsgWorker;
import ru.otus.l161.ClientSocketMsgWorker;

import java.io.IOException;

@Configuration
public class MessageConf {
	private static final String HOST = "localhost";
	private static final int PORT = 5050;

	@Bean
	public SocketMsgWorker getWorker(){
		try {
			SocketMsgWorker client = new ClientSocketMsgWorker(HOST, PORT);
			client.init();
			return client;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
