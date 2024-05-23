package com.framework.base_set.domain.user.model.register;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegisterRQ {

    @NotBlank
    @Length(min = 0, max = 50)
    private String id; // 아이디

    @NotNull
    private String password; // 암호
    
    @NotNull
    private String nickname; // 닉네임

    @Email
    private String email; // 이메일

    @NotNull
    private String hpno; // 휴대폰번호

}
