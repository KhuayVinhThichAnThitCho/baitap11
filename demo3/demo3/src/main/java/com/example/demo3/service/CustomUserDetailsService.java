package com.example.demo3.service;

import com.example.demo3.entity.UserInfo;
import com.example.demo3.entity.Role;
import com.example.demo3.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserInfoRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Tìm theo email trước, không có thì tìm theo name/username
        UserInfo u = userRepository.findByEmail(usernameOrEmail)
                .or(() -> userRepository.findByName(usernameOrEmail))
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

        // Nếu UserInfo có Set<Role> roles:
        Collection<? extends GrantedAuthority> authorities = u.getRoles().stream()
                .map(Role::getName)                         // "ROLE_USER", "ROLE_ADMIN", ...
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Nếu bạn đang lưu roles là String "ROLE_USER,ROLE_ADMIN",
        // thay block trên bằng:
        // Collection<? extends GrantedAuthority> authorities =
        //         java.util.Arrays.stream(u.getRoles().split(","))
        //                 .map(String::trim)
        //                 .filter(s -> !s.isEmpty())
        //                 .map(SimpleGrantedAuthority::new)
        //                 .collect(Collectors.toList());

        return new User(
                u.getEmail(),          // username dùng cho Security (có thể để u.getName() nếu bạn muốn)
                u.getPassword(),       // phải là BCrypt hash
                authorities            // quyền
        );
    }
}
