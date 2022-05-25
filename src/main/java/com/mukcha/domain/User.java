package com.mukcha.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;


@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@ToString(callSuper = true)
@Where(clause = "enabled=true")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseTimeEntity { //  implements UserDetails

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 순차적으로 증가
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    private String birthYear;

    @Enumerated(value = EnumType.STRING) // enum 이름을 DB에 저장
    private Gender gender;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;

    // user -> review (readOnly)
    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Review> reviews = new ArrayList<>();


    // >>> METHODS <<<
    public User update(String nickname, String profileImage, String gender, String birthYear) {
        this.nickname = nickname;
        if (profileImage != null) this.profileImage = profileImage;
        if (birthYear != null) this.birthYear = birthYear;
        if (gender != null) this.gender = transClassGender(gender);
        return this;
    }

    // 회원가입 성별 클래스 전환
    private static Gender transClassGender(String gender) {
        if (gender != null) {
            // naver - F: 여성 - M: 남성 - U: 확인불가
            // google - "male", "female"
            switch (gender) {
                case "F" :
                case "female":
                case "FEMALE":
                return Gender.FEMALE;
                case "M":
                case "male":
                case "MALE":
                return Gender.MALE;
            }
        }
        return null;
    }

    public String getStringGender() {
        try {
            return gender.toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String getAuthorityKey() {
        return this.authority.getKey();
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public void editProfileImage(String profileImageUrl) {
        this.profileImage = profileImageUrl;
    }

    public void disableUser() {
        this.enabled = false;
    }

    public void enableUser() {
        this.enabled = true;
    }


}