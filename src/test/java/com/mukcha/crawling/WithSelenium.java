package com.mukcha.crawling;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.repository.FoodRepository;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;
import com.mukcha.service.S3Uploader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WithSelenium {

    @Autowired protected S3Uploader s3Uploader;
    @Autowired protected CompanyService companyService;
    @Autowired protected FoodService foodService;
    @Autowired protected FoodRepository foodRepository;

    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; // 드라이버 ID
    public static final String WEB_DRIVER_PATH = "D:\\ChromeDriver\\chromedriver.exe"; // 드라이버 경로 D:\ChromeDriver

    protected WebDriver before(String url) throws InterruptedException {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("headless"); // 브라우저 보이지 않기
        WebDriver driver = new ChromeDriver(options);
        driver.get(url); // WebDriver을 해당 url로 이동한다.
        //브라우저 이동시 생기는 로드시간을 기다린다.
		//HTTP 응답속도보다 자바의 컴파일 속도가 더 빠르기 때문에 임의적으로 대기한다.
		Thread.sleep(2000);
        return driver;
    }

    /** >>> METHOD <<< */
    protected String saveImage(String menuName, String originImageUrl, String companyName) {
        BufferedImage saveImage = null;
        try {
            saveImage = ImageIO.read(new URL(originImageUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileName = "/image/"+companyName+"_"+getEncodedFilename(menuName)+".png";
        File file = new File(fileName);
        try {
            ImageIO.write(saveImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s3Uploader.upload(file, "image");
    }

    protected String getEncodedFilename(String displayFileName) {
        String encodedFilename = null;
        try {
            encodedFilename = URLEncoder.encode(displayFileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedFilename;
    }

    protected Company isCompanyPresent(String companyName, String companyLogo) {
        return companyService.findByNameOr(companyName).orElse(
            createCompany(companyName, companyLogo)
        );
    }

    protected Company createCompany(String companyName, String companyLogo) {
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

    protected void saveFood(String foodName, String imageUrl, Company company, Category category) {
        Food food = Food.builder()
                .name(foodName)
                .image(imageUrl)
                .company(company)
                .category(category)
                .build()
        ;
        foodRepository.save(food);
        log.info(">>> 메뉴<"+food.getName()+"> 이 DB에 생성되었습니다." + food.toString());
    }

    protected void updateMenu(String menuName, String imageUrl, Company company, Category category) {
        if (foodService.isPresentByCompanyAndFoodName(company, menuName)) {
            // 있는 메뉴라면 해당 메뉴 정보 업데이트
            Food food = foodService.findByNameAndCompany(company, menuName);
            food.update(menuName, imageUrl, category);
            foodRepository.save(food);
            log.info(">>> 메뉴 <"+food.getName()+">가 업데이트 되었습니다. "+food.toString());
        } else { // 존재하지 않는 메뉴라면 새로 생성
            Food food = Food.builder()
                    .name(menuName)
                    .image(imageUrl)
                    .company(company)
                    .category(category)
                    .build()
            ;
            foodRepository.save(food);
            log.info(">>> 메뉴 <"+food.getName()+">이 생성되었습니다. "+food.toString());
        }
    }

    protected Boolean isFoodPresent(String foodName) {
        if (foodService.findByNameOr(foodName).isPresent()) {
            return true;
        } else {
            return false;
        }
    }

}
