package de.mrcloud.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Test {
    public static List<String> myList = new ArrayList<>();

    public static void main(String[] args) {
        Date date1 = new Date();
        Date date2 = new Date();

        String testname = "introduction-for-mrcloud-handy";
        System.out.println(testname.split("-")[0] + "-" +testname.split("-")[1]);

        String friendCode = "SUZKH-76CC";
        if (friendCode.matches("\\w{5}-\\w{4}")) {
            System.out.println("Is friend code");
        }

        if (date1.getMinutes() == date2.getMinutes()) {
            System.out.println("yes true");
        }
        myList.add("Test2");
        myList.add("Test3");
        myList.add("Test4");

        System.out.println(myList.size());

        long diff = 3600000 * 24;

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (60 * 60 * 1000 * 24);

        System.out.println(diffDays + " days after x");
        System.out.println(diffHours + " hours after x");
        System.out.println(diffMinutes + " minutes after x");
        System.out.println(diffSeconds + " seconds after x");

        String voiceChannel = "Matchmaking 1";

        System.out.println(voiceChannel.split("\\s")[1]);


        String yes = "Matchmaking 02";
        if (yes.matches("Matchmaking \\d*")) {
            System.out.println("y");
        }
        String startDate = "11/03/14 09:30:00";
        String stopDate = "11/03/14 09:33:03";

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
        ZonedDateTime hereAndNow = ZonedDateTime.now();
        String test3 = dateTimeFormatter.format(hereAndNow);
        System.out.println(test3.replaceAll(",", ""));

        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(startDate);
            d2 = format.parse(stopDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
