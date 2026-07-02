DROP TABLE IF EXISTS health_logs;
CREATE TABLE health_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    record_date DATE,
    sleep_hours INT,
    steps INT,
    mood_score INT
);