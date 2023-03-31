package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.examples.liability.CurrencyExchangeService;
import org.apache.flink.streaming.examples.liability.data.BetEvent;
import org.apache.flink.streaming.examples.liability.data.StakeConvertedBetEvent;

public class BetStakeCurrencyConverter implements MapFunction<BetEvent, StakeConvertedBetEvent> {
    private CurrencyExchangeService currencyExchangeService;
    public BetStakeCurrencyConverter(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    @Override
    public StakeConvertedBetEvent map(BetEvent betEvent) throws Exception {
        StakeConvertedBetEvent stakeConverted = new StakeConvertedBetEvent();
        stakeConverted.stake = currencyExchangeService.convert(betEvent.stake, betEvent.currency);
        stakeConverted.selectionId = betEvent.selectionId;
        stakeConverted.state = betEvent.state;
        stakeConverted.status = betEvent.status;

        return stakeConverted;
    }
}
