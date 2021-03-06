package com.mukcha.controller.api;

import java.util.ArrayList;
import java.util.Arrays;
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

    @GetMapping("/crawling/BBQ")
    public ResponseEntity<Object> crawlingBBQ() {
        Map<String, String> body;
        try {
            body = BBQ();
            return ResponseEntity.ok(body);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/crawling/빅스타피자")
    public ResponseEntity<Object> crawlingBigstar() {
        Map<String, String> body;
        body = bigstarPizza();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/crawling/푸라닭")
    public ResponseEntity<Object> crawlingPuradak() {
        Map<String, String> body;
        body = puradak();
        return ResponseEntity.ok(body);
    }


    public Map<String, String> puradak() {
        Map<String, String> result = new HashMap<>();
        String companyName = "푸라닭";
        Company company = companyRepository.findByName(companyName).orElseGet(() -> {
            Company comp = Company.builder()
                .name(companyName)
                .image("https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_puradak.png")
                .build();
            return companyService.save(comp);
        });
        List<String> chickenList = new ArrayList<>();
        chickenList.add("https://puradakchicken.com/menu/product.asp?page=1&sermode=0&sermode2=0&serdiv="); // 치킨메뉴 뼈 1페이지
        chickenList.add("https://puradakchicken.com/menu/product.asp?page=2&sermode=0&sermode2=0&serdiv="); // 치킨메뉴 뼈 2페이지
        chickenList.add("https://puradakchicken.com/menu/product.asp?sermode=2"); // 푸레스트 (닭가슴살 메뉴)
        result.putAll(puradakCrawling(chickenList, company, Category.CHICKEN));
        List<String> sideList = new ArrayList<>();
        sideList.add("https://puradakchicken.com/menu/product.asp?page=1&sermode=1&sermode2=&serdiv="); // 사이드메뉴 1페이지
        sideList.add("https://puradakchicken.com/menu/product.asp?page=2&sermode=1&sermode2=&serdiv="); // 사이드메뉴 2페이지
        sideList.add("https://puradakchicken.com/menu/product.asp?page=3&sermode=1&sermode2=&serdiv="); // 사이드메뉴 3페이지
        sideList.add("https://puradakchicken.com/menu/product.asp?sermode=3"); // 베이커리
        result.putAll(puradakCrawling(sideList, company, Category.SIDEMENU));
        List<String> pizzaList = new ArrayList<>();
        pizzaList.add("https://puradakchicken.com/menu/product.asp?sermode=4"); // 피자
        result.putAll(puradakCrawling(pizzaList, company, Category.PIZZA));
        return result;
    }
    private Map<String, String> puradakCrawling(List<String> urlList, Company company,Category category) {
        List<String> menuUrlList = new ArrayList<>();
        Map<String, String> data = new HashMap<>();
        for (String url : urlList) {
            WebDriver driver = setupDriver(url);
            List<WebElement> lists = driver.findElements(By.cssSelector("#contents > div.inConts > div.photo_list.allmenu > ul > li"));
            for (WebElement menu : lists) {
                String menuPage = menu.findElement(By.cssSelector("a")).getAttribute("href");
                menuUrlList.add(menuPage);
            }
            driver.quit();
        }
        List<String> menuNameFilter = Arrays.asList("배달", "보틀", "2개", "3개");
        for (String url : menuUrlList) {
            WebDriver driver = setupDriver(url);
            String mName = driver.findElement(By.cssSelector("#contents > div.photo_detail.white > div > div > div.pro_name > span > p.title")).getText();
            if (!menuNameFilter.stream().anyMatch(filter -> mName.contains(filter))) {
                String menuName = mName;
                if (menuName.contains("(1개)")) {
                    menuName = menuName.replace("(1개)", "");
                }
                String image = driver.findElement(By.xpath("//*[@id=\"contents\"]/div[1]/div/div/div[2]/img")).getAttribute("src");
                String imageUrl = saveImage(menuName, image, "puradak");
                updateMenu(menuName, imageUrl, company, category);
                data.put(menuName, image);
            }
            driver.quit();
        }
        return data;
    }



    public Map<String, String> bigstarPizza() {
        Map<String, String> result = new HashMap<>();
        String companyName = "빅스타피자";
        Company saved = companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("/logo/logo-bigstar_pizza.png")
                .build();
            return companyService.save(company);
        });
        String originUrl = "http://www.bigstarpizza.co.kr/page/menu";
        List<String> urlList = new ArrayList<>();
        WebDriver driver = setupDriver(originUrl);
        List<WebElement> categoryList = driver.findElements(By.className("menu_list"));
        for (WebElement cate : categoryList) {
            List<WebElement> menuList = cate.findElements(By.cssSelector("div"));
            for (WebElement menu : menuList) {
                String nUrl = menu.getAttribute("onclick");
                if (nUrl != null) {
                    nUrl = nUrl.replace("location.href=", "");
                    nUrl = nUrl.replace("\'", "");
                    nUrl = "http://www.bigstarpizza.co.kr" + nUrl;
                    urlList.add(nUrl);
                }
            }
        }
        driver.quit();
        List<String> classifySide = Arrays.asList("후라이", "새우링", "핫바");
        List<String> classifyChicken = Arrays.asList("버팔로", "텐더", "치킨");
        List<String> classifyPasta = Arrays.asList("스파게티", "파스타");
        for (String url : urlList) {
            WebDriver nDriver = setupDriver(url);
            String menuName = nDriver.findElement(By.xpath("//*[@id=\"container\"]/article/div/div[1]/div[1]/div/span[2]")).getText();
            if (!menuName.contains("반반")) {
                String image = nDriver.findElement(By.xpath("//*[@id=\"container\"]/article/div/div[1]/div[3]/div[1]/img")).getAttribute("src");
                result.put(menuName, image);
                String imageUrl = saveImage(menuName, image, "bigstarPizza");
                if (menuName.contains("떡볶이")) {
                    updateMenu(menuName, imageUrl, saved, Category.TTEOKBOKKI);
                } else if (classifyChicken.stream().anyMatch(m -> menuName.contains(m))) {
                    updateMenu(menuName, imageUrl, saved, Category.CHICKEN);
                } else if (classifyPasta.stream().anyMatch(m -> menuName.contains(m))) {
                    updateMenu(menuName, imageUrl, saved, Category.PASTA);
                } else if (classifySide.stream().anyMatch(m -> menuName.contains(m))) {
                    updateMenu(menuName, imageUrl, saved, Category.SIDEMENU);
                } else {
                    updateMenu(menuName, imageUrl, saved, Category.PIZZA);
                }
            }
            nDriver.quit();
        }
        return result;
    }


    public Map<String, String> BBQ() throws InterruptedException {
        Map<String, String> result = new HashMap<>();
        String companyName = "BBQ";
        Company saved = companyRepository.findByName(companyName).orElseGet(() -> {
            Company company = Company.builder()
                .name(companyName)
                .image("")
                .build();
            return companyService.save(company);
        });
        String originUrl = "https://www.bbq.co.kr/menu/menuList.asp";
        WebDriver driver = setupDriver(originUrl);
        String[] filterTag = {"세트", "음료", "반반"};
        List<String> urlList = new ArrayList<>();
        // find tags
        List<WebElement> tags = driver.findElements(By.xpath("/html/body/div[5]/div[2]/article/section/div[2]/ul/li"));
        for (WebElement tag : tags) {
            String tagName = tag.findElement(By.cssSelector("a")).getText();
            if (!List.of(filterTag).stream().anyMatch(t -> tagName.contains(t))) {
                String newUrl = tag.findElement(By.cssSelector("a")).getAttribute("href");
                urlList.add(newUrl);
            }
        }
        driver.quit();
        String[] filterMenu = {"반반", "레귤러", "소스", "시즈닝", "10개", "반마리"};
        for (String url : urlList) {
            WebDriver nDriver = setupDriver(url);
            List<WebElement> boxes = nDriver.findElements(By.xpath("/html/body/div[5]/div[2]/article/section/div[2]/div/div"));
            for (WebElement box : boxes) {
                String menuName = box.findElement(By.xpath("div[2]/p[1]")).getText();
                if (!List.of(filterMenu).stream().anyMatch(n -> menuName.contains(n))) {
                    String name = menuName;
                    if (name.contains("(라지)")) {
                        name = name.replace(" (라지)", "");
                    } else if (name.contains("\n")) {
                        name = name.replace("\n", " ");
                    }
                    String image = box.findElement(By.xpath("div[1]/a/img")).getAttribute("src");
                    result.put(name, image);
                }
            }
            nDriver.quit();
        }
        String[] classifySide = {"볼", "칩스", "스틱", "멘보샤", "튀김", "껍데기", "소떡", "삼계탕"};
        result.forEach((menuName, image) -> {
            String imageUrl = saveImage(menuName, image, "bbq");
            if (menuName.contains("버거")) {
                updateMenu(menuName, imageUrl, saved, Category.HAMBURGER);
            } else if (menuName.contains("피자")) {
                updateMenu(menuName, imageUrl, saved, Category.PIZZA);
            } else if (List.of(classifySide).stream().anyMatch(n -> menuName.contains(n))) {
                updateMenu(menuName, imageUrl, saved, Category.SIDEMENU);
            } else {
                updateMenu(menuName, imageUrl, saved, Category.CHICKEN);
            }
        });
        return result;
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
                    menuName = menuName.replace(" 5조각", "");
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

}
