package com.stockini.easyproducts.tools;

import com.stockini.easyproducts.dtos.ProductDetail;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.WebSearchResults;
import dev.langchain4j.web.search.tavily.TavilyWebSearchEngine;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ProductSearchTool {
    


    private final WebSearchEngine searchEngine;

    public ProductSearchTool(String apiKey) {
        this.searchEngine = TavilyWebSearchEngine.builder()
                .apiKey(apiKey)
                .build();
    }

    @Tool("Search the web for detailed information about a product, including description, images, and URLs")
    public ProductDetail searchProduct(String productName) {

        log.info("Searching for product: {} ✅", productName);

        WebSearchResults searchResults = searchEngine.search(productName);

        if (searchResults.results() != null && !searchResults.results().isEmpty()) {
            var first = searchResults.results().get(0); // this is a WebSearchOrganicResult

            ProductDetail detail = new ProductDetail();
            detail.setName(productName);
            detail.setDescription(first.snippet());  // snippet/summary
            detail.setProductUrl(first.url().toString());       // URL
            // detail.setImageUrl(...) // Tavily doesn’t give direct images
            return detail;
        }

        // fallback
        ProductDetail fallback = new ProductDetail();
        fallback.setName(productName);
        fallback.setDescription("No info found");
        fallback.setProductUrl("");
        return fallback;
    }
}
