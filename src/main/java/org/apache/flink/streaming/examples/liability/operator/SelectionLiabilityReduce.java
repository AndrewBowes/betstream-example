package org.apache.flink.streaming.examples.liability.operator;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.examples.liability.data.SelectionLiability;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SelectionLiabilityReduce implements ReduceFunction<SelectionLiability> {
    @Override
    public SelectionLiability reduce(SelectionLiability t1, SelectionLiability t2) throws Exception {
        SelectionLiability t3 = new SelectionLiability();
        t3.cumulativeLiability = t1.cumulativeLiability + t2.cumulativeLiability;
        t3.selectionId = t1.selectionId;
        t3.stateLiability = Stream.concat(t1.stateLiability.entrySet().stream(), t2.stateLiability.entrySet().stream()).collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (value1, value2) -> value1 + value2));

        return t3;
    }
}