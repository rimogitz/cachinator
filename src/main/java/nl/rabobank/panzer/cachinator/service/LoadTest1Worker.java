package nl.rabobank.panzer.cachinator.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.panzer.cachinator.model.ProcessStatus;


@Slf4j
public class LoadTest1Worker implements Runnable {

    @Setter
    private boolean keepRunning = true;

    @Getter
    private int threadNum;
    private int repetitions;

    private CachinatorService cacheService;
    private KeyService keyService;
    private LoadTestService parentService;

    /**
     * The value to be written in each key itself. Generated dynamically before each test.
     */
    private String value;

    /**
     * Delay between the first and second step of each iteration of the test
     */
    @Getter
    @Setter
    private int delay;

    @Getter
    private ProcessStatus status = new ProcessStatus();

    public LoadTest1Worker(LoadTestService parentService, CachinatorService cacheService, KeyService keyService, int repeat, String value, int threadNum, int delay) {
//        System.out.println("INSTANTIATING WORKER " + threadNum + " WITH " + repeat + " REPETITIONS");
        this.cacheService = cacheService;
        this.keyService = keyService;
        this.value = value;
        this.threadNum = threadNum;
        this.repetitions = repeat;
        this.delay = delay;
        this.parentService = parentService;
    }

    private long putKey(String key, String value) {
        long startTime = System.nanoTime();
        cacheService.store(key, value);
        return System.nanoTime() - startTime;
    }

    private long getKey(String key) {
        long startTime = System.nanoTime();
        cacheService.retrieve(key);
        return System.nanoTime() - startTime;
    }

    private void hitmiss() {
        String key = keyService.getNewStrKey();
        long elapsedTime = putKey(key, value);
        long timestamp = System.currentTimeMillis();
        System.out.println(timestamp + "; Thread " + threadNum + "; CX_UPDATE; " + (float)elapsedTime/1000000);

        status.incElapsedTime(elapsedTime);
        elapsedTime = getKey(key);
        timestamp = System.currentTimeMillis();
        System.out.println(timestamp + "; Thread " + threadNum + "; CX_READ_HIT; " + (float)elapsedTime/1000000);
        status.incElapsedTime(elapsedTime);

        status.incOps(2);
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                //Do nothing
            }
        }

        String key2 = keyService.getNewStrKey();
        elapsedTime = getKey(key2);
        timestamp = System.currentTimeMillis();
        System.out.println(timestamp + "; Thread " + threadNum + "; CX_READ_MISS; " + (float)elapsedTime/1000000);
        status.incElapsedTime(elapsedTime);

        elapsedTime = putKey(key2, value);
        timestamp = System.currentTimeMillis();
        System.out.println(timestamp + "; Thread " + threadNum + "; CX_UPDATE; " + (float)elapsedTime/1000000);
        status.incElapsedTime(elapsedTime);

        status.incOps(2);
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                //Do nothing
            }
        }
    }

    @Override
    public void run() {
//        System.out.println("RUNNING THREAD " + threadNum);
        if (repetitions > 0) {
            for (int i = 0; i < repetitions; i++) {
                hitmiss();
            }
        } else {
            while (keepRunning) {
                hitmiss();
            }
            keepRunning = true; //to enable the next continuous run
        }
        status.stopTest();
        parentService.executionFinished(threadNum);
    }
}
