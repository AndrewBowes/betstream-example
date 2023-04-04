package org.apache.flink.streaming.examples.liability;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.examples.liability.data.BetEvent;
import org.apache.flink.streaming.examples.liability.data.SelectionLiability;
import org.apache.flink.streaming.examples.liability.util.CLI;

public class Main {
    public static void main(String[] args) throws Exception {
        final CLI params = CLI.fromArgs(args);
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(params.getExecutionMode());
        env.getConfig().setGlobalJobParameters(params);

        DataStream<BetEvent> bets = env.fromElements(BetEvent.betEvents).name("in-memory-bets");

        final boolean currencyConvert = true;

        final DataStream<SelectionLiability> selectionLiabilities = Topology.flow(bets, currencyConvert);

        selectionLiabilities.print();

        env.execute();
    }
}
