package com.mukcha.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.UserDto;
import com.mukcha.domain.ErrorMessage;
import com.mukcha.domain.Gender;
import com.mukcha.domain.User;
import com.mukcha.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    // 회원가입 서비스
    public User signUp(User user) {
        // 프로필 이미지가 없을 경우 기본 프로필로 대체
        if (user.getProfileImage() == null) {
            user.editProfileImage("/profile/blank.png");
        }
        // set enable
        user.enableUser();
        log.info("회원가입이 진행되었습니다." + user.toString());
        return userRepository.save(user);
    }


    // 회원가입 시 유효성 체크
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();
        // 유효성 검사에 실패한 필드들은 Map 자료구조를 이용하여 키값과 에러 메시지를 응답
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField()); // 키 : valid_{dto 필드명}
            validatorResult.put(validKeyName, error.getDefaultMessage()); // 메시지 : dto에서 작성한 message 값
        }
        return validatorResult;
    }


    /* FINDING SERVICES */
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage())
        );
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage())
        );
    }

    public Optional<User> findByEmailOr(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage())
        );
    }


    /* UPDATE SERVICES */
    public void updateEmail(Long userId, String email) {
        User user = findUser(userId);
        user.editEmail(email);
        userRepository.save(user);
    }

    public void updateNickname(Long userId, String nickname) {
        User user = findUser(userId);
        user.editNickname(nickname);
        userRepository.save(user);
    }

    public void updateProfileImage(Long userId, String profileImageUrl) {
        User user = findUser(userId);
        user.editProfileImage(profileImageUrl);
        userRepository.save(user);
    }

    public void updateGender(Long userId, String stringGender) {
        User user = findUser(userId);
        Gender gender = transClassGender(stringGender);
        user.editGender(gender);
        userRepository.save(user);
    }

    public void updateBirthYear(Long userId, String birthYear) {
        User user = findUser(userId);
        user.editBirthyear(birthYear);
        userRepository.save(user);
    }


    // 로그인된 회원의 정보를 불러옵니다
    public UserDto getSessionUserInfo(SessionUser sessionUser) {
        UserDto userDto = new UserDto();
        User u = findByEmail(sessionUser.getEmail());
        userDto.setUserId(u.getUserId());
        userDto.setEmail(u.getEmail());
        userDto.setNickname(u.getNickname());
        userDto.setProfileImage(u.getProfileImage());
        if (u.getGender() != null) {
            userDto.setGender(u.getGender().toString());
        }
        if (u.getBirthYear() != null) {
            userDto.setBirthYear(u.getBirthYear());
        }
        return userDto;
    }

    // 성별 클래스 전환 for DTO
    public Gender transClassGender(String stringGender) {
        if (stringGender.equals("MALE")) {
            return Gender.MALE;
        } else if (stringGender.equals("FEMALE")) {
            return Gender.FEMALE;
        }
        return null;
    }



    // 회원 삭제 (enabled = false)
    @Transactional(rollbackFor = {RuntimeException.class})
    public void disableUser(Long userId) {
        User user = findUser(userId);
        user.editNickname(null); // nickname null 처리
        user.disableUser(); // disable 처리
        userRepository.save(user);
    }


}
/*

    private final AuthenticationManager authenticationManager;
    // 권한 부여
    public void addAuthority(Long userId, String authority) {
        userRepository.findById(userId).ifPresent(user -> {
            Authority newRole = new Authority(user.getUserId(), authority);
            if (user.getAuthorities() == null) { // 아무 권한이 없다면
                HashSet<Authority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
                userRepository.save(user);
            } else if (!user.getAuthorities().contains(newRole)) { // 해당 권한만 가지고 있지 않다면
                HashSet<Authority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities()); // 기존의 권한에서
                authorities.add(newRole); // new role 추가
                user.setAuthorities(authorities);
                userRepository.save(user);
            }
        });
    }
    // 권한 제거
    public void removeAuthority(Long userId, String authority) {
        userRepository.findById(userId).ifPresent(user -> {
            Authority targetRole = new Authority(user.getUserId(), authority); // target role 생성
            if (user.getAuthorities() == null) { // 아무 권한이 없다면
                return;
            } else if (user.getAuthorities().contains(targetRole)) { // 해당 권한을 가지고 있다면
                Set<Authority> authorities = user.getAuthorities()
                    .stream()
                    .filter(auth -> !auth.equals(targetRole)).collect(Collectors.toSet());
                user.setAuthorities(authorities);
                userRepository.save(user);
            }
        });
    }
    // NAVER 회원가입
    public User saveNaverUser(
        String email,
        String naverId,
        String nickname,
        String profileImage,
        String gender,
        String birthyear,
        String birthday
    ) {
        User user = User.builder()
                    .email(email)
                    .nickname(nickname)
                    .profileImage(profileImage)
                    .gender(transClassGender4Naver(gender))
                    .birthday(transClassLocalDate4Naver(birthyear, birthday))
                    .build()
        ;
        return signUp(user);
    }
    // 네이버 회원가입 생년월일 클래스 전환
    private LocalDate transClassLocalDate4Naver(String birthyear, String birthday) {
        // 사용자 생일 (MM-DD 형식)
        if (birthyear != null || birthday != null) {
            String[] b = birthday.split("-");
            return LocalDate.of(Integer.parseInt(birthyear), Integer.parseInt(b[0]), Integer.parseInt(b[1]));
        }
        return null;
    }
    // 인코딩된 패스워드 비교 메소드
    public Boolean isPasswordSame(String oldPwd, String newPwd) {
        if (passwordEncoder.matches(oldPwd, newPwd)) {
            return true;
        }
        return false;
    }
    // 로그인 진행
    public void doLogin(String email, String password) {
        log.info(">>> 회원<"+email+">님의 로그인을 진행합니다.");
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    // 권한을 넣어서 함께 로그인 진행
    public void doLoginWithAuth(Object principal, String password, Collection<? extends GrantedAuthority> authorities) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, password, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    // 생년월일 클래스 전환 for DTO
    public LocalDate transClassLocalDate(String year, String month, String day) {
        if (year == null || month == null || day == null) {
            return null;
        } else if (year.isEmpty() || month.isEmpty() || day.isEmpty()) {
            return null;
        }
        Integer intYear = Integer.parseInt(year);
        Integer intMonth = Integer.parseInt(month);
        Integer intDay = Integer.parseInt(day);
        return LocalDate.of(intYear, intMonth, intDay);
    }
*/