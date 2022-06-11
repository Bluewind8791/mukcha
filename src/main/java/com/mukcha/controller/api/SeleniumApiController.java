package com.mukcha.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/crawling/굽네치킨")
    public ResponseEntity<Object> crawlingGoobne() {
        try {
            Map<String, String> body = goobne();
            return ResponseEntity.ok(body);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/crawling/버거킹")
    public ResponseEntity<Object> crawlingBurgerKing() {
        try {
            Map<String, String> body = burgerking();
            return ResponseEntity.ok(body);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/crawling/불닭발땡초동대문엽기떡볶이")
    public ResponseEntity<Object> crawlingYupdduk() {
        Map<String, String> body = dongdaemunYupdduk();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/crawling/처갓집양념치킨")
    public ResponseEntity<Object> crawlingCheoga() {
        Map<String, String> body = cheoga();
        return ResponseEntity.ok(body);
    }

    public Map<String, String> kfc() {
        Map<String, String> result = new HashMap<>();
        String companyName = "KFC";
        Company saved = companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_KFC.png")
                .build();
            return companyService.save(company);
        });
        List<String> urlList = new ArrayList<>();
        urlList.add("https://www.kfckorea.com/menu/burger"); // 버거
        urlList.add("https://www.kfckorea.com/menu/chicken"); // 치킨
        for (String url : urlList) {
            Map<String, String> data = new HashMap<>();
            WebDriver driver = setupDriver(url);
            List<WebElement> menuList = driver.findElements(By.xpath("//*[@id=\"app\"]/div[2]/div/section/div[2]/div/ul/li"));
            for (WebElement menu : menuList) {
                String menuName = menu.findElement(By.xpath("h3")).getText();
                if (menuName.contains("5조각")) {
                    menuName = menuName.replace("5조각", "");
                }
                String name = menuName;
                String[] filtering = {"팩" ,"박스", "세트", "1조각", "3조각", "8조각"};
                if ( !List.of(filtering).stream().anyMatch(n -> name.contains(n)) ) { // filtering
                    String image = menu.findElement(By.xpath("div[1]/a/img")).getAttribute("src");
                    data.put(name, image);
                }
            }
            driver.quit();
            if (url.contains("burger")) {
                save(data, saved, "kfc", Category.HAMBURGER); // save
            } else if (url.contains("chicken")) {
                save(data, saved, "kfc", Category.CHICKEN); // save
            }
            result.putAll(data);
        }
        return result;
    }


    public Map<String, String> cheoga() {
        Map<String, String> result = new HashMap<>();
        String companyName = "처갓집양념치킨";
        Company saved = companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_cheoga.png")
                .build();
            return companyService.save(company);
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
                    String image = menu.findElement(By.xpath("ul/li[1]/img")).getAttribute("src");
                    Map<String, String> data = new HashMap<>();
                    data.put(menuName, image);
                    String[] classification = {"치즈볼", "후라이", "라이스", "근위"};
                    if (List.of(classification).stream().anyMatch(n -> menuName.contains(n))) {
                        save(data, saved, "cheoga", Category.SIDEMENU); // save
                    } else {
                        save(data, saved, "cheoga", Category.CHICKEN); // save
                    }
                    result.putAll(data);
                }
            }
            driver.quit();
        }
        return result;
    }


    public Map<String, String> dongdaemunYupdduk() {
        Map<String, String> result = new HashMap<>();
        String companyName = "불닭발땡초동대문엽기떡볶이";
        Company targetCompany = companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_dongyupdduk.png")
                .build();
            return companyService.save(company);
        });
        WebDriver driver = setupDriver("https://www.yupdduk.com/sub/hotmenu?mode=1");
        // 떡볶이 메뉴
        Map<String, String> data = new HashMap<>();
        List<WebElement> ddokList = driver.findElements(By.xpath("//*[@id=\"cust1div\"]/div/div[4]/div"));
        for (WebElement menu : ddokList) {
            String menuName = menu.findElement(By.xpath("div/p[1]")).getText();
            if (!menuName.isEmpty()) {
                String image = menu.findElement(By.xpath("div/img")).getAttribute("src");
                data.put(menuName, image);
            }
        }
        save(data, targetCompany, "yupdduk", Category.TTEOKBOKKI); // save
        result.putAll(data);
        data.clear();
        // 닭발 메뉴
        driver.findElement(By.cssSelector("#FunctionTab5")).click();
        List<WebElement> feetList = driver.findElements(By.xpath("//*[@id=\"cust5div\"]/div/div[2]/div"));
        for (WebElement menu : feetList) {
            String menuName = menu.findElement(By.xpath("div/p[1]")).getText();
            if (!menuName.isEmpty()) {
                String image = menu.findElement(By.xpath("div/img")).getAttribute("src");
                data.put(menuName, image);
            }
        }
        driver.quit();
        save(data, targetCompany, "yupdduk", Category.SIDEMENU); // save
        result.putAll(data);
        return result;
    }


    public Map<String, String> burgerking() throws InterruptedException {
        Map<String, String> result = new HashMap<>();
        String companyName = "버거킹";
        Company savedCompany = companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_burgerking.png")
                .build();
            return companyService.save(company);
        });
        WebDriver driver = setupDriver("https://www.burgerking.co.kr");
        Map<String, String> data = new HashMap<>();
        data = burgerkingMoveTabAndCrawling(driver, "프리미엄");
        save(data, savedCompany, "burgerking", Category.HAMBURGER); // save
        result.putAll(data);
        data.clear();

        data = burgerkingMoveTabAndCrawling(driver, "와퍼");
        save(data, savedCompany, "burgerking", Category.HAMBURGER); // save
        result.putAll(data);
        data.clear();

        data = burgerkingMoveTabAndCrawling(driver, "주니어&버거");
        save(data, savedCompany, "burgerking", Category.HAMBURGER); // save
        result.putAll(data);
        data.clear();

        data = burgerkingMoveTabAndCrawling(driver, "올데이킹&치킨버거");
        save(data, savedCompany, "burgerking", Category.HAMBURGER); // save
        result.putAll(data);
        data.clear();

        data = burgerkingMoveTabAndCrawling(driver, "사이드");
        save(data, savedCompany, "burgerking", Category.SIDEMENU); // save
        result.putAll(data);
        driver.quit();
        return result;
    }
    private Map<String, String> burgerkingMoveTabAndCrawling(WebDriver driver, String tabName) throws InterruptedException {
        Map<String, String> result = new HashMap<>();
        List<WebElement> menuTabs = driver.findElements(By.tagName("span")); // 모든 span tag를 가져온다
        for (int i=0; i<menuTabs.size(); i++) {
			if (menuTabs.get(i).getText().equals(tabName)) {
				menuTabs.get(i).click();
                System.out.println("<<< 탭 진입 "+tabName);
				break;
			}
		}
		Thread.sleep(1000);
        List<WebElement> menus = driver.findElements(By.xpath("//*[@id=\"app\"]/div/div[3]/div[2]/div/div[2]/ul/li"));
        for (WebElement menu : menus) {
            String menuName = menu.findElement(By.className("tit")).getText();
            // 행사, 세트, 소스, 시즈닝 제품은 제외
            String[] filtering = {"행사", "세트", "소스", "시즈닝"};
            if ( !List.of(filtering).stream().anyMatch(n -> menuName.contains(n)) ) {
                String image = menu.findElement(By.xpath("div[1]/span/img")).getAttribute("src");
                result.put(menuName, image);
            }
        }
        return result;
    }


    public Map<String, String> goobne() throws InterruptedException {
        Map<String, String> result = new HashMap<>();
        String companyName = "굽네치킨";
        Company targetCompany = companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                    .name(companyName)
                    .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_goobne.jpg")
                    .build();
            return companyService.save(company);
        });
        WebDriver driver = setupDriver("https://www.goobne.co.kr/menu/menu_list.jsp");
        Map<String, String> chickenList = goobneCrawling(driver, "치킨 시리즈");
        chickenList.forEach((menuName, image) -> {
            String imageUrl = saveImage(menuName, image, "goobne");
            updateMenu(menuName, imageUrl, targetCompany, Category.CHICKEN); // save
        });
        result.putAll(chickenList);
        Map<String, String> pizzaList = goobneCrawling(driver, "피자 시리즈");
        chickenList.forEach((menuName, image) -> {
            String imageUrl = saveImage(menuName, image, "goobne");
            updateMenu(menuName, imageUrl, targetCompany, Category.PIZZA); // save
        });
        result.putAll(pizzaList);
        driver.quit();
        return result;
    }
    private Map<String, String> goobneCrawling(WebDriver driver, String tabName) throws InterruptedException {
        Map<String, String> result = new HashMap<>();
        List<WebElement> menuTabs = driver.findElements(By.tagName("a")); // 모든 a tag를 가져온다
        for (int i=0; i<menuTabs.size(); i++) {
			if (menuTabs.get(i).getText().equals(tabName)) {
				menuTabs.get(i).click();
                System.out.println(">>> 탭 진입 - "+tabName);
                Thread.sleep(1000);
                break;
			}
		}
        List<WebElement> chickenList = driver.findElements(By.xpath("//*[@id=\"menu_list\"]/li"));
        for (WebElement menu : chickenList) {
            String menuName = menu.findElement(By.xpath("p/span")).getText();
            if (!menuName.contains("반반")) { // 반반메뉴 제외
                String image = menu.findElement(By.xpath("img")).getAttribute("src");
                result.put(menuName, image);
            }
        }
        return result;
    }


    public Map<String, String> baskin31() throws InterruptedException {
        Map<String, String> result = new HashMap<>();
        String companyName = "배스킨라빈스";
        companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_baskin.png")
                .build();
            return companyService.save(company);
        });
        List<String> urlList = new ArrayList<>();
        urlList.add("http://www.baskinrobbins.co.kr/menu/list.php?Page=1&top=A&sub="); // 1 page
        urlList.add("http://www.baskinrobbins.co.kr/menu/list.php?Page=2&top=A&sub="); // 2 page
        for (String url : urlList) {
            WebDriver driver = setupDriver(url);
            List<WebElement> menuItem = driver.findElements(By.cssSelector("li[class=item]"));
            for (WebElement menu : menuItem) {
                String menuName = menu.findElement(By.cssSelector("a > figure > figcaption > span")).getText();
                if (!menuName.contains("레디팩")) {
                    String image = menu.findElement(By.cssSelector("a > figure > span > img")).getAttribute("src");
                    result.put(menuName, image);
                    String imageUrl = saveImage(menuName, image, "baskin31");
                    updateMenu(menuName, imageUrl, companyService.findByName(companyName), Category.ICECREAM);
                }
            }
            driver.quit();
        }
        return result;
    }


    public Map<String, String> chickenPlus() throws InterruptedException {
        Map<String, String> result = new HashMap<>();
        String companyName = "치킨플러스";
        Company saved = companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_chickenPlus.jpg")
                .build();
            return companyService.save(company);
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
                String menuName = menu.findElement(By.cssSelector("div.mn1_txt > p.mn1_tit.menu_s_title")).getText();
                menu.findElement(By.className("thumbMenu")).click(); // thumbMenu click
                String image = menu.findElement(By.xpath("//*[@id=\"MenuImage\"]")).getAttribute("src");
                result.put(menuName, image);
                String imageUrl = saveImage(menuName, image, "chickenplus"); // save image
                updateMenu(menuName, imageUrl, saved, category); // update Food
            }
            driver.close();
        }
        return result;
    }


    private void save(Map<String, String> data, Company company, String folderName, Category category) {
        data.forEach((menuName, image) -> {
            String imageUrl = saveImage(menuName, image, folderName);
            updateMenu(menuName, imageUrl, company, category);
        });
    }


}
