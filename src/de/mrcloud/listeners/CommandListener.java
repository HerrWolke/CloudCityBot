package de.mrcloud.listeners;

import de.mrcloud.SQL.SqlMain;
import de.mrcloud.utils.Static;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        Guild server = e.getGuild();
        Member member = e.getMember();
        Message message = e.getMessage();
        String messageContent = message.getContentRaw();
        TextChannel txtChannel = e.getChannel();

        long seconds = 0L;
        long min = 0L;
        long hour = 0L;
        long day = 0L;
        String activeSince = "";

        SqlMain sql = new SqlMain();
        Statement statement = null;
        int s = 0;


        try {
            statement = Objects.requireNonNull(sql.mariaDB()).createStatement();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }


        if (messageContent.equalsIgnoreCase("&reg")) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
            String formated = member.getTimeJoined().format(format);

            System.out.println(member.getTimeJoined());
            try {
                ResultSet resultSetCheck = statement.executeQuery("SELECT * FROM Users WHERE userID = " + member.getUser().getId() + ";");

                if (!resultSetCheck.next()) {
                    statement.executeQuery("INSERT into Users(userName,dateJoined,userID)" + "\n" + "VALUES('" + member.getUser().getName() + "','" + formated + "'," + member.getId() + ");");
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
                e1.getLocalizedMessage();
            }
        } else if (messageContent.equalsIgnoreCase("&test")) {
            txtChannel.sendMessage("Die Version des Bots ist " + Static.VERSION).queue();
        } else if (messageContent.equalsIgnoreCase("&version")) {
            while (member.getRoles().size() > s) {
                System.out.println(member.getRoles().get(s).getName());
                s++;
            }
        } else if (messageContent.equalsIgnoreCase("&profile")) {
            ResultSet resultSetToSet = null;
            try {
                resultSetToSet = statement.executeQuery("SELECT * FROM Users WHERE UserID = " + member.getUser().getId() + ";");


                while (resultSetToSet.next()) {
                    day = resultSetToSet.getLong("channelTimeDays");
                    hour = resultSetToSet.getLong("channelTimeHours");
                    min = resultSetToSet.getLong("channelTimeMinutes");
                    seconds = resultSetToSet.getLong("channelTimeSeconds");
                    activeSince = resultSetToSet.getString("dateJoined");

                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            EmbedBuilder embBuilder = new EmbedBuilder();
            embBuilder.setTitle("Profile Info");
            embBuilder.setAuthor(e.getAuthor().getName() + "'s Profile", e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
            embBuilder.setColor(Color.decode("#2ecc71"));
            embBuilder.addField("Active Since", activeSince, true);
            embBuilder.addField("Time in Voice Channel", day + " days, " + hour + " hours, " + min + " minutes," + seconds + " seconds ", true);
            txtChannel.sendMessage(embBuilder.build()).complete();
        } else if (messageContent.equalsIgnoreCase("&test2")) {
            txtChannel.sendMessage("GERMAN/DEUTSCH" +
                    "\n" +
                    "Hey " + member.getAsMention() + " du bist jetzt im Himmel :cloud: .Willkommen auf unserem Discord(CloudCity). Ließ dir bitte noch die Regeln durch, um Probleme zu vermeiden!\n" +
                    "\n" +
                    "Bei uns \uD835\uDC1F\uD835\uDC22\uD835\uDC27\uD835\uDC1D\uD835\uDC1E\uD835\uDC2C\uD835\uDC2D du in dem #\uD835\uDC2C\uD835\uDC1E\uD835\uDC2B\uD835\uDC2F\uD835\uDC1E\uD835\uDC2B_\uD835\uDC22\uD835\uDC29\uD835\uDC2C Channel viele \uD835\uDC1C\uD835\uDC28\uD835\uDC28\uD835\uDC25\uD835\uDC1E \uD835\uDC0C\uD835\uDC1A\uD835\uDC29\uD835\uDC2C die du zusammen mit deinen Freunden spielen kannst. Unser Server ist hauptsächlich für Counter-Strike: Global Offensive ausgelegt.\n" +
                    "\uD835\uDC03\uD835\uDC2E \uD835\uDC24\uD835\uDC1A\uD835\uDC27\uD835\uDC27\uD835\uDC2C\uD835\uDC2D \uD835\uDC1D\uD835\uDC22\uD835\uDC2B \uD835\uDC2C\uD835\uDC1E\uD835\uDC25\uD835\uDC1B\uD835\uDC2C\uD835\uDC2D \uD835\uDC11\uD835\uDC28\uD835\uDC25\uD835\uDC25\uD835\uDC1E\uD835\uDC27 \uD835\uDC20\uD835\uDC1E\uD835\uDC1B\uD835\uDC1E\uD835\uDC27 und musst dabei nur auf einige Nachrichten mit einem Emote reagieren.\n" +
                    "\n" +
                    "Mit !rank kannst du dein aktuellen Rank sehen,mit !levels kannst du \n" +
                    "das Rank Leaderborad ansehen.Mit &profile kannst du sehen wie lange du auf dem Discord bist und wie viel Zeit du in voice channels verbracht hast.\n" +
                    "Viel Spaß auf unserem Discord:butterfly:").queue();
        }
    }
}


