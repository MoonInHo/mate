package com.mooninho.ordermanager.exception.exception.owner;

import com.mooninho.ordermanager.exception.ApplicationException;
import com.mooninho.ordermanager.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class DuplicateUsernameException extends ApplicationException {

    public DuplicateUsernameException() {
        super(HttpStatus.CONFLICT, ErrorCode.DUPLICATE_USERNAME_ERROR, "이미 존재하는 아이디입니다.");
    }
}
