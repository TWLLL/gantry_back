package com.qljt.gantry.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuliangliang
 * @create 2020-02-26 13:39
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String test(){
        return "hhello";
    }
}
