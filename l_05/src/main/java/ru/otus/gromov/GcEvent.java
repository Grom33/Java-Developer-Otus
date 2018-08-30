package ru.otus.gromov;

public class GcEvent {
    private String gcType;
    private String gcName;
    private long startTime;
    private long duration;

    public String getGcName() {
        return gcName;
    }

    public void setGcName(String gcName) {
        this.gcName = gcName;
    }

    public static int compareStartTime(GcEvent gce1, GcEvent gce2) {
        return Long.compare(gce1.getStartTime(), gce2.getStartTime());
    }

    public String getGcType() {
        return gcType;
    }

    public void setGcType(String gcType) {
        this.gcType = gcType;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
