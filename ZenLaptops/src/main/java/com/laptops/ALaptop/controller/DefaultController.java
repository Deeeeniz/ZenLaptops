package com.laptops.ALaptop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class DefaultController {
    @GetMapping("/login")
    public String getLoginPage() {
        return "sign-in";
    }
}
