package ru.otus.gromov;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.util.*;

class GcStatistic {
    private Map<String, List<GcEvent>> collectorsEvent;
    private Map<String, Float> avgDuration = new HashMap<>();
    private Map<String, Float> avgCollections = new HashMap<>();
    private Map<String, Float> totalTime = new HashMap<>();
    private Map<String, String> gcName = new HashMap<>();
    private Long duration;
    private Long operations;
    private List<Long> durationBigCycle;
    private Long avgDurationBigCycle;
    private Long avgOperationsPerSecond;


    GcStatistic() {
        collectorsEvent = new HashMap<>();
    }

    void registerEvent(GarbageCollectionNotificationInfo info) {
        GcEvent gcEvent = new GcEvent();
        String gctype = getGctype(info);
        gcEvent.setGcType(gctype);
        gcEvent.setGcName(info.getGcName());
        gcEvent.setStartTime(info.getGcInfo().getStartTime());
        gcEvent.setDuration(info.getGcInfo().getDuration());

        if (collectorsEvent.containsKey(gctype)) {
            collectorsEvent.get(gctype).add(gcEvent);
        } else {
            collectorsEvent.put(gctype, new ArrayList<>(Collections.singletonList(gcEvent)));
        }
    }

    synchronized void printStatistics() {
        calcStatistic();
        System.out.println("Duration of 100_000 operations : " + ((float) avgDurationBigCycle / 1000) + " sec; ");
        System.out.println("Operations per second: " + avgOperationsPerSecond + "; ");
        System.out.println("Count of operations : " + operations + "; ");
        System.out.println("Time of life application: " + (duration / 1000) + " sec.;");
        System.out.println();
        collectorsEvent.forEach((key, value) ->
                System.out.println("Generation type:" + key + "; \n"
                        + "GC name: " + gcName.get(key) + "; \n"
                        + " Count of collections: " + value.size() + "; \n"
                        + " Average count: " + avgCollections.get(key) + " per min.; \n"
                        + " Average duration: " + (avgDuration.get(key) / 1000) + " s. per collection; \n"
                        + " Total Duration: " + (totalTime.get(key) / 1000) + " sec.; \n"
                        + "------------------------"));
    }

    private void calcStatistic() {
        for (Map.Entry<String, List<GcEvent>> entry : collectorsEvent.entrySet()) {
            int count = entry.getValue().size();
            float avgDur = ((float) entry.getValue().stream().mapToInt(g -> (int) g.getDuration()).sum()) / count;
            float avgCol = ((count / ((float) (duration / 60_000))));
            float stw = entry.getValue().stream().mapToInt(g -> (int) g.getDuration()).sum();
            avgDuration.put(entry.getKey(), avgDur);
            avgCollections.put(entry.getKey(), avgCol);
            totalTime.put(entry.getKey(), stw);
            gcName.put(entry.getKey(), entry.getValue().get(0).getGcName());
        }
        List<Long> timeOfCycle = getDiff(durationBigCycle);

        avgDurationBigCycle = (timeOfCycle.stream().mapToLong(Long::longValue).sum() / timeOfCycle.size());
        avgOperationsPerSecond = (long)(100_000.0/((float) avgDurationBigCycle / 1000.0));

    }

    private List<Long> getDiff(List<Long> list) {
        List<Long> result = new ArrayList<>();
        long previousValue = 0;
        for (long value : list) {
            if (previousValue > 0) result.add(value - previousValue);
            previousValue = value;
        }
        return result;
    }

    private String getGctype(GarbageCollectionNotificationInfo info) {
        String gctype = info.getGcAction();
        if ("end of minor GC".equals(gctype)) {
            gctype = "Young";
        } else if ("end of major GC".equals(gctype)) {
            gctype = "Old";
        } else {
            gctype = "Unknown";
        }
        return gctype;
    }

    void setDuration(Long duration) {
        this.duration = duration;
    }


    void setOperations(Long operations) {
        this.operations = operations;
    }

    public void setDurationBigCycle(List<Long> durationBigCycle) {
        this.durationBigCycle = durationBigCycle;
    }
}
