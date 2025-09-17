package com.stockini.easyproducts.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String name;
    private int quantity;
    private String category;
    private Long price;
    private String description;
    private String imageUrl;   
    private String productUrl; 
    
}
