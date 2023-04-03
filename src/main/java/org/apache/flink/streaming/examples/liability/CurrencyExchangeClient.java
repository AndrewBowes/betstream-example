package org.apache.flink.streaming.examples.liability;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CurrencyExchangeClient {
    private final Map<String, Float> exchangeRates;

    public CurrencyExchangeClient() {
        exchangeRates = createExchangeRates();
    }

    public CompletableFuture<Float> getExchangeRate(String currencyCode) {
        return CompletableFuture.completedFuture(exchangeRates.get(currencyCode));
    }

    public void close() {

    }

    public static Map<String, Float> createExchangeRates() {
        Map<String, Float> exchangeRates = new HashMap<>();
        exchangeRates.put("cad", 0.74f);
        exchangeRates.put("gbp", 1.24f);
        exchangeRates.put("eur", 1.09f);
        exchangeRates.put("jpy", 0.0075f);

        return exchangeRates;
    }
}
