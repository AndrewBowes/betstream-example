package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.examples.liability.data.SelectionLiability;
import org.apache.flink.streaming.examples.liability.data.StakeConvertedBet;
import org.apache.flink.util.Collector;

import java.util.HashMap;

public class SelectionLiabilityCalculator implements FlatMapFunction<StakeConvertedBet, SelectionLiability> {
    @Override
    public void flatMap(
            StakeConvertedBet stakeConvertedBet,
            Collector<SelectionLiability> collector) throws Exception {
        SelectionLiability liability = new SelectionLiability();
        liability.selectionId = stakeConvertedBet.selectionId;
        liability.cumulativeLiability = stakeConvertedBet.stake;
        liability.stateLiability = new HashMap<>();
        liability.stateLiability.put(stakeConvertedBet.state, stakeConvertedBet.stake);

        collector.collect(liability);
    }
}
