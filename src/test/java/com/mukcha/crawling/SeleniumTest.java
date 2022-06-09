package com.mukcha.crawling;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mukcha.controller.api.helper.WithSelenium;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class SeleniumTest extends WithSelenium {

    @Test
    void uploadTest() {
        String fileName = "/image/burgerking_"+getEncodedFilename("기네스와퍼")+".png";
        File file = new File(fileName);
        String imageUrl = s3Uploader.upload(file, "image");
        System.out.println(imageUrl);
    }


    @Test
    void baskin31() throws InterruptedException {
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
            List<WebElement> menuItem = driver.findElements(By.cssSelector("li[class=item]"));
            for (WebElement menu : menuItem) {
                String name = menu.findElement(By.cssSelector("a > figure > figcaption > span")).getText();
                if (!name.contains("레디팩")) {
                    String image = menu.findElement(By.cssSelector("a > figure > span > img")).getAttribute("src");
                    result.put(name, image);
                }
            }
            driver.quit();
        }
        System.out.println(result);
    }


    @Test
    void goobne() throws InterruptedException {
        String companyName = "굽네치킨";
        companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                    .name(companyName)
                    .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_goobne.jpg")
                    .build();
            Company saved = companyService.save(company);
            return saved;
        });
        WebDriver driver = setupDriver("https://www.goobne.co.kr/menu/menu_list.jsp");
        List<WebElement> menuTabs = driver.findElements(By.tagName("a")); // 모든 a tag를 가져온다
        for (int i=0; i<menuTabs.size(); i++) {
			if (menuTabs.get(i).getText().equals("치킨 시리즈")) {
				menuTabs.get(i).click();
                System.out.println(">>> 탭 진입 - 치킨 시리즈");
                Thread.sleep(2000);
                break;
			}
		}
        List<WebElement> chickenList = driver.findElements(By.xpath("//*[@id=\"menu_list\"]/li"));
        for (WebElement menu : chickenList) {
            String menuName = menu.findElement(By.xpath("p/span")).getText();
            if (!menuName.contains("반반")) { // 반반메뉴 제외
                System.out.println(">>> "+menuName);
                String image = menu.findElement(By.xpath("img")).getAttribute("src");
                System.out.println(">>> "+image);
            }
        }
        for (int i=0; i<menuTabs.size(); i++) {
            if (menuTabs.get(i).getText().equals("피자 시리즈")) {
				menuTabs.get(i).click();
                System.out.println(">>> 탭 진입 - 피자 시리즈");
                Thread.sleep(2000);
                break;
			}
        }
        List<WebElement> pizzaList = driver.findElements(By.xpath("//*[@id=\"menu_list\"]/li"));
        for (WebElement menu : pizzaList) {
            String menuName = menu.findElement(By.xpath("p/span")).getText();
            // 반반메뉴 제외
            if (!menuName.contains("반반")) {
                System.out.println(">>> "+menuName);
                String image = menu.findElement(By.xpath("img")).getAttribute("src");
                System.out.println(">>> "+image);
            }
        }
        driver.quit();
    }


    @Test
    void burgerking() {
        String companyName = "버거킹";
        Company savedCompany = companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_burgerking.png")
                .build();
            Company saved = companyService.save(company);
            return saved;
        });
        WebDriver driver = setupDriver("https://www.burgerking.co.kr");
        burgerkingMoveTabAndCrawling(driver, savedCompany, "프리미엄", Category.HAMBURGER);
        burgerkingMoveTabAndCrawling(driver, savedCompany, "와퍼", Category.HAMBURGER);
        burgerkingMoveTabAndCrawling(driver, savedCompany, "주니어&버거", Category.HAMBURGER);
        burgerkingMoveTabAndCrawling(driver, savedCompany, "올데이킹&치킨버거", Category.HAMBURGER);
        burgerkingMoveTabAndCrawling(driver, savedCompany, "사이드", Category.SIDEMENU);
        driver.quit();
    }
    private void burgerkingMoveTabAndCrawling(WebDriver driver, Company company, String tabName, Category category) {
        List<WebElement> menuTabs = driver.findElements(By.tagName("span")); // 모든 span tag를 가져온다
        for (int i=0; i<menuTabs.size(); i++) {
			if (menuTabs.get(i).getText().equals(tabName)) {
				menuTabs.get(i).click();
                System.out.println("<<< 탭 진입 "+tabName);
				break;
			}
		}
		try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            driver.quit();
        }
        List<WebElement> menus = driver.findElements(By.xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div/div[2]/ul/li"));
        for (WebElement menu : menus) {
            String menuName = menu.findElement(By.className("tit")).getText();
            // 행사, 세트, 소스, 시즈닝 제품은 제외
            String[] filtering = {"행사", "세트", "소스", "시즈닝"};
            if ( !List.of(filtering).stream().anyMatch(n -> menuName.contains(n)) ) {
                System.out.println(">>> "+menuName);
                String image = menu.findElement(By.xpath("div[1]/span/img")).getAttribute("src");
                System.out.println(">>> "+image);
            }
        }
    }


    @Test
    void dongdaemunYupdduk() {
        String companyName = "불닭발땡초동대문엽기떡볶이";
        companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("https://www.yupdduk.com/images/logo61.png")
                .build();
            Company saved = companyService.save(company);
            return saved;
        });
        WebDriver driver = setupDriver("https://www.yupdduk.com/sub/hotmenu?mode=1");
        // 떡볶이 메뉴
        List<WebElement> ddokList = driver.findElements(By.xpath("//*[@id=\"cust1div\"]/div/div[4]/div"));
        for (WebElement menu : ddokList) {
            String menuName = menu.findElement(By.xpath("div/p[1]")).getText();
            if (!menuName.isEmpty()) {
                System.out.println(">>> "+menuName);
                String image = menu.findElement(By.xpath("div/img")).getAttribute("src");
                System.out.println(">>> "+image);
            }
        }
        // 닭발 메뉴
        driver.findElement(By.cssSelector("#FunctionTab5")).click();
        List<WebElement> feetList = driver.findElements(By.xpath("//*[@id=\"cust5div\"]/div/div[2]/div"));
        for (WebElement menu : feetList) {
            String menuName = menu.findElement(By.xpath("div/p[1]")).getText();
            if (!menuName.isEmpty()) {
                System.out.println(">>> "+menuName);
                String image = menu.findElement(By.xpath("div/img")).getAttribute("src");
                System.out.println(">>> "+image);
            }
        }
        driver.quit();
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