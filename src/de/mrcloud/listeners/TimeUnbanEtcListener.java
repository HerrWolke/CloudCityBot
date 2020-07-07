package de.mrcloud.listeners;

import de.mrcloud.listeners.moderation.ModCommands;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class TimeUnbanEtcListener extends ListenerAdapter {
    @Override
    public void onGenericGuild(@Nonnull GenericGuildEvent e) {
        super.onGenericGuild(e);
        Date toUnmuteWhen = new Date();

        //Formats current time in a normal format
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
        ZonedDateTime hereAndNow = ZonedDateTime.now();
        String testDate = dateTimeFormatter.format(hereAndNow);
        String stopDate = testDate.replaceAll(",", "");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        //---------------------------------------

        try {
            toUnmuteWhen = format.parse(stopDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        Date finalToUnmuteWhen = toUnmuteWhen;
        ModCommands.muted.forEach((member, date) -> {
            if(date.before(finalToUnmuteWhen)) {
                Guild server = e.getGuild();
                server.removeRoleFromMember(member,server.getRoleById("617058794899374119")).queue();
            }
        });
    }
}
