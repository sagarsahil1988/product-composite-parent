package com.techpractice.product_composite_service;

import com.techpractice.api.core.product.Product;
import com.techpractice.api.core.recommendation.Recommendation;
import com.techpractice.api.core.review.Review;
import com.techpractice.product_composite_service.services.ProductCompositeIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebClient
class ProductCompositeServiceApplicationTests {

	private static final int PRODUCT_ID_OK = 1;

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ProductCompositeIntegration productCompositeIntegration;

	@BeforeEach
	public void setUp(){
		Mockito.when(productCompositeIntegration.getProduct(PRODUCT_ID_OK))
				.thenReturn(new Product(PRODUCT_ID_OK, "name", 1, "mock-address"));
		Mockito.when(productCompositeIntegration.getRecommendations(PRODUCT_ID_OK))
				.thenReturn(Collections.singletonList(new Recommendation(PRODUCT_ID_OK,1, "author",1, "content", "mock-address")));
		Mockito.when(productCompositeIntegration.getReviews(PRODUCT_ID_OK))
				.thenReturn(Collections.singletonList(new Review(PRODUCT_ID_OK,1, "author","subject", "content", "mock-address")));
	}

	@Test
	public void testGetProductById(){
		this.webTestClient.get()
				.uri("/product-composite/" + PRODUCT_ID_OK)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.productId").isEqualTo(PRODUCT_ID_OK)
				.jsonPath("$.recommendations.length()").isEqualTo(1)
				.jsonPath("$.reviews.length()").isEqualTo(1);
	}


	@Test
	void contextLoads() {
	}

}
