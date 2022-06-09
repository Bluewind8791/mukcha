package com.mukcha.controller.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.ErrorMessage;
import com.mukcha.domain.Food;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
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


// ROLE_ADMIN 만 접근가능
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class JSoupApiController {

    private final FoodService foodService;
    private final CompanyService companyService;
    private final FoodRepository foodRepository;
    private final CompanyRepository companyRepository;

    @GetMapping(value = "/crawling")
    public String crawling() {
        System.out.println(">>> CRAWLING START <<<");
        // kfc();
        // 처갓집양념치킨();
        // dongdaemunYupdduk();
        return "redirect:/admin";
    }



    void baedduck() {
        Company company = isCompanyPresent("배달떡볶이(배떡)", "http://baedduck.co.kr/theme/bae-default/layout/assets/img/brand_04.png");
        Document doc;
        String url = "http://baedduck.co.kr/subpage/menu";
        try {
            doc = Jsoup.connect(url).get();
            Elements menus = doc.select("div[class=menu-item]");
            for (Element menu : menus) {
                String menuName = menu.select("p").text();
                if (menuName.contains("예정")) {
                    continue;
                }
                if (!isFoodPresent(menuName) && !menuName.isEmpty()) {
                    String image = menu.select("img").attr("src");
                    image = "http://baedduck.co.kr" + image;
                    saveFood(menuName, image, company);
                }
            }
        } catch (IOException e) {
            System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company.getName());
            e.printStackTrace();
        }
    }

    public void sinjeon() {
        Company company = isCompanyPresent("신전떡볶이", "http://sinjeon.co.kr/img/common/logo_2022.png");
        Document doc;
        List<String> urlList = new ArrayList<>();
        urlList.add("http://sinjeon.co.kr/doc/menu03.php"); // 떡볶이
        urlList.add("http://sinjeon.co.kr/doc/menu04.php"); // 튀김
        urlList.add("http://sinjeon.co.kr/doc/menu05.php"); // 라이스
        for (String url : urlList) {
            try {
                doc = Jsoup.connect(url).get();
                // 메뉴이름: li:nth-child(1) > a > p > span
                Elements menuList = doc.select("li[class=cars]");
                for (Element menu : menuList) {
                    String menuName = menu.select("a > p > span").text();
                    // 만약 같은 이름의 메뉴가 없고 공백이 아니라면 DB 저장
                    if (!isFoodPresent(menuName) && !menuName.isEmpty()) {
                        // 이미지: li:nth-child(1) > a > img
                        String image = menu.select("a > img").attr("src");
                        image = "http://sinjeon.co.kr" + image;
                        saveFood(menuName, image, company);
                    }
                }
            } catch (IOException e) {
                System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company.getName());
                e.printStackTrace();
            }
        }
    }

    public void BBQ() {
        Document doc;
        Company company = isCompanyPresent("BBQ", "");
        String getFoodName;
        String getImageUrl;
        List<String> urlList = new ArrayList<>();
        urlList.add("https://www.bbq.co.kr/menu/menuList.asp?cidx=113&cname=신메뉴"); // 신메뉴
        urlList.add("https://www.bbq.co.kr/menu/menuList.asp?cidx=114&cname=황올+시그니처");
        urlList.add("https://www.bbq.co.kr/menu/menuList.asp?cidx=124&cname=1인분+메뉴"); // 1인분 메뉴
        urlList.add("https://www.bbq.co.kr/menu/menuList.asp?cidx=7&cname=황올한+양념"); // 황올한 양념
        urlList.add("https://www.bbq.co.kr/menu/menuList.asp?cidx=8&cname=황올한+구이"); // 황올한 구이
        try {
            for (String url : urlList) {
                doc = Jsoup.connect(url).get();
                Elements boxes = doc.select("div[class=box]");
                Elements foodName = doc.select("div[class=info]").select("p[class=name]");
                Elements image = doc.select("div[class=box]").select("div[class=img]").select("a > img");
                for (int i=0; i<boxes.size(); i++) {
                    // menu name
                    getFoodName = foodName.get(i).text();
                    // image URL
                    getImageUrl = image.get(i).absUrl("src");
                    // 만약 같은 이름의 메뉴가 없고 공백이 아니라면 DB 저장
                    if (!isFoodPresent(getFoodName) && !getFoodName.isEmpty()) {
                        saveFood(getFoodName, getImageUrl, company);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company.getName());
            e.printStackTrace();
        }
    }


    public void bigstarPizza() {
        Company company = isCompanyPresent("빅스타피자", "/logo/logo-bigstar_pizza.png");
        Document doc;
        String url = "http://www.bigstarpizza.co.kr/page/menu";
        try {
            doc = Jsoup.connect(url).get();
            // 카테고리 리스트
            Elements categoryList = doc.select("div[class=menu_list]");
            for (Element cate : categoryList) {
                // 메뉴 리스트
                Elements menuList = cate.select("div");
                for (Element menu : menuList) {
                    // 메뉴 이름을 가져온다 - #s1-1 > div.menu_list > div:nth-child(1) > div.menu_name > strong
                    String menuName = menu.select("div[class=menu_name]").select("strong").text();
                    if (menuName.length() == 0 || menuName.length() >= 12 || menuName.contains("반반")) {
                        continue;
                    }
                    // 만약 같은 이름의 메뉴가 없다면
                    if (!isFoodPresent(menuName)) {
                        // 메뉴 이미지를 가져온다 - #s1-1 > div.menu_list > div:nth-child(1) > div.menu_img > img
                        Elements imageE = menu.select("div[class=menu_img]").select("img");
                        String image = imageE.attr("src");
                        if (image.length() == 0) {
                            continue;
                        }
                        image = "http://www.bigstarpizza.co.kr" + image;
                        // save DB
                        saveFood(menuName, image, company);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company.getName());
            e.printStackTrace();
        }
    }


    public void dominoPizza() {
        Company company = isCompanyPresent("도미노피자", "/logo/domino.png");
        List<String> urlList = new ArrayList<>();
        urlList.add("https://web.dominos.co.kr/goods/list?dsp_ctgr=C0101"); // 메뉴
        urlList.add("https://web.dominos.co.kr/goods/onePizzalist"); // 1인 메뉴
        Document doc;
        try {
            for (String url : urlList) {
                doc = Jsoup.connect(url).get();
                // #content > div > div > article > div:nth-child(6)
                Elements menuList = doc.select("#content > div > div > article").select("div[class=menu-list]");
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
                            // 실행 전 체크할 것
                            saveFood(foodName, image, company);
                        }
                    }
                }
            }
        } catch (IOException e1) {
            System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company.getName());
            e1.printStackTrace();
        }
    }

    public void 페리카나() {
        Company company = isCompanyPresent("페리카나", "");
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
                for (Element e : lists) {
                    // image url: #contents > ul.menu_list.menu_pop > li:nth-child(1) > a > figure > img
                    String image = e.select("a > figure > img").first().absUrl("src");
                    // menu name: #contents > ul.menu_list.menu_pop > li:nth-child(1) > a > figure > figcaption
                    String foodName = e.select("a > figure > figcaption").text();
                    // 만약 같은 이름의 메뉴가 없다면 DB에 저장
                    if (!isFoodPresent(foodName)) {
                        // 실행 전 체크할 것
                        saveFood(foodName, image, company);
                    }
                }
            }
        } catch (IOException e1) {
            System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company.getName());
            e1.printStackTrace();
        }
    }





    // --- METHODS ---

    private Company createCompany(String companyName, String companyLogo) {
        Company company = Company.builder()
                            .image(companyLogo)
                            .name(companyName)
                            .build()
        ;
        if (company.getImage() == "") {
            System.out.println(">>> 회사 <"+company.getName()+">의 로고 이미지를 설정해주세요.");
        }
        return companyService.save(company);
    }


    private Company isCompanyPresent(String companyName, String companyLogo) {
        Company company = companyRepository.findByName(companyName).orElse(
            createCompany(companyName, companyLogo)
        );
        return company;
    }


    private Boolean isFoodPresent(String foodName) {
        if (foodService.findByNameOr(foodName).isPresent()) {
            return true;
        } else {
            return false;
        }
    }


    private void saveFood(String foodName, String imageUrl, Company company) {
        Food food = Food.builder()
                .name(foodName)
                .image(imageUrl)
                .company(company)
                .build()
        ;
        if (foodName.contains("피자") || foodName.contains("그라탕") ) {
            food.setCategory(Category.PIZZA);
        } else if (foodName.contains("치킨") || foodName.contains("닭강정") || foodName.contains("후라이드") ) {
            food.setCategory(Category.CHICKEN);
        } else if (foodName.contains("떡볶이") ) {
            food.setCategory(Category.TTEOKBOKKI);
        } else if (foodName.contains("파스타") || foodName.contains("스파게티") ) {
            food.setCategory(Category.PASTA);
        } else {
            food.setCategory(Category.SIDEMENU);
        }
        foodRepository.save(food);
    }


}