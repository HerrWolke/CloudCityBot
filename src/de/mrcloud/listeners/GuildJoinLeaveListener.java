package de.mrcloud.listeners;

import de.mrcloud.SQL.SqlMain;
import de.mrcloud.utils.JDAUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GuildJoinLeaveListener extends ListenerAdapter {
    JDAUtils utils = new JDAUtils();
    public static HashMap<User, String> hasLeftOurServer = new HashMap<>();

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent e) {
        super.onGuildMemberJoin(e);

        //Variables
        Guild server = e.getGuild();
        Member member = e.getMember();
        SqlMain sql = new SqlMain();
        Statement statement = null;
        //-------

        List<Permission> deny = Collections.singletonList(Permission.VIEW_CHANNEL);


        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
        String formated = member.getTimeJoined().format(format);

        Category category = server.getCategoryById("717017605575278673");

        try {
            statement = Objects.requireNonNull(SqlMain.mariaDB()).createStatement();


            ResultSet resultSetCheck = statement.executeQuery("SELECT * FROM Users WHERE userID = " + member.getUser().getId() + ";");

            if (!resultSetCheck.next()) {
                statement.executeQuery("INSERT INTO Users(UserName,dateJoined,UserID)" + "\n" + "VALUES('" + member.getUser().getName() + "','" + formated + "'," + member.getId() + ");");
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
            System.err.println("An SQL Error");
            System.out.println(e1.getLocalizedMessage());
            System.err.println("------------");
        } finally {
            try {
                SqlMain.mariaDB().close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        assert category != null;
        category.createTextChannel("Introduction for " + member.getEffectiveName()).addRolePermissionOverride(514511396491231233L, null, deny).addMemberPermissionOverride(member.getIdLong(), deny, null).queue((chan) -> {
            utils.GreenBuilder("Welcome", "Um den Server freizuschalten, gib dir deine Wingman und Matchmaking Rollen in #csgo_roles.Schicke ansonsten bitte noch dein Freundescode hier rein, damit dich andere einfach adden können. Dies ist nicht notwendig, aber empfohlen. ", member, chan, false, 0);
            utils.GreenBuilder("Info", "Wenn du Fragen hast, kannst du gerne in den support voice channel joinen.", member, chan, false, 0);
            utils.RedBuilder("Info", "Du kannst auch später deinen Freundescode setzten mit &setfriendcode. Wenn du alles hier gelesen und verstanden hast, antworte mit *DEINEM FREUNDESCODE* oder mit *SPÄTER*", member, chan, false, 0);
        });


        if (member.getId().equals("424203652442488832")) {
            server.addRoleToMember(member, server.getRolesByName("Admin 🌹", true).get(0)).queue();
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        @Nonnull
        Member member = Objects.requireNonNull(e.getMember());

        if (e.getChannel().getName().equalsIgnoreCase("introduction-for-" + e.getAuthor().getName())) {
            if (!e.getAuthor().isBot()) {
                //compares the text with a regex
                if (e.getMessage().getContentRaw().matches("\\w{5}-\\w{4}")) {
                    utils.GreenBuilder("Success", "Your friend code has been set to " + e.getMessage().getContentRaw(), member, e.getChannel(), false, 0);
                    try {
                        Objects.requireNonNull(SqlMain.mariaDB()).createStatement().executeQuery("UPDATE Users SET FriendCode = '" + e.getMessage().getContentRaw() + "' WHERE UserID = " + e.getMember().getId() + ";");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    e.getChannel().delete().queueAfter(20, TimeUnit.SECONDS);
                } else if(e.getMessage().getContentRaw().equalsIgnoreCase("später")) {
                    e.getChannel().sendMessage("Ok, du kannst jederzeit deinen Friendcode mit &setfriendcode setzen").queue();
                    e.getChannel().delete().queueAfter(20, TimeUnit.SECONDS);
                }

                else {
                    utils.YellowBuilder("Usage Help", "Your provided code is not a valid friend code", member, e.getChannel(), true, 15);
                }
            }

            //Checks if the channel name is split by spaces and replaces them with the discorrd -
        } else if (e.getChannel().getName().equalsIgnoreCase("introduction-for-" + e.getAuthor().getName().replaceAll("\\s++", "-"))) {
            if (!e.getAuthor().isBot()) {
                //compares the text with a regex
                if (e.getMessage().getContentRaw().matches("\\w{5}-\\w{4}")) {
                    utils.GreenBuilder("Success", "Your friend code has been set to " + e.getMessage().getContentRaw(), member, e.getChannel(), false, 0);
                    try {
                        Objects.requireNonNull(SqlMain.mariaDB()).createStatement().executeQuery("UPDATE Users SET FriendCode '= " + e.getMessage().getContentRaw() + "' WHERE UserID = " + e.getMember().getId() + ";");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    e.getChannel().delete().queue();
                }  else if(e.getMessage().getContentRaw().equalsIgnoreCase("später")) {
                    e.getChannel().sendMessage("Ok, du kannst jederzeit deinen Friendcode mit &setfriendcode setzen").queue();
                    e.getChannel().delete().queueAfter(20, TimeUnit.SECONDS);
                }

                else {
                    utils.YellowBuilder("Usage Help", "Your provided code is not a valid friend code", member, e.getChannel(), true, 15);
                }
            }
        }
    }

    @Override
    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent e) {
        super.onGuildMemberRemove(e);

        User user = e.getUser();
        user.openPrivateChannel().queue(privateChannel -> utils.PrivateBlackBuilder("Hi", "Ich habe bemerkt dass du unseren Discord verlassen hast. Hattest du ein problem mit einer Person oder generell mit dem Server? Du kannst gerner mit Feedback hierauf antworten.",user, privateChannel, false, 0));
    }

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent e) {
        super.onPrivateMessageReceived(e);
        Message message = e.getMessage();
        String messageContent = message.getContentRaw();
        PrivateChannel privateChannel = e.getChannel();
        User user = e.getAuthor();



    }
}

