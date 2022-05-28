package com.mukcha.domain;


public enum ErrorMessage {

    USER_NOT_FOUND("해당 회원을 찾을 수 없습니다."),
    COMPANY_NOT_FOUND("해당 회사를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND("해당 리뷰를 찾을 수 없습니다."),
    MENU_NOT_FOUND("해당 메뉴를 찾을 수 없습니다."),
    FAIL_DELETE_REVIEW("해당 리뷰 삭제에 실패하였습니다."),
    ;

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
