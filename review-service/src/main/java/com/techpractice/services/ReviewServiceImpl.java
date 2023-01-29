package com.techpractice.services;

import com.techpractice.api.core.review.Review;
import com.techpractice.api.core.review.ReviewService;
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
public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ServiceUtil serviceUtil;

    @Autowired
    public ReviewServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Review> getReviews(int productId) {
        LOG.debug("Reviews returned for productId: {}", productId);
        if( productId < 1) throw new InvalidInputException("Invalid productId: "+ productId);
        if ( productId == 13 ) throw new NotFoundException("No Reviews found for productId: "+ productId);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(new Review(productId, 1, "Review Author 1","Review Subject 1", "Review Content 1", serviceUtil.getServiceAddress()));
        reviewList.add(new Review(productId, 2, "Review Author 2","Review Subject 2", "Review Content 2", serviceUtil.getServiceAddress()));
        reviewList.add(new Review(productId, 3, "Review Author 3","Review Subject 3", "Review Content 3", serviceUtil.getServiceAddress()));
        reviewList.add(new Review(productId, 4, "Review Author 4","Review Subject 4", "Review Content 4", serviceUtil.getServiceAddress()));
        reviewList.add(new Review(productId, 5, "Review Author 5","Review Subject 5", "Review Content 5", serviceUtil.getServiceAddress()));
        reviewList.add(new Review(productId, 6, "Review Author 6","Review Subject 6", "Review Content 6", serviceUtil.getServiceAddress()));
        return reviewList;
    }
}
