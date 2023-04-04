package org.apache.flink.streaming.examples.liability;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.flink.streaming.examples.liability.data.BetEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class AsyncKeystoreRequest extends RichAsyncFunction<BetEvent, BetEvent> {
    private KeystoreClient client;

    @Override
    public void open(Configuration parameters) throws Exception {
        client = new KeystoreClient();
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

    @Override
    public void asyncInvoke(BetEvent betEvent, ResultFuture<BetEvent> resultFuture) throws Exception {
        final Future<Boolean> newKey = client.registerKey(betEvent.getKey());

        CompletableFuture.supplyAsync(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                try {
                    return newKey.get();
                } catch (InterruptedException | ExecutionException e) {
                    // Normally handled explicitly.
                    return null;
                }
            }
        }).thenAccept((Boolean nk) -> {
            Collection<BetEvent> betEvents;
            if(nk) {
                betEvents = Collections.singleton(betEvent);
            } else {
                betEvents = Collections.emptyList();
            }

            resultFuture.complete(betEvents);
        });
    }
}
