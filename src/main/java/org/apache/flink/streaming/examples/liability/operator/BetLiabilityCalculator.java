package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.examples.liability.data.*;
import org.apache.flink.util.Collector;

public class BetLiabilityCalculator implements FlatMapFunction<BetEvent, BetLiability> {
    @Override
    public void flatMap(
            BetEvent betEvent,
            Collector<BetLiability> collector) throws Exception {
        BetLiability betLiability = new BetLiability();

        float stakeDelta;

        if (betEvent.state == BetState.ACTIVE) {
            stakeDelta = calculateStakeDeltaForActiveBet(betEvent.bet);
        } else {
            stakeDelta = calculateStakeDeltaForFinalBet(betEvent.bet);
        }

        betLiability.betEvent = betEvent;
        betLiability.currentLiability = stakeDelta;

        collector.collect(betLiability);
    }

    private float calculateStakeDeltaForActiveBet(Bet bet) {
        if (betIsPartialCashout(bet)) {
            return -(bet.stake - bet.currentStake);
        } else {
            return bet.currentStake;
        }
    }

    private float calculateStakeDeltaForFinalBet(Bet bet) {
        if (bet.status == BetStatus.CASHED_OUT) {
            return -(bet.stake / 2.0f);
        } else {
            return -bet.stake;
        }
    }

    private boolean betIsPartialCashout(Bet bet) {
        return bet.changeInTerms.stream().anyMatch(changeInTerms -> changeInTerms.reasonCode == Reason.PARTIAL_CASHOUT);
    }
}
