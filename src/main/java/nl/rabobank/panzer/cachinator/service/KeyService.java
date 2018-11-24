package nl.rabobank.panzer.cachinator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KeyService {

    private static final int PREFIX_SIZE = 5;
    private int key = 0;
    private String prefix;

    public KeyService () {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz"
                + "0123456789";
        prefix = new Random().ints(PREFIX_SIZE, 0, chars.length())
                .mapToObj(i -> "" + chars.charAt(i))
                .collect(Collectors.joining());
        log.info("Generated key prefix for this run: {}", prefix);
    }

    private int getNewKey() {
        return ++key;
    }

    String getNewStrKey() {
        return "" + prefix + getNewKey();
    }

}
