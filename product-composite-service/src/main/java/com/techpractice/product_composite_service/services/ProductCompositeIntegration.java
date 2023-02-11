package com.techpractice.product_composite_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techpractice.api.core.product.Product;
import com.techpractice.api.core.product.ProductService;
import com.techpractice.api.core.recommendation.Recommendation;
import com.techpractice.api.core.recommendation.RecommendationService;
import com.techpractice.api.core.review.Review;
import com.techpractice.api.core.review.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {

    private final static Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);
    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProductCompositeIntegration(RestTemplate restTemplate, ObjectMapper objectMapper,
                                       @Value("${app.product-service.host}") String productServiceHost,
                                       @Value("${app.product-service.port}") int    productServicePort,
                                       @Value("${app.recommendation-service.host}") String recommendationServiceHost,
                                       @Value("${app.recommendation-service.port}") int    recommendationServicePort,
                                       @Value("${app.review-service.host}") String reviewServiceHost,
                                       @Value("${app.review-service.port}") int    reviewServicePort) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product/";
        recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation?productId=";
        reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";
    }

    @Override
    public Product getProduct(int productId) {
        LOG.info("Retrieving Product Information for productId: {}", productId);
        String url = productServiceUrl + productId;
        LOG.info("Product Service URL: {}", url);
        Product product = restTemplate.getForObject(url, Product.class);
        LOG.info("Product info received!!!");
        return product;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        String url = recommendationServiceUrl + productId;
        LOG.info("Recommendation Service URL: {}", url);
        List<Recommendation> recommendationList = restTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Recommendation>>(){}).getBody();
        return recommendationList;
    }

    @Override
    public List<Review> getReviews(int productId) {
        String url = reviewServiceUrl + productId;
        List<Review> reviewList = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Review>>() {}).getBody();
        return reviewList;
    }
}
