---
description: ROLE_ADMIN 및 ROLE_USER의 권한을 가진 사용자가 접근 가능한 리뷰 CRUD API입니다.
---

# Review API

{% swagger method="post" path="/api/users/{userId}/menus/{foodId}" baseUrl="http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com" summary="리뷰 항목 중, '코멘트'와 '별점' 항목을 추가 혹은 수정합니다." %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="path" required="true" name="userId" type="Long" %}
현재 로그인한 유저의 ID
{% endswagger-parameter %}

{% swagger-parameter in="path" required="true" name="foodId" type="Long" %}
해당 메뉴의 ID
{% endswagger-parameter %}

{% swagger-parameter in="header" name="Authorization" required="true" %}
"Bearer "+{token}
{% endswagger-parameter %}

{% swagger-parameter in="body" name="comment" %}

{% endswagger-parameter %}

{% swagger-parameter in="body" name="rating" required="true" %}
해당 리뷰의 점수
{% endswagger-parameter %}

{% swagger-response status="200: OK" description="" %}
```javascript
{
    // Response
}
```
{% endswagger-response %}
{% endswagger %}

{% swagger method="put" path="/api/users/{userId}/menus/{foodId}" baseUrl="http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com" summary="리뷰 항목 중, '먹은 날짜'를 추가 혹은 수정합니다." %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="path" name="userId" type="Long" required="true" %}
현재 로그인한 유저의 ID
{% endswagger-parameter %}

{% swagger-parameter in="path" name="foodId" type="Long" required="true" %}
해당 메뉴의 ID
{% endswagger-parameter %}

{% swagger-parameter in="header" name="Authorization" required="true" %}
"Bearer "+{token}
{% endswagger-parameter %}

{% swagger-parameter in="body" name="eatenDate" %}
"2000-01-01" 형태의 날짜
{% endswagger-parameter %}

{% swagger-response status="200: OK" description="" %}
```javascript
{
    // Response
}
```
{% endswagger-response %}
{% endswagger %}

{% swagger method="delete" path="/api/users/{userId}/menus/{foodId}" baseUrl="http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com" summary="해당 리뷰를 삭제합니다." %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="header" required="true" name="Authorization" %}
"Bearer "+{token}
{% endswagger-parameter %}

{% swagger-parameter in="path" name="userId" type="Long" required="true" %}
현재 로그인한 유저의 ID
{% endswagger-parameter %}

{% swagger-parameter in="path" type="Long" name="foodId" required="true" %}
해당 메뉴의 ID
{% endswagger-parameter %}

{% swagger-response status="200: OK" description="" %}
```javascript
{
    // Response
}
```
{% endswagger-response %}

{% swagger-response status="400: Bad Request" description="삭제에 실패한 경우" %}
```javascript
{
    // Response
}
```
{% endswagger-response %}
{% endswagger %}
