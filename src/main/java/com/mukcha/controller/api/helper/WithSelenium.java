package com.mukcha.controller.api.helper;


import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;

import com.mukcha.service.CompanyService;


public class WithSelenium extends CrawlingHelper {

    @Autowired protected CompanyService companyService;

    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; // 드라이버 ID
    public static final String WEB_DRIVER_PATH = "D:\\ChromeDriver\\chromedriver.exe"; // 드라이버 경로 D:\ChromeDriver

    protected WebDriver setupDriver(String url) {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless"); // 브라우저 보이지 않기
        WebDriver driver = new ChromeDriver(options);
        driver.get(url); // WebDriver을 해당 url로 이동한다.
        //브라우저 이동시 생기는 로드시간을 기다린다.
		//HTTP 응답속도보다 자바의 컴파일 속도가 더 빠르기 때문에 임의적으로 대기한다.
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        return driver;
    }

}
