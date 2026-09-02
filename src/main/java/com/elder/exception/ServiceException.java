package com.elder.exception;

/**
 * 业务异常：service 层校验不通过等预期内的错误时抛出，
 * message 就是给用户看的提示信息
 */
public class ServiceException extends RuntimeException{

    public ServiceException(String message) {
        super(message);
    }
}
