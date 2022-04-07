# 먹챠 [![Build Status](https://app.travis-ci.com/Bluewind8791/mukcha.svg?branch=main)](https://app.travis-ci.com/Bluewind8791/mukcha)

## Live 사이트 [바로가기](http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com/)

"이 집은 이건 맛있는데 저건 맛이 별로야... 그런데 왜 메뉴별로 평가할 수 없는거지?"

이제 좀 더 자세하게 평가하고 기록하고 싶다! 메뉴별로 평가하여 기록하는 서비스 **<먹챠>**

## 설명

- 해당 프로젝트는 Spring Boot, JPA, Bootstrap5 및 Thymeleaf 등의 기술을 메인으로 사용하여 만든 웹 페이지 프로젝트입니다.
- 수많은 프렌차이즈의 각 프렌차이즈의 메뉴들을 DB에 추가하여, 사용자는 그 메뉴들을 각각 평가하고 코멘트 등의 방법으로 기록할 수 있습니다.

## 서비스 구현 목표

- Spring Boot 와 JPA 기술을 중점적으로 사용하여 back-end 위주의 웹 페이지 개발.
- front-end 부분은 BootStrap 5 및 thymeleaf 기술을 사용하여 간단하게 개발.
- ~~회원 기능 : 로그인, 회원 가입 및 회원 정보 수정 및 회원 탈퇴 기능.~~
  - 회원 로그인 및 회원가입 기능은 OAuth2에 모두 일임.
  - Google, Naver 를 통한 회원 가입 및 로그인 기능.
- Spring Security 기술을 통한 보안 및 권한별 접근 페이지 차별화.
- JPA Specification 기술을 사용한 검색 기능.
- 회원에 한하여 메뉴에 대하여 평가(5점 만점), 코멘트, 먹은 날짜 기록 기능.
- AWS EC2를 사용한 서버 구축.
- AWS RDS 서버 DB 구축 (MySQL)
- Travis CI 와 CodeDeploy를 이용한 자동 배포 시스템.
- Nginx를 이용한 무중단 배포.

### *(구현예정)*

- 메뉴 정보를 크롤링으로 긁어와서 DB에 저장할 수 있는 기능 (구현중)
- 다른 회원이 적은 코멘트에 좋아요 기능 추가.
  - 메뉴 상세보기 페이지에서 좋아요 가장 많이 받은순의 코멘트로 정렬


## 사용 기술

- Spring Boot
- Spring Security
- JPA
- Bootstrap 5
- Thymeleaf
- MySQL
- Travis CI
- CodeDeploy
- Nginx