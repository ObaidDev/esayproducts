package com.stockini.easyproducts.services;


import com.stockini.easyproducts.dtos.ProductResponse;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ProductExtractionAgent {

    @SystemMessage("""
        You are a product extraction and enrichment assistant.
        Step 1: Extract product information from text (name, quantity, category, price).
                If price is not mentioned, use 0.
        Always return valid JSON that matches the ProductResponse schema.
    """)
    ProductResponse extractAndEnrich(@UserMessage String inventoryText);
}
