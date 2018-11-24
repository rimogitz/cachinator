package nl.rabobank.panzer.cachinator.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@AllArgsConstructor
@Slf4j
public class CalendarClockDifferenceRestService {

//    @Value("${SERVICE_PORT:80}")
    private String serverPort = "80";

//    private static final String CACHINATOR_WE_URL = "http://cachinator-redis.apps.pcf-t01-we.rabobank.nl";
//    private static final String CACHINATOR_NE_URL = "http://cachinator-redis.apps.pcf-t01-ne.rabobank.nl";
    @Value("${CACHINATOR_NE_URL:http://cachinator-redis.apps.pcf-t01-ne.rabobank.nl}")
    private String CACHINATOR_NE_URL;

    @Value("${CACHINATOR_WE_URL:http://cachinator-redis.apps.pcf-t01-we.rabobank.nl}")
    private String CACHINATOR_WE_URL;
//    private boolean running = false;

    public CalendarClockDifferenceRestService() {

    }

    @RequestMapping(method = RequestMethod.GET, path="/latency/env")
    public String environment() {
        log.info("ENV");
        System.out.println(System.getenv().toString());
        return System.getenv().toString();
    }

    @RequestMapping(method = RequestMethod.GET, path="/latency/start/{region}")
    public long step1(@PathVariable("region") String region) {
//        log.info("STEP1");
//        System.out.println(System.getenv().values());
//        if (!running) {
//            running = true;
            long currentTimeMillis = System.currentTimeMillis();
            long startNanoTime = System.nanoTime();
            RestTemplate restTemplate = new RestTemplate();
            String url = (region.equals("WE") ? CACHINATOR_NE_URL : CACHINATOR_WE_URL) + ":" + serverPort + "/latency/measure/" + region + "/" + Long.toString(currentTimeMillis);
//            log.info("Calling " + url);
            long remoteTimeStamp = restTemplate.getForObject(url, Long.class);
            long elapsedTimeMilli = System.nanoTime()/1000 - startNanoTime/1000;

            log.info("Start time: " + currentTimeMillis + "; Remote time: " + remoteTimeStamp + "; delta: " + (remoteTimeStamp - currentTimeMillis) + "; elapsed time: " + elapsedTimeMilli);
            return (remoteTimeStamp - currentTimeMillis);
//        } else {
//            running = true;
//            return 0;
//        }
    }

    @RequestMapping(method = RequestMethod.GET, path="/latency/measure/{region}/{remoteTimeStamp}")
    public long step2(@PathVariable("region") String region, @PathVariable("remoteTimeStamp") long remoteTimeStamp) {
//        log.info("STEP2");
        long currentTimeMillis = System.currentTimeMillis();
        log.info("Received message with content " + remoteTimeStamp + " at " + currentTimeMillis);
//        log.info("Replying " + currentTimeMillis);
//        this.step1(region.equals("NE") ? "WE" : "NE");
        return currentTimeMillis;
    }

//    @RequestMapping(method = RequestMethod.POST, path="/latency/stop")
//    public String stop() {
//        running = false;
//        return "Test Stopped!";
//    }


}
