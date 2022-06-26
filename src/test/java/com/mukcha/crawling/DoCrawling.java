package com.mukcha.crawling;


import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mukcha.controller.api.JSoupApiController;
import com.mukcha.controller.api.SeleniumApiController;



@SpringBootTest
@ActiveProfiles("set1")
public class DoCrawling {

    @Autowired private SeleniumApiController seleniumApiController;
    @Autowired private JSoupApiController jSoupApiController;


    @Test
    @DisplayName("푸라닭 크롤링")
    void CrawlingPuradak() throws Exception {
        seleniumApiController.puradak();
    }

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

    @Test
    @DisplayName("배떡 크롤링")
    void crawlingBbadduk() throws IOException {
        jSoupApiController.baedduck();
    }

    @Test
    @DisplayName("신전떡볶이 크롤링")
    void crawlingSinjeon() throws IOException {
        jSoupApiController.sinjeon();
    }

    @Test
    @DisplayName("BBQ 크롤링")
    void crawlingBBQ() throws InterruptedException {
        seleniumApiController.BBQ();
    }

    @Test
    @DisplayName("도미노피자 크롤링")
    void crawlingDomino() throws IOException {
        jSoupApiController.dominoPizza();
    }

    @Test
    @DisplayName("페리카나 크롤링")
    void crawlingPericana() throws IOException {
        jSoupApiController.pericana();
    }

    @Test
    @DisplayName("빅스타피자 크롤링")
    void crawlingBigstar() throws IOException {
        seleniumApiController.bigstarPizza();
    }


}
