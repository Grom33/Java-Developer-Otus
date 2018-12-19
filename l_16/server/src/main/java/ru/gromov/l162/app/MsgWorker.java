package ru.gromov.l162.app;

import ru.gromov.l162.messages.TransferMessage;

import java.io.Closeable;

/**
 * Created by tully.
 */
public interface MsgWorker extends Closeable {
    void send(TransferMessage msg);

    TransferMessage poll();

    @Blocks
    TransferMessage take() throws InterruptedException;

    void close();
}
