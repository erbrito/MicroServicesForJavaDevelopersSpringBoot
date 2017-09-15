package com.example;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
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

	@Autowired
	private Environment env;
	
    private RestTemplate template = new RestTemplate();
    private String saying;
    private String backendServiceHost;
    private int backendServicePort;
    private String backendServiceContext;
    
    @PostConstruct
    public void postConstruct() {
    	backendServiceContext = env.getProperty("greeting.backendServiceContext", "");
    }
    
    @RequestMapping(value="/greeting", method = RequestMethod.GET, produces = "text/plain")
    public String greeting(){
        String backendServiceUrl = getBackendServiceUrl(backendServiceHost, backendServicePort, backendServiceContext);
        
        BackendDTO response = template.getForObject(backendServiceUrl, BackendDTO.class, saying);
        return response.getGreeting() + " at host: " + response.getIp();
    }
    
    private String getBackendServiceUrl(String host, int port, String context) {
    	return StringUtils.isEmpty(context) 
			? String.format("http://%s:%d/api/backend?greeting={greeting}", host, port)
			: String.format("http://%s:%d/%s/api/backend?greeting={greeting}", host, port, context);
    }

    @RequestMapping(value = "/greeting-circuit-breaker", method = RequestMethod.GET, produces = "text/plain")
    public String greetingHystrix() {
        BackendCommand backendCommand = new BackendCommand(backendServiceHost, backendServicePort, backendServiceContext)
                .withSaying(saying).withTemplate(template);
        BackendDTO response = backendCommand.execute();
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
