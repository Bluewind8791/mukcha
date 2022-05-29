# User API

ROLE_ADMIN 및 ROLE_USER의 권한을 가진 사용자가 접근 가능한 유저 정보를 수정하는 API입니다.


---
## 해당 회원의 정보 수정

| Method | URI
|--|--|
| `PUT` | /api/users/{userId}

### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Path**
userId | Long | 현재 로그인한 유저의 ID | required
**> Header**
| Authorization | String | "Bearer  "+{token} | required
**> Body**
nickname | String | 회원의 닉네임 | optional
gender | String | 회원의 성별 | optional
birthYear | String | 회원의 생년 ("yyyy" 형식) | optional
**> Responses**
| **Status** | | **Description** |
200 | | OK
400 | | Bad Request



---
## 회원 탈퇴

해당 회원 탈퇴 (User.enable = false)를 진행합니다.

| Method | URI
|--|--|
| `PATCH` | /api/users/{userId}

### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Path**
userId | Long | 현재 로그인한 유저의 ID | required
**> Header**
| Authorization | String | "Bearer  "+{token} | required
**> Responses**
| **Status** | | **Description** |
200 | | OK
400 | | Bad Request