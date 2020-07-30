package de.mrcloud.utils;

import de.mrcloud.SQL.SqlMain;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class JDAUtils {


    public static boolean hasRole(String roleName, Member member, Guild server) {
        boolean hasRole = false;
        int i = 0;

        while (member.getRoles().size() > i) {
            List<Role> roles = member.getRoles();
            if (roles.get(i).getName().equals("roleName")) {
                hasRole = true;
            }
            i++;
        }

        return hasRole;
    }

    /**
     *
     * @param Title Title of the Message
     * @param ErrorText The content of the message body
     * @param member The member who should be used for {@link EmbedBuilder#setAuthor(String, String, String)}
     * @param txtChannel Chooses the channel the message should be send to
     * @param delete Boolean if the message should be deleted
     * @param deleteAfter After how many seconds the message should be deleted
     */
    public static void RedBuilder(String Title, String ErrorText, Member member, TextChannel txtChannel, boolean delete, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#d63031"));
        embBuilder.setDescription(ErrorText);
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(deleteAfter, TimeUnit.SECONDS);
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }

    }

    /**
     *
     * @param Title Title of the Message
     * @param Text The content of the message body
     * @param member The member who should be used for {@link EmbedBuilder#setAuthor(String, String, String)}
     * @param txtChannel Chooses the channel the message should be send to
     * @param delete Boolean if the message should be deleted
     * @param deleteAfter After how many seconds the message should be deleted
     */
    public static void BlackBuilder(String Title, String Text, Member member, TextChannel txtChannel, boolean delete, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#1e272e"));
        embBuilder.setDescription(Text);
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(deleteAfter, TimeUnit.SECONDS);
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }

    }

    //Open a private channel with a user and sends them a message
    /**
     *
     * @param Title Title of the Message
     * @param Text The content of the message body
     * @param user The member who should be used for {@link EmbedBuilder#setAuthor(String, String, String)} and the person the message is send to {@link User#openPrivateChannel()}
     * @param delete Boolean if the message should be deleted
     * @param deleteAfter After how many seconds the message should be deleted
     */
    public static void PrivateBlackBuilder(String Title, String Text, User user, boolean delete, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
        embBuilder.setColor(Color.decode("#1e272e"));
        embBuilder.setDescription(Text);
        user.openPrivateChannel().queue(PrivateChannel -> {
            PrivateChannel.sendMessage(embBuilder.build()).queue();
        });

    }
    /**
     *
     * @param Title Title of the Message
     * @param InfoText The content of the message body
     * @param member The member who should be used for {@link EmbedBuilder#setAuthor(String, String, String)}
     * @param txtChannel Chooses the channel the message should be send to
     * @param delete Boolean if the message should be deleted
     * @param deleteAfter After how many seconds the message should be deleted
     */
    public static void YellowBuilder(String Title, String InfoText, Member member, TextChannel txtChannel, boolean delete, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#f9ca24"));
        embBuilder.setDescription(InfoText);
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(deleteAfter, TimeUnit.SECONDS);
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }

    }
    public static void UsageHelp( String InfoText, Member member, TextChannel txtChannel) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle("Usage Help");
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#f9ca24"));
        embBuilder.setDescription(InfoText);
        txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);


    }

    public static void GreenBuilder(String Title, String InfoText, Member member, TextChannel txtChannel, boolean delete, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#2ecc71"));
        embBuilder.setDescription(InfoText);
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(deleteAfter, TimeUnit.SECONDS);
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }

    }

    public static void BlueBuilder(String Title, String InfoText, TextChannel txtChannel, Member member, boolean delete, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#00a8ff"));
        embBuilder.setDescription(InfoText);
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(deleteAfter, TimeUnit.SECONDS);
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }

    }


    public static void Generell(Member member, TextChannel txtChannel, String MessageColor, String Title, String Desc, boolean delete, int toDeleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode(MessageColor));
        embBuilder.setDescription(Desc);
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(toDeleteAfter, TimeUnit.SECONDS);
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }

    }

    public static void PrivGenerell(Member member, String MessageColor, String Title, String Desc) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode(MessageColor));
        embBuilder.setDescription(Desc);
        member.getUser().openPrivateChannel().queue(PrivateChannel -> {
            PrivateChannel.sendMessage(embBuilder.build()).queue();
        });

    }

    public static void downloaded(GuildMessageReceivedEvent e, TextChannel txtChannel) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle("Success");
        embBuilder.setAuthor(e.getAuthor().getName() + " uploaded a File", e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
        embBuilder.setColor(Color.decode("#2ecc71"));
        embBuilder.setDescription("Downloaded File " + e.getMessage().getAttachments().get(0).getFileName());
        txtChannel.sendMessage(embBuilder.build()).complete();
    }

    public static List<Member> getMembersWithRole(Guild server, String roleName) {
        return server.getMembersWithRoles(server.getRolesByName(roleName, true).get(0));
    }

    public static void addRoleToMember(Guild server, Member meber, String roleName) {
        server.addRoleToMember(meber, server.getRolesByName(roleName, true).get(0)).queue();
    }

    public static String getSqlCollumString(Connection connection, String collumName, Member member) {
        String toGet = "";
        try {
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Users WHERE UserID = " + member.getId() + ";");
            while (result.next()) {
                toGet = result.getString(collumName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        } finally {
            try {
                SqlMain.mariaDB().close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return toGet;
    }
    public static int getSqlCollumInt(Connection connection, String collumName, Member member) {
        int toGet = 0;
        try {
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Users WHERE UserID = " + member.getId() + ";");
            while (result.next()) {
                toGet = result.getInt(collumName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }
        return toGet;
    }
    public static void setSQLCollum(Connection connection,String ID,String collumName, String toFillWith) {
        try {
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Users WHERE UserID = " + ID + ";");
            if(result != null && result.next()) {
                statement.executeQuery("UPDATE Users SET " + collumName + " = '" + toFillWith + "' WHERE UserID = " + ID + ";");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * WARNING: Requieres a database with a collum for userID.
     * @exception SQLException May cause a {@link SQLException} otherwise
     *
     *
     * @param connection The database connecting from which the data should be written to
     * @param ID The ID of the user that it should be written to
     * @param collumName The name of the collum that should be replaced with the int
     * @param toFillWith The value that should be written
     *
     */
    public static void setSQLCollumInt(Connection connection,String ID,String collumName, int toFillWith) {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM Users WHERE UserID = " + ID + ";");
            if(result != null && result.next()) {
                statement.executeQuery("UPDATE Users SET " + collumName + " = '" + toFillWith + "' WHERE UserID = " + ID + ";");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                SqlMain.mariaDB().close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * WARNING: Requieres a database with a collum for userID.
     * @exception SQLException May cause a {@link SQLException} otherwise
     *
     *
     * @param connection The database connecting from which the data should be written to
     * @param ID The ID of the user that it should be written to
     * @param collumName The name of the collum that should be replaced with the int
     * @param toFillWith The value that should be written
     */
    public static void setSQLCollumBoolean(Connection connection,String ID,String collumName, boolean toFillWith) {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM Users WHERE UserID = " + ID + ";");
            if(result != null && result.next()) {
                statement.executeQuery("UPDATE Users SET " + collumName + " = " + toFillWith + " WHERE UserID = " + ID + ";");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                SqlMain.mariaDB().close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void addRoleToMemberByString(Member member, Guild server, String RoleName, int whichRoleToGet, boolean ignoreCase) {
        server.addRoleToMember(member, server.getRolesByName(RoleName, ignoreCase).get(whichRoleToGet)).queue();
    }

    public static void removeRoleFromMemberByString(Member member, Guild server, String RoleName, int whichRoleToGet, boolean ignoreCase) {
        server.removeRoleFromMember(member, server.getRolesByName(RoleName, ignoreCase).get(whichRoleToGet)).queue();
    }

    /**
     *
     * @param server The server on which this action should be performed
     * @param toAdd The role that should be added to all members
     */
    public static void addRoleToAllMembers(Guild server,Role toAdd) {
        List<Member> members = server.getMembers();
        int i = 0;
        while(members.size() > i) {
            server.addRoleToMember(members.get(i),toAdd).queue();
        }
    }

    //Gets all messages in a channel
    public static List<Message> getChannelMessages(TextChannel textChannel, int limit) {
        return textChannel.getIterableHistory().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

}

