package com.mukcha.controller.api;

import javax.validation.Valid;

import com.mukcha.controller.dto.CompanyRequestDto;
import com.mukcha.controller.dto.FoodSaveRequestDto;
import com.mukcha.controller.dto.FoodUpdateRequestDto;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/admin")
public class AdminApiController {

    private final CompanyService companyService;
    private final FoodService foodService;


    // 회사 추가 메소드
    @PostMapping("/companies")
    public ResponseEntity<?> saveCompany(@RequestBody CompanyRequestDto requestDto) {
        companyService.save(requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 메뉴 추가 메소드
    @PostMapping("/menus")
    public ResponseEntity<?> saveFood(@Valid @RequestBody FoodSaveRequestDto requestDto) {
        foodService.save(requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    // 회사 수정 메소드
    @PutMapping("/companies/{companyId}")
    public ResponseEntity<?> editCompany(
        @PathVariable Long companyId,
        @RequestBody CompanyRequestDto requestDto
    ) {
        companyService.update(companyId, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 메뉴 수정 메소드
    @PutMapping("/menus/{foodId}")
    public ResponseEntity<?> editFood(
        @PathVariable Long foodId,
        @RequestBody FoodUpdateRequestDto requestDto
    ) {
        foodService.update(foodId, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // 회사 삭제 메소드
    @DeleteMapping("/companies/{companyId}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long companyId) {
        boolean result = companyService.deleteCompany(companyId);
        if (result) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    // 메뉴 삭제 메소드
    @DeleteMapping("/menus/{foodId}")
    public ResponseEntity<?> deleteFood(@PathVariable Long foodId) {
        if (foodService.deleteById(foodId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("정상적으로 삭제되지 않았습니다.", HttpStatus.BAD_REQUEST);
        }
    }
    // @DeleteMapping(value = "/menus/{foodId}")
    // public String deleteFood(@PathVariable Long foodId, HttpServletRequest request) {
    //     foodService.deleteFood(foodId);
    //     // return "redirect:/admin";
    //     String referer = request.getHeader("Referer");
    //     return "redirect:"+ referer;
    // }


}
