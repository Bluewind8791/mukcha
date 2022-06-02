# Admin API

`ROLE_ADMIN`의 권한을 가진 사용자만이 접근 가능한 Admin 페이지 API 입니다.


---
## 관리자 루트 페이지

![admin](https://user-images.githubusercontent.com/85560758/171651058-4c131dc6-c96e-4024-a4c6-df45b172aa69.png)

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


![admin_companies_1](https://user-images.githubusercontent.com/85560758/171652954-1a5aaf6d-05cd-4388-a53f-bbe55da8e392.png)


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


![admin_menus](https://user-images.githubusercontent.com/85560758/171653540-65cf7ba6-c8cf-42ed-8b72-ee7918dddbe9.png)

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