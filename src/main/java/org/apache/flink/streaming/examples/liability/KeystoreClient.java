package org.apache.flink.streaming.examples.liability;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class KeystoreClient {
    private Set<String> keys;
    public KeystoreClient() {
        keys = new HashSet<String>();
    }

    public void close() {

    }

    public CompletableFuture<Boolean> registerKey(String key) {
        if(!keys.contains(key)) {
            keys.add(key);
            return CompletableFuture.completedFuture(true);
        }

        return CompletableFuture.completedFuture(false);
    }
}
