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
        String test = "12h";
        if(test.matches("(\\d*)h")) System.out.println("Faggot");

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        //Formats current time in a normal format
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
        ZonedDateTime hereAndNow = ZonedDateTime.now();
        String testDate = dateTimeFormatter.format(hereAndNow);
        String stopDate = testDate.replaceAll(",", "");
        //---------------------------------------
        String superTest = "1:12:13";
        System.out.println("YEEET" + superTest.split(":")[2]);


        System.out.println(stopDate);
        String dateNiet = "06.06.2020 16:59:10";

        Date testDate0 = new Date();
        Date testDate1 = new Date();



        try {
            testDate0 = format.parse(dateNiet);
            testDate1 = format.parse(stopDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        testDate0.setMinutes(testDate0.getMinutes() + 2);

        System.out.println(testDate0);

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


    }
}
