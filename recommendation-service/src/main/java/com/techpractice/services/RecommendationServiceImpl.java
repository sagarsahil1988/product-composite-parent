package com.techpractice.services;

import com.techpractice.api.core.recommendation.Recommendation;
import com.techpractice.api.core.recommendation.RecommendationService;
import com.techpractice.utils.exceptions.InvalidInputException;
import com.techpractice.utils.exceptions.NotFoundException;
import com.techpractice.utils.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RecommendationServiceImpl implements RecommendationService {
    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);
    private final ServiceUtil serviceUtil;

    @Autowired
    public RecommendationServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        LOG.debug("Recommendations returned for productId: {}", productId);

        if( productId < 1 ) throw new InvalidInputException("Invalid productId: "+ productId);
        if ( productId == 13 ) throw new NotFoundException("No Recommendations found for productId: " + productId);

        List<Recommendation> recommendationList = new ArrayList<>();
        recommendationList.add(new Recommendation(productId, 1, "Author 1"  , 1, "Content 1", serviceUtil.getServiceAddress()));
        recommendationList.add(new Recommendation(productId, 2, "Author 2"  , 2, "Content 2", serviceUtil.getServiceAddress()));
        recommendationList.add(new Recommendation(productId, 3, "Author 3"  , 3, "Content 3", serviceUtil.getServiceAddress()));
        recommendationList.add(new Recommendation(productId, 4, "Author 4"  , 4, "Content 4", serviceUtil.getServiceAddress()));
        recommendationList.add(new Recommendation(productId, 5, "Author 5"  , 5, "Content 5", serviceUtil.getServiceAddress()));

        return recommendationList;
    }
}
