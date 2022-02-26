---
---


## @Valid 정리 표

| Annotation Type | Description |  
| -               | - |
| Email	          | 올바른 형식의 이메일 주소여야 합니다.
| Pattern         | 지정된 정규식과 일치하는 문자열이여야 합니다. (regexp  => 정규식 문자열을 지정한다)
| -               |
| AssertFalse     | 값이 항상 false 여야 합니다.
| AssertTrue      | 값이 항상 true 여야 합니다.
| -               |
| NotBlank        | null 이 아니어야 하며, 하나 이상의 공백이 아닌 문자를 포함해야 합니다.
| NotEmpty        | null 이거나 비어 있으면 안 됩니다.
| NotNull         | null 이 아니어야 합니다.
| Null            | null 이어야 합니다.
| -               |
| Future          | 미래의 순간, 날짜 또는 시간이어야 합니다.
| FutureOrPresent | 현재 또는 미래의 순간, 날짜 또는 시간이어야 합니다.
| Past            | 과거의 순간, 날짜 또는 시간이어야 합니다.
| PastOrPresent	  | 과거 또는 현재의 순간, 날짜 또는 시간이어야 합니다.
| -               |
| Negative        | 완전히 음수여야 합니다.
| NegativeOrZero  | 음수 또는 0 이어야 합니다.
| Positive        | 반드시 양수여야 합니다
| PositiveOrZero  | 양수 또는 0 이어야 합니다.
| -               |
| Max	          | 값이 지정된 최대값보다 작거나 같아야 하는 숫자여야 합니다.
| Min	          | 값이 지정된 최소값보다 크거나 같아야 하는 숫자여야 합니다.
| DecimalMax      | 값이 지정된 최대값보다 작거나 같아야 하는 숫자여야 합니다. (String value)
| DecimalMin      | 값이 지정된 최소값보다 크거나 같아야 하는 숫자여야 합니다. (String value)
| Size            | 주석이 달린 요소 크기는 지정된 경계(포함) 사이에 있어야 합니다. (max, min)
| Digits          | 허용되는 범위 내의 숫자여야 합니다. (integer  => 이 숫자에 허용되는 최대 정수 자릿수 / fraction  => 이 숫자에 허용되는 최대 소수 자릿수)

---

## 참고

- https://javaee.github.io/javaee-spec/javadocs/javax/validation/constraints/package-summary.html
- 쟈미님의 devlog : [링크](https://jyami.tistory.com/55)