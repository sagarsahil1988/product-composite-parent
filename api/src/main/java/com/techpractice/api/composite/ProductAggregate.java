package com.techpractice.api.composite;

import java.util.List;

public class ProductAggregate {

    private final int productId;
    private final String name;
    private final int weight;
    private final List<RecommendationSummary> recommendationSummaryList;
    private final List<ReviewSummary> reviewSummaryList;
    private final ServiceAddresses serviceAddresses;

    public ProductAggregate(int productId, String name, int weight, List<RecommendationSummary> recommendationSummaryList, List<ReviewSummary> reviewSummaryList, ServiceAddresses serviceAddresses) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
        this.recommendationSummaryList = recommendationSummaryList;
        this.reviewSummaryList = reviewSummaryList;
        this.serviceAddresses = serviceAddresses;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public List<RecommendationSummary> getRecommendationSummaryList() {
        return recommendationSummaryList;
    }

    public List<ReviewSummary> getReviewSummaryList() {
        return reviewSummaryList;
    }

    public ServiceAddresses getServiceAddresses() {
        return serviceAddresses;
    }
}
