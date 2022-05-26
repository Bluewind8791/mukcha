# API 문서 개요

Mukcha 서비스를 구현하기 위하여 사용하고 있는 API 설계 문서입니다.

API의 종류는 크게 다음와 같이 나눌 수 있습니다.

* 회사
* 메뉴
* 리뷰
* 유저

각 API는 ajax를 통하여 비동기 통신을 하도록 이루어져 있으며 모든 API 결과는 다음과 같습니다.



{% hint style="success" %}
요청에 대한 응답에 성공하였을 경우 (200)
{% endhint %}

{% hint style="success" %}
POST 요청에 성공하여 데이터가 성공적으로 생성되었을 경우 (201)
{% endhint %}

{% hint style="warning" %}
허용되지 않는 접근 방식 (400)
{% endhint %}



## API 목록

{% content-ref url="company-api.md" %}
[company-api.md](company-api.md)
{% endcontent-ref %}

{% content-ref url="menu-api.md" %}
[menu-api.md](menu-api.md)
{% endcontent-ref %}

{% content-ref url="review-api.md" %}
[review-api.md](review-api.md)
{% endcontent-ref %}

{% content-ref url="user-api.md" %}
[user-api.md](user-api.md)
{% endcontent-ref %}
