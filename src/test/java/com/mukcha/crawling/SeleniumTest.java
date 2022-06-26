package com.mukcha.crawling;

import java.io.File;
import java.util.Map;

import com.mukcha.controller.api.SeleniumApiController;
import com.mukcha.controller.api.helper.WithSelenium;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class SeleniumTest extends WithSelenium {

    @Autowired
    private SeleniumApiController seleniumApiController;

    @Test
    void puradak() {
        Map<String, String> result = seleniumApiController.puradak();
        result.forEach((key, value) -> {
            System.out.println(">>> "+key);
            System.out.println(">>> "+value);
        });
    }

    @Test
    void baskin31() throws InterruptedException {
        Map<String, String> result = seleniumApiController.baskin31();
        result.forEach((key, value) -> {
            System.out.println(">>> "+key);
            System.out.println(">>> "+value);
        });
    }

    @Test
    void goobne() throws InterruptedException {
        Map<String, String> result = seleniumApiController.goobne();
        result.forEach((key, value) -> {
            System.out.println(">>> "+key);
            System.out.println(">>> "+value);
        });
    }

    @Test
    void burgerking() throws InterruptedException {
        Map<String, String> result = seleniumApiController.burgerking();
        result.forEach((key, value) -> {
            System.out.println(">>> "+key);
            System.out.println(">>> "+value);
        });
    }

    @Test
    void dongdaemunYupdduk() {
        Map<String, String> result = seleniumApiController.dongdaemunYupdduk();
        result.forEach((key, value) -> {
            System.out.println(">>> "+key);
            System.out.println(">>> "+value);
        });
    }

    @Test
    public void cheoga() {
        Map<String, String> result = seleniumApiController.cheoga();
        result.forEach((key, value) -> {
            System.out.println(">>> "+key);
            System.out.println(">>> "+value);
        });
    }

    @Test
    public void kfc() {
        Map<String, String> result = seleniumApiController.kfc();
        result.forEach((key, value) -> {
            System.out.println(">>> "+key+">");
            System.out.println(">>> "+value+">");
        });
    }

    @Test
    void BBQ() throws InterruptedException {
        Map<String, String> result = seleniumApiController.BBQ();
        result.forEach((key, value) -> {
            System.out.println(">>> "+key);
            System.out.println(">>> "+value);
        });
    }

    @Test
    void bigstarTest() {
        Map<String, String> result = seleniumApiController.bigstarPizza();
        result.forEach((key, value) -> {
            System.out.println(">>> "+key+">");
            System.out.println(">>> "+value);
        });
    }

    @Test
    void uploadTest() {
        String fileName = "/image/burgerking_"+getEncodedFilename("기네스와퍼")+".png";
        File file = new File(fileName);
        String imageUrl = s3Uploader.upload(file, "image");
        System.out.println(imageUrl);
    }

}