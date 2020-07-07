package de.mrcloud.listeners.moderation;

import de.mrcloud.SQL.SqlMain;
import de.mrcloud.utils.JDAUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class ModCommands extends ListenerAdapter {
    public static HashMap<String,Integer> muteReasonsTime = new HashMap<>();
    public static List<String> muteReasonsList = new ArrayList<>();
    public static HashMap<Integer,Double> muteTime = new HashMap<>();
    public static HashMap<Member,Date> muted = new HashMap<>();

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);
        if (!e.getChannel().getId().equals("617057983783895045") && !e.getMessage().isWebhookMessage() && !e.getMember().getUser().isBot() ) {
            if (e.getMessage().getContentRaw().split("\\s++")[0].equalsIgnoreCase("&mute")) {
                if (!muted.containsKey(e.getMessage().getMentionedMembers().get(0))) {
                    Guild server = e.getGuild();
                    Member member = Objects.requireNonNull(e.getMember());
                    Message message = e.getMessage();
                    String messageContent = message.getContentRaw();
                    String[] messageContentSplit = messageContent.split("\\s++");
                    TextChannel txtChannel = e.getChannel();
                    Date mutedUntil = new Date();
                    JDAUtils utils = new JDAUtils();
                    Member toMute = message.getMentionedMembers().get(0);

                    //Formats current time in a normal format
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
                    ZonedDateTime hereAndNow = ZonedDateTime.now();
                    String testDate = dateTimeFormatter.format(hereAndNow);
                    String stopDate = testDate.replaceAll(",", "");
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    //---------------------------------------

                    try {
                        mutedUntil = format.parse(stopDate);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println(messageContentSplit[2]);
                    if (messageContentSplit[1].matches("(\\d*)h")) {
                        if (Integer.parseInt(messageContentSplit[1]) > 23) {
                        }
                    } else if (muteReasonsList.contains(messageContentSplit[2])) {
                        int timeToMuteFromList = muteReasonsTime.get(messageContentSplit[2]);
                        int timesHasBeenMuted = utils.getSqlCollumInt(SqlMain.mariaDB(), "MuteTimes", toMute);
                        int timeToBanFinal = (int) (muteTime.get(timesHasBeenMuted) * timeToMuteFromList);

                        mutedUntil.setMinutes(mutedUntil.getMinutes() + timeToBanFinal);
                        utils.addRoleToMember(server, toMute, "Muted");
                        server.mute(toMute,true).queue();
                        muted.put(toMute, mutedUntil);
                        utils.setSQLCollumInt(SqlMain.mariaDB(), toMute.getId(), "MuteTimes", (timesHasBeenMuted + 1));
                    }
                }
            }
        }
    }
}
