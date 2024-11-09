package com.BDiary.webDemo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    // 홈페이지.. 이건 프론트? 불러오기
    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User != null) {
            return "Welcome " + oauth2User.getAttribute("name");
        }
        return "Welcome guest";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
