package ru.gromov.l162.server;

import ru.gromov.l162.app.Blocks;
import ru.gromov.l162.app.Msg;
import ru.gromov.l162.app.MsgWorker;
import ru.gromov.l162.channel.SocketMsgWorker;
import ru.gromov.l162.messages.TransferMessage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tully.
 */
public class BlockingEchoSocketMsgServer {
    private static final Logger logger = Logger.getLogger(NonBlockingEchoSocketMsgServer.class.getName());

    private static final int THREADS_NUMBER = 1;
    private static final int PORT = 5050;
    private static final int ECHO_DELAY = 100;

    private final ExecutorService executor;
    private final List<MsgWorker> workers;

    public BlockingEchoSocketMsgServer() {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        workers = new CopyOnWriteArrayList<>();
    }

    @Blocks
    public void start() throws Exception {
        executor.submit(this::echo);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Server started on port: " + serverSocket.getLocalPort());
            while (!executor.isShutdown()) {
                final Socket socket = serverSocket.accept(); //blocks
                final SocketMsgWorker worker = new SocketMsgWorker(socket);
                worker.init();

                worker.addShutdownRegistration(() -> workers.remove(worker));

                workers.add(worker);
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void echo() {
        while (true) {
            for (MsgWorker client : workers) {
                TransferMessage msg = client.poll(); //get
                while (msg != null) {
                    System.out.println("Echoing the message: " + msg.toString());
                    client.send(msg);
                    msg = client.poll();
                }
            }
            try {
                Thread.sleep(ECHO_DELAY);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.toString());
            }
        }
    }
}
