package nl.rabobank.panzer.cachinator.rest;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import nl.rabobank.panzer.cachinator.model.ProcessStatus;
import nl.rabobank.panzer.cachinator.model.ProcessStatusResponse;
import nl.rabobank.panzer.cachinator.service.LoadTestService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LoadTestRestService {
    @NonNull
    private LoadTestService loadTestService;

    @RequestMapping(method = RequestMethod.POST, path="/process/delay/{delay}")
    public String setDelay(@PathVariable("delay") int delay) {
        loadTestService.setDelay(delay);
        return loadTestService.getConfig();
    }

    @RequestMapping(method = RequestMethod.GET, path="/process/delay")
    public int getDelay() {
        return loadTestService.getDelay();
    }

    @RequestMapping(method=RequestMethod.POST, path="/process/loadtest1/{valueLength}/{repeat}/{threads}")
    public String loadtest1(@PathVariable("valueLength") int valueLength, @PathVariable(value = "repeat") int repeat, @PathVariable(value = "threads") int threads) {
//        loadTestService.generateValue(valueLength);
        if (loadTestService.loadtest1(valueLength, repeat, threads))
            return "Load Test 1 started";
        else
            return "Another test is in place already. Use /process/status endpoint to get info on that.";
    }

    @RequestMapping(method = RequestMethod.GET, path="/process/status")
    public ProcessStatusResponse getProcessStatus() {
        return loadTestService.getProcessStatus().getStatus();
    }

    @RequestMapping(method = RequestMethod.PUT, path="/process/status/window/{windowSize}")
    public String setWindowSize(@PathVariable(value = "windowSize") int size) {
        loadTestService.setWindowSize(size);
        return "Window size set to " + size;
    }

    @RequestMapping(method = RequestMethod.GET, path="/process/status/window")
    public int setWindowSize() {
        return loadTestService.getWindowSize();
    }

    @RequestMapping(method = RequestMethod.POST, path="/process/stop")
    public String stopProcess() {
        loadTestService.stopExecution();
        return "Execution stop signal sent!";
    }

}
