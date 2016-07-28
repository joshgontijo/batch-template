package com.josue.distributed;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by Josue on 26/07/2016.
 */
public class JobEvent implements Serializable {

    private final String masterId;
    private final String id;

    private final Properties data = new Properties();

    public JobEvent(String masterId) {
        this.masterId = masterId;
        this.id = UUID.randomUUID().toString().substring(0, 8);
    }

    public String getMasterId() {
        return masterId;
    }

    public String getId() {
        return id;
    }

    public void put(String key, String value) {
        this.data.setProperty(key, value);
    }

    public String get(String key) {
        return this.data.getProperty(key);
    }

    public Properties wrapProperties(){
        Properties props = new Properties();
        props.setProperty("masterId", masterId);
        props.setProperty("id", id);
        props.putAll(data);
        return props;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobEvent)) return false;

        JobEvent jobEvent = (JobEvent) o;

        if (masterId != null ? !masterId.equals(jobEvent.masterId) : jobEvent.masterId != null) return false;
        if (id != null ? !id.equals(jobEvent.id) : jobEvent.id != null) return false;
        return data != null ? data.equals(jobEvent.data) : jobEvent.data == null;

    }

    @Override
    public int hashCode() {
        int result = masterId != null ? masterId.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }
}
