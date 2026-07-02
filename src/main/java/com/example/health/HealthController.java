package com.example.health;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HealthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String index(Model model) {
        try {
            List<Map<String, Object>> logs = jdbcTemplate.queryForList("SELECT * FROM health_logs ORDER BY record_date DESC");
            model.addAttribute("logs", logs);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "index";
    }

    @PostMapping("/add")
    public String addLog(@RequestParam String date, 
                         @RequestParam int sleep, 
                         @RequestParam int steps, 
                         @RequestParam int mood) {
        String sql = "INSERT INTO health_logs (record_date, sleep_hours, steps, mood_score) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, date, sleep, steps, mood);
        return "redirect:/";
    }

   // 在 HealthController.java 中新增這段
@PostMapping("/delete")
public String deleteLog(@RequestParam int id) {
    String sql = "DELETE FROM health_logs WHERE id = ?";
    jdbcTemplate.update(sql, id);
    return "redirect:/"; // 刪除後自動跳轉回首頁
}
}