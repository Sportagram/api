package com.BDiary.webDemo.controller;

import com.BDiary.webDemo.dto.CustomOAuth2User;
import com.BDiary.webDemo.entity.user;
import com.BDiary.webDemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;

/*
@Controller  // @ResponseBody 어노테이션 제거
public class MainController {

    @GetMapping("/api/main")
    @ResponseBody
    public Map<String, Object> getUserInfo(Authentication authentication) {
        Map<String, Object> userInfo = new HashMap<>();

        if (authentication != null) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            userInfo.put("username", oAuth2User.getUsername());
            userInfo.put("name", oAuth2User.getName());
        }

        return userInfo;
    }
}

 */


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")  // 모든 엔드포인트가 /api로 시작하도록 설정
public class MainController {

    private final UserRepository userRepository;

    @Autowired
    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user")  // /api/user로 접근
    public ResponseEntity<Map<String, Object>> getUserInfo(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        if (authentication != null) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            // user DB에서 해당 사용자 정보 조회하기
            user user = userRepository.findByGooglename(oAuth2User.getUsername());

            response.put("username", oAuth2User.getUsername());
            response.put("name", oAuth2User.getName());
            response.put("isProfileComplete", user.getNickname() != null && user.getMyteam() != null);
            response.put("nickname", user.getNickname());
            response.put("myteam", user.getMyteam());
            response.put("userid", user.getUserId());

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    // 위에서 React에서 요청의 결과로 nickname 혹은 myteam 둘 중 하나가 null이라면 false.
    // False 시 다음과 같은 nickname 및 myteam 설정으로 넘어가야 함.
    // 둘다 not null이면 true. 이때는 환영 메세지 페이지로 이동
    // 프로필 설정을 위한 엔드포인트
    @PostMapping("/profile")  // /api/profile로 접근
    public ResponseEntity<?> setProfile(@RequestBody ProfileRequest profileRequest, Authentication authentication) {
        if (authentication != null) {
            try {
                CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
                user user = userRepository.findByGooglename(oAuth2User.getUsername());

                user.setNickname(profileRequest.getNickname());
                user.setMyteam(profileRequest.getMyteam());
                userRepository.save(user);

                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

// 프로필 요청을 위한 DTO
class ProfileRequest {
    private String nickname;
    private String myteam;

    // getter와 setter
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