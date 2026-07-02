package com.example.health;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping(value = "/", produces = "text/html;charset=UTF-8")
    public String hello() {
        return "<h1>哈囉！健康系統已經成功連線並啟動啦！</h1>" +
               "<p><a href='/logs' style='font-size: 20px; color: blue;'>👉 點我查看健康紀錄表</a></p>";
    }

    @GetMapping(value = "/logs", produces = "text/html;charset=UTF-8")
    public String getLogs() {
        try {
            // 從資料庫撈取資料
            List<Map<String, Object>> logs = jdbcTemplate.queryForList("SELECT * FROM health_logs");
            
            // 畫出 HTML 表格
            StringBuilder html = new StringBuilder();
            html.append("<html><head><meta charset='UTF-8'><title>健康紀錄</title></head><body style='font-family: sans-serif; padding: 20px;'>");
            html.append("<h2>🩺 我的健康紀錄表</h2>");
            html.append("<table border='1' cellpadding='10' style='border-collapse: collapse; text-align: center;'>");
            html.append("<tr style='background-color: #f2f2f2;'><th>ID</th><th>心跳率 (bpm)</th><th>步數</th><th>紀錄時間</th></tr>");

            for (Map<String, Object> log : logs) {
                html.append("<tr>")
                    .append("<td>").append(log.get("id")).append("</td>")
                    .append("<td>").append(log.get("heart_rate")).append("</td>")
                    .append("<td>").append(log.get("steps")).append("</td>")
                    .append("<td>").append(log.get("record_date")).append("</td>")
                    .append("</tr>");
            }
            html.append("</table><br><a href='/'>返回首頁</a></body></html>");
            return html.toString();
            
        } catch (Exception e) {
            return "資料庫連線中，或發生錯誤：" + e.getMessage();
        }
    }
}