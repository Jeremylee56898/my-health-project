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
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("/api/data")
@ResponseBody
public List<Map<String, Object>> getChartData() {
    // 確保這裡 SELECT 的欄位名稱跟資料庫完全一致，且包含所有欄位
    return jdbcTemplate.queryForList("SELECT RECORD_DATE, SLEEP_HOURS, STEPS, MOOD_SCORE FROM health_logs ORDER BY RECORD_DATE ASC");
}
}