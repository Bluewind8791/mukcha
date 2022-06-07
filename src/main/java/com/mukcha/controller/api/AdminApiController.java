package com.mukcha.controller.api;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.mukcha.config.dto.SpringDocApiResponse;
import com.mukcha.controller.dto.CompanyRequestDto;
import com.mukcha.controller.dto.FoodSaveRequestDto;
import com.mukcha.controller.dto.FoodUpdateRequestDto;
import com.mukcha.domain.ErrorMessage;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/admin")
@Tag(
    name = "Admin API Controller", 
    description = "ROLE_ADMIN의 권한을 가진 사용자만이 접근 가능한 회사 및 메뉴를 CRUD하는 API입니다."
)
public class AdminApiController {

    private final CompanyService companyService;
    private final FoodService foodService;


    @SpringDocApiResponse
    @ApiResponse(
        responseCode = "200", 
        description = "응답에 성공하였습니다."
    )
    @PostMapping("/companies")
    @Operation(summary = "회사 추가 메소드", description = "새로운 회사 테이블을 추가합니다.")
    public ResponseEntity<?> saveCompany(@Valid @RequestBody CompanyRequestDto requestDto) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("_links",
            linkTo(methodOn(AdminApiController.class).saveCompany(requestDto)).withSelfRel()
        );
        if (companyService.save(requestDto)) {
            return new ResponseEntity<>(headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
    }

    @SpringDocApiResponse
    @PostMapping("/menus")
    @Operation(summary = "메뉴 추가 메소드", description = "새로운 메뉴 테이블을 추가합니다.")
    public ResponseEntity<?> saveFood(@Valid @RequestBody FoodSaveRequestDto requestDto) {
        if (foodService.save(requestDto)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @SpringDocApiResponse
    @PutMapping("/companies/{companyId}")
    @Operation(summary = "회사 수정 메소드", description = "기존의 회사 테이블을 수정합니다.")
    public ResponseEntity<?> editCompany(
        @Parameter(description = "수정하고자 하는 회사의 ID", required = true) @PathVariable Long companyId,
        @RequestBody CompanyRequestDto requestDto
    ) {
        if (companyService.update(companyId, requestDto)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @SpringDocApiResponse
    @PutMapping("/menus/{foodId}")
    @Operation(summary = "메뉴 수정 메소드", description = "기존의 메뉴 테이블을 수정합니다.")
    public ResponseEntity<?> editFood(
        @Parameter(description = "수정하고자 하는 메뉴의 ID", required = true) @PathVariable Long foodId,
        @RequestBody FoodUpdateRequestDto requestDto
    ) {
        if (foodService.update(foodId, requestDto)) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<>(ErrorMessage.FAIL_UPDATE.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @SpringDocApiResponse
    @DeleteMapping("/companies/{companyId}")
    @Operation(summary = "회사 삭제 메소드", description = "기존의 회사 테이블을 삭제합니다.")
    public ResponseEntity<?> deleteCompany(
        @Parameter(description = "삭제하고자 하는 회사의 ID", required = true) @PathVariable Long companyId
    ) {
        if (companyService.deleteCompany(companyId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @SpringDocApiResponse
    @DeleteMapping("/menus/{foodId}")
    @Operation(summary = "메뉴 삭제 메소드", description = "기존의 메뉴 테이블을 삭제합니다.")
    public ResponseEntity<?> deleteFood(@PathVariable Long foodId) {
        if (foodService.deleteById(foodId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ErrorMessage.FAIL_DELETE.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
