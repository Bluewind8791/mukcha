---
coverY: 0
---

# MUKCHA

라이브 웹사이트 [바로가기](http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com/)

"이 집은 이건 맛있는데 저건 맛이 별로야... 그런데 왜 메뉴별로 평가할 수 없는거지?"\
이제 좀 더 자세하게 평가하고 기록하고 싶다! 메뉴별로 평가하여 기록하는 서비스 **<먹챠>**

***

## 서비스 설명

* 국내의 각 프랜차이즈의 메뉴들을 DB에 추가하여 사용자는 그 메뉴들을 각각 평가하고 코멘트 등의 방법으로 기록할 수 있습니다.
* 검색 기능을 통하여 프랜차이즈 이름, 메뉴 이름 및 사용자 이름을 간편하게 검색할 수 있습니다.
* 비회원은 기록할 수 없으며 보는것만 가능합니다.
* 회원가입한 회원은 메뉴에 대하여 평가(5점 만점), 코멘트, 먹은 날짜 등을 기록할 수 있습니다.

***

## 개발 설명

### 프론트엔드

* **BootStrap 5** 및 **Thymeleaf** 기술을 사용하여 간단하게 개발하였습니다.

### 백엔드

* **Spring Framework** 위에서 **Spring Boot** 및 **Spring Data JPA**를 사용하여 개발하였습니다.
* 회원 로그인 및 회원 가입 기능은 **OAuth2**를 통하여 Google, Naver 아이디를 통한 간단 회원가입 및 로그인을 구현하였습니다.
* 간편한 메뉴 데이터베이스 관리를 위하여 관리자 ROLE을 생성하고, **Spring Security** 기술을 통하여 보안 및 권한별 접근 페이지 차별화를 시도하였습니다.
* **JPA Specification** 기술을 활용한 검색 기능을 구현하였습니다.
* 프랜차이즈 메뉴 데이터베이스 구축을 최대한 자동화 시키기 위하여 **JSoup 및 Selenium 을 통한 HTML Parsing**(크롤링)을 사용하였습니다.

### SERVER

* **AWS EC2**를 사용하여 서버를 구축하고 24시간 라이브 서비스를 제공하고 있습니다.
* DBMS는 **MYSQL**을 사용하였으며, 현재는 **AWS RDS**를 사용하여 AWS EC2 서버와 연결하여 서비스하고 있습니다.
* **AWS S3**, ~~Travis~~와 **AWS CodeDeploy**를 사용하여 Github에 수정된 코드가 push되면 자동 배포되도록 구축하였습니다.
  * Travis의 무료 기간이 끝난 관계로 **Github Actions**로 변경하였습니다.
* **Nginx**를 사용하여 배포중에 서비스가 끊기지 않도록 무중단 배포를 구현하였습니다.

***

## 사용 기술

![spring](https://img.shields.io/badge/-Spring-6DB33F?style=for-the-badge\&logo=spring\&logoColor=white) ![spring-boot](https://github.com/img.shields.io/badge/-Spring%20Boot-6DB33F?style=for-the-badge\&logo=springboot\&logoColor=white) ![spring security](https://github.com/img.shields.io/badge/-Spring%20Security-6DB33F?style=for-the-badge\&logo=springsecurity\&logoColor=white) ![spring data jpa](https://github.com/img.shields.io/badge/-Spring%20Data%20JPA-6DB33F?style=for-the-badge)\
![aws](https://img.shields.io/badge/-AWS-232F3E?style=for-the-badge\&logo=amazonaws\&logoColor=white) ![GitHub Actions](https://github.com/img.shields.io/badge/-GitHub%20Actions-2088FF?style=for-the-badge\&logo=GitHubActions\&logoColor=white) ![NGINX](https://img.shields.io/badge/-NGINX-009639?style=for-the-badge\&logo=NGINX\&logoColor=white) ![MySQL](https://img.shields.io/badge/-MySQL-4479A1?style=for-the-badge\&logo=MySQL\&logoColor=white) ![Linux](https://img.shields.io/badge/-Linux-FCC624?style=for-the-badge\&logo=Linux\&logoColor=white)\
![html](https://img.shields.io/badge/-HTML5-E34F26?style=for-the-badge\&logo=html5\&logoColor=white) ![Thymeleaf](https://img.shields.io/badge/-Thymeleaf-005F0F?style=for-the-badge\&logo=Thymeleaf\&logoColor=white) ![Bootstrap5](https://img.shields.io/badge/-Bootstrap5-7952B3?style=for-the-badge\&logo=Bootstrap\&logoColor=white)

***

## 느낀 점

* 개인 프로젝트의 경우에는 회원 관리를 OAuth2을 적극 활용하여 다른 서비스 개발에 집중하는 것이 좋을것 같습니다.
* 주요 기능은 테스트 코드를 반드시 작성하는 것이 시간을 아끼는 길인거 같습니다.
* 인터넷 강의를 따라 만드는 것이 아닌, 제가 사용하고 싶은 서비스를 직접 부딪히며 만들어 보니 실제로 해당 기술들을 어떻게 사용하는지, 왜 필요한지를 알 수 있었습니다.
* 또한 백엔드 개발자는 백엔드만 할 줄 알면 된다고 생각했었는데, 이 프로젝트를 통하여 전반적인 넓은 이해가 필요하다는 것을 알게 되었습니다.
