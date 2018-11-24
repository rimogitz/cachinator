package nl.rabobank.panzer.cachinator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
public class ProcessStatusResponse implements Serializable {
    @Getter
    private String status;

    @Getter
    private long operations;

    @Getter
    private long elapsedTimeMillis;

    @Getter
    private Date startTimeDate;

    @Getter
    private Date endTimeDate;

    @Getter
    private double tps;

    @Getter
    private int numThreads;

    @Getter
    private double transactionElapsedTime;

    @Getter
    private double averageTransactionTime;

    @Getter
    private int windowNumOps;

    @Getter
    private double windowTps;

    @Getter
    private int eventsListLimit;

    @Getter
    private int eventsListSize;
}
