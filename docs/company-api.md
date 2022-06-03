# Company API


## 회사 페이지

![companies_1](https://user-images.githubusercontent.com/85560758/171858843-67de3914-a0f1-4dc1-9519-17076222392e.png)

| Method | URI
|--|--|
| `GET` | `/companies/{companyId}`

### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Path**
companyId | Long | 해당 회사의 ID | required
**> Responses**
| **Status** | | **Description** |
200 | | OK

- Sample Response:
```json

```



---
## 새로운 회사 테이블 추가

`ROLE_ADMIN`의 권한을 가진 관리자만이 접근 가능한, 회사 테이블을 추가하는 API입니다.

![image](https://user-images.githubusercontent.com/85560758/171653972-ef8e7012-ee2b-44e3-af16-14b53ee66aea.png)


| Method | URI
|--|--|
| `POST` | `/api/admin/companies`

### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Header**
| Authorization | String | "Bearer  "+{token} | required
**> Body**
companyName | String | 회사명 | required
companyLogo | String | 회사 로고 이미지 URL | optional
**> Responses**
| **Status** | | **Description** |
201 | | Created


---
## 해당 회사 정보 수정

`ROLE_ADMIN`의 권한을 가진 관리자만이 접근 가능한, 해당 회사의 정보를 수정하는 API입니다.

![image](https://user-images.githubusercontent.com/85560758/171654110-80703ccc-d0ba-4b53-a7e3-114c8067ee73.png)


| Method | URI
|--|--|
| `PUT` | `/api/admin/companies/{companyId}`


### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Path**
companyId | Long | 해당 회사의 ID | required
**> Header**
| Authorization | String | "Bearer  "+{token} | required
**> Body**
companyName | String | 회사명 | optional
companyLogo | String | 회사 로고 이미지 URL | optional
**> Responses**
| **Status** | | **Description** |
200 | | OK


---
## 해당 회사 테이블 삭제

`ROLE_ADMIN`의 권한을 가진 관리자만이 접근 가능한, 해당 회사 테이블을 삭제하는 API입니다.

![image](https://user-images.githubusercontent.com/85560758/171654456-979496cf-6a78-4ec8-83af-0627a1fd6013.png)

| Method | URI
|--|--|
| `DELETE` | `/api/admin/companies/{companyId}`


### Parameters

| Name | Type |  Description  | Required
|-|-|-|-|
**> Path**
companyId | Long | 해당 회사의 ID | required
**> Header**
| Authorization | String | "Bearer  "+{token} | required
**> Responses**
| **Status** | | **Description** |
200 | | OK
400 | | Bad Request (삭제 실패 시)