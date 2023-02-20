package com.techpractice.product_composite_service;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.web.client.RestTemplate;
@SpringBootApplication
@ComponentScan("com.techpractice")
public class ProductCompositeServiceApplication {

	@Value("${api.common.title}")
	String apiTitle;
	@Value("${api.common.description}")
	String apiDescription;
	@Value("${api.common.version}")
	String apiVersion;
	@Value("${api.common.termsOfService}")
	String apiTermsOfService;
	@Value("${api.common.contact.name}")
	String apiContactName;
	@Value("${api.common.contact.url}")
	String apiContactUrl;
	@Value("${api.common.contact.email}")
	String apiContactEmail;

	@Value("${api.common.license}")
	String apiLicense;
	@Value("${api.common.licenseUrl}")
	String apiLicenseUrl;
	@Value("${api.common.external.doc.description}")
	String apiExternalDocDescription;

	@Value("${api.common.external.doc.url}")
	String apiExternalDocUrl;


	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}


	public static void main(String[] args) {
		SpringApplication.run(ProductCompositeServiceApplication.class, args);
	}

//	Adding open api documentation

	@Bean
	public OpenAPI getOpenApiDocumentation() {
		return new OpenAPI()
				.info(new Info().title(apiTitle)
						.description(apiDescription)
						.version(apiVersion)
						.contact(new Contact()
								.name(apiContactName)
								.url(apiContactUrl)
								.email(apiContactEmail))
						.termsOfService(apiTermsOfService)
						.license(new License()
								.name(apiLicense)
								.url(apiLicenseUrl)))
				.externalDocs(new ExternalDocumentation()
						.description(apiExternalDocDescription)
						.url(apiExternalDocUrl));
	}
//	@Bean
//	public Docket apiDocumentation(){
//		return new Docket(DocumentationType.SWAGGER_2)
//				.select()
//				.apis(RequestHandlerSelectors.basePackage("com.techpractice.product_composite_service"))
//				.paths(PathSelectors.any())
//				.build()
//				.globalResponses(HttpMethod.GET, Collections.emptyList())
//				.apiInfo(new ApiInfo(
//						apiTitle,
//						apiDescription,
//						apiVersion,
//						apiTermsOfServiceUrl,
//						new Contact(apiContactName, apiContactUrl, apiContactEmail),
//						apiLicense,
//						apiLicenseUrl,
//						Collections.emptyList()
//				));
//	}
}


