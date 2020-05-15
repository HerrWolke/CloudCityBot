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
        }else if (messageContent.equalsIgnoreCase("&test")) {
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
                } catch(SQLException e1){
                    e1.printStackTrace();
                }

            EmbedBuilder embBuilder = new EmbedBuilder();
            embBuilder.setTitle("Profile Info");
            embBuilder.setAuthor(e.getAuthor().getName() + "'s Profile", e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
            embBuilder.setColor(Color.decode("#2ecc71"));
            embBuilder.addField("Active Since", activeSince, true);
            embBuilder.addField("Time in Voice Channel", day + " days, " + hour + " hours, " + min + " minutes," + seconds + " seconds ", true);
            txtChannel.sendMessage(embBuilder.build()).complete();

            }
        }
    }


