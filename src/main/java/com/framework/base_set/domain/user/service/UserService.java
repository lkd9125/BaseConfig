package com.framework.base_set.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.base_set.domain.user.model.register.UserRegisterRQ;
import com.framework.base_set.global.exception.AppException;
import com.framework.base_set.global.exception.ExceptionCode;
import com.framework.base_set.global.jpa.entity.AuthoritiesEntity;
import com.framework.base_set.global.jpa.entity.UsersDtlEntity;
import com.framework.base_set.global.jpa.entity.UsersEntity;
import com.framework.base_set.global.jpa.entity.AuthoritiesEntity.AuthorityId;
import com.framework.base_set.global.jpa.repository.UsersRepository;
import com.framework.base_set.global.security.model.UserType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;


    /**
     * 회원추가
     * @param rq
     * @return
     */
    @Transactional
    public boolean registerUsers(UserRegisterRQ rq) throws AppException{
        
        String username = rq.getId();
        boolean duplicateCheck = usersRepository.findById(username).isPresent();

        usersRepository.findById(username);

        if(duplicateCheck) throw new AppException(ExceptionCode.DATA_DUPLICATE);

        // Users 테이블 Insert
        UsersEntity users = new UsersEntity();
        users.setUsername(username);
        users.setPassword(passwordEncoder.encode(rq.getPassword()));
        users.setEmail(rq.getEmail());
        users.setEnabled(true);
        users.setAccountNonExpired(true);
        users.setAccountNonLock(true);
        users.setPassFailCount(0);


        // UsersDtl 테이블 Insert
        UsersDtlEntity usersDtl = new UsersDtlEntity();
        usersDtl.setUsername(username);
        usersDtl.setNickname(rq.getNickname());
        usersDtl.setPhoneNumber(rq.getHpno());

        // Users의 권한 Authorities 테이블 Insert
        AuthoritiesEntity authorities = new AuthoritiesEntity();
        AuthorityId id = new AuthorityId();
        id.setUsername(username);
        id.setAuthority(UserType.USER.getValue());
        authorities.setId(id);

        users.setUsersDtl(usersDtl);
        users.setAuthorities(authorities);

        usersRepository.save(users);

        return true;
    }
}
