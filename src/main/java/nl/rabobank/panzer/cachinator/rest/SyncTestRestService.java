package nl.rabobank.panzer.cachinator.rest;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import nl.rabobank.panzer.cachinator.service.SyncTestService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SyncTestRestService {
    @NonNull
    private SyncTestService syncTestService;

    @RequestMapping(method = RequestMethod.POST, path="/sync/write/{valueLength}/{repeat}")
    public String write(@PathVariable("valueLength") int valueLength, @PathVariable("repeat") int repeat) {
        if (syncTestService.writeTest(valueLength, repeat))
            return "Sync Test Write started!";
        else
            return "Another test is in place already.";
    }

    @RequestMapping(method = RequestMethod.POST, path="/sync/read/{delay}/{timeout}")
    public String read(@PathVariable("delay") int delay, @PathVariable("timeout") int timeout) {
        if (syncTestService.readTest(delay, timeout))
            return "Sync Test Read started!";
        else
            return "Another test is in place already.";
    }

    @RequestMapping(method = RequestMethod.POST, path="/sync/stop")
    public String stopProcess() {
        syncTestService.stopTest();
        return "Execution stop signal sent!";
    }

}
