package com.example.bai11.repository;

import com.example.bai11.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

    // tìm user theo email (dùng cho login)
    Optional<UserInfo> findByEmail(String email);

    // nếu muốn tìm theo name
    Optional<UserInfo> findByName(String name);
}
