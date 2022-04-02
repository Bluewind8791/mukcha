package com.mukcha.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class CrawlingControllerTest {

    private String ERROR_MESSAGE = ">>> 해당 회사의 정보를 불러올 수 없습니다.";


    @Test
    void testCrawling() {
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
                System.out.println(ERROR_MESSAGE + company);
                e.printStackTrace();
            }
        }
    }


}
