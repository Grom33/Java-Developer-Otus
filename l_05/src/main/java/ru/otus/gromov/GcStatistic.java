package ru.otus.gromov;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.util.*;

class GcStatistic {
    private Map<String, List<GcEvent>> collectorsEvent;

    GcStatistic() {
        collectorsEvent = new HashMap<>();
    }

    void registerEvent(GarbageCollectionNotificationInfo info) {
        GcEvent gcEvent = new GcEvent();
        String gctype = getGctype(info);
        gcEvent.setGcType(gctype);

        gcEvent.setStartTime(info.getGcInfo().getStartTime());
        gcEvent.setDuration(info.getGcInfo().getDuration());

        if (collectorsEvent.containsKey(gctype)) {
            collectorsEvent.get(gctype).add(gcEvent);
        } else {
            collectorsEvent.put(gctype, new ArrayList<>(Collections.singletonList(gcEvent)));
        }
    }

    private String getGctype(GarbageCollectionNotificationInfo info) {
        String gctype = info.getGcAction();
        if ("end of minor GC".equals(gctype)) {
            gctype = "Young Gen GC";
        } else if ("end of major GC".equals(gctype)) {
            gctype = "Old Gen GC";
        }
        return gctype;
    }

    synchronized void printStatistics() {
        Map<String, Float> avgDuration = new HashMap<>();
        Map<String, Float> avgCollections = new HashMap<>();
        Map<String, Float> totalTimeSTW = new HashMap<>();

        for (Map.Entry<String, List<GcEvent>> entry : collectorsEvent.entrySet()) {
            Long endTime = entry.getValue().stream().max(GcEvent::compareStartTime).orElse(new GcEvent()).getStartTime();
            int count = entry.getValue().size();
            float avgDur = ((float) entry.getValue().stream().mapToInt(g -> (int) g.getDuration()).sum()) / count;
            float avgCol = ((((float) (endTime / 60_000)) / count));
            float stw = entry.getValue().stream().mapToInt(g -> (int) g.getDuration()).sum();
            avgDuration.put(entry.getKey(), avgDur);
            avgCollections.put(entry.getKey(), avgCol);
            totalTimeSTW.put(entry.getKey(), stw);
        }
        System.out.println();
        collectorsEvent.forEach((key, value) ->
                System.out.println("Collections name:" + key + ";"
                        + " Count of collections: " + value.size() + "; "
                        + " Avg count: " + avgCollections.get(key) + " per min.;"
                        + " Avg duration: " + (avgDuration.get(key) / 1000) + " s. per collection"
                        + " Total time STW: " + (totalTimeSTW.get(key) / 1000) + " sec."));
    }
}
