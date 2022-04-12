package br.com.uff.askme.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static String formatLocalDateTimeToBrazilianDatabaseStyle(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(localDateTime);
    }
}
