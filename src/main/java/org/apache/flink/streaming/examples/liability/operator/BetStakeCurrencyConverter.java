package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.examples.liability.CurrencyExchangeService;
import org.apache.flink.streaming.examples.liability.data.BetEvent;

public class BetStakeCurrencyConverter implements MapFunction<BetEvent, BetEvent> {
    private CurrencyExchangeService currencyExchangeService;
    public BetStakeCurrencyConverter(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    @Override
    public BetEvent map(BetEvent betEvent) throws Exception {
        BetEvent stakeConverted = new BetEvent();
        stakeConverted.stake = currencyExchangeService.convert(betEvent.stake, betEvent.currency);
        stakeConverted.selectionId = betEvent.selectionId;
        stakeConverted.state = betEvent.state;
        stakeConverted.status = betEvent.status;

        return stakeConverted;
    }
}
