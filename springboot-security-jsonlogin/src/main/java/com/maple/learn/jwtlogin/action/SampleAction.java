package com.maple.learn.jwtlogin.action;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SampleAction {

    @RequestMapping("/admin/page")
    public Map<String,Object> adminpage(Model model) {

        return new HashMap(){{put("url","/admin/page");}};
    }
    @RequestMapping("/user/page")
    public Map<String,Object> userpage(Model model) {
        return new HashMap(){{put("url","/user/page");}};
    }

}
