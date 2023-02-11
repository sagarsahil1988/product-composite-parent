package com.techpractice.api.composite;

public class ServiceAddresses {
    private final String compositeAddress;
    private final String productAddress;
    private final String recommendationAddress;
    private final String reviewAddress;

    public ServiceAddresses(String compositeAddress, String productAddress, String recommendationAddress, String reviewAddress) {
        this.compositeAddress = compositeAddress;
        this.productAddress = productAddress;
        this.recommendationAddress = recommendationAddress;
        this.reviewAddress = reviewAddress;
    }

    public String getCompositeAddress() {
        return compositeAddress;
    }

    public String getProductAddress() {
        return productAddress;
    }

    public String getRecommendationAddress() {
        return recommendationAddress;
    }

    public String getReviewAddress() {
        return reviewAddress;
    }
}
