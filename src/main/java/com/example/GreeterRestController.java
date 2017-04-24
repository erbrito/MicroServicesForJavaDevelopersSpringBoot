package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by ebrito on 2017-04-21.
 */
@RestController
@RequestMapping("/api")
@ConfigurationProperties(prefix="greeting")
public class GreeterRestController {

    private RestTemplate template = new RestTemplate();
    private String saying;
    private String backendServiceHost;
    private int backendServicePort;

    @RequestMapping(value="/greeting",
    method = RequestMethod.GET,
    produces = "text/plain")
    public String greeting(){
        String backendServiceUrl = String.format(
                "http://%s:%d/api/backend?greeting={greeting}",
                backendServiceHost, backendServicePort);
        BackendDTO response = template.getForObject(
                backendServiceUrl, BackendDTO.class, saying
        );
        return response.getGreeting() + " at host: " + response.getIp();
    }

    public String getSaying() {
        return saying;
    }

    public void setSaying(String saying) {
        this.saying = saying;
    }

    public String getBackendServiceHost() {
        return backendServiceHost;
    }

    public void setBackendServiceHost(String backendServiceHost) {
        this.backendServiceHost = backendServiceHost;
    }



    public int getBackendServicePort() {
        return backendServicePort;
    }

    public void setBackendServicePort(int backendServicePort) {
        this.backendServicePort = backendServicePort;
    }



}
