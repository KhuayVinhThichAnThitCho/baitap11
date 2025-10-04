package com.example.bai11.Service;

import com.example.bai11.entity.UserInfo;
import com.example.bai11.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInfoRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserInfo addUser(UserInfo userInfo) {
        // Chuẩn hoá input
        String name  = userInfo.getName()  == null ? "" : userInfo.getName().trim();
        String email = userInfo.getEmail() == null ? "" : userInfo.getEmail().trim().toLowerCase();
        String rawPw = userInfo.getPassword();

        if (name.isEmpty() || email.isEmpty() || rawPw == null || rawPw.isBlank()) {
            throw new IllegalArgumentException("Tên, email, và mật khẩu là bắt buộc");
        }

        // Kiểm tra trùng
        repository.findByEmail(email).ifPresent(u -> {
            throw new IllegalStateException("Email đã tồn tại: " + email);
        });
        // Nếu muốn chặn trùng name:
        repository.findByName(name).ifPresent(u -> {
            throw new IllegalStateException("Tên đã tồn tại: " + name);
        });

        // Chuẩn hoá role(s)
        String roles = (userInfo.getRoles() == null || userInfo.getRoles().isBlank())
                ? "ROLE_USER"
                : normalizeRoles(userInfo.getRoles());

        // Mã hoá mật khẩu
        String encoded = passwordEncoder.encode(rawPw);

        // Lưu
        UserInfo toSave = new UserInfo();
        toSave.setName(name);
        toSave.setEmail(email);
        toSave.setPassword(encoded);
        toSave.setRoles(roles);

        UserInfo saved = repository.save(toSave);

        // Không trả password cho client (nếu cần trả về) → xoá trước khi return
        saved.setPassword(null);
        return saved;
    }

    private String normalizeRoles(String rolesCsv) {
        // ví dụ: "admin, ROLE_user" -> "ROLE_ADMIN,ROLE_USER"
        String[] split = rolesCsv.split(",");
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            String r = s.trim().toUpperCase();
            if (r.isEmpty()) continue;
            if (!r.startsWith("ROLE_")) r = "ROLE_" + r;
            if (sb.length() > 0) sb.append(',');
            sb.append(r);
        }
        return sb.toString();
    }
}
