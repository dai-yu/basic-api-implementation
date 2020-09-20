package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.UserIdIncorrectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes= UserController.class)
public class UserControllerHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, UserIdIncorrectException.class})
    public ResponseEntity rsExceptionHandler(Exception e){
        String errorMessage;
        if (e instanceof MethodArgumentNotValidException){
            errorMessage = "invalid user";
        }else {
            errorMessage=e.getMessage();
        }
        Error error=new Error();
        error.setError(errorMessage);

        Logger logger= LoggerFactory.getLogger(RsEventHandler.class);
        logger.error(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }
}
