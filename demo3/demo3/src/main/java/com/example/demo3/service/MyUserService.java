package com.example.demo3.service;

import com.example.demo3.entity.Role;
import com.example.demo3.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class MyUserService implements UserDetails {

    private final UserInfo user;

    public MyUserService(UserInfo user) {
        this.user = user;
    }

    // Map Set<Role> -> authorities
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();              // nếu bạn dùng Set<Role>
        return roles.stream()
                .map(Role::getName)                     // "ROLE_USER", "ROLE_ADMIN"
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();                      // đã mã hoá BCrypt trong DB
    }

    @Override
    public String getUsername() {
        // Có thể trả name hoặc email. Trả email để người dùng đăng nhập bằng email.
        return user.getEmail();
    }

    @Override public boolean isAccountNonExpired()  { return true; }
    @Override public boolean isAccountNonLocked()   { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()            { return true; } // nếu entity có field enabled thì trả về user.isEnabled()
}
