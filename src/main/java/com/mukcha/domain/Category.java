package com.mukcha.domain;

public enum Category {

    CHICKEN("치킨"),
    PIZZA("피자"),
    HAMBURGER("햄버거"),
    TTEOKBOKKI("떡볶이"), // tteok-bokki
    PASTA("파스타"),
    SIDEMENU("사이드메뉴"),
    ICECREAM("아이스크림"),
    ;

    private String korean;

    Category(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }

}
