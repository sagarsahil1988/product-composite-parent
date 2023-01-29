package com.techpractice.utils.http;

import com.techpractice.utils.exceptions.InvalidInputException;
import com.techpractice.utils.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody HttpErrorInfo handleNotFoundExceptions(ServerHttpRequest request, Exception exception){
       return createHttpErrorInfo(HttpStatus.NOT_FOUND, request, exception);

}

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
@ExceptionHandler(InvalidInputException.class)
public @ResponseBody HttpErrorInfo handleInvalidInputException(ServerHttpRequest request, Exception exception){
    return createHttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, request,exception);
}
    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus,
                                              ServerHttpRequest request, Exception exception) {

        final String path = request.getURI().getPath();
        final String message = exception.getMessage();

        LOG.debug("Returning HTTP Status : {} for path : {}, message: {}", httpStatus, path, message);

        return new HttpErrorInfo(httpStatus, path, message);
    }

}
