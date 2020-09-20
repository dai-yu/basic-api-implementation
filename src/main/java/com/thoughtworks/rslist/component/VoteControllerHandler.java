package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.api.VoteController;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.VoteIndexNotValidException;
import com.thoughtworks.rslist.exception.VoteNotValidParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes= VoteController.class)
public class VoteControllerHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, VoteIndexNotValidException.class, VoteNotValidParamException.class})
    public ResponseEntity rsExceptionHandler(Exception e){
        String errorMessage;
        if (e instanceof MethodArgumentNotValidException){
            errorMessage = "invalid vote";
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
