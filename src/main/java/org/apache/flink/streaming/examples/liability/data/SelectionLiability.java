package org.apache.flink.streaming.examples.liability.data;

import java.util.Map;

public class SelectionLiability {
    public int selectionId;
    public float liability;
    public Map<String, Float> stateLiability;

    @Override
    public String toString() {
        return "selectionId: " + selectionId + " liability: " + liability + " states: " + stateLiability;
    }
}
