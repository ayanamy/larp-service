package com.larp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "index";
    }
}
