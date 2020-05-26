package de.mrcloud.listeners;

import de.mrcloud.SQL.SqlMain;
import de.mrcloud.utils.JDAUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
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

        List<Permission> deny = Collections.singletonList(Permission.VIEW_CHANNEL);


        System.out.println(member.getTimeJoined());
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
        String formated = member.getTimeJoined().format(format);

        Category category = server.getCategoryById("710482363578056704");

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
            utils.GreenBuilder("Welcome", member, chan, "Um den Server freizuschalten, gib dir deine Wingman und Matchmaking Rollen in #csgo_roles.Schicke ansonsten bitte noch dein Freundescode hier rein, damit dich andere einfach adden können. Dies ist nicht notwendig, aber empfohlen. ", 0, false);
            utils.GreenBuilder("Info", member, chan, "Wenn du Fragen hast, kannst du gerne in den support voice channel joinen.", 0, false);
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
                    utils.GreenBuilder("Success", member, e.getChannel(), "Your friend code has been set to " + e.getMessage().getContentRaw(), 0, false);
                    try {
                        Objects.requireNonNull(SqlMain.mariaDB()).createStatement().executeQuery("UPDATE Users SET FriendCode = '" + e.getMessage().getContentRaw() + "' WHERE UserID = " + e.getMember().getId() + ";");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    e.getChannel().delete().queue();
                } else {
                    utils.YellowBuilder("Usage Help", member, e.getChannel(), "Your provided code is not a valid friend code", 15, true);
                }
            }

            //Checks if the channel name is split by spaces and replaces them with the discorrd -
        } else if (e.getChannel().getName().equalsIgnoreCase("introduction-for-" + e.getAuthor().getName().replaceAll("\\s++", "-"))) {
            if (!e.getAuthor().isBot()) {
                //compares the text with a regex
                if (e.getMessage().getContentRaw().matches("\\w{5}-\\w{4}")) {
                    utils.GreenBuilder("Success", member, e.getChannel(), "Your friend code has been set to " + e.getMessage().getContentRaw(), 0, false);
                    try {
                        Objects.requireNonNull(SqlMain.mariaDB()).createStatement().executeQuery("UPDATE Users SET FriendCode '= " + e.getMessage().getContentRaw() + "' WHERE UserID = " + e.getMember().getId() + ";");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    e.getChannel().delete().queue();
                } else {
                    utils.YellowBuilder("Usage Help", e.getMember(), e.getChannel(), "Your provided code is not a valid friend code", 15, true);
                }
            }
        }
    }
}

