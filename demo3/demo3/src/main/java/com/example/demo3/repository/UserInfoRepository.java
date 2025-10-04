package com.example.demo3.repository;

import com.example.demo3.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    @Query("select u from UserInfo u where u.name = :username")
    Optional<UserInfo> getUserByUsername(@Param("username") String username);

    Optional<UserInfo> findByEmail(String email);

    Optional<UserInfo> findByName(String name);

    Optional<UserInfo> findFirstByNameIgnoreCaseOrEmailIgnoreCase(String name, String email);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}