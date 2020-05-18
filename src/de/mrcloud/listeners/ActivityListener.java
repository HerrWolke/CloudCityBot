package de.mrcloud.listeners;

import de.mrcloud.SQL.SqlMain;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class ActivityListener extends ListenerAdapter {

    public HashMap<String, String> timeInChannel = new HashMap<>();
    Statement statement;



    {
        try {
            statement = Objects.requireNonNull(SqlMain.mariaDB()).createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent e) {
        super.onGuildVoiceJoin(e);

        Guild server = e.getGuild();
        VoiceChannel voiceChannelJoined = e.getChannelJoined();
        VoiceChannel voiceChannelLeft = e.getChannelLeft();
        Category category = e.getChannelJoined().getParent();
        Member member = e.getMember();


        if (!e.getChannelJoined().getName().equals("afk-bots-players")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
            ZonedDateTime hereAndNow = ZonedDateTime.now();
            String test = dateTimeFormatter.format(hereAndNow);
            String date = test.replaceAll(",", "");
            timeInChannel.put(member.getUser().getId(), date);
        }

    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent e) {
        super.onGuildVoiceMove(e);

        Guild server = e.getGuild();
        VoiceChannel voiceChannelJoined = e.getChannelJoined();
        VoiceChannel voiceChannelLeft = e.getChannelLeft();
        Category category = e.getChannelJoined().getParent();
        Member member = e.getMember();
        if (e.getChannelJoined().getName().equals("afk-bots-players")) {
            server.kickVoiceMember(member).queue();
        }
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent e) {
        super.onGuildVoiceLeave(e);

        Guild server = e.getGuild();
        VoiceChannel voiceChannelJoined = e.getChannelJoined();
        VoiceChannel voiceChannelLeft = e.getChannelLeft();
        Member member = e.getMember();

        saveChannelTime(member, timeInChannel);

    }

    public void saveChannelTime(Member member, HashMap<String, String> timeInChannel) {




        long seconds = 0L;
        long min = 0L;
        long hour = 0L;
        long day = 0L;

        long putseconds = 0L;
        long putmin = 0L;
        long puthour = 0L;
        long putday = 0L;

        long diffDays = 0L;
        long diffHours = 0L;
        long diffMinutes = 0L;
        long diffSeconds = 0L;

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");


        String date = timeInChannel.get(member.getUser().getId());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
        ZonedDateTime hereAndNow = ZonedDateTime.now();
        String test = dateTimeFormatter.format(hereAndNow);
        String stopDate = test.replaceAll(",", "");




        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(date);
            d2 = format.parse(stopDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        long diff = d2.getTime() - d1.getTime();
        diffSeconds = diff / 1000 % 60;
        diffMinutes = diff / (60 * 1000) % 60;
        diffHours = diff / (60 * 60 * 1000) % 24;
        diffDays = diff / (60 * 60 * 1000 * 24);

        try {
            ResultSet resultSetCheck = statement.executeQuery("SELECT * FROM Users WHERE UserID = " + member.getUser().getId() + ";");
            ResultSet resultSetToSet = statement.executeQuery("SELECT * FROM Users WHERE UserID = " + member.getUser().getId() + ";");

            while(resultSetToSet.next()) {
                day = resultSetToSet.getLong("channelTimeDays");
                hour =resultSetToSet.getLong("channelTimeHours");
                min = resultSetToSet.getLong("channelTimeMinutes");
                seconds = resultSetToSet.getLong("channelTimeSeconds");
            }


            putday = day + diffDays + (hour / 24);
            puthour = hour + diffHours + (min / 60) % 60;
            putmin = min + (seconds / 60 % 60) + diffMinutes;
            putseconds = seconds % 60 + diffSeconds;

            if (resultSetCheck != null && resultSetCheck.next()) {


                statement.executeQuery("UPDATE Users SET channelTimeDays = " + putday + " WHERE UserID = " + member.getId() + ";");
                statement.executeQuery("UPDATE Users SET channelTimeHours = " + puthour + " WHERE UserID = " + member.getId() + ";");
                statement.executeQuery("UPDATE Users SET channelTimeMinutes = " + putmin + " WHERE UserID = " + member.getId() + ";");
                statement.executeQuery("UPDATE Users SET channelTimeSeconds = " + putseconds + " WHERE UserID = " + member.getId() + ";");
            }
        } catch (SQLException e1) {
            e1.printStackTrace();


        }
        timeInChannel.remove(member.getUser().getId());

    }
}
