package com.ep.weare.common;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    public static String getTimeAgoOrFormatted(LocalDateTime pastTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(pastTime, currentTime);

        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return "방금";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + "분 전";
        } else {
            // 1시간 이상이면 지정된 형식으로 포맷팅
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
            return pastTime.format(formatter);
        }
    }

}
