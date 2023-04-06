package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.flink.streaming.examples.liability.CurrencyExchangeClient;
import org.apache.flink.streaming.examples.liability.data.BetEvent;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class AsyncBetEventCurrencyConverter extends RichAsyncFunction<BetEvent, BetEvent> {
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
    public void asyncInvoke(BetEvent betEvent, ResultFuture<BetEvent> resultFuture) throws Exception {
        final Future<Float> exchangeRate = client.getExchangeRate(betEvent.currency);

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
            BetEvent currencyConverted = new BetEvent();
            currencyConverted.selectionId = betEvent.selectionId;
            currencyConverted.betId = betEvent.betId;
            currencyConverted.currency = betEvent.currency;
            currencyConverted.destination = betEvent.destination;
            currencyConverted.status = betEvent.status;
            currencyConverted.stake = betEvent.stake * er;

            resultFuture.complete(Collections.singleton(currencyConverted));
        });
    }
}
