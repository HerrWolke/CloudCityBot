package de.mrcloud.listeners;

import de.mrcloud.SQL.SqlMain;
import de.mrcloud.utils.JDAUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandListener extends ListenerAdapter {
    public static List<String> top10Members = new ArrayList<>();
    JDAUtils utils = new JDAUtils();
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);
        String top10String = " ";

        JDAUtils utils = new JDAUtils();


        Guild server = e.getGuild();
        Member member = e.getMember();
        Message message = e.getMessage();
        String messageContent = message.getContentRaw();
        TextChannel txtChannel = e.getChannel();
        int f = 0;




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
        } else if (messageContent.equalsIgnoreCase("&version")) {
            txtChannel.sendMessage("Die Version des Bots ist " + Static.VERSION).queue();
        } else if (messageContent.equalsIgnoreCase("&test")) {
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
                    "Hey " +    member.getAsMention() + " du bist jetzt im Himmel :cloud: .Willkommen auf unserem Discord(CloudCity). Ließ dir bitte noch die Regeln durch, um Probleme zu vermeiden!\n" +
                    "\n" +
                    "Bei uns \uD835\uDC1F\uD835\uDC22\uD835\uDC27\uD835\uDC1D\uD835\uDC1E\uD835\uDC2C\uD835\uDC2D du in dem #\uD835\uDC2C\uD835\uDC1E\uD835\uDC2B\uD835\uDC2F\uD835\uDC1E\uD835\uDC2B_\uD835\uDC22\uD835\uDC29\uD835\uDC2C Channel viele \uD835\uDC1C\uD835\uDC28\uD835\uDC28\uD835\uDC25\uD835\uDC1E \uD835\uDC0C\uD835\uDC1A\uD835\uDC29\uD835\uDC2C die du zusammen mit deinen Freunden spielen kannst. Unser Server ist hauptsächlich für Counter-Strike: Global Offensive ausgelegt.\n" +
                    "\uD835\uDC03\uD835\uDC2E \uD835\uDC24\uD835\uDC1A\uD835\uDC27\uD835\uDC27\uD835\uDC2C\uD835\uDC2D \uD835\uDC1D\uD835\uDC22\uD835\uDC2B \uD835\uDC2C\uD835\uDC1E\uD835\uDC25\uD835\uDC1B\uD835\uDC2C\uD835\uDC2D \uD835\uDC11\uD835\uDC28\uD835\uDC25\uD835\uDC25\uD835\uDC1E\uD835\uDC27 \uD835\uDC20\uD835\uDC1E\uD835\uDC1B\uD835\uDC1E\uD835\uDC27 und musst dabei nur auf einige Nachrichten mit einem Emote reagieren.\n" +
                    "\n" +
                    "Die wihtigsten Commands findest du in #bot-commands\n" +
                    "Viel Spaß auf unserem Discord:butterfly:\n" +
                    "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------").queue();
        } else if (messageContent.equalsIgnoreCase("&top10")) {
            ResultSet top10 = null;
            int i = 0;
            try {
               top10 = statement.executeQuery("SELECT * FROM Users ORDER BY channelTimeDays DESC, channelTimeHours DESC, channelTimeMinutes DESC, channelTimeSeconds DESC;");

               while(top10.next() && i < 10) {
                   System.out.println(top10.getString("channelTimeDays") + top10.getString("channelTimeHours") + top10.getString("channelTimeMinutes") + top10.getString("channelTimeSeconds") );
                   top10String += ("```" + i + "." + top10.getString("UserName") + ":```     " + top10.getString("channelTimeDays") + " Days " + top10.getString("channelTimeHours") + " hours " + top10.getString("channelTimeMinutes") + " minutes " + top10.getString("channelTimeSeconds") + " seconds " + "\n");
                   i++;
               }


               utils.Generell(member,txtChannel,"#16a085","Top 10 voice members",top10String,false,0);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            finally {
                try {
                    sql.mariaDB().close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } else if (messageContent.split("\\s++")[0].equalsIgnoreCase("&setfriendcode")) {
            if(e.getMessage().getContentRaw().split("\\s++")[1].matches("\\w{5}-\\w{4}")) {
                utils.GreenBuilder("Success", e.getMember(), e.getChannel(), "Your friend code has been set to " + e.getMessage().getContentRaw().split("\\s++")[1], 0, false);
                try {
                    SqlMain.mariaDB().createStatement().executeQuery("UPDATE Users SET FriendCode = '" + e.getMessage().getContentRaw().split("\\s++")[1] + "' WHERE UserID  = '" + e.getMember().getId() + "';");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } else {
                utils.YellowBuilder("Usage Help",member,txtChannel,"Your provided code is not a valid friend code. Please use &setfriendcode [FRIEND CODE] <- Format: abcde-abcd",15,true);
            }
        } else if (messageContent.split("\\s++")[0].equalsIgnoreCase("&help")) {
            utils.Generell(member,txtChannel,"#487eb0","Command List","&profile \n" + "&setfriendcode [FRIENDCODE] \n" + "&top10 \n" + "&version \n",false,30);
        } else if (messageContent.split("\\s++")[0].equalsIgnoreCase("&membercount")) {

            utils.BlueBuilder("Member Count",member,txtChannel,"There are " + server.getMemberCount() + " people on this server",0,false);

        }
    }
}


