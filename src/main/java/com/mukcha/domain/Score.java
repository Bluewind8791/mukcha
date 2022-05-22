package com.mukcha.domain;


public enum Score {

    BEST(5), // 5 인생메뉴에요
    GOOD(4), // 4 또 먹고싶어요
    SOSO(3), // 3 한번은 먹을만해요
    NOTGOOD(2), // 2 추천하고 싶지않아요
    BAD(1); // 1 완전 별로였어요

    public int value;

    public int getValue() {
        return this.value;
    }

    public String getRating() {
        switch (this) {
            case BEST:
                return "인생 메뉴에요!";
            case GOOD:
                return "또 먹고 싶어요";
            case SOSO:
                return "한 번은 먹을만해요";
            case NOTGOOD:
                return "추천하고 싶지 않아요";
            case BAD:
                return "완전 별로였어요";
        }
        return null;
    }

    Score(int value) {
        this.value = value;
    }

}