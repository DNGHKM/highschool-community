package com.dnghkm.high_school_community.exception;

public class SchoolNotMatchException extends RuntimeException {
    public SchoolNotMatchException() {
        super("학교 정보가 불일치합니다.");
    }
}
