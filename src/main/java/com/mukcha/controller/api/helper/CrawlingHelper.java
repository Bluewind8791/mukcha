package com.mukcha.controller.api.helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;

import com.mukcha.service.S3Uploader;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.service.FoodService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrawlingHelper {

    @Autowired protected S3Uploader s3Uploader;
    @Autowired protected FoodService foodService;
    @Autowired protected FoodRepository foodRepository;
    @Autowired protected CompanyRepository companyRepository;


    protected void save(Map<String, String> data, Company company, String folderName, Category category) {
        data.forEach((menuName, image) -> {
            String imageUrl = saveImage(menuName, image, folderName);
            updateMenu(menuName, imageUrl, company, category);
        });
    }

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
            log.info(">>> Image Write에 실패하였습니다.");
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
