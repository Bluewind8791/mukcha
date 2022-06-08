package com.mukcha.controller.api.helper;

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

    protected WebDriver setupDriver(String url) throws InterruptedException {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless"); // 브라우저 보이지 않기
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
        String fileName = "/image/"+companyName+"/"+getEncodedFilename(menuName)+".png";
        File file = new File(fileName);
        try {
            ImageIO.write(saveImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(">>> 이미지가 S3로 업로드 되었습니다. "+fileName);
        return s3Uploader.upload(file, "image/"+companyName);
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


}
