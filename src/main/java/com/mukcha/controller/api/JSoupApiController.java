package com.mukcha.controller.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mukcha.controller.api.helper.CrawlingHelper;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.service.CompanyService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


// ROLE_ADMIN 만 접근가능
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/admin")
public class JSoupApiController extends CrawlingHelper {

    private final CompanyService companyService;
    private final CompanyRepository companyRepository;


    @GetMapping("/crawling/배달떡볶이")
    public ResponseEntity<Object> crawlingCheoga() {
        Map<String, String> body;
        try {
            body = baedduck();
            return ResponseEntity.ok(body);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/crawling/신전떡볶이")
    public ResponseEntity<Object> crawlingSinjeon() {
        Map<String, String> body;
        try {
            body = sinjeon();
            return ResponseEntity.ok(body);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/crawling/도미노피자")
    public ResponseEntity<Object> crawlingDomino() {
        Map<String, String> body;
        try {
            body = dominoPizza();
            return ResponseEntity.ok(body);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/crawling/페리카나")
    public ResponseEntity<Object> crawlingPericana() {
        Map<String, String> body;
        try {
            body = pericana();
            return ResponseEntity.ok(body);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public Map<String, String> pericana() throws IOException {
        Map<String, String> result = new HashMap<>();
        String companyName = "페리카나";
        Company saved = companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("")
                .build();
            return companyService.save(company);
        });
        List<String> urlList = new ArrayList<>();
        urlList.add("https://pelicana.co.kr/menu/menu_original.html"); // 오리지널
        urlList.add("https://pelicana.co.kr/menu/menu_sunsal.html"); // 순살
        urlList.add("https://pelicana.co.kr/menu/menu_sinsunpin.html"); // 신선핀
        urlList.add("https://pelicana.co.kr/menu/menu_sinsunwing.html"); // 신선윙
        urlList.add("https://pelicana.co.kr/menu/menu_periwing.html"); // 페리윙봉
        urlList.add("https://pelicana.co.kr/menu/menu_rospin.html"); // 로스핀
        urlList.add("https://pelicana.co.kr/menu/menu_bburio.html"); // 뿌리오
        urlList.add("https://pelicana.co.kr/menu/menu_mapsak.html"); // 맵삭치킨
        for (String url : urlList) {
            Map<String, String> data = periMethod(url);
            result.putAll(data);
            save(data, saved, "pericana", Category.CHICKEN);
        }
        Map<String, String> data = periMethod("https://pelicana.co.kr/menu/side_menu.html"); // 사이드
        result.putAll(data);
        save(data, saved, "pericana", Category.SIDEMENU);
        return result;
    }
    private Map<String, String> periMethod(String url) throws IOException {
        Map<String, String> data = new HashMap<>();
        Document doc = Jsoup.connect(url).get();
        Elements lists = doc.select("#contents > ul.menu_list.menu_pop > li");
        for (Element e : lists) {
            String foodName = e.select("a > figure > figcaption").text();
            String image = e.select("a > figure > img").first().absUrl("src");
            data.put(foodName, image);
        }
        return data;
    }


    public Map<String, String> dominoPizza() throws IOException {
        Map<String, String> result = new HashMap<>();
        String companyName = "도미노피자";
        Company saved = companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("/logo/domino.png")
                .build();
            return companyService.save(company);
        });
        List<String> urlList = new ArrayList<>();
        urlList.add("https://web.dominos.co.kr/goods/list?dsp_ctgr=C0101"); // 메뉴
        urlList.add("https://web.dominos.co.kr/goods/onePizzalist"); // 1인 메뉴
        for (String url : urlList) {
            Map<String, String> data = dominoMethod(url);
            save(data, saved, "domino", Category.PIZZA);
            result.putAll(data);
        }
        Map<String, String> data = dominoMethod("https://web.dominos.co.kr/goods/list?dsp_ctgr=C0201"); // 사이드디시
        result.putAll(data);
        data.forEach((name,image) -> {
            String imageUrl = saveImage(name, image, "domino");
            if (name.contains("파스타") || name.contains("스파게티") || name.contains("페투치니")) {
                updateMenu(name, imageUrl, saved, Category.PASTA); // save
            } else {
                updateMenu(name, imageUrl, saved, Category.SIDEMENU); // save
            }
        });
        return result;
    }
    private Map<String, String> dominoMethod(String url) throws IOException {
        Map<String, String> data = new HashMap<>();
        Document doc = Jsoup.connect(url).get();
        Elements menuList = doc.selectXpath("//*[@id=\"content\"]/div/div/article/div[5]/ul/li");
        for (Element menu : menuList) {
            // .../div[2]/div
            String foodName = menu.select("div.prd-cont").select("div[class=subject]")
                .text()
                .replace(" NEW", "")
                .replace(" BEST", "")
                .replace(" 기간한정", "")
                .replace(" 50%할인", "")
            ;
            if (!foodName.isEmpty()) {
                String image = menu.select("div.prd-img > a").first().select("img").first().absUrl("data-src");
                data.put(foodName, image);
            }
            
        }
        return data;
    }


    public Map<String, String> sinjeon() throws IOException {
        Map<String, String> result = new HashMap<>();
        String companyName = "신전떡볶이";
        Company saved = companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("http://sinjeon.co.kr/img/common/logo_2022.png")
                .build();
            return companyService.save(company);
        });
        Document doc;
        List<String> urlList = new ArrayList<>();
        urlList.add("http://sinjeon.co.kr/doc/menu03.php"); // 떡볶이
        urlList.add("http://sinjeon.co.kr/doc/menu04.php"); // 튀김
        urlList.add("http://sinjeon.co.kr/doc/menu05.php"); // 라이스
        for (String url : urlList) {
            doc = Jsoup.connect(url).get();
            String[] filtering = {"분모자" ,"납작면", "토핑"};
            Elements menuList = doc.select("li[class=cars]");
            for (Element menu : menuList) {
                String menuName = menu.select("a > p > span").text();
                if (!List.of(filtering).stream().anyMatch(n -> menuName.contains(n))) {
                    String image = menu.select("a > img").attr("src");
                    image = "http://sinjeon.co.kr" + image;
                    String imageUrl = saveImage(menuName, image, "baedduck");
                    if (menuName.contains("떡볶이")) {
                        updateMenu(menuName, imageUrl, saved, Category.TTEOKBOKKI); // save
                    } else {
                        updateMenu(menuName, imageUrl, saved, Category.SIDEMENU); // save
                    }
                    result.put(menuName, image);
                }
            }
        }
        return result;
    }


    public Map<String, String> baedduck() throws IOException {
        Map<String, String> result = new HashMap<>();
        String companyName = "배달떡볶이";
        Company saved = companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_baedduck.png")
                .build();
            return companyService.save(company);
        });
        Document doc;
        String url = "http://baedduck.co.kr/subpage/menu";
        doc = Jsoup.connect(url).get();
        Elements menus = doc.select("div[class=menu-item]");
        for (Element menu : menus) {
            String menuName = menu.select("p").text();
            if (menuName.contains("예정") || menuName.contains("세트")) {
                continue;
            }
            String image = menu.select("img").attr("src");
            image = "http://baedduck.co.kr" + image;
            String imageUrl = saveImage(menuName, image, "baedduck");
            if (menuName.contains("떡볶이")) {
                updateMenu(menuName, imageUrl, saved, Category.TTEOKBOKKI);
            } else {
                updateMenu(menuName, imageUrl, saved, Category.SIDEMENU);
            }
            result.put(menuName, image);
        }
        return result;
    }

}