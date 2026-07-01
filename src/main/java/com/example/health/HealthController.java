package com.example.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HealthController {

    // 呼叫 Spring Boot 內建的資料庫查詢工具
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping(value = "/", produces = "text/html;charset=UTF-8")
    public String showHealthLogs() {
        try {
            // 下達 SQL 指令，去 health_logs 資料表撈出所有資料
            // ⚠️ 備註：請確認你的資料表名稱真的是 health_logs
            List<Map<String, Object>> logs = jdbcTemplate.queryForList("SELECT * FROM health_logs");

            // 開始組合 HTML 網頁標籤
            StringBuilder html = new StringBuilder();
            html.append("<div style='font-family: sans-serif; padding: 20px;'>");
            html.append("<h2 style='color: #2c3e50;'>我的智慧健康日誌</h2>");
            
            // 畫一個漂亮的表格
            html.append("<table border='1' cellpadding='10' style='border-collapse: collapse; width: 100%; text-align: center;'>");
            html.append("<tr style='background-color: #3498db; color: white;'>");
            html.append("<th>紀錄日期</th><th>睡眠時數</th><th>步數</th><th>心情分數</th>");
            html.append("</tr>");

            // 用迴圈把每一筆資料塞進表格裡
            for (Map<String, Object> log : logs) {
                html.append("<tr>");
                html.append("<td>").append(log.get("log_date")).append("</td>");
                html.append("<td>").append(log.get("sleep_hours")).append("</td>");
                html.append("<td>").append(log.get("steps")).append("</td>");
                html.append("<td>").append(log.get("mood_score")).append("</td>");
                html.append("</tr>");
            }
            html.append("</table>");
            html.append("</div>");

            return html.toString();

        } catch (Exception e) {
            // 如果資料庫連線失敗，會在網頁上顯示錯誤原因，方便我們除錯！
            return "<h2>糟糕！資料庫連線出錯了：</h2><p>" + e.getMessage() + "</p>";
        }
    }
}