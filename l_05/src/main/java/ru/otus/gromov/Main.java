package ru.otus.gromov;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.management.ManagementFactory.getGarbageCollectorMXBeans;

@SuppressWarnings("InfiniteLoopStatement")
public class Main {
    public static void main(String[] args) {
        AtomicBoolean resume = new AtomicBoolean(true);
        AtomicLong countOperations = new AtomicLong(0L);
        AtomicLong start = new AtomicLong(0L);
        AtomicLong stop = new AtomicLong(0L);
        ArrayList<Long> timePer1000 = new ArrayList<>();
        GcStatistic gcStatistic = new GcStatistic();
        installGCMonitoring(gcStatistic, resume);

        Thread fabricThread = new Thread() {
            @Override
            public void run() {
                super.run();
                start.set(System.currentTimeMillis());
                ArrayList<String> list = new ArrayList<>();
                try {
                    while (true) {
                        for (int i = 0; i < 30; i++) {
                            list.add(String.valueOf(Math.random() * 1_000_000_000));
                            if (countOperations.incrementAndGet() % 100_000 == 0) timePer1000.add(System.currentTimeMillis());
                        }
                        for (int i = 0; i < 5; i++) {
                            list.remove(list.size() - 1);
                        }
                        try {
                            sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (OutOfMemoryError e) {
                    stop.set(System.currentTimeMillis());
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
                        gcStatistic.setDuration(stop.get() - start.get());
                        gcStatistic.setOperations(countOperations.get());
                        gcStatistic.setDurationBigCycle(timePer1000);
                        gcStatistic.printStatistics();
                        return;
                    }
                    try {
                        sleep(1000);
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