package com.framework.base_set.global.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.framework.base_set.global.jpa.entity.UsersEntity;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, String>{
    
} 
