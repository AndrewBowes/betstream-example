package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.examples.liability.data.BetEvent;

public class DuplicateFilter implements FilterFunction<BetEvent> {
    @Override
    public boolean filter(BetEvent betEvent) throws Exception {
        return !betEvent.duplicate;
    }
}
