package com.vpc.rest.api.myretail.controller;

import com.vpc.rest.api.myretail.exception.ApiException;
import com.vpc.rest.api.myretail.model.PriceData;
import com.vpc.rest.api.myretail.model.ProductDetailsResponse;
import com.vpc.rest.api.myretail.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;
import rx.Subscriber;

import java.util.concurrent.Callable;


@RestController
@RequestMapping(value = "/myretail/v1")
public class MyRetailController {

    private static final Logger log = LoggerFactory.getLogger(MyRetailController.class);

    @Autowired
    private PriceService priceService;

    @GetMapping(value="/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<Object> retrieveProductData (@PathVariable String id) throws ApiException {

        DeferredResult<Object> deferredResult = new DeferredResult<>();

        Observable<ProductDetailsResponse> productDetailsObservable = Observable
                .fromCallable(new Callable<ProductDetailsResponse>() {
                    @Override
                    public ProductDetailsResponse call() throws ApiException {
                        /*Invoke the PriceService to get complete productdetails.
                        Internally priceService invokes target RedSky api using
                        RestClient to get productName.
                         */
                        return priceService.getProductDetails(id);

                    }
                });
        Subscriber<ProductDetailsResponse> productDetailsSubscriber = new Subscriber<ProductDetailsResponse>() {

            @Override
            public void onNext(ProductDetailsResponse response) {
                // On successful response from service layer set result in deferred result object
                log.debug("Deferred result has received a response");
                deferredResult.setResult(response);
            }

            @Override
            public void onCompleted() {
                // Can be used to perform post actions
                log.info("Successfully completed");
            }

            @Override
            public void onError(Throwable ex) {
                // Executed on error from the service layer
                ProductDetailsResponse productDetailsResponse = new ProductDetailsResponse(id, null, null, ex.getMessage());
                log.error("Error while retrieving product details for {} is {}",id,ex.getMessage());
                deferredResult.setErrorResult(
                        new ResponseEntity<Object>(productDetailsResponse, HttpStatus.NOT_FOUND));
            }
        };

        productDetailsObservable.subscribe(productDetailsSubscriber);
        return deferredResult;
    }

    @PutMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<Object> updateProductDetails(@PathVariable String id, @RequestBody ProductDetailsResponse productDetailsResponse) throws ApiException {

        DeferredResult<Object> deferredResult = new DeferredResult<>();

        Observable<PriceData> priceDataObservable = Observable
                .fromCallable(new Callable<PriceData>() {
                    @Override
                    public PriceData call() throws ApiException {
                        if(id.equalsIgnoreCase(productDetailsResponse.getId())) {
                            /*Invoke the PriceService to update priceDetails.
                              First invokes the getProduct details API to get the product
                              details and update them.
                            */
                            PriceData priceData= priceService.updateProductPrice(productDetailsResponse);
                            return priceData;
                        }
                        else{
                            throw new ApiException("ID is not matching id in request Body");
                        }

                    }
                });
        Subscriber<PriceData> productDetailsSubscriber = new Subscriber<PriceData>() {
            @Override
            public void onNext(PriceData response) {
                // On successful response from service layer set result in deferred result object
                log.debug("Deferred result has received a response");
                deferredResult.setResult(response); // Result is set to the
                // DeferredResult
            }
            @Override
            public void onError(Throwable ex) {
                // Executed on error from the service layer
                ProductDetailsResponse productDetailsResponse = new ProductDetailsResponse(id,null,null,ex.getMessage());
                log.error("Error while retrieving product details for {} is {}",id,ex.getMessage());
                deferredResult.setErrorResult(
                        new ResponseEntity<Object>(productDetailsResponse, HttpStatus.NOT_FOUND));
            }

            @Override
            public void onCompleted() {
                // Can be used to perform post actions
                log.info("Successfully completed");
            }
        };

        priceDataObservable.subscribe(productDetailsSubscriber);
        return deferredResult;
    }
}
