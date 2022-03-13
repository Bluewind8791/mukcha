package com.mukcha.controller;

import java.time.LocalDate;
import java.util.Map;
import javax.validation.Valid;

import com.mukcha.controller.dto.UserDto;
import com.mukcha.domain.Authority;
import com.mukcha.domain.Gender;
import com.mukcha.domain.User;
import com.mukcha.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    // VIEW - 회원가입
    @GetMapping("/join")
    public String joinForm(Model model, UserDto userDto) {
        model.addAttribute("form", new UserDto());
        return "user/joinForm";
    }


    // SAVE - 회원가입
    @PostMapping("/join")
    public String join(
        @Valid UserDto userDto,
        BindingResult bindingResult,
        Model model,
        Errors errors
    ) {
        // 비밀번호 확인 불일치
        if (!userDto.getPassword().equals(userDto.getRePassword())) {
            model.addAttribute("form", userDto); // 회원가입 실패 시 입력 데이터 유지
            model.addAttribute("valid_rePassword", "비밀번호가 일치하지 않습니다.");
            return "user/joinForm";
        }
        // 이메일 중복 시
        if (userService.findByEmail(userDto.getEmail()).isPresent()) {
            model.addAttribute("form", userDto);
            model.addAttribute("valid_email", "이미 사용중인 이메일입니다.");
            return "user/joinForm";
        }
        // 닉네임 중복 시
        if (userService.findByNickname(userDto.getNickname()).isPresent()) {
            model.addAttribute("form", userDto);
            model.addAttribute("valid_nickname", "이미 사용중인 닉네임입니다.");
            return "user/joinForm";
        }
        // @Valid 를 통한 에러 시
        if (errors.hasErrors()) {
            model.addAttribute("form", userDto);
            // 유효성 통과 못한 필드와 메세지 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "user/joinForm";
        }
        
        // 통과 시 회원가입 진행
        User user = saveUser(userDto);
        log.info("회원가입이 처리되었습니다." + user.toString());
        return "redirect:/login";
    }


    // 회원가입 진행 메소드
    private User saveUser(UserDto userDto) {
        // 프로필 이미지가 없을 경우 기본 프로필로 대체
        String profileImage;
        if (userDto.getProfileImage() == null) {
            profileImage = "/profile/blank.png";
        } else {
            profileImage = userDto.getProfileImage();
        }
        final User user = User.builder()
            .email(userDto.getEmail())
            .nickname(userDto.getNickname())
            .password(passwordEncoder.encode(userDto.getPassword()))
            .gender(transClassToGender(userDto.getGender()))
            .birthday(transClassToLocalDate(userDto.getBirthYear(), userDto.getBirthMonth(), userDto.getBirthDayOfMonth()))
            .profileImage(profileImage)
            .enabled(true)
            .build();
        User savedUser = userService.save(user);
        userService.addAuthority(savedUser.getUserId(), Authority.ROLE_USER);
        return savedUser;
    }




    // 회원가입 시 성별 선택 메소드
    private Gender transClassToGender(String formGender) {
        if (formGender.equals("MALE")) {
            return Gender.MALE;
        } else if (formGender.equals("FEMALE")) {
            return Gender.FEMALE;
        }
        return null;
    }


    // 회원가입 시 생년월일
    private LocalDate transClassToLocalDate(String year, String month, String day) {
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


}
