package com.mukcha.crawling;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.mukcha.controller.api.helper.WithSelenium;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class SeleniumCrawlingTest extends WithSelenium {

    Company company;

    @Test
    void baskin31() throws InterruptedException {
        Map<String, String> result = new HashMap<>();
        String companyName = "배스킨라빈스";
        companyService.findByNameOr(companyName).or(() -> {
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
                String name = menu.findElement(By.cssSelector("a > figure > figcaption > span")).getText();
                if (!name.contains("레디팩")) {
                    // #prd_list > aside > ul > li:nth-child(1) > a > figure > span > img
                    String image = menu.findElement(By.cssSelector("a > figure > span > img")).getAttribute("src");
                    result.put(name, image);
                }
            }
            driver.close();
        }
        System.out.println(result);
    }


    /* >>> 굽네치킨 <<< */
    @Test
    void goobne() throws InterruptedException {
        String companyName = "굽네치킨";
        companyService.findByNameOr(companyName).or(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_goobne.jpg")
                .build();
            Company saved = companyService.save(company);
            return Optional.of(saved);
        });

        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("headless"); // 브라우저 보이지 않기
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://www.goobne.co.kr/menu/menu_list.jsp"); // WebDriver을 해당 url로 이동한다.
        //브라우저 이동시 생기는 로드시간을 기다린다.
		//HTTP 응답속도보다 자바의 컴파일 속도가 더 빠르기 때문에 임의적으로 대기한다.
		Thread.sleep(2000);
        goobneMoveTab(driver, "치킨 시리즈", Category.CHICKEN);
        goobneMoveTab(driver, "피자 시리즈", Category.PIZZA);
        driver.close();
    }
    private void goobneMoveTab(WebDriver driver, String tabName, Category category) {
        List<WebElement> menuTabs = driver.findElements(By.tagName("a")); // 모든 a tag를 가져온다
        for (int i=0; i<menuTabs.size(); i++) {
			// span tag 중 "프리미엄"라는 텍스트를 가진 WebElement를 클릭한다.
			if (menuTabs.get(i).getText().equals(tabName)) {
				menuTabs.get(i).click();
                System.out.println("<<< 탭 진입 - "+tabName);
				break;
			}
		}
		try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        goobneCrawling(driver, category);
    }
    private void goobneCrawling(WebDriver driver, Category category) {
        List<WebElement> menuList = driver.findElements(By.xpath("//*[@id=\"menu_list\"]/li"));
        for (WebElement menu : menuList) {
            String menuName = menu.findElement(By.xpath("p/span")).getText();
            // 반반메뉴 제외
            if (!menuName.contains("반반")) {
                System.out.println("<<< menu name: " + menuName);
                String image = menu.findElement(By.xpath("img")).getAttribute("src");
                System.out.println("<<< image: " + image);
                String imageUrl = saveImage(menuName, image, "goobne");
                updateMenu(menuName, imageUrl, company, category);
            }
        }
    }



    /** >>> 버거킹 <<< */
    @Test
    void burgerking() throws InterruptedException {
        String companyName = "버거킹";
        companyService.findByNameOr(companyName).or(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_burgerking.png")
                .build();
            Company saved = companyService.save(company);
            return Optional.of(saved);
        });
        
        
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("headless"); // 브라우저 보이지 않기
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://www.burgerking.co.kr/"); // WebDriver을 해당 url로 이동한다.
        //브라우저 이동시 생기는 로드시간을 기다린다.
		//HTTP 응답속도보다 자바의 컴파일 속도가 더 빠르기 때문에 임의적으로 대기한다.
		Thread.sleep(2000);
        burgerkingMoveTabAndCrawling(driver, "프리미엄", Category.HAMBURGER);
        burgerkingMoveTabAndCrawling(driver, "와퍼", Category.HAMBURGER);
        burgerkingMoveTabAndCrawling(driver, "주니어&버거", Category.HAMBURGER);
        burgerkingMoveTabAndCrawling(driver, "올데이킹&치킨버거", Category.HAMBURGER);
        burgerkingMoveTabAndCrawling(driver, "사이드", Category.SIDEMENU);
        driver.close();
    }
    private void burgerkingMoveTabAndCrawling(WebDriver driver, String tabName, Category category) {
        List<WebElement> menuTabs = driver.findElements(By.tagName("span")); // 모든 span tag를 가져온다
        for (int i=0; i<menuTabs.size(); i++) {
			// span tag 중 "프리미엄"라는 텍스트를 가진 WebElement를 클릭한다.
			if (menuTabs.get(i).getText().equals(tabName)) {
				menuTabs.get(i).click();
                System.out.println("<<< 탭 진입 "+tabName);
				break;
			}
		}
		try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        burgerkingCrawling(driver, category);
    }
    private void burgerkingCrawling(WebDriver driver, Category category) {
        List<WebElement> menus = driver.findElements(By.xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div/div[2]/ul/li"));
        for (WebElement menu : menus) {
            String menuName = menu.findElement(By.className("tit")).getText();
            System.out.println("<<< name:"+menuName);
            // 행사, 세트, 소스 제품은 제외
            if (!menuName.contains("행사") && !menuName.contains("세트") && !menuName.contains("소스")) {
                String image = menu.findElement(By.xpath("div[1]/span/img")).getAttribute("src");
                System.out.println("<<< image:"+image);
                BufferedImage saveImage = null;
                try {
                    saveImage = ImageIO.read(new URL(image));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String fileName = "/image/burgerking_"+getEncodedFilename(menuName)+".png";
                File file = new File(fileName);
                try {
                    ImageIO.write(saveImage, "png", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String imageUrl = s3Uploader.upload(file, "image");
                updateMenu(menuName, imageUrl, company, category);
            }
        }
    }

    // /**
    //  * 이미지를 가져올 때 아이콘이 있는 메뉴는 아이콘만 가져오는 현상이 있음.
    //  */
    // void dongdaemunYupdduk() {
    //     Company company = isCompanyPresent("불닭발땡초동대문엽기떡볶이", "https://www.yupdduk.com/images/logo61.png");
    //     Document doc;
    //     String url = "https://www.yupdduk.com/sub/hotmenu?mode=1";
    //     try {
    //         doc = Jsoup.connect(url).get();
    //         Elements tap = doc.select("#cust1div"); // 떡볶이 메뉴
    //         yupddukCrawling(tap, company);
    //         tap = doc.select("#cust5div");  // 닭발 메뉴
    //         yupddukCrawling(tap, company);
    //     } catch (IOException e) {
    //         System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company.getName());
    //         e.printStackTrace();
    //     }
    // }
    // private void yupddukCrawling(Elements tap, Company company) {
    //     Elements menuList = tap.select("div[class=col-lg-4]");
    //     for (Element menu : menuList) {
    //         String menuName = menu.select("p[class=mpnone hotmenutitle]").text();
    //         if (!isFoodPresent(menuName) && !menuName.isEmpty()) {
    //             String image = menu.select("div").select("img").attr("src").replace("..", "");
    //             if (image.contains("icon")) {
    //                 image = "";
    //             } else {
    //                 image = "https://www.yupdduk.com" + image;
    //             }
    //             saveFood(menuName, image, company);
    //         }
    //     }
    // }


    // /**
    //  * 메뉴 이름 뒤에 설명들도 함께 딸려오는 문제가 있음.
    // */
    // public void 처갓집양념치킨() {
    //     Company company = isCompanyPresent("처갓집양념치킨", "/logo/logo-cheoga.png");
    //     Document doc;
    //     List<String> urlList = new ArrayList<>();
    //     urlList.add("http://www.cheogajip.co.kr/bbs/board.php?bo_table=allmenu&page=1"); // 1페이지
    //     urlList.add("http://www.cheogajip.co.kr/bbs/board.php?bo_table=allmenu&page=2"); // 2페이지
    //     for (String url : urlList) {
    //         try {
    //             doc = Jsoup.connect(url).get();
    //             // 메뉴 리스트 - #gall_ul > li:nth-child(1)
    //             Elements menuList = doc.select("#gall_ul").select("li");
    //             for (Element menu : menuList) {
    //                 // #gall_ul > li:nth-child(1) > ul > li.gall_text_href
    //                 Elements menuElement = menu.select("ul > li.gall_text_href").not("hr").not("ul").not("p").not("h3");
    //                 String menuName = menuElement.text();
    //                 if (menuName.length() == 0) {
    //                     continue;
    //                 }
    //                 String image = menu.select("ul > li.gall_href > img").attr("src");
    //                 saveFood(menuName, image, company);
    //             }
    //         } catch (IOException e) {
    //             System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company);
    //             e.printStackTrace();
    //         }
    //     }
    // }

    // /**
    //  * <img data-v-c16af4d8="" img-server="" alt="타워버거 세트할인" src="https://kfcapi.inicis.com/kfcs_api_img/KFCS/goods/DL_2275376_20220404112503357.png">
    //  * 찍으면 <img img-server="" alt="쏘랑이블랙라벨치킨 5조각" data-v-c16af4d8> 에서 어떻게 url를 가져올 것인가.
    //  */
    // public void kfc() {
    //     Company company = isCompanyPresent("KFC", "");
    //     Document doc;
    //     List<String> urlList = new ArrayList<>();
    //     urlList.add("https://www.kfckorea.com/menu/burger"); // 버거
    //     urlList.add("https://www.kfckorea.com/menu/chicken"); // 치킨
    //     try {
    //         for (String url : urlList) {
    //             doc = Jsoup.connect(url).get();
    //             // 메뉴 리스트
    //             Elements menuList = doc.select("div[menu-list= ]").select("section > ul > li");
    //             for (Element li : menuList) {
    //                 // 메뉴 이름을 가져온다 - section > ul > li:nth-child(1) > h3
    //                 String foodName = li.select("h3").text();
    //                 String[] name = foodName.split(" ");
    //                 foodName = name[0].replace("1＋1", "");
    //                 // 만약 같은 이름의 메뉴가 없다면
    //                 if (!isFoodPresent(foodName)) {
    //                     Elements imageE = li.select("div > a > img");
    //                     String image = imageE.attr("src");
    //                     // 실행 전 체크할 것
    //                     saveFood(foodName, image, company);
    //                 }
    //             }
    //         }
    //     } catch (IOException e1) {
    //         System.out.println(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + company.getName());
    //         e1.printStackTrace();
    //     }
    // }



    @Test
    void uploadTest() {
        String fileName = "/image/burgerking_"+getEncodedFilename("기네스와퍼")+".png";
        File file = new File(fileName);
        String imageUrl = s3Uploader.upload(file, "image");
        System.out.println(imageUrl);
    }

}