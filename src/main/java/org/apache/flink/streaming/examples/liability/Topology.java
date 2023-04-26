package org.apache.flink.streaming.examples.liability;

import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.examples.liability.data.BetEvent;
import org.apache.flink.streaming.examples.liability.data.BetLiability;
import org.apache.flink.streaming.examples.liability.data.SelectionLiability;
import org.apache.flink.streaming.examples.liability.operator.*;

import java.util.concurrent.TimeUnit;

public class Topology {
    public static DataStream<SelectionLiability> flow(DataStream<BetEvent> betStream, boolean currencyConvert) {
        DataStream<BetLiability> betLiabilities = betStream
                .filter(new BetStatusFilter()) // only bet state of ACTIVE,FINAL REMAIN
                .flatMap(new BetLiabilityCalculator()) // Calculates raw liability of incoming message and maps to new betLiability
                .keyBy(value -> value.getKey()); // Key is betID - liability

        // Action of trying to find any existing keys in datastore and drop message if liability is same (dedup solution)
        DataStream<BetLiability> betLiabilityUpdates = AsyncDataStream.unorderedWait(betLiabilities, new AsyncBetLiabilityComparator(), 1000, TimeUnit.MILLISECONDS, 100);

        // converts stake values within bet events to system currency if required
        if(currencyConvert) {
            betLiabilityUpdates = AsyncDataStream.unorderedWait(betLiabilityUpdates, new AsyncBetEventCurrencyConverter(), 1000, TimeUnit.MILLISECONDS, 100);
        }

        return betLiabilityUpdates
                .flatMap(new SelectionLiabilityConverter()) // Calculates the change in liability due to new message - FBL logic will be performed here
                .keyBy(value -> value.selectionId) // partition based on selection id to allow for
                .reduce(new SelectionLiabilityReduce()); // the cumulative liability to be computed
    }
}
