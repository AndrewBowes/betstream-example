package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.flink.streaming.examples.liability.data.BetLiability;

import org.apache.flink.streaming.examples.liability.KeystoreClient;

import java.util.Collection;
import java.util.Collections;


public class AsyncBetLiabilityComparator extends RichAsyncFunction<BetLiability, BetLiability> {

    private KeystoreClient client;

    @Override
    public void open(Configuration parameters) throws Exception {
        client = new KeystoreClient();
    }

    @Override
    public void asyncInvoke(BetLiability betLiability, ResultFuture<BetLiability> resultFuture) throws Exception {
        String betId = String.valueOf(betLiability.betEvent.bet.betId);
        final String existingKey = client.retrieveExistingKeyForBet(betId).get();


        Collection<BetLiability> betLiabilities;


        if (existingKey != null) {
            betLiability.storedLiability = Float.parseFloat(existingKey.substring(existingKey.indexOf(":") + 1));

            if (betLiability.currentLiability == betLiability.storedLiability) {
                betLiabilities = Collections.emptyList();
            } else {
                client.updateFile(betId, betLiability.currentLiability);
                betLiabilities =  Collections.singleton(betLiability);
            }

        } else {
            client.updateFile(betId, betLiability.currentLiability);
            betLiabilities = Collections.singleton(betLiability);
        }

        resultFuture.complete(betLiabilities);
    }
}
