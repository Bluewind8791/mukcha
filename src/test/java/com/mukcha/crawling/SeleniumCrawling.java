package com.mukcha.crawling;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("set1")
// @ActiveProfiles("test")
public class SeleniumCrawling extends WithSelenium {

    Company company;

    /* >>> 치킨플러스 <<< */
    @Test
    void chickenPlus() throws InterruptedException {
        company = companyService.findByName("치킨플러스");
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
            WebDriver driver = before(url);
            List<WebElement> menuItem = driver.findElements(By.cssSelector("div[groupname=MenuItem]"));
            for (WebElement menu : menuItem) {
                // menu name
                String menuName = menu.findElement(By.cssSelector("div.mn1_txt > p.mn1_tit.menu_s_title")).getText();
                System.out.println(">>>"+menuName);
                // image url
                menu.findElement(By.className("thumbMenu")).click(); // thumbMenu click
                String image = menu.findElement(By.xpath("//*[@id=\"MenuImage\"]")).getAttribute("src");
                System.out.println(">>>"+image);
                // save image
                String imageUrl = saveImage(menuName, image, "chickenplus");
                // update Food
                updateMenu(menuName, imageUrl, company, category);
            }
            driver.close();
        }
    }




    /* >>> 굽네치킨 <<< */
    @Test
    void goobne() throws InterruptedException {
        company = isCompanyPresent("굽네치킨", "https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_goobne.jpg");
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
            // 반반메뉴 제외 & DB에 같은 이름이 없다면 진행
            if (!menuName.contains("반반") && !isFoodPresent(menuName)) {
                System.out.println("<<< menu name: " + menuName);
                String image = menu.findElement(By.xpath("img")).getAttribute("src");
                System.out.println("<<< image: " + image);
                String imageUrl = saveImage(menuName, image, "goobne");
                // saveFood
                saveFood(menuName, imageUrl, company, category);
            }
        }
    }



    /** >>> 버거킹 <<< */
    @Test
    void burgerking() throws InterruptedException {
        company = isCompanyPresent("버거킹", "https://mukcha-bucket.s3.ap-northeast-2.amazonaws.com/logo/logo_burgerking.png");
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
                // db에 같은 이름이 없다면 진행
                if (!isFoodPresent(menuName)) {
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
                    // saveFood
                    saveFood(menuName, imageUrl, company, category);
                }
            }
        }
    }


    @Test
    void uploadTest() {
        String fileName = "/image/burgerking_"+getEncodedFilename("기네스와퍼")+".png";
        File file = new File(fileName);
        String imageUrl = s3Uploader.upload(file, "image");
        System.out.println(imageUrl);
    }

}