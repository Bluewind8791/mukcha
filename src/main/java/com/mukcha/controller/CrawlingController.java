package com.mukcha.controller;

import java.io.IOException;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

// ROLE_ADMIN 만 접근가능
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class CrawlingController {

    private final FoodService foodService;
    private final CompanyService companyService;


    @GetMapping(value = "/crawling")
    public String crawling() throws IOException {
        String getFoodName;
        String getImageUrl;
        String url = "http://www.chickenplus.co.kr/menu/default.aspx?menu=치킨메뉴";

        Document doc = Jsoup.connect(url).get();

        Elements lists = doc.select("div[class = mn_area]").select("div[class = mn1]");

        Elements foodName = doc.select("div[class=mn1_txt]").select("div[class=mn1_tit menu_s_title]");
        Elements image = doc.select("div[class=mn1_pd]").select("img[class=thumbMenu]");

        System.out.println(">>> "+lists+" / size: "+lists.size());
        for (int i=0; i<lists.size(); i++) {
            // menu name
            getFoodName = foodName.get(i).text();
            System.out.println(">>> food name: "+getFoodName);
            // image URL
            getImageUrl = image.get(i).absUrl("src");
            System.out.println(">>> url: "+getImageUrl);
            // 만약 같은 이름의 메뉴가 없다면 DB에 저장
            // if (!isPresent(getFoodName)) {
            //     saveFood(getFoodName, getImageUrl, "치킨플러스", Category.CHICKEN);
            // }
        }
        return "redirect:/admin";
    }


    private void BBQ(String url) throws IOException {
        String getFoodName;
        String getImageUrl;
        String urlBBQ = "https://www.bbq.co.kr/menu/menuList.asp?cidx=124&cname=1%EC%9D%B8%EB%B6%84+%EB%A9%94%EB%89%B4";
        Document doc = Jsoup.connect(urlBBQ).get();

        Elements boxes = doc.select("div[class=box]");
        Elements foodName = doc.select("div[class=info]").select("p[class=name]");
        Elements image = doc.select("div[class=box]").select("div[class=img]").select("a > img");

        for (int i=0; i<boxes.size(); i++) {
            // menu name
            getFoodName = foodName.get(i).text();
            System.out.println(">>> "+getFoodName);
            // image URL
            getImageUrl = image.get(i).absUrl("src");
            System.out.println(">>> "+getImageUrl);
            // 만약 같은 이름의 메뉴가 없다면 DB에 저장
            if (!isPresent(getFoodName)) {
                saveFood(getFoodName, getImageUrl, "BBQ", Category.CHICKEN);
            }
        }
    }


    private Boolean isPresent(String foodName) {
        if (foodService.findByName(foodName).isPresent()) {
            return true;
        }
        else {
            return false;
        }
    }


    private void saveFood(String foodName, String imageUrl, String companyName, Category category) {
        Company company;
        try {
            company = companyService.findByName(companyName).get();
        } catch (Exception ex) {
            company = Company.builder()
                    .name(companyName)
                    .image("logo")
                    .build()
            ;
            companyService.save(company);
        }
        Food food = Food.builder()
                .name(foodName)
                .image(imageUrl)
                .company(company)
                .category(category)
                .build()
        ;
        foodService.save(food);
        System.out.println(">>> 음식이 DB에 생성되었습니다."+food.toString());
    }


}
