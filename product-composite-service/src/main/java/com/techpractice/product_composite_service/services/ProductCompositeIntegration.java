package com.techpractice.product_composite_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techpractice.api.core.product.Product;
import com.techpractice.api.core.product.ProductService;
import com.techpractice.api.core.recommendation.Recommendation;
import com.techpractice.api.core.recommendation.RecommendationService;
import com.techpractice.api.core.review.Review;
import com.techpractice.api.core.review.ReviewService;
import com.techpractice.utils.exceptions.InvalidInputException;
import com.techpractice.utils.exceptions.NotFoundException;
import com.techpractice.utils.http.HttpErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

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
        try {
            LOG.info("Retrieving Product Information for productId: {}", productId);
            String url = productServiceUrl + productId;
            LOG.info("Product Service URL: {}", url);
            Product product = restTemplate.getForObject(url, Product.class);

            LOG.info("Product info received!!!");
            return product;
        }catch (HttpClientErrorException exception){

            HttpStatusCode statusCode = exception.getStatusCode();
            if (NOT_FOUND.equals(statusCode)) {
                throw new NotFoundException(getErrorMessage(exception));
            } else if (UNPROCESSABLE_ENTITY.equals(statusCode)) {
                throw new InvalidInputException(getErrorMessage(exception));
            }
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", exception.getStatusCode());
            LOG.warn("Error body: {}", exception.getResponseBodyAsString());
            throw exception;
        }
    }

    private String getErrorMessage(HttpClientErrorException exception) {
        try{
            return objectMapper.readValue(exception.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioException){
            return ioException.getMessage();
        }
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        try {
            String url = recommendationServiceUrl + productId;
            LOG.debug("Will call Recommendation Service on URL {}", url);
            List<Recommendation> recommendationList = restTemplate.exchange(url, HttpMethod.GET,
                    null, new ParameterizedTypeReference<List<Recommendation>>() {
                    }).getBody();
            LOG.debug("Found {} recommendation for productId {}",recommendationList.size(),productId);;
            return recommendationList;
        }catch (Exception exception){
            LOG.warn("Got an exception while requesting recommendations, " +
                    "return zero recommendations: {}", exception.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Review> getReviews(int productId) {
        try {
            String url = reviewServiceUrl + productId;
            LOG.debug("Will call Review Service on URL {}", url);
            List<Review> reviewList = restTemplate.exchange(url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Review>>() {
                    }).getBody();
            LOG.debug("Found {} reviews for productId {}",reviewList.size(),productId);
            return reviewList;
        }catch (Exception exception){
            LOG.warn("Got an exception while requesting reviews, return zero reviews: {}", exception.getMessage());
            return new ArrayList<>();
        }
    }
}
