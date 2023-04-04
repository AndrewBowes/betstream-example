package org.apache.flink.streaming.examples.liability;

import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.examples.liability.data.BetEvent;
import org.apache.flink.streaming.examples.liability.data.SelectionLiability;
import org.apache.flink.streaming.examples.liability.operator.*;

import java.util.concurrent.TimeUnit;

public class Topology {
    public static DataStream<SelectionLiability> flow(DataStream<BetEvent> betStream, boolean currencyConvert) {
        DataStream<BetEvent> betEvents = betStream
                .filter(new BetStatusFilter())
                .keyBy(value -> value.getKey());

        DataStream<BetEvent> distinctBetEvents = AsyncDataStream.unorderedWait(betEvents, new AsyncKeystoreRequest(), 1000, TimeUnit.MILLISECONDS, 100);

        if(currencyConvert) {
            distinctBetEvents = AsyncDataStream.unorderedWait(distinctBetEvents, new AsyncCurrencyExchangeRequest(), 1000, TimeUnit.MILLISECONDS, 100);
        }

        return distinctBetEvents
                .flatMap(new SelectionLiabilityCalculator())
                .keyBy(value -> value.selectionId)
                .reduce(new SelectionLiabilityReduce());
    }
}
