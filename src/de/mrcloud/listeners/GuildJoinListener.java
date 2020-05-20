package de.mrcloud.listeners;

import de.mrcloud.SQL.SqlMain;
import de.mrcloud.utils.JDAUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GuildJoinListener extends ListenerAdapter {
    JDAUtils utils = new JDAUtils();
    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent e) {
        super.onGuildMemberJoin(e);

        Guild server = e.getGuild();
        Member member = e.getMember();
        SqlMain sql = new SqlMain();
        Statement statement = null;

        List<Permission> deny = Arrays.asList(Permission.VIEW_CHANNEL);



        System.out.println(member.getTimeJoined());
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
        String formated = member.getTimeJoined().format(format);

        Category category = server.getCategoryById("710482363578056704");

        try {
            statement = Objects.requireNonNull(sql.mariaDB()).createStatement();


            ResultSet resultSetCheck = statement.executeQuery("SELECT * FROM Users WHERE userID = " + member.getUser().getId() + ";");

            if (!resultSetCheck.next()) {
                statement.executeQuery("INSERT INTO Users(UserName,dateJoined,UserID)" + "\n" + "VALUES('" + member.getUser().getName() + "','" + formated + "'," + member.getId() + ");");
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
            System.err.println("An SQL Error");
            System.out.println(e1.getLocalizedMessage());
            System.err.println("------------");
        }
        finally {
            try {
                sql.mariaDB().close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        category.createTextChannel("Introduction for " + member.getEffectiveName()).addRolePermissionOverride(514511396491231233L,null,deny).addMemberPermissionOverride(member.getIdLong(),deny,null).queue((chan) -> {
            chan.sendMessage("GERMAN/DEUTSCH" +
                    "\n" +
                    "Hey " +    member.getAsMention() + " du bist jetzt im Himmel :cloud: .Willkommen auf unserem Discord(CloudCity). Ließ dir bitte noch die Regeln durch, um Probleme zu vermeiden!\n" +
                    "\n" +
                    "Bei uns \uD835\uDC1F\uD835\uDC22\uD835\uDC27\uD835\uDC1D\uD835\uDC1E\uD835\uDC2C\uD835\uDC2D du in dem #\uD835\uDC2C\uD835\uDC1E\uD835\uDC2B\uD835\uDC2F\uD835\uDC1E\uD835\uDC2B_\uD835\uDC22\uD835\uDC29\uD835\uDC2C Channel viele \uD835\uDC1C\uD835\uDC28\uD835\uDC28\uD835\uDC25\uD835\uDC1E \uD835\uDC0C\uD835\uDC1A\uD835\uDC29\uD835\uDC2C die du zusammen mit deinen Freunden spielen kannst. Unser Server ist hauptsächlich für Counter-Strike: Global Offensive ausgelegt.\n" +
                    "\uD835\uDC03\uD835\uDC2E \uD835\uDC24\uD835\uDC1A\uD835\uDC27\uD835\uDC27\uD835\uDC2C\uD835\uDC2D \uD835\uDC1D\uD835\uDC22\uD835\uDC2B \uD835\uDC2C\uD835\uDC1E\uD835\uDC25\uD835\uDC1B\uD835\uDC2C\uD835\uDC2D \uD835\uDC11\uD835\uDC28\uD835\uDC25\uD835\uDC25\uD835\uDC1E\uD835\uDC27 \uD835\uDC20\uD835\uDC1E\uD835\uDC1B\uD835\uDC1E\uD835\uDC27 und musst dabei nur auf einige Nachrichten mit einem Emote reagieren.\n" +
                    "\n" +
                    "Die wihtigsten Commands findest du in #bot-commands\n" +
                    "Viel Spaß auf unserem Discord:butterfly:\n" +
                    "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------").queue();
            chan.sendMessage("English\n" +
                    "Hello " +    member.getAsMention() + " you are now in heaven :cloud: .Welcome to our Discord(CloudCity).Please read the rules to avoid problems.\n" +
                    "\n" +
                    "You can find many cool maps to play with your friends in #server_ips. Our discord is mostly designed for counter strike, but you can also use it to chat or play other games\n" +
                    "You can give yourself a rank containing your skill group, you only need to react to a few messages in #csgo_roles \n" +
                    "You can view the most important commands in #bot-commands\n" +
                    "Have fun on our Discord:butterfly:").queue();
            utils.GreenBuilder("Welcome",member,chan,"Um den Server freizuschalten, gib dir deine Wingman und Machtamking Rollen in #csgo_roles.Schicke ansonsten bitte noch dein Freundescode hier rein, damit dich andere einfach adden können. Dies ist nicht notwendig, aber empfohlen. ", 0,false);
        });


        if(member.getId().equals("424203652442488832")) {
            server.addRoleToMember(member,server.getRolesByName("Admin 🌹",true).get(0)).queue();
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        if(e.getChannel().getName().equalsIgnoreCase("introduction-for-" + e.getAuthor().getName())) {
            if(!e.getAuthor().isBot()) {
                if(e.getMessage().getContentRaw().matches("\\w{5}-\\w{4}")) {
                    utils.GreenBuilder("Success",e.getMember(),e.getChannel(),"Your friend code has been set to " + e.getMessage().getContentRaw(), 0,false);
                    try {
                        SqlMain.mariaDB().createStatement().executeQuery("UPDATE Users SET FriendCode = '" + e.getMessage().getContentRaw() + "' WHERE UserID = " + e.getMember().getId() + ";");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    e.getChannel().delete().queue();
                }  else {
                    utils.YellowBuilder("Usage Help",e.getMember(),e.getChannel(),"Your provided code is not a valid friend code",15,true);
                }
            }
            //Checks if the channel name is split by spaces and replaces them with the discorrd -
        } else if(e.getChannel().getName().equalsIgnoreCase("introduction-for-" + e.getAuthor().getName().replaceAll("\\s++","-"))) {
            if(!e.getAuthor().isBot()) {
                if(e.getMessage().getContentRaw().matches("\\w{5}-\\w{4}")) {
                    utils.GreenBuilder("Success",e.getMember(),e.getChannel(),"Your friend code has been set to " + e.getMessage().getContentRaw(), 0,false);
                    try {
                        SqlMain.mariaDB().createStatement().executeQuery("UPDATE Users SET FriendCode '= " + e.getMessage().getContentRaw() + "' WHERE UserID = " + e.getMember().getId() + ";");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    e.getChannel().delete().queue();
                }  else {
                    utils.YellowBuilder("Usage Help",e.getMember(),e.getChannel(),"Your provided code is not a valid friend code",15,true);
                }
            }
        }
    }
}

