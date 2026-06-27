import java.sql.*;
import java.time.LocalDate;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://" + System.getenv("MYSQL_HOST") + ":" + System.getenv("MYSQL_PORT") + "/" + System.getenv("MYSQL_DATABASE");
String user = System.getenv("MYSQL_USERNAME");
String password = System.getenv("MYSQL_PASSWORD");
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            // 1. 模擬今天資料並計算風險
            double sleep = 5.0; 
            int steps = 2000; 
            int mood = 3;
            String risk = calculateRisk(sleep, steps, mood);
            
            // 寫入當日資料
            String insertSql = String.format("INSERT INTO health_logs (log_date, sleep_hours, steps, mood_score, risk_level) VALUES ('%s', %.1f, %d, %d, '%s')",
                                LocalDate.now().toString(), sleep, steps, mood, risk);
            stmt.executeUpdate(insertSql);

            // 2. 風險等級徽章 (利用 ANSI 顏色碼)
            String color = risk.equals("高風險") ? "\u001B[31m" : (risk.equals("中風險") ? "\u001B[33m" : "\u001B[32m");
            System.out.println("【今日風險等級徽章】： " + color + risk + "\u001B[0m");

            // 3. 顯示歷史紀錄列表 (撈出最近 5 筆)
            System.out.println("\n--- 過去 5 筆健康趨勢 ---");
            ResultSet rs = stmt.executeQuery("SELECT log_date, risk_level FROM health_logs ORDER BY id DESC LIMIT 5");
            while (rs.next()) {
                System.out.println(rs.getDate("log_date") + " | " + rs.getString("risk_level"));
            }

            con.close();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }
    
    public static String calculateRisk(double sleep, int steps, int mood) {
        if (sleep < 5.5 && steps < 3500 && mood < 4) return "高風險";
        else if (sleep > 7.0 && steps > 6000 && mood > 6) return "低風險";
        else return "中風險";
    }
}