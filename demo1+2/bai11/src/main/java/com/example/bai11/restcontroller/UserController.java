package com.example.bai11.restcontroller;

import com.example.bai11.entity.UserInfo;
import com.example.bai11.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Signup: POST /user/new
    @PostMapping("/new")
    public ResponseEntity<?> addUser(@RequestBody UserInfo userInfo) {
        try {
            UserInfo saved = userService.addUser(userInfo);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống: " + ex.getMessage());
        }
    }
}
