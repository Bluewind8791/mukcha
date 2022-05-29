# Company API

`ROLE_ADMIN`의 권한을 가진 사용자만이 접근 가능한, 회사 정보를 CRUD 하는 API입니다.


---
## 새로운 회사 테이블 추가

| Method | URI
|--|--|
| `POST` | /api/admin/companies

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

| Method | URI
|--|--|
| `PUT` | /api/admin/companies/{companyId}


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

| Method | URI
|--|--|
| `DELETE` | /api/admin/companies/{companyId}


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