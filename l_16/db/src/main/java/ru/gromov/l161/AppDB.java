package ru.gromov.l161;

import lombok.extern.slf4j.Slf4j;
import ru.gromov.l161.base.dataSets.AdressDataSet;
import ru.gromov.l161.base.dataSets.PhoneDataSet;
import ru.gromov.l161.base.dataSets.UserDataSet;
import ru.gromov.l161.service.DBService;
import ru.gromov.l161.service.DBServiceHibernateImpl;
import ru.gromov.l161.service.MessageDispatcher;
import ru.gromov.l162.channel.SocketMsgWorker;
import ru.gromov.l162.messages.MessageType;
import ru.gromov.l162.messages.TransferMessage;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tully.
 */
@Slf4j
public class AppDB {
	private static final Logger logger = Logger.getLogger(AppDB.class.getName());
	private static final String HOST = "localhost";
	private static final int PORT = 5050;
	private static final DBService dbservice = new DBServiceHibernateImpl();
	private static final MessageDispatcher dispatcher = new MessageDispatcher(dbservice);


	public static void main(String[] args) throws Exception {
		initDB();
		new AppDB().init();
	}

	@SuppressWarnings("InfiniteLoopStatement")
	private void init() throws Exception {
		log.info("Init connection");
		SocketMsgWorker client = new ClientSocketMsgWorker(HOST, PORT);
		client.init();
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		log.info("Start listener");
		executorService.submit(() -> {
			try {
				while (true) {
					final TransferMessage msg = client.take();
					dispatcher.dispatch(msg, client);
				}
			} catch (InterruptedException | IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		});

		TransferMessage msg = new TransferMessage();
		log.info("Send handshake message");
		msg.setCommand(MessageType.HANDSHAKE);
		msg.setSender("DB");
		client.send(msg);
	}

	private static void initDB() {
		log.info("Init DB, just for testing");
		UserDataSet user1 = new UserDataSet();
		UserDataSet user2 = new UserDataSet();
		user1.setName("Ivan");
		user1.setAdress(new AdressDataSet("Moscow 1111111"));
		user1.setPhones(Collections.singletonList(new PhoneDataSet("1111111", user1)));
		dbservice.save(user1);
		user2.setName("Petr");
		user2.setAdress(new AdressDataSet("SPB 2222222"));
		user2.setPhones(Collections.singletonList(new PhoneDataSet("3333333", user2)));
		dbservice.save(user2);
	}


}
