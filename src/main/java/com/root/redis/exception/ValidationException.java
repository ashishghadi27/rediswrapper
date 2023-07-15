package com.root.redis.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ValidationException extends Exception{

    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";

    public static final String SERVICE_DOWN = "SERVICE_DOWN";

    public static final String DATA_NOT_FOUND = "DATA_NOT_FOUND";

    public static final String UNAUTHORISED = "UNAUTHORISED";
    private Integer errorCode;
    private String errorMessage;
    private String url;

    public ValidationException(Builder builder){
        super(builder.errorMessage);
        this.errorCode = builder.errorCode;
        this.errorMessage = builder.errorMessage;
        this.url = builder.url;
    }

//
//    public ValidationException(String message){
//        super(message);
//        this.errorCode = DEFAULT_ERROR_CODE;
//        this.errorMessage = message;
//    }
//
//    public ValidationException(Integer errorCode, String message){
//        super(message);
//        this.errorCode = errorCode;
//        this.errorMessage = message;
//    }
//
//    public ValidationException(String message, String url){
//        super(message);
//        this.errorCode = DEFAULT_ERROR_CODE;
//        this.errorMessage = message;
//        this.url = url;
//    }
//
//    public ValidationException(Integer errorCode, String message, String url){
//        super(message);
//        this.errorCode = errorCode;
//        this.errorMessage = message;
//        this.url = url;
//    }

    public static class Builder {

        private Integer errorCode;
        private String errorMessage;
        private String url;

        public ValidationException build(){
            if(this.errorCode == null){
                this.errorCode = HttpStatus.BAD_REQUEST.value();
            }
            return new ValidationException(this);
        }

        public Builder errorCode(Integer errorCode){
            this.errorCode = errorCode;
            return this;
        }

        public Builder errorMessage(String errorMessage){
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder url(String url){
            this.url = url;
            return this;
        }

    }

}
