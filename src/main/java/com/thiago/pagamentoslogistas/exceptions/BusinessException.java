package com.thiago.pagamentoslogistas.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BusinessException extends RuntimeException {

    public BusinessException(final String messageDetails) {
        super(messageDetails);
    }

}