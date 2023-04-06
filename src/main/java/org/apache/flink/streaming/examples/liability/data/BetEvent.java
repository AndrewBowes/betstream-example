package org.apache.flink.streaming.examples.liability.data;

import org.apache.flink.streaming.examples.liability.CurrencyExchangeClient;

import java.util.Map;
import java.util.Random;

public class BetEvent {
    /*public static BetEvent[] betEvents = new BetEvent[] {
            createBetEvent(1, BetStatus.ACTIVE, 1, 10, "gbp", "ny"), // 10 * 1.24 = 12.4
            createBetEvent(1, BetStatus.ACTIVE, 1, 10, "gbp", "ny"), // 10 * 1.24 = 12.4
            createBetEvent(1, BetStatus.ACTIVE, 1, 10, "gbp", "ny"), // 10 * 1.24 = 12.4

            createBetEvent(2, BetStatus.ACTIVE, 1, 15, "cad", "wa"), // 15 * 0.74 = 11.1
            createBetEvent(2, BetStatus.ACTIVE, 1, 15, "cad", "wa"), // 15 * 0.74 = 11.1

            createBetEvent(3, BetStatus.ACTIVE, 2, 5, "eur", "fl"), // 5 * 1.09 = 5.45
            createBetEvent(3, BetStatus.ACTIVE, 2, 5, "eur", "fl"), // 5 * 1.09 = 5.45

            createBetEvent(4, BetStatus.ACTIVE, 3, 20, "jpy", "ca"), // 20 * 0.0075 = 0.15
            createBetEvent(4, BetStatus.ACTIVE, 3, 20, "jpy", "ca"), // 20 * 0.0075 = 0.15

            createBetEvent(1, BetStatus.CASHED_OUT, 1, 10, "gbp", "ny"),
            createBetEvent(1, BetStatus.CASHED_OUT, 1, 10, "gbp", "ny"),

            createBetEvent(2, BetStatus.SETTLED, 2, 5, "eur", "fl"),
            createBetEvent(2, BetStatus.SETTLED, 2, 5, "eur", "fl")
    };*/
    public static BetEvent[] betEvents = generateRandomBetEvents(100, 5);
    public int betId;
    public int selectionId;
    public float stake;
    public String destination;
    public String currency;
    public BetStatus status;

    public String getKey() {
        return String.format("%s-%s", betId, status.name().substring(0, 1));
    }

    private static BetEvent createBetEvent(int betId, BetStatus status, int selectionId, float stake, String currency, String destination) {
        BetEvent betEvent = new BetEvent();
        betEvent.betId = betId;
        betEvent.status = status;
        betEvent.selectionId = selectionId;
        betEvent.stake = stake;
        betEvent.currency = currency;
        betEvent.destination = destination;

        return betEvent;
    }

    private static BetEvent[] generateRandomBetEvents(int count, int selectionCount) {
        BetEvent[] betEvents = new BetEvent[count];
        String[] currencyCodes = new String[] { "cad", "gbp", "eur", "jpy" };
        String[] destinations = new String[] { "ca", "ny", "wa", "fl" };
        Random random = new Random();
        int currentCurrencyCodeIndex = 0;
        int currentDestinationIndex = 0;

        for(int i = 0; i < count; i+=1) {
            betEvents[i] = createBetEvent(
                    random.nextInt(99999),
                    BetStatus.ACTIVE,
                    random.nextInt(selectionCount),
                    random.nextInt(100),
                    currencyCodes[currentCurrencyCodeIndex],
                    destinations[currentDestinationIndex]
            );

            currentCurrencyCodeIndex += 1;
            if(currentCurrencyCodeIndex >= currencyCodes.length) {
                currentCurrencyCodeIndex = 0;
            }

            currentDestinationIndex += 1;
            if(currentDestinationIndex >= destinations.length) {
                currentDestinationIndex = 0;
            }
        }

        return betEvents;
    }
}
