package com.mukcha.domain;


public enum ErrorMessage {

    USER_NOT_FOUND("해당 회원을 찾을 수 없습니다."),
    MENU_NOT_FOUND("해당 메뉴를 찾을 수 없습니다.");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
