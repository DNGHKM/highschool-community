package com.dnghkm.high_school_community.exception;

public class UserNotMatchException extends RuntimeException {
    public UserNotMatchException() {
        super("유저가 일치하지 않습니다.");
    }
}
