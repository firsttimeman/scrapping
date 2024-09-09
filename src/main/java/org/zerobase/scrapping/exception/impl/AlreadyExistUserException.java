package org.zerobase.scrapping.exception.impl;

import org.springframework.http.HttpStatus;
import org.zerobase.scrapping.exception.AbstractException;

public class AlreadyExistUserException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 존재한 사용자명입니다.";
    }
}
