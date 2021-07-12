package com.vpc.rest.api.myretail.service;

import com.vpc.rest.api.myretail.exception.ApiException;
import com.vpc.rest.api.myretail.model.CurrentPrice;
import com.vpc.rest.api.myretail.model.PriceData;
import com.vpc.rest.api.myretail.model.ProductDetailsResponse;
import com.vpc.rest.api.myretail.repository.PricingRepository;
import com.vpc.rest.api.myretail.restclient.RedSkyRestClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    @Autowired
    private RedSkyRestClient redSkyRestClient;

    @Autowired
    private PricingRepository priceRepository;

    @Override
    public ProductDetailsResponse getProductDetails(String Id) throws ApiException {

        ProductDetailsResponse productDetailsResponse = null;

        try {
            String productName = redSkyRestClient.invokeRedSkyApi(Id);
            if (StringUtils.isNotBlank(productName)) {
                PriceData priceData = priceRepository.getPriceData(Id);
                productDetailsResponse = new ProductDetailsResponse(Id, productName, priceData.getCurrentPrice(), null);
            }
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage());
        }
        return  productDetailsResponse;
    }

    @Override
    public PriceData updateProductPrice(ProductDetailsResponse productDetailsResponse) {
        PriceData modifiedPriceData;

        PriceData priceData = priceRepository.getPriceData(productDetailsResponse.getId());
        if (priceData != null) {
            CurrentPrice updatedPrice = new CurrentPrice(productDetailsResponse.getCurrentPrice().getValue(),
                    productDetailsResponse.getCurrentPrice().getCurrencyCode());
            modifiedPriceData = new PriceData(productDetailsResponse.getId(), updatedPrice);

            PriceData updatedPriceData = priceRepository.updatePriceData(modifiedPriceData);
        } else {
            throw new ApiException("PriceData is not found in the DB for productID : " + productDetailsResponse.getId());
        }

        return modifiedPriceData;

    }
}
