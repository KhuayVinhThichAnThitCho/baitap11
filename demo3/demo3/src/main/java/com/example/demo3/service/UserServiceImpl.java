package com.example.demo3.service;

import com.example.demo3.entity.UserInfo;
import com.example.demo3.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UserInfoRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cho phép nhập username HOẶC email
        UserInfo user = userRepository.findByEmail(username)
                .or(() -> userRepository.findByName(username))
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user: " + username));

        // Gói UserInfo vào lớp UserDetails của mình
        return new MyUserService(user);
    }
}
