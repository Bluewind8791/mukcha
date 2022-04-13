package com.mukcha.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.imageio.ImageIO;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;
import com.mukcha.service.S3Uploader;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootTest
@ActiveProfiles("set1")
public class SeleniumCrawling {

    @Autowired private S3Uploader s3Uploader;
    @Autowired private CompanyService companyService;
    @Autowired private FoodService foodService;

    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; // 드라이버 ID
    public static final String WEB_DRIVER_PATH = "D:\\ChromeDriver\\chromedriver.exe"; // 드라이버 경로 D:\ChromeDriver
    Company company;


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


    /** >>> METHOD <<< */

    private String getEncodedFilename(String displayFileName) {
        String encodedFilename = null;
        try {
            encodedFilename = URLEncoder.encode(displayFileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedFilename;
    }

    private Company isCompanyPresent(String companyName, String companyLogo) {
        Company company = companyService.findByName(companyName).orElse(
            createCompany(companyName, companyLogo)
        );
        return company;
    }

    private Company createCompany(String companyName, String companyLogo) {
        Company company = Company.builder()
                            .image(companyLogo)
                            .name(companyName)
                            .build()
        ;
        if (company.getImage() == "") {
            System.out.println(">>> 회사 <"+company.getName()+">의 로고 이미지를 설정해주세요.");
        }
        log.info(">>> 회사가 생성되었습니다: "+company.getName());
        return companyService.save(company);
    }

    private void saveFood(String foodName, String imageUrl, Company company, Category category) {
        Food food = Food.builder()
                .name(foodName)
                .image(imageUrl)
                .company(company)
                .category(category)
                .build()
        ;
        foodService.save(food);
        log.info(">>> 메뉴<+"+food.getName()+"> 이 DB에 생성되었습니다." + food.toString());
    }

    private Boolean isFoodPresent(String foodName) {
        if (foodService.findByName(foodName).isPresent()) {
            return true;
        } else {
            return false;
        }
    }


}
