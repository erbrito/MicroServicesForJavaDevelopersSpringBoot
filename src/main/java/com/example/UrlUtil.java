package com.example;

import org.springframework.util.StringUtils;

public final class UrlUtil {

	private UrlUtil() {
	}
	
    public static String getBackendServiceUrl(String host, int port, String context) {
    	return StringUtils.isEmpty(context) 
			? String.format("http://%s:%d/api/backend?greeting={greeting}", host, port)
			: String.format("http://%s:%d/%s/api/backend?greeting={greeting}", host, port, context);
    }
	
}
