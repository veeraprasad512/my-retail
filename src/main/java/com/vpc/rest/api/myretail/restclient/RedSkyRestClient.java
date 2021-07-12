package com.vpc.rest.api.myretail.restclient;

import com.vpc.rest.api.myretail.constants.MyTargetRetailConstants;
import com.vpc.rest.api.myretail.exception.ApiException;
import com.vpc.rest.api.myretail.model.RedSkyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class RedSkyRestClient {

    private static final Logger log = LoggerFactory.getLogger(RedSkyRestClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${redSkyTarget.url:https://redsky.target.com/v3/pdp/tcin/}")
    private String redSkyTargetUrl;

    public String invokeRedSkyApi (String productId) throws ApiException {

        String title;
        ResponseEntity<RedSkyResponse> responseEntity = null;
        String finalUrl = new StringBuilder(redSkyTargetUrl).append(productId).append(MyTargetRetailConstants.QUERY_PARAM).toString();

        try {
            // Call product-data service to retrieve product name
            log.debug("Url is : {}", finalUrl);
            responseEntity = restTemplate.getForEntity(finalUrl, RedSkyResponse.class);

            RedSkyResponse redSkyResponse = responseEntity.getBody();

            title = redSkyResponse.getProduct().getItem().getProductDescription().getTitle();

        } catch (RestClientException e) {
            throw new ApiException("Error from Target RedSky service : " + e.getMessage());
        } catch (Exception e) {
            throw new ApiException("Product Name not found : " + e.getMessage());
        }
        return title;
    }
}
