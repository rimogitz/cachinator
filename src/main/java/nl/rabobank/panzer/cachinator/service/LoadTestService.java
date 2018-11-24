package nl.rabobank.panzer.cachinator.service;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.panzer.cachinator.model.ProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoadTestService {

    @NonNull
    @Autowired
    private KeyService keyService;

    @NonNull
    @Autowired
    private CachinatorService cacheService;

    /**
     * Length of the string value to be written in each key
     */
    @Getter
    private int valueLength = 0;

    /**
     * The value to be written in each key itself. Generated dynamically before each test.
     */
    private String value;

    /**
     * Delay between the first and second step of each iteration of the test
     */
    @Getter
    private int delay;

    /**
     * A class to hold information about the status of the loadtest.
     */
    private ProcessStatus status = new ProcessStatus();

    private List<LoadTest1Worker> workers = new ArrayList<>();

    /**
     * Given a length a random value us generated using chars and numbers
     *
     * @param length
     */
    public void generateValue(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz"
                + "0123456789";
        value = new Random().ints(length, 0, chars.length())
                .mapToObj(i -> "" + chars.charAt(i))
                .collect(Collectors.joining());
        log.info("Generated value for this run: {}", value);
        valueLength = length;
    }

    public void setDelay(int delay) {
        this.delay = delay;
        for (LoadTest1Worker worker : workers) {
            worker.setDelay(delay);
        }
    }

    public boolean loadtest1(int valueLength, int repeat, int threads) {
        // execute loadtest
        if (!status.startTest())
            return false;

        generateValue(valueLength);

        workers.clear();
        status.setNumThreads(threads);

        // initiate threads number of threads and start the hitmiss loop in each one of them
        // Runnable.run()

        for (int i = 0; i < threads; i++) {
            LoadTest1Worker worker = new LoadTest1Worker(this, cacheService, keyService, repeat, value, i, delay);
            workers.add(worker);
//            System.out.println("STARTING THREAD " + i);
            new Thread(worker).start();
        }

        return true;
    }

    public String getConfig() {
        return "Delay: " + this.getDelay() + "; Value Length: " + this.getValueLength();
    }

    public ProcessStatus getProcessStatus() {
        status.clearStats();
        for (LoadTest1Worker worker : workers)
            status.mergeWith(worker.getStatus());

        status.calculateTps();
        return status;
    }

    public void stopExecution() {
        if (status.isRunning()) {
            for (LoadTest1Worker worker : workers) {
                worker.setKeepRunning(false);
            }
        }
    }

    public synchronized void executionFinished(int threadNum) {
        boolean stillRunning = false;
        for (LoadTest1Worker worker : workers)
            stillRunning &= worker.getStatus().isRunning();

        if (!stillRunning)
            status.stopTest();
    }

    public void setWindowSize(int size) {
        status.setEventsListLimit(size);
    }

    public int getWindowSize() {
        return status.getEventsListLimit();
    }
}
