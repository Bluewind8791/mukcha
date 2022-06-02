# Menu API

## 해당 메뉴의 정보와 리뷰 최대 3개를 보여주는 페이지

| Method | URI
|--|--|
| `GET` | `/menus/{foodId}`


![menus_706](https://user-images.githubusercontent.com/85560758/171654893-22b29507-4274-474d-b537-16a2fef92830.png)

### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Path**
foodId | Long | 해당 메뉴 ID | required
**> Responses**
| **Status** | | **Description** |
200 | | OK


```json
{
  "reviewList": {
    "content": [
      {
        "comment": "너무 기름져요",
        "score": "SOSO",
        "eatenDate": null,
        "userName": "user2",
        "foodName": "츄러스",
        "foodId": 160,
        "userId": 3
      },
      {
        "comment": "계피향이 좋아요",
        "score": "BEST",
        "eatenDate": null,
        "userName": "user1",
        "foodName": "츄러스",
        "foodId": 160,
        "userId": 1
      }
    ],
    "pageable": {
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "offset": 0,
      "pageSize": 3,
      "pageNumber": 0,
      "paged": true,
      "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 2,
    "size": 3,
    "number": 0,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "first": true,
    "numberOfElements": 2,
    "empty": false
  },
  "_links": {
    "rel": "self",
    "href": "http://localhost:8080/menus/160?pageNum=1&size=3"
  },
  "food": {
    "foodId": 160,
    "foodName": "츄러스",
    "foodImage": "http://sinjeon.co.kr/img/sub/menu01/menu44.png",
    "category": "SIDEMENU",
    "companyId": 5,
    "companyName": "신전떡볶이",
    "averageScore": 4.0
  }
}
```

## 해당 메뉴의 모든 리뷰를 보는 페이지

URL: `/menus/{foodId}` 에서 `더보기` 버튼을 눌러 해당 메뉴의 모든 리뷰를 가져옵니다.

![image](https://user-images.githubusercontent.com/85560758/171655145-a014a39b-30f2-47d8-8f8f-14cead460d99.png)

| Method | URI
|--|--|
| `GET` | `/menus/{foodId}/reviews`


### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Path**
foodId | Long | 해당 메뉴 ID | required
**> Responses**
| **Status** | | **Description** |
200 | | OK


- Sample Response:
```json
{
  "reviewList": [
    {
      "comment": "계피향이 좋아요",
      "score": "BEST",
      "eatenDate": null,
      "userName": "유저",
      "foodName": "츄러스",
      "foodId": 160,
      "userId": 1
    }
  ],
  "loginUser": {
    "userId": 1,
    "userEmail": "admin@test.com",
    "userName": "admin"
  }
  "_links": {
    "rel": "self",
    "href": "http://localhost:8080/menus/159/reviews"
  }
}
```

---
## 새로운 메뉴 테이블 추가

- `ROLE_ADMIN`의 권한을 가진 관리자만 접근 가능한 API입니다.
- `/admin/companies/{companyId}`의 URL에 접근하여 해당 회사의 메뉴를 추가합니다.

![image](https://user-images.githubusercontent.com/85560758/171655583-efd83a38-1c92-4ffc-ab89-414b6d761000.png)

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

- `ROLE_ADMIN`의 권한을 가진 관리자만 접근 가능한 API입니다.
- `/admin/companies/{companyId}`의 URL에 접근하여 해당 메뉴를 선택하여 정보를 수정합니다.

![image](https://user-images.githubusercontent.com/85560758/171655968-d93791ed-110d-41bd-a1b3-7b33de183e7c.png)

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

- `ROLE_ADMIN`의 권한을 가진 관리자만 접근 가능한 API입니다.
- `/admin/companies/{companyId}`의 URL에 접근하여 해당 메뉴 테이블을 삭제합니다.
- 해당 메뉴에 연결된 리뷰 또한 함께 삭제됩니다.

![image](https://user-images.githubusercontent.com/85560758/171656436-48bb29c3-606f-454d-a805-e88bd9f5dc62.png)

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