package com.mukcha.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;
import org.springframework.security.core.userdetails.UserDetails;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@ToString(callSuper = true)
@Where(clause = "enabled = true")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 순차적으로 증가
    private Long userId;

    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    private String profileImage;

    private LocalDate birthday;

    @Enumerated(value = EnumType.STRING) // enum 이름을 DB에 저장
    private Gender gender;

    @ColumnDefault("true")
    private boolean enabled;

    @OneToMany(fetch = FetchType.EAGER)
    @Builder.Default
    private List<Want> wantList = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "userId"))
    private Set<Authority> authorities;
    
    @Override
    public String getUsername() {
        return email; // username email 로 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Integer getBirthYear() {
        return birthday.getYear();
    }
    public Integer getBirthMonth() {
        return birthday.getMonthValue();
    }
    public Integer getBirthDayOfMonth() {
        return birthday.getDayOfMonth();
    }
    public String getStringGender() {
        return gender.toString();
    }


}
