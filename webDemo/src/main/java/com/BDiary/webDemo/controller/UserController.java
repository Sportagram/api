package com.BDiary.webDemo.controller;

import com.BDiary.webDemo.dto.CustomOAuth2User;
import com.BDiary.webDemo.entity.user;
import com.BDiary.webDemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 정보 조회 API
    // 설정 페이지에 사용하는 api 입니다.
    @GetMapping("/user/settings")
    public ResponseEntity<Map<String, Object>> getUserSettings(Authentication authentication) {
        if (authentication != null) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            user user = userRepository.findByGooglename(oAuth2User.getUsername());

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("nickname", user.getNickname());
            userInfo.put("email", user.getEmail());
            userInfo.put("myteam", user.getMyteam());

            return ResponseEntity.ok(userInfo);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 사용자 정보 수정 API
    // 수정 완료 버튼 클릭 시 정보 수정 되도록 하면 될 듯 합니다.
    @PutMapping("/user/settings")
    public ResponseEntity<?> updateUserSettings(
            @RequestBody UserSettingsRequest request,
            Authentication authentication) {
        if (authentication != null) {
            try {
                CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
                user user = userRepository.findByGooglename(oAuth2User.getUsername());

                // 닉네임과 응원팀 정보 업데이트
                user.setNickname(request.getNickname());
                user.setMyteam(request.getMyteam());

                userRepository.save(user);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

// 사용자 설정 수정 요청을 위한 DTO
class UserSettingsRequest {
    private String nickname;
    private String myteam;

    // getters and setters
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMyteam() {
        return myteam;
    }

    public void setMyteam(String myteam) {
        this.myteam = myteam;
    }
}