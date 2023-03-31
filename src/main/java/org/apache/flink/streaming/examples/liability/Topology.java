package org.apache.flink.streaming.examples.liability;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.examples.liability.data.Bet;
import org.apache.flink.streaming.examples.liability.data.SelectionLiability;
import org.apache.flink.streaming.examples.liability.operator.ActiveDCFilter;
import org.apache.flink.streaming.examples.liability.operator.SelectionLiabilityCalculator;
import org.apache.flink.streaming.examples.liability.operator.BetStakeCurrencyConverter;
import org.apache.flink.streaming.examples.liability.operator.SelectionLiabilityReduce;

public class Topology {
    private static ActiveDC activeDC = new ActiveDC(true);
    private static CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService(CurrencyExchangeService.createExchangeRates());
    public static DataStream<SelectionLiability> flow(DataStream<Bet> betStream) {
        return betStream
                .filter(new ActiveDCFilter(activeDC))
                .map(new BetStakeCurrencyConverter(currencyExchangeService))
                .flatMap(new SelectionLiabilityCalculator())
                .keyBy(value -> value.selectionId)
                .reduce(new SelectionLiabilityReduce());
    }
}
