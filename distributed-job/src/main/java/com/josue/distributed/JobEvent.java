package com.josue.distributed;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Josue on 26/07/2016.
 */
public class JobEvent implements Serializable {

    private final String masterId;
    private final String id;
    private final int start;
    private final int end;

    public JobEvent(String masterId, int start, int end) {
        this.masterId = masterId;
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.start = start;
        this.end = end;
    }

    public String getMasterId() {
        return masterId;
    }

    public String getId() {
        return id;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobEvent)) return false;

        JobEvent jobEvent = (JobEvent) o;

        if (start != jobEvent.start) return false;
        if (end != jobEvent.end) return false;
        if (masterId != null ? !masterId.equals(jobEvent.masterId) : jobEvent.masterId != null) return false;
        return id != null ? id.equals(jobEvent.id) : jobEvent.id == null;

    }

    @Override
    public int hashCode() {
        int result = masterId != null ? masterId.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + start;
        result = 31 * result + end;
        return result;
    }
}
