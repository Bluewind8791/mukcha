---
description: ROLE_ADMIN 및 ROLE_USER의 권한을 가진 사용자가 접근 가능한 유저 정보를 수정하는 API입니다.
---

# User API

{% swagger method="put" path="/api/users/{userId}" baseUrl="http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com" summary="해당 회원의 정보를 수정합니다." %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="header" name="Authorization" required="true" %}
"Bearer "+{token}
{% endswagger-parameter %}

{% swagger-parameter in="path" name="userId" type="Long" required="true" %}
해당 회원의 ID
{% endswagger-parameter %}

{% swagger-parameter in="body" name="nickname" %}
회원의 닉네임
{% endswagger-parameter %}

{% swagger-parameter in="body" name="gender" %}
회원의 성별
{% endswagger-parameter %}

{% swagger-parameter in="body" name="birthYear" %}
회원의 생년 ("yyyy" 형식)
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

{% swagger method="patch" path="/api/users/{userId}" baseUrl="http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com" summary="해당 회원 탈퇴(User.enable = false)를 진행합니다." %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="header" name="Authorization" %}
"Bearer "+{token}
{% endswagger-parameter %}

{% swagger-parameter in="path" name="userId" type="Long" %}
해당 회원의 ID
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
