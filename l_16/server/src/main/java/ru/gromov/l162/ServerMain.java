package ru.gromov.l162;

import ru.gromov.l162.runner.ProcessRunnerImpl;
import ru.gromov.l162.server.BlockingEchoSocketMsgServer;
import ru.gromov.l162.server.NonBlockingEchoSocketMsgServer;
import ru.gromov.l162.server.NonBlockingLogSocketMsgServer;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tully.
 */

public class ServerMain {
    private static final Logger logger = Logger.getLogger(ServerMain.class.getName());

    private static final String FRONT_START_COMMAND = "java -jar ../front/target/front.jar";
    private static final String DB_START_COMMAND = "java -jar ../db/target/db.jar";
    private static final int CLIENT_START_DELAY_SEC = 1;
    private static final int CLIENTS_COUNT = 0;

    public static void main(String[] args) throws Exception {
        new ServerMain().start();
    }

    private void start() throws Exception {
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        startClients(CLIENTS_COUNT, executorService, FRONT_START_COMMAND);
        startClients(CLIENTS_COUNT, executorService, DB_START_COMMAND);
        startEchoServer();
        executorService.shutdown();
    }

    private void startLogServer() throws Exception {
        new NonBlockingLogSocketMsgServer().start();
    }

    private void startBlockingServer() throws Exception {
        new BlockingEchoSocketMsgServer().start();
    }

    private void startEchoServer() throws Exception {
        final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        final ObjectName name = new ObjectName("ru.otus:type=Server");
        final NonBlockingEchoSocketMsgServer server = new NonBlockingEchoSocketMsgServer();
        mbs.registerMBean(server, name);
        server.start();
    }

    private void startClients(int count, ScheduledExecutorService executorService, String client) {
        for (int i = 0; i < count; i++) {
            executorService.schedule(() -> {
                try {
                    new ProcessRunnerImpl().start(client);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }, CLIENT_START_DELAY_SEC + i, TimeUnit.SECONDS);
        }
    }

}
