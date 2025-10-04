package com.example.bai11.Service;

import com.example.bai11.entity.UserInfo;
import com.example.bai11.repository.UserInfoRepository;
import com.example.bai11.security.UserInfoDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repository.findByEmail(username)
                .or(() -> repository.findByName(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new UserInfoDetails(user);
    }

    // tiện ích: tạo user mới và mã hoá password
    public UserInfo createUser(String name, String email, String rawPassword, String roles) {
        UserInfo u = new UserInfo();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRoles(roles); // ví dụ: "ROLE_ADMIN" hoặc "ROLE_USER,ROLE_ADMIN"
        return repository.save(u);
    }
}
