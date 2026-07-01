package com.example.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String hello() {
        return "哈囉！健康系統已經成功連線並啟動啦！";
    }
}