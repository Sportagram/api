package com.BDiary.webDemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class MyPageController {
    @GetMapping("/my")
    public String myPage(){
        return "my page";
    }
}
