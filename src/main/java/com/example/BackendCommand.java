package com.example;

import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * Created by ebrito on 2017-04-25.
 */
public class BackendCommand extends HystrixCommand<BackendDTO>{
	
    private String host;
    private int port;
    private String context;
    private String saying;
    private RestTemplate template;

    public BackendCommand(String host, int port, String context) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("wildflyswarm.backend"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(10)
                        .withMaxQueueSize(-1))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerEnabled(true)
                        .withCircuitBreakerRequestVolumeThreshold(5)
                        .withMetricsRollingStatisticalWindowInMilliseconds(5000)
                ))
        ;
        this.host = host;
        this.port = port;
        this.context = context;
    }

    public BackendCommand withSaying(String saying) {
        this.saying = saying;
        return this;
    }

    public BackendCommand withTemplate(RestTemplate template) {
        this.template = template;
        return this;
    }




    @Override
    protected BackendDTO run() throws Exception {
        String backendServiceUrl = UrlUtil.getBackendServiceUrl(host, port, context);
        System.out.println("Sending to: " + backendServiceUrl);
        return template.getForObject(backendServiceUrl, BackendDTO.class, saying);

    }
    
    @Override
    protected BackendDTO getFallback() {
        BackendDTO rc = new BackendDTO();
        rc.setGreeting("Greeting fallback!");
        rc.setIp("127.0.0,1");
        rc.setTime(System.currentTimeMillis());
        return rc;
    }
}
