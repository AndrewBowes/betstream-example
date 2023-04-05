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
                .filter(new BetStatusFilter()) // filter out bet events with a status that isn't ACTIVE, CASHED_OUT or SETTLED
                .keyBy(value -> value.getKey()); // might not be necessary dependening on the implementation of the AsyncBetEventDeduplicator

        // discards any subsequent bet events with a status that has already been processed for a given bet
        DataStream<BetEvent> distinctBetEvents = AsyncDataStream.unorderedWait(betEvents, new AsyncBetEventDeduplicator(), 1000, TimeUnit.MILLISECONDS, 100);

        // converts stake values within bet events to system currency if required
        if(currencyConvert) {
            distinctBetEvents = AsyncDataStream.unorderedWait(distinctBetEvents, new AsyncBetEventCurrencyConverter(), 1000, TimeUnit.MILLISECONDS, 100);
        }

        return distinctBetEvents
                .flatMap(new SelectionLiabilityCalculator()) // calculates bet-level liability in respect to a given selection - FBL logic will be performed here
                .keyBy(value -> value.selectionId) // partition based on selection id to allow for
                .reduce(new SelectionLiabilityReduce()); // the cumulative liability to be computed
    }
}
