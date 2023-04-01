package org.apache.flink.streaming.examples.liability;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.examples.liability.data.BetEvent;
import org.apache.flink.streaming.examples.liability.data.SelectionLiability;
import org.apache.flink.streaming.examples.liability.operator.*;

public class Topology {
    private static ActiveDC activeDC = new ActiveDC(true);
    private static CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService(CurrencyExchangeService.createExchangeRates());
    public static DataStream<SelectionLiability> flow(DataStream<BetEvent> betStream) {
        return betStream
                .filter(new ActiveDCFilter(activeDC))
                .keyBy(value -> value.betId + "-" + value.status.name())
                .flatMap(new BetEventDeduplicator())
                .map(new BetStakeCurrencyConverter(currencyExchangeService))
                .flatMap(new SelectionLiabilityCalculator())
                .keyBy(value -> value.selectionId)
                .reduce(new SelectionLiabilityReduce());
    }
}
