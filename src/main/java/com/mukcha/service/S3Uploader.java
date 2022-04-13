package com.mukcha.service;

import java.io.File;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;


    // S3로 업로드하기
    public String upload(File uploadFile, String directory) {
        // S3에 저장될 파일 이름
        String fileName = directory + "/" + uploadFile.getName();
        // S3 upload
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeLocalFile(uploadFile);
        return uploadImageUrl;
    }


    // local에 저장되어있던 이미지 삭제
    private void removeLocalFile(File targetFile) {
        if (targetFile.delete()) {
            log.info(">>> 로컬 파일이 성공적으로 삭제되었습니다.");
            return ;
        }
        log.info(">>> 로컬 파일 삭제가 실패하였습니다.");
    }


    // S3에 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
            new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3Client.getUrl(bucket, fileName).toString(); // return url
    }


    // MultipartFile 을 변환
    // public String upload(MultipartFile multipartFile, String directory) throws IOException {
    //     File uploadFile = convert(multipartFile).orElseThrow(() ->
    //         // 파일 변환할 수 없으면 에러
    //         new IllegalArgumentException(">>> ERROR: MultipartFile -> File convert fail.")
    //     );
    //     return upload(uploadFile, directory);
    // }


    // local에 파일 업로드
    // private Optional<File> convert(MultipartFile multipartFile) throws IOException {
    //     File convertFile = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());
    //     // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
    //     if (convertFile.createNewFile()) {
    //         // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
    //         try (FileOutputStream fos = new FileOutputStream(convertFile)) {
    //             fos.write(multipartFile.getBytes());
    //         }
    //         return Optional.of(convertFile);
    //     }
    //     return Optional.empty();
    // }


}
