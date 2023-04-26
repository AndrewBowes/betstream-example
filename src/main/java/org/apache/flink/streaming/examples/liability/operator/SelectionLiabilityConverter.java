package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.examples.liability.data.BetLiability;
import org.apache.flink.streaming.examples.liability.data.SelectionLiability;
import org.apache.flink.util.Collector;

import java.util.HashMap;

public class SelectionLiabilityConverter implements FlatMapFunction<BetLiability, SelectionLiability> {
    @Override
    public void flatMap(
            BetLiability betLiability,
            Collector<SelectionLiability> collector) throws Exception {

        SelectionLiability selectionLiability = new SelectionLiability();
        selectionLiability.selectionId = betLiability.betEvent.bet.selectionId;
        selectionLiability.liability = betLiability.currentLiability;

        selectionLiability.destinationLiability = new HashMap<>();
        selectionLiability.destinationLiability.put(betLiability.betEvent.bet.destination, selectionLiability.liability);

        collector.collect(selectionLiability);
    }
}
