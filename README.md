# MUKCHA [![Build Status](https://app.travis-ci.com/Bluewind8791/mukcha.svg?branch=main)](https://app.travis-ci.com/Bluewind8791/mukcha)

### [라이브 웹사이트 바로가기](http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com/)


"이 집은 이건 맛있는데 저건 맛이 별로야... 그런데 왜 메뉴별로 평가할 수 없는거지?"

이제 좀 더 자세하게 평가하고 기록하고 싶다! 메뉴별로 평가하여 기록하는 서비스 **<먹챠>**


## 설명


- IDE Tool은 Visual Studio Code을 사용하였습니다.
- Spring Framework 위에서 Spring Boot 및 JPA를 사용하여 REST API를 개발하였습니다.
- DBMS는 MYSQL을 사용하였으며, 현재는 AWS RDS를 사용하여 AWS EC2 서버와 연결하여 서비스하고 있습니다.
- AWS EC2를 사용하여 서버를 구축하고 배포하고 있습니다.
- Travis CI와 AWS CodeDeploy를 사용하여 Git에 수정된 코드가 push되면 자동 배포되도록 구축하였습니다.
- Nginx를 사용하여 배포중에 서비스가 끊기지 않도록 무중단 배포를 구현하였습니다.
- 회원 로그인 및 회원 가입 기능은 OAuth2를 통하여 Google, Naver 아이디를 통한 간단 회원가입 및 로그인을 구현하였습니다.
- JPA Specification 기술을 활용한 검색 기능을 구현하였습니다.
- 간편한 메뉴 데이터베이스 관리를 위하여 관리자 ROLE을 생성하고 Spring Security 기술을 통하여 보안 및 권한별 접근 페이지 차별화를 시도하였습니다.
- 프랜차이즈 메뉴 데이터베이스 구축을 최대한 자동화 시키기 위하여 JSoup 및 Selenium 을 통한 HTML Parsing(크롤링)을 사용하였습니다.
- Front-End 부분은 BootStrap 5 및 Thymeleaf 기술을 사용하여 간단하게 개발하였습니다.


## 서비스 구현

- 많은 프렌차이즈의 각 프렌차이즈의 메뉴들을 DB에 추가하여, 사용자는 그 메뉴들을 각각 평가하고 코멘트 등의 방법으로 기록할 수 있습니다.
- 회원에 한하여 메뉴에 대하여 평가(5점 만점), 코멘트, 먹은 날짜 기록 기능.


## 사용 기술

- Spring Framework
- Spring Boot
- Spring Security
- JPA
- Bootstrap 5
- Thymeleaf
- MySQL
- Travis CI
- AWS CodeDeploy
- Nginx