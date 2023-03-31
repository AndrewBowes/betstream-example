package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.examples.liability.ActiveDC;
import org.apache.flink.streaming.examples.liability.data.Bet;

public class ActiveDCFilter implements FilterFunction<Bet> {
    private ActiveDC activeDC;
    public ActiveDCFilter(ActiveDC activeDC) {
        this.activeDC = activeDC;
    }

    @Override
    public boolean filter(Bet bet) throws Exception {
        return activeDC.isActive();
    }
}
