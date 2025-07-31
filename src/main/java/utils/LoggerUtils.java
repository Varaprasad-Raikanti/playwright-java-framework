package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerUtils {

    public static void log(String message) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println("[LOG " + time + "] " + message);
    }
}
