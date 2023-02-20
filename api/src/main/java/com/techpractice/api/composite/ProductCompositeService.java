package com.techpractice.api.composite;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@Api
@Tag(name = "ProductComposite", description = "REST API for composite product information.")
public interface ProductCompositeService {

//    @ApiOperation(
//            value = "${api.product-composite.get-composite-product.description}",
//            notes = "${api.product-composite.get-composite-product.notes}")
    @Operation(
            summary = "${api.product-composite.get-composite-product.description}",
            description = "${api.product-composite.get-composite-product.notes}")
    @ApiResponses( value = {
            @ApiResponse(code = 400, message = "Bad Request. Invalid format of Request." +
                    "See response message for more information."),
            @ApiResponse(code = 404, message = "Not Found. The specified id does not exist."),
            @ApiResponse(code = 422, message = "Un-processable entity. Input parameter caused the " +
                    "processing to fail. See response message for more information.")
    })

    @GetMapping(value = "/product-composite/{productId}",
    produces = "application/json")
    ProductAggregate getProductAggregate(@PathVariable int productId);

    @GetMapping(value = "/test",
    produces = "application/json")
    String testMethod();

}
