package org.apache.flink.streaming.examples.liability;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CurrencyExchangeService implements Serializable {
    private Map<String, Float> exchangeRates;

    public CurrencyExchangeService(Map<String, Float> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public Map<String, Float> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(Map<String, Float> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public float convert(float value, String currency) {
        return value * exchangeRates.get(currency);
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
