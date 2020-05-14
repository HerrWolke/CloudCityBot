package de.mrcloud.listeners;

import de.mrcloud.SQL.SqlMain;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class GuildJoinListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent e) {
        super.onGuildMemberJoin(e);

        Guild server = e.getGuild();
        Member member = e.getMember();
        SqlMain sql = new SqlMain();
        Statement statement = null;

        System.out.println(member.getTimeJoined());
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
        String formated = member.getTimeJoined().format(format);

        try {
            statement = Objects.requireNonNull(sql.mariaDB()).createStatement();

            statement.executeQuery("INSERT into Users(userName,dateJoined,userID)" + "\n" + "VALUES(" + member.getUser().getName() + "," + formated  + "," + member.getId() + ");");
        } catch (SQLException e1) {
            e1.printStackTrace();
            System.err.println("An SQL Error");
            System.out.println(e1.getLocalizedMessage());
            System.err.println("------------");
        }
    }
}
