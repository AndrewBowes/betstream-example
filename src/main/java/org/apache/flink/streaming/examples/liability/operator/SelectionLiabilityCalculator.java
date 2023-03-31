package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.examples.liability.data.BetEvent;
import org.apache.flink.streaming.examples.liability.data.SelectionLiability;
import org.apache.flink.util.Collector;

import java.util.HashMap;

public class SelectionLiabilityCalculator implements FlatMapFunction<BetEvent, SelectionLiability> {
    @Override
    public void flatMap(
            BetEvent stakeConvertedBetEvent,
            Collector<SelectionLiability> collector) throws Exception {
        SelectionLiability selectionLiability = new SelectionLiability();
        selectionLiability.selectionId = stakeConvertedBetEvent.selectionId;

        float stakeDelta;
        switch(stakeConvertedBetEvent.status) {
            case ACTIVE:
                stakeDelta = stakeConvertedBetEvent.stake;
                break;
            case CASHED_OUT:
                stakeDelta = -(stakeConvertedBetEvent.stake / 2.0f);
                break;
            case SETTLED:
                stakeDelta = -stakeConvertedBetEvent.stake;
                break;
            default:
                stakeDelta = 0;

        }
        selectionLiability.liability = stakeDelta;
        selectionLiability.stateLiability = new HashMap<>();
        selectionLiability.stateLiability.put(stakeConvertedBetEvent.state, stakeDelta);

        collector.collect(selectionLiability);
    }
}
