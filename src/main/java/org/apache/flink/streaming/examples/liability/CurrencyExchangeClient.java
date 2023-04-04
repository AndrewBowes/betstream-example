package org.apache.flink.streaming.examples.liability;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CurrencyExchangeClient {
    private Map<String, Float> exchangeRates;

    public CurrencyExchangeClient() {

    }

    public CompletableFuture<Float> getExchangeRate(String currencyCode) {
        if(exchangeRates == null) {
            System.out.println(String.format("Requested exchange rate for currency code %s", currencyCode));
            exchangeRates = createExchangeRates();
        }

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
