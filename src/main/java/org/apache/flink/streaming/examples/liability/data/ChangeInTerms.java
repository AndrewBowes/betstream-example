package org.apache.flink.streaming.examples.liability.data;

public class ChangeInTerms {

    public int changeNo;
    public Reason reasonCode;

    public ChangeInTerms(int changeNo, Reason reasonCode) {
        this.changeNo = changeNo;
        this.reasonCode = reasonCode;
    }
}

