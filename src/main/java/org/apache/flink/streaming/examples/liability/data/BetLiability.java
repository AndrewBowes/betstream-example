package org.apache.flink.streaming.examples.liability.data;

public class BetLiability {
    public BetEvent betEvent;
    public float currentLiability;
    public float storedLiability;

    public String getKey() {
        return String.format("%s-%s", betEvent.bet.betId, currentLiability);
    }
}
