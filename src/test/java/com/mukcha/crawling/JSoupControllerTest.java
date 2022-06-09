package com.mukcha.crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mukcha.domain.ErrorMessage;

import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class JSoupControllerTest {


    @Test
    void dongdaemunYupdduk() {
        // https://www.yupdduk.com/images/logo61.png
        String company = "불닭발땡초동대문엽기떡볶이";
        Document doc;
        String url = "https://www.yupdduk.com/sub/hotmenu?mode=1";
        try {
            doc = Jsoup.connect(url).get();
            Elements tap = doc.select("#cust1div"); // 떡볶이 메뉴
            yupddukCrawling(tap);
            tap = doc.select("#cust5div");  // 닭발 메뉴
            yupddukCrawling(tap);
        } catch (IOException e) {
            System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company);
            e.printStackTrace();
        }
    }
    private void yupddukCrawling(Elements tap) {
        Elements menuList = tap.select("div[class=col-lg-4]");
        for (Element menu : menuList) {
            String menuName = menu.select("p[class=mpnone hotmenutitle]").text();
            System.out.println(">>> name: "+menuName);
            String image = menu.select("div").select("img").attr("src").replace("..", "");
            if (image.contains("icon")) {
                continue;
            }
            image = "https://www.yupdduk.com" + image;
            System.out.println(">>> image: "+image);
        }
    }



    @Test
    void baedduck() {
        // http://baedduck.co.kr/theme/bae-default/layout/assets/img/brand_04.png
        String company = "배달떡볶이(배떡)";
        Document doc;
        String url = "http://baedduck.co.kr/subpage/menu";
        try {
            doc = Jsoup.connect(url).get();
            Elements menus = doc.select("div[class=menu-item]");
            for (Element menu : menus) {
                //#wrap > section.side-menu.sub-section > div > div > ul > li:nth-child(1) > div > p
                String menuName = menu.select("p").text();
                System.out.println(">>>name:"+menuName);
                if (menuName.contains("예정")) {
                    continue;
                }
                String image = menu.select("img").attr("src");
                image = "http://baedduck.co.kr" + image;
                System.out.println(">>>image:"+image);
            }
        } catch (IOException e) {
            System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company);
            e.printStackTrace();
        }
    }



    @Test
    void sinjeonTest() {
        // http://sinjeon.co.kr/img/common/logo_2022.png
        String company = "신전떡볶이";
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
                    System.out.println(">>> name: "+menuName);
                    // 이미지: li:nth-child(1) > a > img
                    String image = menu.select("a > img").attr("src");
                    image = "http://sinjeon.co.kr" + image;
                    System.out.println(">>> image: "+image);
                }
            } catch (IOException e) {
                System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company);
                e.printStackTrace();
            }
        }
    }

    @Test
    void cheogaTest() {
        String company = "처갓집양념치킨";
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
                    String menuName = menu.select("ul > li.gall_text_href").text();
                    if (menuName.length() == 0) {
                        continue;
                    }
                    System.out.println(">>> name:"+menuName.split(" ")[0] + menuName.split(" ")[1]);
                    String image = menu.select("ul > li.gall_href > img").attr("src");
                    System.out.println(">>> image:"+image);
                }
            } catch (IOException e) {
                System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company);
                e.printStackTrace();
            }
        }
    }

    @Test
    void kfcTest() {
        String company = "KFC";
        Document doc;
        List<String> urlList = new ArrayList<>();
        urlList.add("https://www.kfckorea.com/menu/burger"); // 버거
        urlList.add("https://www.kfckorea.com/menu/chicken"); // 치킨
        for (String url : urlList) {
            try {
                doc = Jsoup.connect(url).get();
                // 메뉴 이름을 가져온다 - #app > div:nth-child(2) > div > section > div:nth-child(2) > div > ul > li:nth-child(1) > h3
                Elements list1 = doc.select("#app > div:nth-child(2) > div > section > div:nth-child(2) > div > ul > li");
                for (Element li : list1) {
                    String menuName = li.select("h3").text();
                    String[] name = menuName.split(" ");
                    menuName = name[0].replace("1＋1", "");
                    System.out.println(">>> foodName: "+menuName);
                    // 해당 메뉴가 DB에 없다면
                    // String menuImage = li.select("div.contents > a > img").attr("src");
                    // <img img-server="" alt="쏘랑이블랙라벨치킨 5조각" data-v-c16af4d8> 에서 어떻게 url를 가져올 것인가. .absUrl("data-v-c16af4d8")
                    System.out.println(">>> menuImage2: "+li.select("div.contents > a > img").first().absUrl("data-v-c16af4d8"));
                }
            } catch (IOException e1) {
                System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company);
                e1.printStackTrace();
            }
        }
    }


    @Test
    void dominoPizza() {
        String company = "도미노피자";    
        List<String> urlList = new ArrayList<>();
        urlList.add("https://web.dominos.co.kr/goods/list?dsp_ctgr=C0101"); // 메뉴
        urlList.add("https://web.dominos.co.kr/goods/onePizzalist"); // 1인 메뉴
        Document doc;
        try {
            for (String url : urlList) {
                doc = Jsoup.connect(url).get();
                Elements menuList = doc.select("#content > div > div > article").select("div[class=menu-list]");
                for (Element e : menuList) {
                    Elements secondList = e.select("ul > li");
                    for (Element li : secondList) {
                        String foodName = li.select("div.prd-cont").select("div[class=subject]")
                                            .text()
                                            .replace("NEW", "")
                                            .replace("BEST", "")
                        ;
                        if (!foodName.isEmpty() && !foodName.isBlank()) {
                            System.out.println(">>> food:"+foodName);
                            // image url을 가져온다: > div.prd-img > a:nth-child(1) > img
                            String image = li.select("div.prd-img > a").first().select("img").first().absUrl("data-src");
                            System.out.println(">>> image:"+image);
                        }
                    }
                }
            }
        } catch (IOException e1) {
            System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company);
            e1.printStackTrace();
        }
    }


    @Test
    void pericanaTest() {
        String company = "페리카나";
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
                    System.out.println(">>> image: "+image);
                    // menu name: #contents > ul.menu_list.menu_pop > li:nth-child(1) > a > figure > figcaption
                    String foodName = e.select("a > figure > figcaption").text();
                    System.out.println(">>> food: "+foodName);
                    // 만약 같은 이름의 메뉴가 없다면 DB에 저장
                    // if (!isFoodPresent(foodName))
                }
            }
        } catch (IOException e1) {
            System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company);
            e1.printStackTrace();
        }
    }


    // 정확한 image URL는 모달을 통하여 가져올 수 밖에 없어서 못가져올듯
    @Test
    void chickenPlusTest() {
        String company = "치킨플러스";

        List<String> urlList = new ArrayList<>();
        urlList.add("http://www.chickenplus.co.kr/menu/default.aspx?menu=치킨메뉴");
        urlList.add("http://www.chickenplus.co.kr/menu/default.aspx?menu=피자메뉴");
        urlList.add( "http://www.chickenplus.co.kr/menu/default.aspx?menu=떡볶이메뉴");
        Document doc;
        try {
            for (String url : urlList) {
                doc = Jsoup.connect(url).get();
                Elements lists = doc.select("div[class=mn_area]").select("div[groupname=MenuItem]");
                for (Element e : lists) {
                    // menu name
                    String foodName = e.select("div.mn1_txt > p.mn1_tit.menu_s_title").text();
                    System.out.println(">>> food name: "+foodName);
                    // 만약 같은 이름의 메뉴가 없다면 DB에 저장
                }
            }
        } catch (IOException e1) {
            System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company);
            e1.printStackTrace();
        }
    }


    @Test
    void BBQ() {
        String company = "BBQ";
        Document doc;
        String getFoodName;
        String getImageUrl;
        List<String> urlList = new ArrayList<>();
        urlList.add("https://www.bbq.co.kr/menu/menuList.asp?cidx=113&cname=신메뉴"); // 신메뉴
        urlList.add("https://www.bbq.co.kr/menu/menuList.asp?cidx=114&cname=황올+시그니처");
        urlList.add("https://www.bbq.co.kr/menu/menuList.asp?cidx=124&cname=1인분+메뉴"); // 1인분 메뉴
        urlList.add("https://www.bbq.co.kr/menu/menuList.asp?cidx=7&cname=황올한+양념"); // 황올한 양념
        urlList.add("https://www.bbq.co.kr/menu/menuList.asp?cidx=8&cname=황올한+구이"); // 황올한 구이
        // urlList.add("https://www.bbq.co.kr/menu/menuList.asp?cidx=99999&cname=사이드메뉴"); // 사이드메뉴

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
                    // if (!isFoodPresent(getFoodName))
                }
            }
        } catch (IOException e) {
            System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company);
            e.printStackTrace();
        }
    }




}
