package com.petBook.controller;

import com.petBook.vo.GoogleLoginVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class TestController {

    @GetMapping("/test")
    public String test() {

        return "test";
    }
}
