---
description: ROLE_ADMIN의 권한을 가진 사용자만이 접근 가능한 회사 정보를 CRUD 하는 API입니다.
---

# Company API

{% swagger method="post" path="/api/admin/companies" baseUrl="http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com" summary="새로운 회사 테이블을 추가합니다." %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="body" required="true" name="companyName" %}
회사명
{% endswagger-parameter %}

{% swagger-parameter in="body" name="companyLogo" %}
회사 로고 이미지 URL
{% endswagger-parameter %}

{% swagger-parameter in="header" name="Authorization" required="true" %}
"Bearer  "+{token}
{% endswagger-parameter %}

{% swagger-response status="201: Created" description="" %}
```javascript
{
    // Response
}
```
{% endswagger-response %}
{% endswagger %}

{% swagger method="put" path="/api/admin/companies/{companyId}" baseUrl="http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com" summary="해당 회사 정보를 수정합니다." %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="path" name="companyId" type="Long" required="true" %}
해당 회사의 PK ID
{% endswagger-parameter %}

{% swagger-parameter in="body" name="companyName" %}
회사명
{% endswagger-parameter %}

{% swagger-parameter in="body" name="companyLogo" %}
회사 로고 이미지 URL
{% endswagger-parameter %}

{% swagger-parameter in="header" name="Authorization" required="true" %}
"Bearer  "+{token}
{% endswagger-parameter %}

{% swagger-response status="200: OK" description="" %}
```javascript
{
    // Response
}
```
{% endswagger-response %}
{% endswagger %}

{% swagger method="delete" path="/api/admin/companies/{companyId}" baseUrl="http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com" summary="해당 회사 테이블을 삭제합니다." %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="path" name="companyId" type="Long" required="true" %}
해당 회사의 ID
{% endswagger-parameter %}

{% swagger-parameter in="header" name="Authorization" required="true" %}
"Bearer  "+{token}
{% endswagger-parameter %}

{% swagger-response status="200: OK" description="요청 성공 시" %}
```javascript
{
    // Response
}
```
{% endswagger-response %}

{% swagger-response status="400: Bad Request" description="삭제 실패 시" %}
```javascript
{
    // Response
}
```
{% endswagger-response %}
{% endswagger %}
