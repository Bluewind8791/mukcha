package com.mukcha.crawling;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mukcha.controller.api.SeleniumApiController;
import com.mukcha.controller.api.helper.WithSelenium;
import com.mukcha.domain.Company;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class SeleniumTest extends WithSelenium {

    @Autowired
    private SeleniumApiController seleniumApiController;

    @Test
    void uploadTest() {
        String fileName = "/image/burgerking_"+getEncodedFilename("기네스와퍼")+".png";
        File file = new File(fileName);
        String imageUrl = s3Uploader.upload(file, "image");
        System.out.println(imageUrl);
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
        String companyName = "처갓집양념치킨";
        companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_cheoga.png")
                .build();
            Company saved = companyService.save(company);
            return saved;
        });
        List<String> urlList = new ArrayList<>();
        urlList.add("http://www.cheogajip.co.kr/bbs/board.php?bo_table=allmenu&page=1"); // 1페이지
        urlList.add("http://www.cheogajip.co.kr/bbs/board.php?bo_table=allmenu&page=2"); // 2페이지
        for (String url : urlList) {
            WebDriver driver = setupDriver(url);
            List<WebElement> menuList = driver.findElements(By.xpath("//*[@id=\"gall_ul\"]/li"));
            for (WebElement menu : menuList) {
                String menuName = menu.findElement(By.xpath("ul/li[2]")).getText().split("\\n")[0];
                if (!menuName.contains("+") && !menuName.contains("9개")) {
                    System.out.println(">>> "+menuName);
                    String image = menu.findElement(By.xpath("ul/li[1]/img")).getAttribute("src");
                    System.out.println(">>> "+image);
                }
            }
            driver.quit();
        }
    }


    @Test
    public void kfc() {
        String companyName = "KFC";
        companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("")
                .build();
            Company saved = companyService.save(company);
            return saved;
        });
        List<String> urlList = new ArrayList<>();
        urlList.add("https://www.kfckorea.com/menu/burger"); // 버거
        urlList.add("https://www.kfckorea.com/menu/chicken"); // 치킨
        for (String url : urlList) {
            WebDriver driver = setupDriver(url);
            List<WebElement> menuList = driver.findElements(By.xpath("//*[@id=\"app\"]/div[2]/div/section/div[2]/div/ul/li"));
            for (WebElement menu : menuList) {
                String menuName = menu.findElement(By.xpath("h3")).getText();
                String[] filtering = {"팩" ,"박스", "세트", "1조각", "3조각", "8조각"};
                if ( !List.of(filtering).stream().anyMatch(n -> menuName.contains(n)) ) {
                    if (menuName.contains("5조각")) {
                        menuName.replace("5조각", "");
                    }
                    System.out.println(">>> "+menuName);
                    String image = menu.findElement(By.xpath("div[1]/a/img")).getAttribute("src");
                    System.out.println(">>> "+image);
                }
            }
            driver.quit();
        }
    }



}