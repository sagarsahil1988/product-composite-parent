package com.techpractice.product_composite_service.services;

import com.techpractice.api.composite.*;
import com.techpractice.api.core.product.Product;
import com.techpractice.api.core.recommendation.Recommendation;
import com.techpractice.api.core.review.Review;
import com.techpractice.utils.exceptions.NotFoundException;
import com.techpractice.utils.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {

    private final static Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private final ProductCompositeIntegration productCompositeIntegration;

    @Autowired
    public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration productCompositeIntegration) {
        this.serviceUtil = serviceUtil;
        this.productCompositeIntegration = productCompositeIntegration;
    }


    @Override
    public ProductAggregate getProductAggregate(int productId) {
        LOG.info("Sending Request to get Product for productId: {}", productId);
        Product product = productCompositeIntegration.getProduct(productId);
        if ( product == null ) throw  new NotFoundException("No product found for productId: " + productId);

        List<Recommendation> recommendations = productCompositeIntegration.getRecommendations(productId);
        List<Review> reviews = productCompositeIntegration.getReviews(productId);
        return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
    }

    @Override
    public String testMethod() {
        return "Working fine till here !!!";
    }

    private ProductAggregate createProductAggregate(Product product, List<Recommendation> recommendations,
                                                    List<Review> reviews, String serviceAddress) {

        /*Summary Recommendation if available*/
        List<RecommendationSummary> recommendationSummaries = (recommendations == null) ? null :
                recommendations.stream().map(recommendation -> new RecommendationSummary(
                        recommendation.getRecommendationId(),
                        recommendation.getAuthor(),
                        recommendation.getRate(),
                        recommendation.getContent()
                )).collect(Collectors.toList());

        /*Summary Reviews if available*/
        List<ReviewSummary> reviewSummaries = (reviews == null) ? null :
                reviews.stream().map(review -> new ReviewSummary(
                        review.getReviewId(),
                        review.getAuthor(),
                        review.getSubject(),
                        review.getContent()
                )).collect(Collectors.toList());

        /*Information regarding Involved Microservices*/

        String productAddress = product.getServiceAddress();
        String recommendationAddress = (recommendations != null && recommendations.size() > 0) ?
                recommendations.get(0).getServiceAddress() : "";
        String reviewAddress = (reviews != null && reviews.size() > 0) ?
                reviews.get(0).getServiceAddress() : "";

        ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, recommendationAddress, reviewAddress);

        return new ProductAggregate(
                product.getProductId(),product.getName(),product.getWeight(),
                recommendationSummaries,
                reviewSummaries, serviceAddresses);
    }
}
