package com.vpc.rest.api.myretail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "PriceData")
public class PriceData {

    @Id
    private String id;
    private CurrentPrice currentPrice;
}
