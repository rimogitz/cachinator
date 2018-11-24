package nl.rabobank.panzer.cachinator.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ProcessStatus implements Serializable {

    @Getter
    private String status;

    @Getter
    private long operations;

    @Getter
    private long elapsedTimeMillis;

    private long startTime;
    private long endTime;

    @Getter
    private Date startTimeDate;


    @Getter
    private double tps;

    @Getter
    @Setter
    private int numThreads;

    @Getter
    private Date endTimeDate;

    @Getter
    private double transactionElapsedTime;

    @Getter
    private double averageTransactionTime;

    @Getter
    private List<Long> events = new LinkedList<>();

    private int windowNumOps;
    private double windowTps;

    @Getter
    private int eventsListLimit;

    public ProcessStatus () {
        status = "Not started";
        operations = 0;
        startTime = 0;
        endTime = 0;
        tps = 0;
        transactionElapsedTime = 0;
        averageTransactionTime = 0;
        windowNumOps = 0;
        windowTps = 0;
        numThreads = 0;
        eventsListLimit = 10000;
    }

    public boolean startTest () {
        if (isRunning()) {
            return false;
        }
        status = "Running";
        startTime = System.currentTimeMillis();
        startTimeDate = new Date(startTime);
        endTime = 0;
        endTimeDate = new Date(0);
        elapsedTimeMillis = 0;
        operations = 0;
        transactionElapsedTime = 0;
        averageTransactionTime = 0;
        events.clear();
        windowNumOps = 0;
        windowTps = 0;
        return true;
    }

    public void setEventsListLimit(int size) {
        eventsListLimit = size;
        trimEventsList();
    }

    public boolean stopTest () {
        if (!isRunning()) {
            return false;
        }
        status = "Finished";
        endTime = System.currentTimeMillis();
        endTimeDate = new Date(endTime);
        return true;
    }

    public void incOps(int ops) {
        operations += ops;
    }

    public boolean isRunning () {
        return (status == "Running");
    }

    public void calculateTps() {
        if (!isRunning()) {
            elapsedTimeMillis = endTime - startTime;
        } else {
            elapsedTimeMillis = System.currentTimeMillis() - startTime;
        }
        if (elapsedTimeMillis > 0) {
            averageTransactionTime =  transactionElapsedTime / (double) operations;
            tps = (double) operations / ((double)(elapsedTimeMillis) / 1000);
//            windowNumOps = events.size();
//            for (long value : events) {
//                windowTps += value;
//            }
//            windowTps /= ((double)(events.size()));
        }
    }

    private synchronized void trimEventsList() {
        if (events.size() >= eventsListLimit) {
            events.subList(0,10).clear();
            events.subList(eventsListLimit-10, events.size()).clear();
        }
    }
    public void incElapsedTime(long elapsedTime) {
        transactionElapsedTime += (double) elapsedTime/1000000;
//        trimEventsList();
//        events.add(elapsedTime/1000000);
    }

    public void mergeWith(ProcessStatus stat) {
        this.operations += stat.getOperations();
        this.transactionElapsedTime += stat.getTransactionElapsedTime();
//        this.events.addAll(stat.getEvents());
//        trimEventsList();
    }

    public void clearStats() {
        operations = 0;
        transactionElapsedTime = 0;
        events.clear();
    }
    
    public ProcessStatusResponse getStatus() {
        return new ProcessStatusResponse(status, operations, elapsedTimeMillis, startTimeDate, endTimeDate, tps,
                numThreads, transactionElapsedTime, averageTransactionTime, windowNumOps, windowTps,
                eventsListLimit, events.size());
    }
    
}
