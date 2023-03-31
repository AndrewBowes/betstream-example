package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.examples.liability.data.BetEvent;

public class BetEventReduce implements ReduceFunction<BetEvent> {
    @Override
    public BetEvent reduce(BetEvent b1, BetEvent b2) throws Exception {
        BetEvent betEvent = new BetEvent();
        betEvent.duplicate = true;

        return betEvent;
    }
}
