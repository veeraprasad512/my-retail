package com.vpc.rest.api.myretail.controller;

import com.vpc.rest.api.myretail.exception.ApiException;
import com.vpc.rest.api.myretail.model.CurrentPrice;
import com.vpc.rest.api.myretail.model.PriceData;
import com.vpc.rest.api.myretail.model.ProductDetailsResponse;
import com.vpc.rest.api.myretail.service.PriceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class MyRetailControllerTest {

    @InjectMocks
    MyRetailController myRetailController;

    @Mock
    PriceService priceService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(myRetailController).build();
    }

    @Test
    public void getProductDetailsTest() throws Exception {
        ProductDetailsResponse productDetailsResponse =  getProductDetailsResponse();
        when(priceService.getProductDetails("123456")).thenReturn(productDetailsResponse);

        MvcResult mockresults = mockMvc.perform(get("/myretail/v1/products/123456").accept(MediaType.APPLICATION_JSON))
                .andReturn();
        mockMvc.perform(asyncDispatch(mockresults))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value("123456"))
                .andExpect(jsonPath("$.name").value("Car"))
                .andExpect(jsonPath("$.current_price.value").value(59955.44))
                .andExpect(jsonPath("$.current_price.currency_code").value("USD"))
                .andReturn();
    }

    @Test
    public void getProductDetailsExceptionTest() throws Exception {
        ProductDetailsResponse productDetailsResponse =  getProductDetailsResponse();
        when(priceService.getProductDetails("123456")).thenThrow(ApiException.class);

        MvcResult mockresults = mockMvc.perform(get("/myretail/v1/products/123456").accept(MediaType.APPLICATION_JSON))
                .andReturn();
        mockMvc.perform(asyncDispatch(mockresults))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void updateProductDetailsTest() throws Exception {
        PriceData priceData =  getProductPriceDetails();
        when(priceService.updateProductPrice(any())).thenReturn(priceData);

        mockMvc.perform(put("/myretail/v1/products/123456").contentType(MediaType.APPLICATION_JSON_VALUE).content(postResponseBody()))
                .andExpect(status().isOk()).andReturn();
    }

    public ProductDetailsResponse getProductDetailsResponse(){
        CurrentPrice currentPrice = new CurrentPrice(59955.44,"USD");
        ProductDetailsResponse productDetailsResponse = new ProductDetailsResponse("123456","Car",currentPrice,null);
        return productDetailsResponse;
    }

    public PriceData getProductPriceDetails(){
        PriceData priceData = new PriceData("123456",new CurrentPrice(84555.55,"USD"));
        return priceData;

    }

    private String postResponseBody() {
        return "{\n" +
                "    \"id\": \"123456\",\n" +
                "    \"name\": \"Car\",\n" +
                "    \"current_price\": {\n" +
                "        \"value\": 59955.44,\n" +
                "        \"currency_code\": \"USD\"\n" +
                "    }\n" +
                "}";
    }

}
