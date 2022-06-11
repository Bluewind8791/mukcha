package com.mukcha.crawling;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mukcha.controller.api.SeleniumApiController;



@SpringBootTest
@ActiveProfiles("set1")
public class DoCrawling {

    @Autowired
    private SeleniumApiController seleniumApiController;


    @Test
    @DisplayName("배스킨라빈스 크롤링")
    void CrawlingBaskin() throws Exception {
        seleniumApiController.baskin31();
    }

    @Test
    @DisplayName("치킨플러스 크롤링")
    void CrawlingChikenplus() throws Exception {
        seleniumApiController.chickenPlus();
    }

    @Test
    @DisplayName("굽네치킨 크롤링")
    void crawlingGoobne() throws Exception {
        seleniumApiController.goobne();
    }

    @Test
    @DisplayName("버거킹 크롤링")
    void crawlingBurgerking() throws Exception {
        seleniumApiController.burgerking();
    }

    @Test
    @DisplayName("동대문엽떡 크롤링")
    void crawlingYupdduk() {
        seleniumApiController.dongdaemunYupdduk();
    }

    @Test
    @DisplayName("처갓집양념치킨 크롤링")
    void crawlingCheoga() {
        seleniumApiController.cheoga();
    }

    @Test
    @DisplayName("KFC 크롤링")
    void crawlingKFC() {
        seleniumApiController.kfc();
    }




}
