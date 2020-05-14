package de.mrcloud.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        String yes = "Matchmaking 02";
        if(yes.matches("Matchmaking \\d*")) {
            System.out.println("y");
        }
        String startDate = "11/03/14 09:30:00";
        String stopDate = "11/03/14 09:33:03";

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
        ZonedDateTime hereAndNow = ZonedDateTime.now();
        String test = dateTimeFormatter.format(hereAndNow);
        System.out.println(test.replaceAll(",",""));

        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(startDate);
            d2 = format.parse(stopDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = d2.getTime() - d1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
    }
}
