package com.stockini.easyproducts.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {

    private String name;
    private String description;
    private String productUrl;
    private String imageUrl;  // optional
    
}
