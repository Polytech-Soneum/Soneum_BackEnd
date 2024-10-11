package com.example.soneumproject.detail;

import com.example.soneumproject.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Users user;

    public CustomUserDetails(Users user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자에게 할당된 권한 목록을 반환
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getUserPassword(); // 사용자의 비밀번호 반환
    }

    @Override
    public String getUsername() {
        return user.getUserID(); // 사용자의 사용자명 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부를 반환 (여기서는 만료되지 않았다고 설정)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부를 반환 (여기서는 잠금되지 않았다고 설정)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호) 만료 여부를 반환 (여기서는 만료되지 않았다고 설정)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부를 반환 (여기서는 활성화되었다고 설정)
    }

    public Users getUser() {
        return user; // `Users` 엔티티 반환
    }
}
