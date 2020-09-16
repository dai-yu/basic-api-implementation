package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotVaildException;
import com.thoughtworks.rslist.exception.RsEventNotValidParamException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RsEventHandler {
    @ExceptionHandler({RsEventNotVaildException.class, MethodArgumentNotValidException.class, RsEventNotValidParamException.class})
    public ResponseEntity rsExceptionHandler(Exception e){
        String errorMessage;
        if (e instanceof MethodArgumentNotValidException){
            errorMessage = "invaild param";
        }else {
            errorMessage=e.getMessage();
        }
        Error error=new Error();
        error.setError(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }
}
