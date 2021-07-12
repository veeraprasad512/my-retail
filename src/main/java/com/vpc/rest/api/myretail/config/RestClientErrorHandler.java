package com.vpc.rest.api.myretail.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class RestClientErrorHandler implements ResponseErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(RestClientErrorHandler.class);

    public RestClientErrorHandler() {
    }

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return isError(clientHttpResponse.getStatusCode());
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        log.error("Response error: {} {}", clientHttpResponse.getStatusCode(), clientHttpResponse.getStatusText());
    }

    public static boolean isError(HttpStatus status) {
        HttpStatus.Series series = status.series();
        return HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series);
    }
}
