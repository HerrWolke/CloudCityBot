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

public class JDAUtils {
    public boolean hasRole(String roleName, Member member, Guild server) {
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


    public void RedBuilder(String Title, String ErrorText, Member member, TextChannel txtChannel, boolean delete, int deleteAfter) {
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

    public void BlackBuilder(String Title, String Text, Member member, TextChannel txtChannel, boolean delete, int deleteAfter) {
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

    public void PrivateBlackBuilder(String Title, String Text, User user, PrivateChannel txtChannel, boolean delete, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
        embBuilder.setColor(Color.decode("#1e272e"));
        embBuilder.setDescription(Text);
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(deleteAfter, TimeUnit.SECONDS);
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }

    }

    public void YellowBuilder(String Title, String InfoText, Member member, TextChannel txtChannel, boolean delete, int deleteAfter) {
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
    public void UsageHelp( String InfoText, Member member, TextChannel txtChannel) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle("Usage Help");
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#f9ca24"));
        embBuilder.setDescription(InfoText);
            txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);


    }

    public void GreenBuilder(String Title, String InfoText, Member member, TextChannel txtChannel, boolean delete, int deleteAfter) {
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

    public void BlueBuilder(String Title, String InfoText, TextChannel txtChannel, Member member, boolean delete, int deleteAfter) {
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


    public void Generell(Member member, TextChannel txtChannel, String MessageColor, String Title, String Desc, boolean delete, int toDeleteAfter) {
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

    public void PrivGenerell(Member member, PrivateChannel txtChannel, String MessageColor, String Title, String Desc, boolean delete, int toDeleteAfter) {
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

    public void downloaded(GuildMessageReceivedEvent e, TextChannel txtChannel) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle("Success");
        embBuilder.setAuthor(e.getAuthor().getName() + " uploaded a File", e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
        embBuilder.setColor(Color.decode("#2ecc71"));
        embBuilder.setDescription("Downloaded File " + e.getMessage().getAttachments().get(0).getFileName());
        txtChannel.sendMessage(embBuilder.build()).complete();
    }

    public List<Member> getMembersWithRole(Guild server, String roleName) {
        return server.getMembersWithRoles(server.getRolesByName(roleName, true).get(0));
    }

    public void addRoleToMember(Guild server, Member meber, String roleName) {
        server.addRoleToMember(meber, server.getRolesByName(roleName, true).get(0)).queue();
    }

    public String getSqlCollumString(Connection connection, String collumName, Member member) {
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
    public int getSqlCollumInt(Connection connection, String collumName, Member member) {
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
    public void setSQLCollum(Connection connection,String ID,String collumName, String toFillWith) {
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
    public void setSQLCollumInt(Connection connection,String ID,String collumName, int toFillWith) {
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

    public void addRoleToMemberByString(Member member, Guild server, String RoleName, int whichRoleToGet, boolean ignoreCase) {
        server.addRoleToMember(member, server.getRolesByName(RoleName, ignoreCase).get(whichRoleToGet)).queue();
    }
    public void removeRoleFromMemberByString(Member member, Guild server, String RoleName, int whichRoleToGet, boolean ignoreCase) {
        server.removeRoleFromMember(member, server.getRolesByName(RoleName, ignoreCase).get(whichRoleToGet)).queue();
    } public void addRoleToAllMembers(Guild server,Role toAdd) {
        List<Member> members = server.getMembers();
        int i = 0;
        while(members.size() > i) {
            server.addRoleToMember(members.get(i),toAdd).queue();
        }
    }
}

