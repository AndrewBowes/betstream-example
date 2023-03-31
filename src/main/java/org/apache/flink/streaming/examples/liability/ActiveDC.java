package org.apache.flink.streaming.examples.liability;

import java.io.Serializable;

public class ActiveDC implements Serializable {
    private boolean active;

    public ActiveDC(boolean active) {
        this.active = active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
