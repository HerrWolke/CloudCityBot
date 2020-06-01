package de.mrcloud.listeners;

import de.mrcloud.SQL.SqlMain;
import de.mrcloud.utils.JDAUtils;
import de.mrcloud.utils.Static;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.EmoteManager;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);
        //Variables----------------
        String top10String = " ";

        JDAUtils utils = new JDAUtils();

        Guild server = e.getGuild();
        @Nonnull
        Member member = Objects.requireNonNull(e.getMember());
        Message message = e.getMessage();
        String messageContent = message.getContentRaw();
        TextChannel txtChannel = e.getChannel();

        long seconds = 0L;
        long min = 0L;
        long hour = 0L;
        long day = 0L;
        String activeSince = "";

        Statement statement = null;
        int s = 0;
        //----------------------------

        try {
            statement = Objects.requireNonNull(SqlMain.mariaDB()).createStatement();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        assert statement != null;
        if (messageContent.equalsIgnoreCase("&reg")) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
            String formated = member.getTimeJoined().format(format);


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
            ResultSet resultSetToSet;
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

        } else if (messageContent.equalsIgnoreCase("&top10")) {
            ResultSet top10;
            int i = 0;
            try {
                //Sorts the top 10 list and saves it in the string
                top10 = statement.executeQuery("SELECT * FROM Users ORDER BY channelTimeDays DESC, channelTimeHours DESC, channelTimeMinutes DESC, channelTimeSeconds DESC;");

                while (top10.next() && i < 10) {
                    // saves all collums from the database into a single string which will then be used in the message
                    top10String += ("```" + (i + 1) + "." + top10.getString("UserName") + ":```     " + top10.getString("channelTimeDays") + " Days " + top10.getString("channelTimeHours") + " hours " + top10.getString("channelTimeMinutes") + " minutes " + top10.getString("channelTimeSeconds") + " seconds " + "\n");
                    i++;
                }

                //Sends the top 10 list
                utils.Generell(member, txtChannel, "#16a085", "Top 10 voice members", top10String, false, 0);
            } catch (SQLException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    Objects.requireNonNull(SqlMain.mariaDB()).close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } else if (messageContent.split("\\s++")[0].equalsIgnoreCase("&setfriendcode")) {
            if (e.getMessage().getContentRaw().split("\\s++")[1].matches("\\w{5}-\\w{4}")) {
                utils.GreenBuilder("Success", "Your friend code has been set to " + e.getMessage().getContentRaw().split("\\s++")[1], e.getMember(), e.getChannel(), false, 0);
                try {
                    //Sets the friend code in the database
                    Objects.requireNonNull(SqlMain.mariaDB()).createStatement().executeQuery("UPDATE Users SET FriendCode = '" + e.getMessage().getContentRaw().split("\\s++")[1] + "' WHERE UserID  = '" + e.getMember().getId() + "';");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } else {
                //If the friend code does not match the pattern of a friend code this message will be send
                utils.YellowBuilder("Usage Help", "Your provided code is not a valid friend code. Please use &setfriendcode [FRIEND CODE] <- Format: abcde-abcd", member, txtChannel, true, 15);
            }
        } else if (messageContent.equalsIgnoreCase("&help")) {
            //Lists all commands
            utils.Generell(member, txtChannel, "#487eb0", "Command List", "&profile \n" + "&setfriendcode [FRIENDCODE] \n" + "&top10 \n" + "&version \n", false, 30);
        } else if (messageContent.equalsIgnoreCase("&membercount")) {
            //Sends a message containing a member count
            utils.BlueBuilder("Member Count", "There are " + server.getMemberCount() + " people on this server", txtChannel, member, false, 0);
        } else if (messageContent.equalsIgnoreCase("&copyrole")) {
                message.getMentionedRoles().get(0).createCopy().queue();
        } else if (messageContent.equalsIgnoreCase("&sendRoleMessages")) {

            if (member.getId().equals(Static.CLOUD_ID_STRING)) {
                message.delete().queue();
                txtChannel.sendMessage("Wähle den Emoji, der mit dem \uD835\uDD4E\uD835\uDD56\uD835\uDD65\uD835\uDD65\uD835\uDD5C\uD835\uDD52\uD835\uDD5E\uD835\uDD61\uD835\uDD57-ℝ\uD835\uDD52\uD835\uDD5F\uD835\uDD58 den du in CS:GO hast, übereinstimmt\n" +
                        "Silver Ⅰ → " + server.getEmotesByName("s1",true).get(0).getAsMention() +  "\n" +
                        "Silver Ⅱ → " + server.getEmotesByName("s2",true).get(0).getAsMention() +  "\n" +
                        "Silver Ⅲ → " + server.getEmotesByName("s3",true).get(0).getAsMention() +  "\n" +
                        "Silver Ⅳ → " + server.getEmotesByName("s4",true).get(0).getAsMention() +  "\n" +
                        "Silver Elite → " + server.getEmotesByName("se",true).get(0).getAsMention() +  "\n" +
                        "Silver Elite Master → " + server.getEmotesByName("sem",true).get(0).getAsMention() +  "\n" +
                        "Gold Nova Ⅰ → " + server.getEmotesByName("gn1",true).get(0).getAsMention() +  "\n" +
                        "Gold Nova Ⅱ → " + server.getEmotesByName("gn2",true).get(0).getAsMention() +  "\n" +
                        "Gold Nova Ⅲ → " + server.getEmotesByName("gn3",true).get(0).getAsMention() +  "\n" +
                        "Gold Nova Ⅳ → " + server.getEmotesByName("gn4",true).get(0).getAsMention() +  "\n" +
                        "Master Guardian Ⅰ → " + server.getEmotesByName("mg",true).get(0).getAsMention() +  "\n" +
                        "Master Guardian Ⅱ → " + server.getEmotesByName("mg2",true).get(0).getAsMention() +  "\n" +
                        "Master Guardian Elite → " + server.getEmotesByName("mge",true).get(0).getAsMention() +  "\n" +
                        "Distinguished Master Guardian → " + server.getEmotesByName("dmg",true).get(0).getAsMention() +  "\n" +
                        "Legendary Eagle → :le: " + server.getEmotesByName("le",true).get(0).getAsMention() +  "\n" +
                        "Legendary Eagle Master → :lem: " + server.getEmotesByName("lem",true).get(0).getAsMention() +  "\n" +
                        "Supreme Master First Class → :supreme: " + server.getEmotesByName("supreme",true).get(0).getAsMention() +  "\n" +
                        "Global Elite → " + server.getEmotesByName("global",true).get(0).getAsMention()).queue(message1 -> {
                    int i = 0;
                    message1.addReaction(server.getEmotesByName("unranked", true).get(0)).queue();
                    while (SearchingForMatchmakingListener.compareEmojiToRole.keySet().toArray().length > i) {
                        message1.addReaction(server.getEmotesByName(SearchingForMatchmakingListener.compareEmojiToRole.keySet().toArray()[i].toString(), true).get(0)).queue();
                        i++;
                    }
                });
                txtChannel.sendMessage("Wähle den Emoji, der mit dem \uD835\uDD4E\uD835\uDD5A\uD835\uDD5F\uD835\uDD58\uD835\uDD5E\uD835\uDD52\uD835\uDD5F-ℝ\uD835\uDD52\uD835\uDD5F\uD835\uDD58 den du in CS:GO hast, übereinstimmt\n" +
                        "Silver Ⅰ → " + server.getEmotesByName("s1",true).get(0).getAsMention() +  "\n" +
                        "Silver Ⅱ → " + server.getEmotesByName("s2",true).get(0).getAsMention() +  "\n" +
                        "Silver Ⅲ → " + server.getEmotesByName("s3",true).get(0).getAsMention() +  "\n" +
                        "Silver Ⅳ → " + server.getEmotesByName("s4",true).get(0).getAsMention() +  "\n" +
                        "Silver Elite → " + server.getEmotesByName("se",true).get(0).getAsMention() +  "\n" +
                        "Silver Elite Master → " + server.getEmotesByName("sem",true).get(0).getAsMention() +  "\n" +
                        "Gold Nova Ⅰ → " + server.getEmotesByName("gn1",true).get(0).getAsMention() +  "\n" +
                        "Gold Nova Ⅱ → " + server.getEmotesByName("gn2",true).get(0).getAsMention() +  "\n" +
                        "Gold Nova Ⅲ → " + server.getEmotesByName("gn3",true).get(0).getAsMention() +  "\n" +
                        "Gold Nova Ⅳ → " + server.getEmotesByName("gn4",true).get(0).getAsMention() +  "\n" +
                        "Master Guardian Ⅰ → " + server.getEmotesByName("mg",true).get(0).getAsMention() +  "\n" +
                        "Master Guardian Ⅱ → " + server.getEmotesByName("mg2",true).get(0).getAsMention() +  "\n" +
                        "Master Guardian Elite → " + server.getEmotesByName("mge",true).get(0).getAsMention() +  "\n" +
                        "Distinguished Master Guardian → " + server.getEmotesByName("dmg",true).get(0).getAsMention() +  "\n" +
                        "Legendary Eagle → :le: " + server.getEmotesByName("le",true).get(0).getAsMention() +  "\n" +
                        "Legendary Eagle Master → :lem: " + server.getEmotesByName("lem",true).get(0).getAsMention() +  "\n" +
                        "Supreme Master First Class → :supreme: " + server.getEmotesByName("supreme",true).get(0).getAsMention() +  "\n" +
                        "Global Elite → " + server.getEmotesByName("global",true).get(0).getAsMention()).queue(message1 -> {
                    int i = 0;
                    message1.addReaction(server.getEmotesByName("unranked", true).get(0)).queue();
                    while (SearchingForMatchmakingListener.compareEmojiToRole.keySet().toArray().length > i) {
                        message1.addReaction(server.getEmotesByName(SearchingForMatchmakingListener.compareEmojiToRole.keySet().toArray()[i].toString(), true).get(0)).queue();
                        i++;
                    }
                });
                txtChannel.sendMessage("Wenn du dir den Prime Status in CS:GO gekauft hast, wähle den grünen Hacken. Wenn nicht dann das rote Kreuz.\n" +
                        "Prime  →   :white_check_mark:\n" +
                        "none prime  →   :x:").queue(message1 -> {
                            message1.addReaction(server.getEmotesByName("hackengrn",true).get(0)).queue();
                            message1.addReaction("U+274C").queue();
                        });
            }
        } else if (messageContent.split("\\s++")[0].equalsIgnoreCase("&say")) {
            String[] splitMessage = messageContent.split("\\s++");
            if(splitMessage.length == 2) {
                if(!message.getMentionedChannels().isEmpty()) {
                    message.getMentionedChannels().get(0).sendMessage(splitMessage[1]).queue();
                }
            }
        } else if (messageContent.equalsIgnoreCase("&testfeature")) {
            List<Permission> deny = Collections.singletonList(Permission.VIEW_CHANNEL);
            server.createTextChannel("Introduction for " + member.getEffectiveName()).addRolePermissionOverride(514511396491231233L, null, deny).addMemberPermissionOverride(499207441456824322L, deny, null).queue((chan) -> {
                utils.GreenBuilder("Welcome", "Um den Server freizuschalten, gib dir deine Wingman und Matchmaking Rollen in #csgo_roles.Schicke ansonsten bitte noch dein Freundescode hier rein, damit dich andere einfach adden können. Dies ist nicht notwendig, aber empfohlen. ", member, chan, false, 0);
                utils.GreenBuilder("Info", "Wenn du Fragen hast, kannst du gerne in den support voice channel joinen.", member, chan, false, 0);
                utils.RedBuilder("Info", "Du kannst auch später deinen Freundescode setzten mit &setfriendcode. Wenn du alles hier gelesen und verstanden hast, antworte mit *DEINEM FREUNDESCODE* oder mit *SPÄTER*", member, chan, false, 0);
            });

        }
    }
}

