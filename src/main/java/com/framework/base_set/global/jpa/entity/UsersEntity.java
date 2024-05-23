package com.framework.base_set.global.jpa.entity;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "USERS")
public class UsersEntity implements UserDetails{

    @Id
    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "REFRESH_TOKEN", nullable = true)
    private String refreshToken;

    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;

    @Column(name = "ACCOUNT_NON_LOCK", nullable = false)
    private boolean accountNonLock;

    @Column(name = "ACCOUNT_NON_EXPRIED", nullable = false)
    private boolean accountNonExpired;

    @Column(name = "PASS_FAIL_COUNT", nullable = false)
    private Integer passFailCount;

    // @Column(name = "REFRESH_TOKEN", nullable = false)
    // private String refreshToken;

    // @Column(name = "FILE_GROUP_NO", nullable = true)
    // private int flieGroupNo;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME")
    private Set<AuthoritiesEntity> authorities;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME")
    private UsersDtlEntity usersDtl;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(AuthoritiesEntity authoritiesEntity) {

        

        if(authorities == null){
            this.authorities = new HashSet<>();
        }

        this.authorities.add(authoritiesEntity);
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLock;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.enabled;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
   
}
