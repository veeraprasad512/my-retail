package com.vpc.rest.api.myretail.restclient;

import com.vpc.rest.api.myretail.exception.ApiException;
import com.vpc.rest.api.myretail.model.Item;
import com.vpc.rest.api.myretail.model.Product;
import com.vpc.rest.api.myretail.model.ProductDescription;
import com.vpc.rest.api.myretail.model.RedSkyResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RedSkyRestClientTest {

    @Mock
    RestTemplate restTemplateMock;

    @Mock
    ResponseEntity<RedSkyResponse> responseEntityMock;

    @InjectMocks
    RedSkyRestClient redSkyApiMock;

    @Test
    public void invokeRedSkyApi(){
        ReflectionTestUtils.setField(redSkyApiMock, "redSkyTargetUrl", "http://testurl:8888/");
        given(restTemplateMock.getForEntity(anyString(), ArgumentMatchers.eq(RedSkyResponse.class))).willReturn(responseEntityMock);
        given(responseEntityMock.getBody()).willReturn(responseBody());
        String title = redSkyApiMock.invokeRedSkyApi("1234");
        assertEquals("Car",title);

    }

    @Test(expected= ApiException.class)
    public void invokeRedSkyApiException(){
        ReflectionTestUtils.setField(redSkyApiMock, "redSkyTargetUrl", "http://testurl:8888/");
        given(restTemplateMock.getForEntity(anyString(), any())).willThrow(ApiException.class);
        String title = redSkyApiMock.invokeRedSkyApi("1234");
        assertEquals("Car",title);

    }

    private RedSkyResponse responseBody() {
        ProductDescription productDescription = new ProductDescription();
        Item item = new Item();
        Product product = new Product();
        RedSkyResponse redSkyApi = new RedSkyResponse();
        productDescription.setTitle("Car");
        item.setProductDescription(productDescription);
        product.setItem(item);
        redSkyApi.setProduct(product);
        return redSkyApi;
    }
}
