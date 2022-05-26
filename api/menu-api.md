---
description: ROLE_ADMIN의 권한을 가진 사용자만이 접근 가능한 메뉴 정보를 CRUD 하는 API입니다.
---

# Menu API

{% swagger method="post" path="/api/admin/menus" baseUrl="http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com" summary="새로운 메뉴 테이블을 추가합니다." %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="header" name="Authorization" required="true" %}
"Bearer "+{token}
{% endswagger-parameter %}

{% swagger-parameter in="body" name="foodName" required="true" %}
Name of menu
{% endswagger-parameter %}

{% swagger-parameter in="body" name="foodImage" %}
Image URL of menu
{% endswagger-parameter %}

{% swagger-parameter in="body" name="category" required="true" %}
Category of menu
{% endswagger-parameter %}

{% swagger-parameter in="body" name="companyName" required="true" %}
Name of the company that created the menu
{% endswagger-parameter %}

{% swagger-response status="201: Created" description="" %}
```javascript
{
    // Response
}
```
{% endswagger-response %}
{% endswagger %}

{% swagger method="put" path="/api/admin/menus/{foodId}" baseUrl="http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com" summary="해당 메뉴의 정보를 수정합니다." %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="path" name="foodId" type="Long" required="true" %}
ID value of corresponding menu
{% endswagger-parameter %}

{% swagger-parameter in="header" name="Authorization" required="true" %}
"Bearer "+{token}
{% endswagger-parameter %}

{% swagger-parameter in="body" name="foodName" %}
Name of menu
{% endswagger-parameter %}

{% swagger-parameter in="body" name="foodImage" %}
Image URL of menu
{% endswagger-parameter %}

{% swagger-parameter in="body" name="category" %}
Category of menu
{% endswagger-parameter %}

{% swagger-parameter in="body" name="companyName" %}
Name of the company that created the menu
{% endswagger-parameter %}

{% swagger-response status="200: OK" description="" %}
```javascript
{
    // Response
}
```
{% endswagger-response %}
{% endswagger %}

{% swagger method="delete" path="/api/admin/menus/{foodId}" baseUrl="http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com" summary="해당 메뉴 테이블을 삭제합니다." %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="header" name="Authorization" required="true" %}
"Bearer "+{token}
{% endswagger-parameter %}

{% swagger-parameter in="path" name="foodId" type="Long" required="true" %}

{% endswagger-parameter %}

{% swagger-response status="200: OK" description="" %}
```javascript
{
    // Response
}
```
{% endswagger-response %}

{% swagger-response status="400: Bad Request" description="" %}
```javascript
{
    // Response
}
```
{% endswagger-response %}
{% endswagger %}
