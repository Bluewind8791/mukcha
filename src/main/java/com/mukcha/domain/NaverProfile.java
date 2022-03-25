package com.mukcha.domain;

import lombok.Data;

@Data
public class NaverProfile {

    private String resultcode;
    private String message;
    private NaverReponse response;

    @Data
    public class NaverReponse {
        public String id;
        public String nickname;
        public String profile_image;
        public String gender;
        public String email;
        public String birthday;
        public String birthyear;
    }

}

/*
naverProfileResponse: {
    "resultcode":"00",
    "message":"success",
    "response":{
        "id":"",
        "nickname":"",
        "profile_image":"",
        "gender":"M",
        "email":",
        "birthday":"12-14",
        "birthyear":"1991"
    }
}
*/
