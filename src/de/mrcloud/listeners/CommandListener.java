package de.mrcloud.listeners;

import de.mrcloud.SQL.SqlMain;
import de.mrcloud.utils.Static;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
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

        SqlMain sql = new SqlMain();
        Statement statement = null;


        if (messageContent.equalsIgnoreCase("&reg")) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
            String formated = member.getTimeJoined().format(format);

            System.out.println(member.getTimeJoined());
            try {
                statement = Objects.requireNonNull(sql.mariaDB()).createStatement();
                ResultSet resultSetCheck = statement.executeQuery("SELECT * FROM Users WHERE userID = " + member.getUser().getId() + ";");

                if (!resultSetCheck.next()) {
                    statement.executeQuery("INSERT into Users(userName,dateJoined,userID)" + "\n" + "VALUES('" + member.getUser().getName() + "','" + formated + "'," + member.getId() + ");");
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
                e1.getLocalizedMessage();
            }
        }
        if (messageContent.equalsIgnoreCase("&version")) {
            txtChannel.sendMessage("Die Version des Bots ist " + Static.VERSION).queue();
        }
    }
}
