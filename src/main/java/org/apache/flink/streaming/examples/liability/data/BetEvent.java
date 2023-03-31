package org.apache.flink.streaming.examples.liability.data;

public class BetEvent {
    public static BetEvent[] betEvents = new BetEvent[] {
            createBetEvent(1, 1, BetStatus.ACTIVE, 1, 10, "gbp", "ny"), // 10 * 1.24 = 12.4
            createBetEvent(1, 1, BetStatus.ACTIVE, 1, 10, "gbp", "ny"), // 10 * 1.24 = 12.4
            createBetEvent(1, 2, BetStatus.ACTIVE, 1, 15, "cad", "wa"), // 15 * 0.74 = 11.1
            createBetEvent(1, 3, BetStatus.ACTIVE, 2, 5, "eur", "fl"), // 5 * 1.09 = 5.45
            createBetEvent(1, 4, BetStatus.ACTIVE, 3, 20, "jpy", "ca"), // 20 * 0.0075 = 0.15
            createBetEvent(2, 1, BetStatus.CASHED_OUT, 1, 10, "gbp", "ny"),
            createBetEvent(2, 2, BetStatus.SETTLED, 2, 5, "eur", "fl")
    };
    public int sequenceNumber;
    public int betId;
    public int selectionId;
    public float stake;
    public String state;
    public String currency;
    public BetStatus status;
    public boolean duplicate;

    private static BetEvent createBetEvent(int sequenceNumber, int betId, BetStatus status, int selectionId, float stake, String currency, String state) {
        BetEvent betEvent = new BetEvent();
        betEvent.sequenceNumber = sequenceNumber;
        betEvent.betId = betId;
        betEvent.status = status;
        betEvent.selectionId = selectionId;
        betEvent.stake = stake;
        betEvent.currency = currency;
        betEvent.state = state;
        betEvent.duplicate = false;

        return betEvent;
    }
}
