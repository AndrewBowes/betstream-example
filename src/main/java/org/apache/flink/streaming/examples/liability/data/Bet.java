package org.apache.flink.streaming.examples.liability.data;

import java.util.List;

public class Bet {

    public int betId;
    public int selectionId;
    public float stake; // Doesn't change after partial cash out
    public float currentStake;
    public float payout;
    public float potentialWin;
    public String destination;
    public String currency;
    public BetStatus status;
    public BetResult result;
    public List<ChangeInTerms> changeInTerms;
    public Bet(
            int betId, int selectionId, float stake, float currentStake, float payout,
            float potentialWin, String destination, String currency, BetStatus status, BetResult result, List<ChangeInTerms> changeInTerms
    ) {
        this.betId = betId;
        this.selectionId = selectionId;
        this.stake = stake;
        this.currentStake = currentStake;
        this.payout = payout;
        this.potentialWin = potentialWin;
        this.destination = destination;
        this.currency = currency;
        this.status = status;
        this.result = result;
        this.changeInTerms = changeInTerms;
    }


}
