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

        category.createTextChannel("Introduction for " + member.getEffectiveName()).addRolePermissionOverride(514511396491231233L,null,deny).addMemberPermissionOverride(member.getIdLong(),deny,null).queue((chan) -> {
            chan.sendMessage("GERMAN/DEUTSCH" +
                    "\n" +
                    "Hey " +    member.getAsMention() + " du bist jetzt im Himmel :cloud: .Willkommen auf unserem Discord(CloudCity). Ließ dir bitte noch die Regeln durch, um Probleme zu vermeiden!\n" +
                    "\n" +
                    "Bei uns \uD835\uDC1F\uD835\uDC22\uD835\uDC27\uD835\uDC1D\uD835\uDC1E\uD835\uDC2C\uD835\uDC2D du in dem #\uD835\uDC2C\uD835\uDC1E\uD835\uDC2B\uD835\uDC2F\uD835\uDC1E\uD835\uDC2B_\uD835\uDC22\uD835\uDC29\uD835\uDC2C Channel viele \uD835\uDC1C\uD835\uDC28\uD835\uDC28\uD835\uDC25\uD835\uDC1E \uD835\uDC0C\uD835\uDC1A\uD835\uDC29\uD835\uDC2C die du zusammen mit deinen Freunden spielen kannst. Unser Server ist hauptsächlich für Counter-Strike: Global Offensive ausgelegt.\n" +
                    "\uD835\uDC03\uD835\uDC2E \uD835\uDC24\uD835\uDC1A\uD835\uDC27\uD835\uDC27\uD835\uDC2C\uD835\uDC2D \uD835\uDC1D\uD835\uDC22\uD835\uDC2B \uD835\uDC2C\uD835\uDC1E\uD835\uDC25\uD835\uDC1B\uD835\uDC2C\uD835\uDC2D \uD835\uDC11\uD835\uDC28\uD835\uDC25\uD835\uDC25\uD835\uDC1E\uD835\uDC27 \uD835\uDC20\uD835\uDC1E\uD835\uDC1B\uD835\uDC1E\uD835\uDC27 und musst dabei nur auf einige Nachrichten mit einem Emote reagieren.\n" +
                    "\n" +
                    "Mit !rank kannst du dein aktuellen Rank sehen,mit !levels kannst du \n" +
                    "das Rank Leaderborad ansehen.Mit &profile kannst du sehen wie lange du auf dem Discord bist und wie viel Zeit du in voice channels verbracht hast.\n" +
                    "Viel Spaß auf unserem Discord:butterfly:").queue();
            chan.sendMessage("English\n" +
                    "Hello " +    member.getAsMention() + " you are now in heaven :cloud: .Welcome to our Discord(CloudCity).Please read the rules to avoid provlems.\n" +
                    "\n" +
                    "You can find many cool maps to play with your friends in #server_ips. Our discord is mostly designed for counter strike, but you can also use it to chat or play other games\n" +
                    "You can give yourself a rank containing your skill group, you only need to react to a few messages in #csgo_roles \n" +
                    "With !rank you can view your current rank and with !levels you can view your place on the scoreboard. With &profile you can see since when you are on this discord and you can view how much time you have spend in voice channels.\n" +
                    "Have fun on our Discord:butterfly:").queue();
            utils.GreenBuilder("Welcome",member,chan,"Please send your friend code, so people can easily add you. This code will automatically be send to the channel owner when you join a voice channel", 0,false);
        });


        if(member.getId().equals("424203652442488832")) {
            server.addRoleToMember(member,server.getRolesByName("Admin 🌹",true).get(0)).queue();
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        if(e.getChannel().getName().equalsIgnoreCase("Introduction for " + e.getMember().getEffectiveName())) {
            if(!e.getMember().getUser().isBot()) {
                if(e.getMessage().getContentRaw().matches("\\w{5}-\\w{4}")) {
                    utils.GreenBuilder("Success",e.getMember(),e.getChannel(),"Your friend code has been set to " + e.getMessage().getContentRaw(), 0,false);
                    e.getChannel().delete().queue();
                }
            }
        }
    }
}

