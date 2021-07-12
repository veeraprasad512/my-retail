package com.vpc.rest.api.myretail.service;

import com.vpc.rest.api.myretail.model.PriceData;
import com.vpc.rest.api.myretail.model.ProductDetailsResponse;

public interface PriceService {

    ProductDetailsResponse getProductDetails(String Id);

    PriceData updateProductPrice(ProductDetailsResponse productDetailsResponse);

}
