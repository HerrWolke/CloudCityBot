package de.mrcloud.listeners.csgo;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GuildJoinLeaveListener extends ListenerAdapter {
    public static HashMap<User, String> hasLeftOurServer = new HashMap<>();
    public static List<Permission> deny = Arrays.asList(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE);
    public static List<Permission> allow = Arrays.asList(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE);

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent e) {
        super.onGuildMemberJoin(e);

        //Variables
        Guild server = e.getGuild();
        Member member = e.getMember();
        SqlMain sql = new SqlMain();
        Statement statement = null;
        //-------


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

        // TODO: 29.07.2020 add a feature to choose language

        //Creates the channel
        assert category != null;
        category.createTextChannel("Introduction for " + member.getEffectiveName()).addMemberPermissionOverride(member.getIdLong(), allow, null).queue((chan) -> {
            chan.upsertPermissionOverride(server.getRoleById(514511396491231233L)).setPermissions(null, deny).queue();
            JDAUtils.GreenBuilder("Welcome", "Um den Server freizuschalten, gib dir deine Wingman und Matchmaking Rollen in #csgo_roles.Schicke ansonsten bitte noch dein Freundescode hier rein, damit dich andere einfach adden können. Dies ist nicht notwendig, aber empfohlen. ", member, chan, false, 0);
            JDAUtils.GreenBuilder("Info", "Wenn du Fragen hast, kannst du gerne in den support voice channel joinen.", member, chan, false, 0);
            JDAUtils.RedBuilder("Info", "Du kannst auch später deinen Freundescode setzten mit &setfriendcode. Wenn du alles hier gelesen und verstanden hast, antworte mit *DEINEM CSGO FREUNDESCODE * oder mit *SPÄTER*", member, chan, false, 0);
            chan.delete().queueAfter(5,TimeUnit.MINUTES);
        });
        //----------------


        //Gives the "Raindrop" role to the member after 20 in order to give time to read the infos in the introduction channel
        server.addRoleToMember(member, server.getRoleById("720304258137718835")).queueAfter(20, TimeUnit.SECONDS);

        if (member.getId().equals("424203652442488832")) {
            server.addRoleToMember(member, server.getRolesByName("Admin 🌹", true).get(0)).queue();
        }
    }

    @Override
    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent e) {
        super.onGuildMemberRemove(e);

        //Variables
        Guild server = e.getGuild();
        Member member = e.getMember();
        User user = e.getUser();
        //--------

        if(user.isBot()) return;
        JDAUtils.PrivateBlackBuilder("Hi","Ich habe bemerkt dass du den Server verlassen hast. Hat das irgendeinen besonderen Grund? Wenn ja, teile ihn uns gerne mit indem du hier in diesem Chat antwortest",user,false,0);
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        String shouldBe = "";
        Member member = null;
        member = e.getMember();

        //Checks that it is not a webhook message
        if (!e.getMessage().isWebhookMessage()) return;

        //Splits the channel name after - so it can ignore the name at the end of the introduction name channel
        String[] splitChannelName = e.getChannel().getName().split("-");
        if (splitChannelName.length > 2) {
            shouldBe = splitChannelName[0] + "-" + splitChannelName[1];
        }


        //Checks if someone sends sends his friend code to the introduction channel
        if (shouldBe.equals("introduction-for")) {
            if (!e.getAuthor().isBot()) {
                //compares the text with a regex
                if (e.getMessage().getContentRaw().matches("\\w{5}-\\w{4}")) {
                    JDAUtils.GreenBuilder("Success", "Your friend code has been set to " + e.getMessage().getContentRaw(), member, e.getChannel(), false, 0);
                    try {
                        Objects.requireNonNull(SqlMain.mariaDB()).createStatement().executeQuery("UPDATE Users SET FriendCode = '" + e.getMessage().getContentRaw() + "' WHERE UserID = " + e.getMember().getId() + ";");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    e.getChannel().delete().queueAfter(20, TimeUnit.SECONDS);
                } else if (e.getMessage().getContentRaw().equalsIgnoreCase("später")) {
                    e.getChannel().sendMessage("Ok, du kannst jederzeit deinen Friendcode mit &setfriendcode setzen").queue();
                    e.getChannel().delete().queueAfter(20, TimeUnit.SECONDS);
                } else {
                    JDAUtils.YellowBuilder("Usage Help", "Your provided code is not a valid friend code", member, e.getChannel(), true, 15);
                }
            }
        }
    }


    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent e) {
        super.onPrivateMessageReceived(e);
        Message message = e.getMessage();
        String messageContent = message.getContentRaw();
        PrivateChannel privateChannel = e.getChannel();
        User user = e.getAuthor();

        System.out.println( user.getAsTag());
        System.out.println("...");
    }
}

