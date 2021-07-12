package com.vpc.rest.api.myretail.repository;

import com.vpc.rest.api.myretail.model.CurrentPrice;
import com.vpc.rest.api.myretail.model.PriceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PricingRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void saveProductDetails() {
        List<PriceData> productPrizeDetailsList = new ArrayList<PriceData>();
        productPrizeDetailsList.add(new PriceData("13860428", new CurrentPrice(15.45, "USD")));
        productPrizeDetailsList.add(new PriceData("15117729", new CurrentPrice(13.45, "GBP")));
        productPrizeDetailsList.add(new PriceData("13264003", new CurrentPrice(18.45, "INR")));
        productPrizeDetailsList.add(new PriceData("12954218", new CurrentPrice(21.45, "AUD")));
        for (PriceData priceData : productPrizeDetailsList) {
            mongoTemplate.save(priceData);
        }
    }

    public PriceData getPriceData(String id) {
        return mongoTemplate.findById(id, PriceData.class);
    }

    public PriceData updatePriceData(PriceData priceData) {
        return mongoTemplate.save(priceData);
    }
}
