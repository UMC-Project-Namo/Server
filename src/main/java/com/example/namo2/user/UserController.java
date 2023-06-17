package com.example.namo2.user;

import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponse;
import com.example.namo2.user.dto.SignUpReq;
import com.example.namo2.user.dto.SignUpRes;
import com.example.namo2.user.dto.SocialSignUpReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Api(value = "User")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/kakao/signup")
    @ApiOperation(value = "카카오 로그인 ")
    public BaseResponse<SignUpRes> kakaoSignup(@Valid @RequestBody SocialSignUpReq signUpReq) throws BaseException {
        SignUpRes signupRes = userService.signupKakao(signUpReq);
        return new BaseResponse<>(signupRes);
    }

    @PostMapping(value = "/naver/signup")
    @ApiOperation(value = "네이버 로그인")
    public BaseResponse<SignUpRes> naverSignup(@Valid @RequestBody SocialSignUpReq signUpReq) throws BaseException {
        SignUpRes signupRes = userService.signupNaver(signUpReq);
        return new BaseResponse<>(signupRes);
    }

    @PostMapping(value = "/reissuance")
    @ApiOperation(value = "토큰 재발급")
    public BaseResponse<SignUpRes> reissueAccessToken(@Valid @RequestBody SignUpReq signUpReq) throws BaseException {
        SignUpRes signupRes = userService.reissueAccessToken(signUpReq);
        return new BaseResponse<>(signupRes);
    }

}
