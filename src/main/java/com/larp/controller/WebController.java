package com.larp.controller;

import com.larp.service.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController {
    @Autowired
    private WebSocket webSocket;

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "index";
    }

    @GetMapping("/webSocket/{sid}")
    public ModelAndView socket(@PathVariable String sid) {
        System.out.println((sid));
        ModelAndView mav=new ModelAndView("/webSocket");
        mav.addObject("sid",sid);
        return mav;
    }
}
