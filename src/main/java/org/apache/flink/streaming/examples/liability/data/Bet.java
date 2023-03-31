package org.apache.flink.streaming.examples.liability.data;

public class Bet {
    public static Bet[] bets = new Bet[] {
            createBet(1, 1, 10, "gbp", "ny"), // 10 * 1.24 = 12.4
            createBet(2, 1, 15, "cad", "wa"), // 15 * 0.74 = 11.1
            createBet(3, 2, 5, "eur", "fl"), // 5 * 1.09 = 5.45
            createBet(4, 3, 20, "jpy", "ca") // 20 * 0.0075 = 0.15
    };
    public int betId;
    public int selectionId;
    public float stake;
    public String state;
    public String currency;

    private static Bet createBet(int betId, int selectionId, float stake, String currency, String state) {
        Bet bet = new Bet();
        bet.betId = betId;
        bet.selectionId = selectionId;
        bet.stake = stake;
        bet.currency = currency;
        bet.state = state;

        return bet;
    }
}
