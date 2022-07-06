# Review API


## 리뷰 테이블 추가 / 수정

- 로그인 한 유저만이 접근 가능한 API입니다.
- 리뷰 항목 중, '코멘트'와 '별점' 항목을 추가 혹은 수정합니다.
- `/menus/{foodId}`의 URL에 접근하여 해당 메뉴에 `평가하기` 버튼을 눌러서 리뷰를 추가합니다.
- 평가 수정은 해당 `/menus/{foodId}` URL에서 `평가 수정` 버튼을 눌러서 리뷰를 수정합니다.


![image](https://user-images.githubusercontent.com/85560758/171657872-b8c9165d-0ff4-4c3a-a392-f88504f7de5d.png)


| Method | URI
|--|--|
| `POST` | `/api/users/{userId}/menus/{foodId}`

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

- 로그인 한 유저만이 접근 가능한 API입니다.
- 리뷰 항목 중, '먹은 날짜'를 추가 혹은 수정합니다.
- `/menus/{foodId}`의 URL에 접근하여 해당 메뉴에 `먹은 날짜` 버튼을 눌러서 먹은 날짜를 추가 및 수정합니다.

![image](https://user-images.githubusercontent.com/85560758/171658821-8f1030cd-c743-4086-b8c9-e530819c7b80.png)

| Method | URI
|--|--|
| `PUT` | `/api/users/{userId}/menus/{foodId}`

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

- 로그인 한 유저만이 접근 가능한 API입니다.
- `/menus/{foodId}`의 URL에 접근하여 해당 메뉴에 `리뷰 삭제` 버튼을 눌러서 본인이 작성한 리뷰를 삭제합니다.

![image](https://user-images.githubusercontent.com/85560758/171659102-692cbdb3-b4ec-4191-a815-5bd5965d9f6e.png)

| Method | URI
|--|--|
| `DELETE` | `/api/users/{userId}/menus/{foodId}`

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