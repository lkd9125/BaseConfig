package com.framework.base_set.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.framework.base_set.domain.user.model.register.RegisterRS;
import com.framework.base_set.domain.user.model.register.UserRegisterRQ;
import com.framework.base_set.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;


    /**
     * 회원가입
     * @param rq 
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterRQ rq){
        log.warn("AccountController - registerUser()");
        log.warn("RegisterRQ -> {}", rq);

        boolean result = userService.registerUsers(rq);

        RegisterRS rs = new RegisterRS();
        rs.setResult(result);

        return ResponseEntity.ok().body(null);
    }
}
