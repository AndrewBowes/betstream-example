package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.examples.liability.data.BetEvent;

public class BetStatusFilter implements FilterFunction<BetEvent> {
    @Override
    public boolean filter(BetEvent betEvent) throws Exception {
        switch(betEvent.state) {
            case ACTIVE:
            case FINAL:
                return true;
            default:
                return false;
        }
    }
}
