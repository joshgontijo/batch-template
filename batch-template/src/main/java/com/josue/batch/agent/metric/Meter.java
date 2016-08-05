package com.josue.batch.agent.metric;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Created by Josue on 04/08/2016.
 */
public class Meter {

    private final Map<String, MeterItem> averages = new ConcurrentHashMap<>();
    private final boolean enabled;

    public Meter(boolean enabled) {
        this.enabled = enabled;
    }

    public void start(String key) {
        if (!enabled) {
            return;
        }
        MeterItem average = averages.get(key);
        if (average == null) {
            averages.put(key, new MeterItem());
        }
        averages.get(key).start();
    }

    public void end(String key) {
        if (!enabled) {
            return;
        }
        MeterItem average = averages.get(key);
        if (average == null) {
            return;
        }
        averages.get(key).end();
    }

    public boolean isEnabled() {
        return enabled;
    }

    Map<String, Long> average() {
        return averages.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> {
                            MeterItem value = e.getValue();
                            if (value.counter.get() == 0) {
                                return 0L;
                            }
                            return value.time.get() / value.counter.get();
                        }
                ));
    }

    class MeterItem {
        private long start = 0;
        final AtomicLong time = new AtomicLong();
        final AtomicLong counter = new AtomicLong();

        void start() {
            this.start = System.currentTimeMillis();
            counter.incrementAndGet();
        }

        void end() {
            time.addAndGet(System.currentTimeMillis() - start);
        }
    }

}
