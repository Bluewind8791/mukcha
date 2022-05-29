# Review API

ROLE_ADMIN 및 ROLE_USER의 권한을 가진 사용자가 접근 가능한, 리뷰 CRUD API입니다.



---
## 리뷰 테이블 추가 / 수정

리뷰 항목 중, '코멘트'와 '별점' 항목을 추가 혹은 수정합니다.

| Method | URI
|--|--|
| `POST` | /api/users/{userId}/menus/{foodId}

### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Path**
userId | Long | 현재 로그인한 유저의 ID | required
foodId | Long | 해당 메뉴의 ID | required
**> Header**
| Authorization | String | "Bearer  "+{token} | required
**> Body**
comment | String | 리뷰 코멘트 | optional
rating | String | 해당 리뷰 점수 | required
**> Responses**
| **Status** | | **Description** |
200 | | OK



---
## 리뷰 테이블 수정

리뷰 항목 중, '먹은 날짜'를 추가 혹은 수정합니다.

| Method | URI
|--|--|
| `PUT` | /api/users/{userId}/menus/{foodId}

### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Path**
userId | Long | 현재 로그인한 유저의 ID | required
foodId | Long | 해당 메뉴의 ID | required
**> Header**
| Authorization | String | "Bearer  "+{token} | required
**> Body**
eatenDate | String | "yyyy-mm-dd" 형태의 날짜 | required
**> Responses**
| **Status** | | **Description** |
200 | | OK



---
## 리뷰 테이블 삭제

| Method | URI
|--|--|
| `DELETE` | /api/users/{userId}/menus/{foodId}

### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Path**
userId | Long | 현재 로그인한 유저의 ID | required
foodId | Long | 해당 메뉴의 ID | required
**> Header**
| Authorization | String | "Bearer  "+{token} | required
**> Responses**
| **Status** | | **Description** |
200 | | OK
400 | | Bad Request(삭제에 실패한 경우)