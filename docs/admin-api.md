# Admin API

`ROLE_ADMIN`의 권한을 가진 사용자만이 접근 가능한 Admin 페이지 API 입니다.


---
## 관리자 루트 페이지

| Method | URI
|--|--|
| `GET` | /admin

### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Header**
| Authorization | String | "Bearer  "+{token} | required
**> Responses**
| **Status** | | **Description** |
200 | | OK

- Sample Response:
```json
{
  "_links": {
    "rel": "self",
    "href": "http://localhost:8080/admin/"
  },
  "loginUser": {
    "userId": 1,
    "userEmail": "admin@test.com",
    "userName": "admin"
  },
  "companyList": [
    {
      "companyId": 1,
      "companyName": "빅스타피자",
      "companyLogo": "/logo/logo-bigstar_pizza.png",
      "updatedAt": "2022-05-29T21:54:54.98298",
      "createdAt": "2022-05-29T21:54:54.98298"
    },
    ...
  ],
  "foodList": [
    {
      "foodId": 160,
      "foodName": "츄러스",
      "foodImage": "http://sinjeon.co.kr/img/sub/menu01/menu44.png",
      "category": "SIDEMENU",
      "companyId": 5,
      "companyName": "신전떡볶이",
      "averageScore": 0.0
    },
    ...
  ],
  "categoryList": [
    "CHICKEN",
    "PIZZA",
    ...
  ]
}
```

---
## 해당 회사 정보 관리자 페이지

| Method | URI
|--|--|
| `GET` | /admin/companies/{companyId}

### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Path**
| companyId | Long | 해당 회사 ID | required
**> Header**
| Authorization | String | "Bearer  "+{token} | required
**> Responses**
| **Status** | | **Description** |
201 | | Created


- Sample Response:
```json
{
  "_links": {
    "rel": "self",
    "href": "http://localhost:8080/admin/companies/1"
  },
  "loginUser": {
    "userId": 1,
    "userEmail": "admin@test.com",
    "userName": "admin"
  },
  "foodList": [
    {
      "foodId": 160,
      "foodName": "츄러스",
      "foodImage": "http://sinjeon.co.kr/img/sub/menu01/menu44.png",
      "category": "SIDEMENU",
      "companyId": 5,
      "companyName": "신전떡볶이",
      "averageScore": 0.0
    },
    ...
  ],
  "categoryList": [
    "CHICKEN",
    "PIZZA",
    ...
  ],
  "company": {
    "companyId": 1,
    "companyName": "빅스타피자",
    "companyLogo": "/logo/logo-bigstar_pizza.png",
    "updatedAt": "2022-05-29T23:29:59.104895",
    "createdAt": "2022-05-29T23:29:59.104895"
  }
}
```


---
## 모든 메뉴 보기 관리자 페이지

| Method | URI
|--|--|
| `GET` | /admin/menus

### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Header**
| Authorization | String | "Bearer  "+{token} | required
**> Responses**
| **Status** | | **Description** |
201 | | Created


- Sample Response:
```json
{
  "_links": {
    "rel": "self",
    "href": "http://localhost:8080/admin/menus"
  },
  "loginUser": {
    "userId": 1,
    "userEmail": "admin@test.com",
    "userName": "admin"
  },
  "companyList": [
    {
      "companyId": 1,
      "companyName": "빅스타피자",
      "companyLogo": "/logo/logo-bigstar_pizza.png",
      "updatedAt": "2022-05-29T23:29:59.104895",
      "createdAt": "2022-05-29T23:29:59.104895"
    },
  ...
  "foodList": [
    {
      "foodId": 160,
      "foodName": "츄러스",
      "foodImage": "http://sinjeon.co.kr/img/sub/menu01/menu44.png",
      "category": "SIDEMENU",
      "companyId": 5,
      "companyName": "신전떡볶이",
      "averageScore": 0.0
    },
  ...
  ],
  "categoryList": [
    "CHICKEN",
    "PIZZA",
    ...
  ]
}
```


---
## 새로운 메뉴 테이블 추가

| Method | URI
|--|--|
| `POST` | /api/admin/menus

### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Header**
| Authorization | String | "Bearer  "+{token} | required
**> Body**
foodName | String | 메뉴명 | required
foodImage | String | 메뉴 이미지 이미지 URL | optional
category | String | 메뉴 카테고리 | required
companyName | String | 해당 메뉴의 회사명 | required
**> Responses**
| **Status** | | **Description** |
201 | | Created


---
## 해당 메뉴 정보 수정

| Method | URI
|--|--|
| `PUT` | /api/admin/menus/{foodId}

### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Path**
foodId | Long | 해당 메뉴 ID | required
**> Header**
| Authorization | String | "Bearer "+{token} | required
**> Body**
foodName | String | 메뉴명 | optional
foodImage | String | 메뉴 이미지 이미지 URL | optional
category | String | 메뉴 카테고리 | optional
companyName | String | 해당 메뉴의 회사명 | required
**> Responses**
| **Status** | | **Description** |
200 | | OK



---
## 해당 메뉴 테이블 삭제

| Method | URI
|--|--|
| `DELETE` | /api/admin/menus/{foodId}


### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Path**
foodId | Long | 해당 메뉴 ID | required
**> Header**
| Authorization | String | "Bearer  "+{token} | required
**> Responses**
| **Status** | | **Description** |
200 | | OK
400 | | Bad Request (삭제 실패 시)
