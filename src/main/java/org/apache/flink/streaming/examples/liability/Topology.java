package org.apache.flink.streaming.examples.liability;

import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.examples.liability.data.BetEvent;
import org.apache.flink.streaming.examples.liability.data.SelectionLiability;
import org.apache.flink.streaming.examples.liability.operator.*;

import java.util.concurrent.TimeUnit;

public class Topology {
    private static ActiveDC activeDC = new ActiveDC(true);

    public static DataStream<SelectionLiability> flow(DataStream<BetEvent> betStream) {
        DataStream<BetEvent> betLiabilities = betStream
                .filter(new ActiveDCFilter(activeDC))
                .keyBy(value -> value.betId + "-" + value.status.name().substring(0, 1))
                .flatMap(new BetEventDeduplicator());

        return AsyncDataStream.unorderedWait(betLiabilities, new AsyncCurrencyExchangeRequest(), 1000, TimeUnit.MILLISECONDS, 100)
                .flatMap(new SelectionLiabilityCalculator())
                .keyBy(value -> value.selectionId)
                .reduce(new SelectionLiabilityReduce());
    }
}
