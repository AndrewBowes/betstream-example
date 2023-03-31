package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.examples.liability.CurrencyExchangeService;
import org.apache.flink.streaming.examples.liability.data.Bet;
import org.apache.flink.streaming.examples.liability.data.StakeConvertedBet;

public class BetStakeCurrencyConverter implements MapFunction<Bet, StakeConvertedBet> {
    private CurrencyExchangeService currencyExchangeService;
    public BetStakeCurrencyConverter(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    @Override
    public StakeConvertedBet map(Bet bet) throws Exception {
        StakeConvertedBet stakeConverted = new StakeConvertedBet();
        stakeConverted.stake = currencyExchangeService.convert(bet.stake, bet.currency);
        stakeConverted.selectionId = bet.selectionId;
        stakeConverted.state = bet.state;

        return stakeConverted;
    }
}
