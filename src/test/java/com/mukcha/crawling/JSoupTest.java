package com.mukcha.crawling;

import java.io.IOException;
import java.util.Map;

import com.mukcha.controller.api.JSoupApiController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class JSoupTest {

    @Autowired
    private JSoupApiController jSoupApiController;


    @Test
    void baedduck() throws IOException {
        Map<String, String> result = jSoupApiController.baedduck();
        result.forEach((key, value) -> {
            System.out.println(">>> "+key);
            System.out.println(">>> "+value);
        });
    }

    @Test
    void sinjeonTest() throws IOException {
        Map<String, String> result = jSoupApiController.sinjeon();
        result.forEach((key, value) -> {
            System.out.println(">>> "+key);
            System.out.println(">>> "+value);
        });
    }

    @Test
    void dominoPizza() throws IOException {
        Map<String, String> result = jSoupApiController.dominoPizza();
        result.forEach((key, value) -> {
            System.out.println(">>> "+key+">");
            System.out.println(">>> "+value);
        });
    }

    @Test
    void pericanaTest() throws IOException {
        Map<String, String> result = jSoupApiController.pericana();
        result.forEach((key, value) -> {
            System.out.println(">>> "+key+">");
            System.out.println(">>> "+value);
        });
    }

}