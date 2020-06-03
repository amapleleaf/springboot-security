package com.maple.learn.secure.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.Map;

@Controller
public class SampleAction {
    @GetMapping("/home")
    public String home(Map<String, Object> model) {
        model.put("message", "Hello World");
        model.put("title", "Hello Home");
        model.put("date", new Date());
        return "home";
    }

    @RequestMapping("/admin/page")
    public String adminpage(Model model) {
        model.addAttribute("reqinfo","/admin/page");
        return "common";
    }
    @RequestMapping("/user/page")
    public String userpage(Model model) {
        model.addAttribute("reqinfo","/user/page");
        return "common";
    }

    @RequestMapping("/timeout")
    public String timeout() {
        return "timeout";
    }

}
