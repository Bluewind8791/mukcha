package com.mukcha.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// ROLE_ADMIN 만 접근가능
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class CrawlingController {

    private final FoodService foodService;
    private final CompanyService companyService;


    @GetMapping(value = "/crawling")
    public String crawling() {
        System.out.println(">>> CRAWLING START >>>");
        kfc();
        dominoPizza();
        페리카나();
        치킨플러스();
        BBQ();
        return "redirect:/admin";
    }



    public void 처갓집() {
        // 처갓집 DB 생성할 것
    }


    public void kfc() {
        Company company = isCompanyPresent("KFC");
        Document doc;
        List<String> urlList = new ArrayList<>();
        urlList.add("https://www.kfckorea.com/menu/burger"); // 버거
        urlList.add("https://www.kfckorea.com/menu/chicken"); // 치킨
        try {
            for (String url : urlList) {
                doc = Jsoup.connect(url).get();
                // 메뉴 리스트
                Elements menuList = doc.select("div[menu-list= ]").select("section > ul > li");
                for (Element li : menuList) {
                    // 메뉴 이름을 가져온다 - section > ul > li:nth-child(1) > h3
                    String foodName = li.select("h3").text();
                    String[] name = foodName.split(" ");
                    foodName = name[0].replace("1＋1", "");
                    // 만약 같은 이름의 메뉴가 없다면
                    if (!isFoodPresent(foodName)) {
                        Elements imageE = li.select("div > a > img");
                        String image = imageE.attr("src");
                        System.out.println(">>> image: "+image+">");
                        // 실행 전 체크할 것
                        if (url.contains("chicken")) {
                            saveFood(foodName, image, company, Category.CHICKEN);
                        } else if (url.contains("burger")) {
                            saveFood(foodName, image, company, Category.HAMBURGER);
                        }
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    public void dominoPizza() {
        Company company = isCompanyPresent("도미노피자");
        
        List<String> urlList = new ArrayList<>();
        urlList.add("https://web.dominos.co.kr/goods/list?dsp_ctgr=C0101"); // 메뉴
        urlList.add("https://web.dominos.co.kr/goods/onePizzalist"); // 1인 메뉴
        Document doc;
        try {
            for (String url : urlList) {
                doc = Jsoup.connect(url).get();
                // #content > div > div > article > div:nth-child(6)
                Elements menuList = doc.select("#content > div > div > article").select("div[class=menu-list]");
                System.out.println(">>> list size: "+menuList.size());
                for (Element e : menuList) {
                    // second list: #content > div > div > article > div:nth-child(6) > ul > li:nth-child(1)
                    Elements secondList = e.select("ul > li");
                    for (Element li : secondList) {
                        // image url을 가져온다: > div.prd-img > a:nth-child(1) > img
                        String image = li.select("div.prd-img > a").first().select("img").first().absUrl("data-src");
                        // 메뉴 이름을 가져온다: > div.prd-cont > div
                        String foodName = li.select("div.prd-cont").select("div[class=subject]")
                                            .text()
                                            .replace("NEW", "")
                                            .replace("BEST", "")
                        ;
                        // 만약 같은 이름의 메뉴가 없거나 공백이 아니라면 DB에 저장
                        if (!isFoodPresent(foodName)) {
                            System.out.println(">>> food:"+foodName+"|");
                            System.out.println(">>> image:"+image+"|");
                            // 실행 전 체크할 것
                            saveFood(foodName, image, company, Category.PIZZA);
                        }
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    public void 페리카나() {
        Company company = isCompanyPresent("페리카나");

        List<String> urlList = new ArrayList<>();
        urlList.add("https://pelicana.co.kr/menu/menu_original.html"); // 오리지널
        urlList.add("https://pelicana.co.kr/menu/menu_sunsal.html"); // 순살
        urlList.add("https://pelicana.co.kr/menu/menu_sinsunpin.html"); // 신선핀
        urlList.add("https://pelicana.co.kr/menu/menu_sinsunwing.html"); // 신선윙
        urlList.add("https://pelicana.co.kr/menu/menu_periwing.html"); // 페리윙봉
        urlList.add("https://pelicana.co.kr/menu/menu_bburio.html"); // 뿌리오
        urlList.add("https://pelicana.co.kr/menu/menu_mapsak.html"); // 맵삭치킨
        urlList.add("https://pelicana.co.kr/menu/menu_rospin.html"); // 로스핀
        Document doc;
        try {
            for (String url : urlList) {
                doc = Jsoup.connect(url).get();
                Elements lists = doc.select("#contents > ul.menu_list.menu_pop > li");
                System.out.println(">>> size: "+lists.size());
                for (Element e : lists) {
                    // image url: #contents > ul.menu_list.menu_pop > li:nth-child(1) > a > figure > img
                    String image = e.select("a > figure > img").first().absUrl("src");
                    System.out.println(">>> image: "+image);
                    // menu name: #contents > ul.menu_list.menu_pop > li:nth-child(1) > a > figure > figcaption
                    String foodName = e.select("a > figure > figcaption").text();
                    System.out.println(">>> food: "+foodName);
                    // 만약 같은 이름의 메뉴가 없다면 DB에 저장
                    if (!isFoodPresent(foodName)) {
                        // 실행 전 체크할 것
                        saveFood(foodName, image, company, Category.CHICKEN);
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    // 정확한 image URL는 모달을 통하여 가져올 수 밖에 없어서 못가져올듯
    public void 치킨플러스() {
        Company company = isCompanyPresent("치킨플러스");

        List<String> urlList = new ArrayList<>();
        urlList.add("http://www.chickenplus.co.kr/menu/default.aspx?menu=치킨메뉴");
        urlList.add("http://www.chickenplus.co.kr/menu/default.aspx?menu=피자메뉴");
        urlList.add( "http://www.chickenplus.co.kr/menu/default.aspx?menu=떡볶이메뉴");
        Document doc;
        try {
            for (String url : urlList) {
                doc = Jsoup.connect(url).get();
                Elements lists = doc.select("div[class=mn_area]").select("div[groupname=MenuItem]");
                System.out.println(">>> size: "+lists.size());
                for (Element e : lists) {
                    // menu name
                    String foodName = e.select("div.mn1_txt > p.mn1_tit.menu_s_title").text();
                    System.out.println(">>> food name: "+foodName);
                    // 만약 같은 이름의 메뉴가 없다면 DB에 저장
                    if (!isFoodPresent(foodName)) {
                        if (url.contains("떡볶이")) {
                            saveFood(foodName, "", company, Category.TTEOKBOKKI);
                        } else if (url.contains("피자")) {
                            saveFood(foodName, "", company, Category.PIZZA);
                        } else if (url.contains("치킨")) {
                            saveFood(foodName, "", company, Category.CHICKEN);
                        }
                    }
                }
            }
        } catch (IOException e1) {
            System.out.println(">>> 치킨플러스의 정보를 불러올 수 없습니다.");
            e1.printStackTrace();
        }
    }


    public void BBQ() {
        Document doc;
        Company company = isCompanyPresent("BBQ");
        String getFoodName;
        String getImageUrl;
        List<String> urlList = new ArrayList<>();
        urlList.add("https://www.bbq.co.kr/menu/menuList.asp?cidx=124&cname=1%EC%9D%B8%EB%B6%84+%EB%A9%94%EB%89%B4");

        try {
            for (String url : urlList) {
                doc = Jsoup.connect(url).get();
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
                    if (!isFoodPresent(getFoodName)) {
                        saveFood(getFoodName, getImageUrl, company, Category.CHICKEN);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(">>> BBQ의 정보를 불러올 수 없습니다.");
            e.printStackTrace();
        }
    }

    // --- METHODS ---

    private Company createCompany(String companyName) {
        Company company = Company.builder()
                            .image("")
                            .name("BBQ")
                            .build()
        ;
        companyService.save(company);
        log.info(">>> 회사 <"+company.getName()+">의 로고 이미지를 설정해주세요.");
        return company;
    }


    private Company isCompanyPresent(String companyName) {
        Company company = companyService.findByName(companyName).orElse(
            createCompany(companyName)
        );
        return company;
    }


    private Boolean isFoodPresent(String foodName) {
        if (foodService.findByName(foodName).isPresent()) {
            return true;
        } else {
            return false;
        }
    }


    private void saveFood(String foodName, String imageUrl, Company company, Category category) {
        Food food = Food.builder()
                .name(foodName)
                .image(imageUrl)
                .company(company)
                .category(category)
                .build()
        ;
        foodService.save(food);
        log.info(">>> 메뉴<+"+food.getName()+"> 이 DB에 생성되었습니다.");
    }


}
