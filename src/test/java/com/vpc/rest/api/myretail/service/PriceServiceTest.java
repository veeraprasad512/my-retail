package com.vpc.rest.api.myretail.service;

import com.vpc.rest.api.myretail.exception.ApiException;
import com.vpc.rest.api.myretail.model.CurrentPrice;
import com.vpc.rest.api.myretail.model.PriceData;
import com.vpc.rest.api.myretail.model.ProductDetailsResponse;
import com.vpc.rest.api.myretail.repository.PricingRepository;
import com.vpc.rest.api.myretail.restclient.RedSkyRestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PriceServiceTest {

    @Mock
    RedSkyRestClient redSkyRestClient;

    @InjectMocks
    PriceServiceImpl priceService;

    @Mock
    PricingRepository pricingRepository;

    @Test
    public void getProductDetailsTest(){

        given(redSkyRestClient.invokeRedSkyApi(anyString())).willReturn("Car");
        PriceData priceData = new PriceData("1234", new CurrentPrice(5999.99,"USD"));
        given(pricingRepository.getPriceData(anyString())).willReturn(priceData);
        ProductDetailsResponse productDetailsResponse=priceService.getProductDetails("1234");
        assertEquals("1234",productDetailsResponse.getId());
        assertEquals("Car",productDetailsResponse.getName());
        assertEquals("USD",productDetailsResponse.getCurrentPrice().getCurrencyCode());

    }

    @Test(expected= ApiException.class)
    public void getProductDetailsTestException(){

        given(redSkyRestClient.invokeRedSkyApi(anyString())).willThrow(ApiException.class);
        ProductDetailsResponse productDetailsResponse=priceService.getProductDetails("1234");

    }

    @Test(expected= ApiException.class)
    public void getProductDetailsTestSQlException(){

        given(redSkyRestClient.invokeRedSkyApi(anyString())).willReturn("Car");
        PriceData productPrizeDetails = new PriceData("1234", new CurrentPrice(5999.99,"USD"));
        given(pricingRepository.getPriceData(anyString())).willReturn(null);
        ProductDetailsResponse productDetailsResponse=priceService.getProductDetails("1234");
        assertEquals("1234",productDetailsResponse.getId());
        assertEquals("Car",productDetailsResponse.getName());
        assertEquals("USD",productDetailsResponse.getCurrentPrice().getCurrencyCode());

    }
}
