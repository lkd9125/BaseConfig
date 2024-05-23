package com.framework.base_set.global.security.model;

import lombok.Getter;

@Getter
public enum UserType {

    ADMIN("ADMIN", "관리자"),
    COMPANY("COMPANY", "사업자"),
    USER("USER", "개인"),
    
    ;

    private String value;
    private String desc;


    private UserType(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

}
