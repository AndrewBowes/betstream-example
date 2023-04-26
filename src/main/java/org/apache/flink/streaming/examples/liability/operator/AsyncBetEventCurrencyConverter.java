package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.flink.streaming.examples.liability.CurrencyExchangeClient;
import org.apache.flink.streaming.examples.liability.data.*;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class AsyncBetEventCurrencyConverter extends RichAsyncFunction<BetLiability, BetLiability> {
    private transient CurrencyExchangeClient client;

    @Override
    public void open(Configuration parameters) throws Exception {
        client = new CurrencyExchangeClient();
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

    @Override
    public void asyncInvoke(BetLiability betLiability, ResultFuture<BetLiability> resultFuture) throws Exception {
        final Future<Float> exchangeRate = client.getExchangeRate(betLiability.betEvent.bet.currency);

        CompletableFuture.supplyAsync(new Supplier<Float>() {
            @Override
            public Float get() {
                try {
                    return exchangeRate.get();
                } catch (InterruptedException | ExecutionException e) {
                    // Normally handled explicitly.
                    return null;
                }
            }
        }).thenAccept( (Float er) -> {
            BetEvent currencyConvertedBetEvent = new BetEvent();
            BetLiability currencyConverted = new BetLiability();

            Bet bet = betLiability.betEvent.bet;
            Bet currencyConvertedBet = new Bet(
                    bet.betId, bet.selectionId, bet.stake * er, bet.currentStake * er, bet.payout * er, bet.potentialWin * er, bet.destination,
                    bet.currency, bet.status, bet.result, bet.changeInTerms
            );
            currencyConvertedBetEvent.bet = bet;
            currencyConvertedBetEvent.state = betLiability.betEvent.state;

            currencyConverted.betEvent = currencyConvertedBetEvent;
            currencyConverted.currentLiability = betLiability.currentLiability * er;
            currencyConverted.storedLiability = betLiability.storedLiability * er;


            resultFuture.complete(Collections.singleton(currencyConverted));
        });
    }
}
