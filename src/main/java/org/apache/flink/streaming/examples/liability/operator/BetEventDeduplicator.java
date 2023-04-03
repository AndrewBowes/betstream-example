package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.examples.liability.data.BetEvent;
import org.apache.flink.util.Collector;

public class BetEventDeduplicator extends RichFlatMapFunction<BetEvent, BetEvent> {
    private ValueState<Boolean> keyProcessed;

    @Override
    public void open(Configuration conf) {
        ValueStateDescriptor<Boolean> desc = new ValueStateDescriptor<>("keyProcessed", Types.BOOLEAN);
        keyProcessed = getRuntimeContext().getState(desc);
    }

    @Override
    public void flatMap(BetEvent betEvent, Collector<BetEvent> collector) throws Exception {
        if(keyProcessed.value() == null) {
            collector.collect(betEvent);
            keyProcessed.update(true);
        }
    }
}
