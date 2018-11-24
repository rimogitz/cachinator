package nl.rabobank.panzer.cachinator.service;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.panzer.cachinator.model.Puko;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SyncTestService {

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

    private boolean testRunning = false;
    private boolean keepRunning = true;

    /**
     * Delay between the reads
     */
//    @Getter
//    private int delay;

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

    private void write() {
        long key = System.currentTimeMillis()/1000 + 1;

        //wait until there
        try {
            // wait until the beginning of the next second
//            long nanoTime = System.nanoTime();
            long nanoTime = System.currentTimeMillis();
//            long delayMs = (nanoTime/1000000000 + 1)*1000 - nanoTime/1000000;
            long delayMs = (nanoTime/1000 + 1)*1000 - nanoTime;
            Thread.sleep(delayMs);

            //write it to the database
            cacheService.store(Long.toString(key),value);
            log.info("Wrote key " + key + " in the db at " + System.currentTimeMillis()/1000);
        } catch (InterruptedException e) {
            log.error("ERROR waiting to write the key " + key + " to the database");
        }

    }

    public boolean writeTest(int valueLength, int repeat) {
        if (testRunning)
            return false;

        testRunning = true;

        generateValue(valueLength);

        if (repeat > 0) {
            for (int i = 0; i < repeat; i++) {
                write();
            }
        } else {
            while (keepRunning) {
                write();
            }
            keepRunning = true; //to enable the next continuous run
        }

        // test is not running anymore
        testRunning = false;

        // household cleaning
        this.valueLength = 0;
        value = "";

        return true;
    }

    public boolean readTest(final int delay, final int timeout) {
        if (testRunning)
            return false;

        testRunning = true;

        while(keepRunning) {
            final long startTime = System.nanoTime();
            final long key = System.currentTimeMillis()/1000 + 1; //this is the key to search for in this loop in seconds

            new Thread(() -> {
//                log.info("[" + System.currentTimeMillis() + "] New thread spanned for key " + key + " with delay " + delay + " and timeout " + timeout);
                boolean reachedTimeout = false, found = false;
                long finishTime = System.nanoTime()/1000000 + timeout;

                while(!reachedTimeout && !found) {
//                    final long start = System.nanoTime();
                    long currentTimeMillis = System.currentTimeMillis();
                    Puko result = cacheService.retrieve(Long.toString(key));
                    if (result != null) {
                        // found it
                        long pukoTime = result.getTimestamp().getTime();
                        log.info("[" + System.currentTimeMillis() + "] Found " + key + " @" + currentTimeMillis + " -> Diff: " + (currentTimeMillis - key*1000));
                        found = true;
                    };

                    //calculate if we reached the timeout
                    if (System.nanoTime()/1000000 >= finishTime) {
                        reachedTimeout = true;
                        log.info("[" + System.currentTimeMillis() + "] Key " + key + " not found after " + timeout + "ms. Giving up now.");
                    } else {
                        // if we did not reach timeout and delay is greater than 0, we wait for the delay
                        if (delay > 0) {
                            try {
                                Thread.sleep(delay);
                            } catch (InterruptedException e) {
                            }
                        }
                    }

//                    log.info("delay: " + (System.nanoTime() - start));
                }
//                log.info("[" + System.currentTimeMillis() + "] Thread " + key + " finished.");
            }).start();

            // wait until the start of the next second to span a new Thread
            long delayMs = (startTime/1000000000 + 1)*1000 - System.nanoTime()/1000000;
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
            }
        }

        keepRunning = true;

        // test is not running anymore
        testRunning = false;

        return true;
    }

    public void stopTest() {
        keepRunning = false;
    }
}
