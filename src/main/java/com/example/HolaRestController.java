package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ebrito on 2017-04-21.
 */
@RestController
@RequestMapping("/api")
@ConfigurationProperties(prefix="helloapp")
public class HolaRestController {
    private String saying;
    @RequestMapping(method = RequestMethod.GET, value = "/hola", produces = "text/plain")
    public String hola() throws UnknownHostException {
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().getHostAddress();

        }catch (UnknownHostException e){
            hostname = "unknown";
        }
        return saying + " " + hostname;
    }
    public String getSaying() {
        return saying;
    }

    public void setSaying(String saying) {
        this.saying = saying;
    }
}
