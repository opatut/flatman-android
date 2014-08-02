package de.opatut.flatman.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {
    public static Date parseDate(String dateString) {
        SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        try {
            return iso.parse(dateString);
        } catch(ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d");
        return format.format(date);
    }

    public static String formatMoney(int amount) {
        return String.format("%.02f", amount/100.f);
    }
}
