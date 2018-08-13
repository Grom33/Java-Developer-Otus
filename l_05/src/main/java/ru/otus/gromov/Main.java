package ru.otus.gromov;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.management.ManagementFactory.getGarbageCollectorMXBeans;

@SuppressWarnings("InfiniteLoopStatement")
public class Main {
    public static void main(String[] args) {
        AtomicBoolean resume = new AtomicBoolean(true);
        AtomicLong countOperations = new AtomicLong(0L);
        GcStatistic gcStatistic = new GcStatistic();
        installGCMonitoring(gcStatistic, resume);


        Thread fabricThread = new Thread() {
            @Override
            public void run() {
                super.run();
                ArrayList<String> list = new ArrayList<>();

                try {
                    while (true) {
                        for (int i = 0; i < 30; i++) {
                            list.add(String.valueOf(Math.random() * 1_000_000_000));
                            countOperations.incrementAndGet();
                        }
                        for (int i = 0; i < 5; i++) {
                            list.remove(list.size() - 1);
                        }
                        try {
                            sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (OutOfMemoryError e) {
                    resume.set(false);
                }
            }
        };

        Thread statisticThread = new Thread() {
            @Override
            public void run() {
                super.run();
                do {
                    if (!resume.get()) {
                        gcStatistic.printStatistics();
                        System.out.println("Count of operations: " + countOperations.get());
                        return;
                    }
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (true);
            }
        };
        fabricThread.start();
        statisticThread.start();
    }

    private static void installGCMonitoring(GcStatistic gcStatistic, AtomicBoolean resume) {
        for (GarbageCollectorMXBean gcbean : getGarbageCollectorMXBeans()) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION) && resume.get())
                    gcStatistic.registerEvent(GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData()));
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }
}