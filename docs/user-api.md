# User API

## 해당 회원의 정보 수정

- 로그인 한 유저만이 접근 가능한 API입니다.
- `/api/user/edit`의 URL에 접근하여 회원 정보를 수정합니다.

![user_edit](https://user-images.githubusercontent.com/85560758/171837027-161e34c0-38f7-48aa-a5f7-97935c4f03e5.png)

| Method | URI
|--|--|
| `PUT` | `/api/users/{userId}`

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

- 로그인 한 유저만이 접근 가능한 API입니다.
- `/api/user/edit`의 URL에 접근하여 해당 회원 탈퇴 (`User.enable = false`)를 진행합니다.

![image](https://user-images.githubusercontent.com/85560758/171837668-e7bf4663-dcd5-4bbd-984f-641f8e9e4238.png)


| Method | URI
|--|--|
| `PATCH` | `/api/users/{userId}`

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