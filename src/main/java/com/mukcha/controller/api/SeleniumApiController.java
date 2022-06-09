package com.mukcha.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mukcha.controller.api.helper.WithSelenium;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/admin")
public class SeleniumApiController extends WithSelenium {

    @GetMapping("/crawling/new")
    public ResponseEntity<Object> crawlingNew() {
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/crawling/배스킨라빈스")
    public ResponseEntity<Object> crawlingBaskin() {
        try {
            Map<String, String> body = baskin31();
            return ResponseEntity.ok(body);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/crawling/치킨플러스")
    public ResponseEntity<Object> crawlingChickenPlus() {
        try {
            Map<String, String> body = chickenPlus();
            return ResponseEntity.ok(body);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    private Map<String, String> baskin31() throws InterruptedException {
        Map<String, String> result = new HashMap<>();
        String companyName = "배스킨라빈스";
        companyRepository.findByName(companyName).or(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_baskin.png")
                .build();
            Company saved = companyService.save(company);
            return Optional.of(saved);
        });
        List<String> urlList = new ArrayList<>();
        urlList.add("http://www.baskinrobbins.co.kr/menu/list.php?Page=1&top=A&sub="); // 1 page
        urlList.add("http://www.baskinrobbins.co.kr/menu/list.php?Page=2&top=A&sub="); // 2 page
        for (String url : urlList) {
            WebDriver driver = setupDriver(url);
            // #prd_list > aside > ul > li:nth-child(1) > a > figure > figcaption > span
            List<WebElement> menuItem = driver.findElements(By.cssSelector("li[class=item]"));
            for (WebElement menu : menuItem) {
                String menuName = menu.findElement(By.cssSelector("a > figure > figcaption > span")).getText();
                if (!menuName.contains("레디팩")) {
                    // #prd_list > aside > ul > li:nth-child(1) > a > figure > span > img
                    String image = menu.findElement(By.cssSelector("a > figure > span > img")).getAttribute("src");
                    result.put(menuName, image);
                    // save image
                    String imageUrl = saveImage(menuName, image, "baskin31");
                    // update Food
                    updateMenu(menuName, imageUrl, companyService.findByName(companyName), Category.ICECREAM);
                }
            }
            driver.close();
        }
        return result;
    }

    private Map<String, String> chickenPlus() throws InterruptedException {
        Map<String, String> result = new HashMap<>();
        String companyName = "치킨플러스";
        companyRepository.findByName(companyName).or(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_chickenPlus.jpg")
                .build();
            Company saved = companyService.save(company);
            return Optional.of(saved);
        });

        List<String> urlList = new ArrayList<>();
        urlList.add("http://www.chickenplus.co.kr/menu/default.aspx?menu=치킨메뉴");
        urlList.add("http://www.chickenplus.co.kr/menu/default.aspx?menu=피자메뉴");
        urlList.add( "http://www.chickenplus.co.kr/menu/default.aspx?menu=떡볶이메뉴");
        for (String url : urlList) {
            Category category = null;
            if (url.contains("치킨")) {
                category = Category.CHICKEN;
            } else if (url.contains("피자")) {
                category = Category.PIZZA;
            } else if (url.contains("떡볶이")) {
                category = Category.TTEOKBOKKI;
            }
            WebDriver driver = setupDriver(url);
            List<WebElement> menuItem = driver.findElements(By.cssSelector("div[groupname=MenuItem]"));
            for (WebElement menu : menuItem) {
                // menu name
                String menuName = menu.findElement(By.cssSelector("div.mn1_txt > p.mn1_tit.menu_s_title")).getText();
                // image url
                menu.findElement(By.className("thumbMenu")).click(); // thumbMenu click
                String image = menu.findElement(By.xpath("//*[@id=\"MenuImage\"]")).getAttribute("src");
                result.put(menuName, image);
                // save image
                String imageUrl = saveImage(menuName, image, "chickenplus");
                // update Food
                updateMenu(menuName, imageUrl, companyService.findByName(companyName), category);
            }
            driver.close();
        }
        return result;
    }



}
