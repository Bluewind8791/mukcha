package com.mukcha.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.ErrorMessage;
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
        System.out.println(">>> CRAWLING START <<<");
        // 치킨플러스();
        // kfc();
        // 처갓집양념치킨();
        // dongdaemunYupdduk();
        return "redirect:/admin";
    }

    public void dbInitCrawling() {
        빅스타피자();
        // dominoPizza();
        페리카나();
        // BBQ();
        sinjeon();
        // baedduck();
    }


    /**
     * 이미지를 가져올 때 아이콘이 있는 메뉴는 아이콘만 가져오는 현상이 있음.
     */
    void dongdaemunYupdduk() {
        Company company = isCompanyPresent("불닭발땡초동대문엽기떡볶이", "https://www.yupdduk.com/images/logo61.png");
        Document doc;
        String url = "https://www.yupdduk.com/sub/hotmenu?mode=1";
        try {
            doc = Jsoup.connect(url).get();
            Elements tap = doc.select("#cust1div"); // 떡볶이 메뉴
            yupddukCrawling(tap, company);
            tap = doc.select("#cust5div");  // 닭발 메뉴
            yupddukCrawling(tap, company);
        } catch (IOException e) {
            System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company.getName());
            e.printStackTrace();
        }
    }
    private void yupddukCrawling(Elements tap, Company company) {
        Elements menuList = tap.select("div[class=col-lg-4]");
        for (Element menu : menuList) {
            String menuName = menu.select("p[class=mpnone hotmenutitle]").text();
            if (!isFoodPresent(menuName) && !menuName.isEmpty()) {
                String image = menu.select("div").select("img").attr("src").replace("..", "");
                if (image.contains("icon")) {
                    image = "";
                } else {
                    image = "https://www.yupdduk.com" + image;
                }
                saveFood(menuName, image, company);
            }
        }
    }


    /**
     * 메뉴 이름 뒤에 설명들도 함께 딸려오는 문제가 있음.
    */
    public void 처갓집양념치킨() {
        Company company = isCompanyPresent("처갓집양념치킨", "/logo/logo-cheoga.png");
        Document doc;
        List<String> urlList = new ArrayList<>();
        urlList.add("http://www.cheogajip.co.kr/bbs/board.php?bo_table=allmenu&page=1"); // 1페이지
        urlList.add("http://www.cheogajip.co.kr/bbs/board.php?bo_table=allmenu&page=2"); // 2페이지
        for (String url : urlList) {
            try {
                doc = Jsoup.connect(url).get();
                // 메뉴 리스트 - #gall_ul > li:nth-child(1)
                Elements menuList = doc.select("#gall_ul").select("li");
                for (Element menu : menuList) {
                    // #gall_ul > li:nth-child(1) > ul > li.gall_text_href
                    Elements menuElement = menu.select("ul > li.gall_text_href").not("hr").not("ul").not("p").not("h3");
                    String menuName = menuElement.text();
                    if (menuName.length() == 0) {
                        continue;
                    }
                    String image = menu.select("ul > li.gall_href > img").attr("src");
                    saveFood(menuName, image, company);
                }
            } catch (IOException e) {
                System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company);
                e.printStackTrace();
            }
        }
    }

    /**
     * <img data-v-c16af4d8="" img-server="" alt="타워버거 세트할인" src="https://kfcapi.inicis.com/kfcs_api_img/KFCS/goods/DL_2275376_20220404112503357.png">
     * 찍으면 <img img-server="" alt="쏘랑이블랙라벨치킨 5조각" data-v-c16af4d8> 에서 어떻게 url를 가져올 것인가.
     */
    public void kfc() {
        Company company = isCompanyPresent("KFC", "");
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

    /**
     * 정확한 image URL는 모달을 통하여 가져올 수 밖에 없어서 못가져올듯
     */
    public void 치킨플러스() {
        Company company = isCompanyPresent("치킨플러스", "");

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
                    saveFood(foodName, "", company);
                }
            }
        } catch (IOException e1) {
            System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company.getName());
            e1.printStackTrace();
        }
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

    void sinjeon() {
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

    void BBQ() {
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


    void 빅스타피자() {
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


    void dominoPizza() {
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

    void 페리카나() {
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
        Company company = companyService.findByNameOr(companyName).orElse(
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
        foodService.save(food);
        log.info(">>> 메뉴<+"+food.getName()+"> 이 DB에 생성되었습니다." + food.toString());
    }


}
