# 먹챠

식당별로 평가? 이제 좀 더 자세하게 평가하고 기록하고 싶다!

프랜차이즈 메뉴별로 평가하여 기록하는 서비스 <먹챠>

## 서비스 구현 목표

- 회원 가입
- 회원 정보 수정
- Spring Security 로 보안화
- OAuth 를 통한 회원가입 및 로그인 기능
- 메뉴 정보 크롤링으로 긁어와서 DB에 저장
  - 메뉴 이름
  - 메뉴 사진

- 메뉴 나열
  ```
  - MENU -
  └ category
    └ company
    └ company
    └ company
  └ category
    └ company
    └ company
  ```

- 사용자가 메뉴에 대해서 할 수 있는 기능
  - 별점(별5개만점) / 코멘트 / 먹고싶어요
  - 코멘트에 좋아요 기능 추가

- BootStrap5 를 사용한 간단한 페이지 작업

---
## 시나리오

- 비회원은 조회만 가능하다.

- 비회원은 사이트에 가입할 수 있다.
  - 사이트에 가입할때 username, password, email 은 필수 항목이다.
  - birthday, gender 은 선택항목이다.
- 회원은 메뉴를 조회할 수 있다.
- 회원은 메뉴를 조회하고 평가할 수 있다.
- 회원은 메뉴를 조회하고 평가한 항목에 한하여 코멘트를 할 수 있다.

- 회원은 본인이 평가한 모든 메뉴를 조회할 수 있다.
- 회원은 본인이 평가한 모든 코멘트를 조회할 수 있다.

- 관리자는 회사를 추가할 수 있다.
- 관리자는 음식을 추가할 수 있다.


## 사용자 도메인 기능 리스트

### Food

- 음식을 생성한다.
- 음식 이름, 회사, 이미지URL, 카테고리를 수정한다.
- 회사 목록을 가져온다
- 회사별 음식 목록을 가져온다
- 카테고리를 가져온다
- 카테고리 별 음식을 가져온다

### User

- 사용자를 생성 한다.
- 이메일과 이름은 중복될 수 없다.
- 이메일을 제외한 이름, 패스워드, 기타 정보를 수정할 수 있다.
- 닉네임을 통하여 검색할 수 있다.
- email로 검색할 수 있다.


### Company

* 회사를 생성한다
* 회사의 음식/메뉴들을 추가한다.
* 회사의 음식/메뉴는 중복하여 들어가지 않는다.
* 회사의 이름, 이미지URL를 수정한다.
* 회사의 음식을 삭제한다.


### Review

* 리뷰를 생성한다.
* 점수를 매기지 않고 먹은날짜 설정과 코멘트를 달 수 없다.
* 리뷰의 점수, 코멘트, 먹은날짜를 수정한다.
* 점수를 삭제하면 리뷰가 삭제된다.


