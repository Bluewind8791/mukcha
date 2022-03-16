# 먹챠

"이 집은 이건 맛있는데 저건 맛이 별로야... 그런데 왜 메뉴별로 평가할 수 없는거지?"

이제 좀 더 자세하게 평가하고 기록하고 싶다! 메뉴별로 평가하여 기록하는 서비스 **<먹챠>**

## 설명

- 해당 프로젝트는 Spring Boot, JPA, Bootstrap5 및 Thymeleaf 를 사용하여 만든 웹페이지입니다.
- 수많은 프렌차이즈의 각 프렌차이즈의 메뉴들을 DB에 추가하여, 사용자는 그 메뉴들을 각각 평가하고 코멘트 등의 방법으로 기록할 수 있습니다.

## 서비스 구현 목표

- Spring Boot 와 JPA 기술을 중점적으로 사용하여 back-end 위주의 웹 페이지 개발.
- front-end 부분은 BootStrap 5 및 thymeleaf 기술을 사용하여 간단하게 개발.
- 회원 기능 : 로그인, 회원 가입 및 회원 정보 수정 및 회원 탈퇴 기능.
- Spring Security 기술을 통한 보안 및 권한별 접근 페이지 차별화.
- JPA Specification 기술을 사용한 검색 기능.
- 회원에 한하여 메뉴에 대하여 평가(5점 만점), 코멘트, 먹은 날짜 기록 기능.

### *(구현예정)*
- OAuth 를 통한 회원가입 및 로그인 기능. (Kakao 계정)
- 다른 회원이 적은 코멘트에 좋아요 기능.
  - 메뉴 상세보기 페이지에서 좋아요 가장 많이 받은순의 코멘트로 정렬
- 메뉴 정보를 크롤링으로 긁어와서 DB에 저장할 수 있는 기능
  - 메뉴 이름
  - 메뉴 사진

## 사용 기술
- Spring Boot
- Spring Security
- JPA
- Bootstrap 5
- Thymeleaf
- MySQL


## 설치

1. application.yml 에서 MySQL 개인의 username과 password로 변경
```yml
datasource:
  username: root
  password: 1234
```

2. run java
- src.main.java.com.mukcha 경로의 `BackendApplication` class에서 실행.
- 혹은 Dashboard 에서 mukcha 서버 실행

3. 브라우저 "http://localhost:8000/" 으로 웹페이지 접근.