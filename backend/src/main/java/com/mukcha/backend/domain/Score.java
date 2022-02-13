package com.mukcha.backend.domain;

public enum Score {
    
    BAD(1), // 1 완전 별로였어요
    NOTGOOD(2), // 2 추천하고 싶지않아요
    SOSO(3), // 3 한번은 먹을만해요
    GOOD(4), // 4 또 먹고싶어요
    BEST(5); // 5 인생메뉴에요

    public int value;
    public int getValue() {
        return this.value;
    }
    Score(int value) {
        this.value = value;
    }
    
}
