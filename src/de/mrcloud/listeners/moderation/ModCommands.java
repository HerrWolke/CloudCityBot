package de.mrcloud.listeners.moderation;

import de.mrcloud.utils.DataStorageClass;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ModCommands extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);
        if (!e.getChannel().getId().equals("617057983783895045") && !e.getMessage().isWebhookMessage() && !e.getMember().getUser().isBot()) {
            Guild server = e.getGuild();
            Member member = Objects.requireNonNull(e.getMember());
            Message message = e.getMessage();
            String messageContent = message.getContentRaw();
            String[] args = messageContent.split("\\s++");
            TextChannel txtChannel = e.getChannel();
            Date mutedUntil = new Date();
            Member toMute = message.getMentionedMembers().get(0);

            if (args[0].equalsIgnoreCase("&mute")) {
                if (DataStorageClass.moderators.contains(member)) {
                    if (message.getMentionedMembers().size() == 0) {
                        if (!DataStorageClass.muted.containsKey(message.getMentionedMembers().get(0))) {
                            Date bannedUntil = timeToBan(args);
                            DataStorageClass.muted.put(message.getMentionedMembers().get(0), bannedUntil);
                            JDAUtils.addRoleToMemberByString(message.getMentionedMembers().get(0), server, "Muted", 0, true);
                            if (bannedUntil.getYear() == 2100) {
                                JDAUtils.GreenBuilder("Erfolg", "Der Member " + message.getMentionedMembers().get(0).getEffectiveName() + " wurde permanent gemutet!", member, txtChannel, false, 0);
                            } else {
                                JDAUtils.GreenBuilder("Erfolg", "Der Member " + message.getMentionedMembers().get(0).getEffectiveName() + " wurde bis " + bannedUntil + " gemutet!", member, txtChannel, false, 0);
                            }


                        } else {
                            JDAUtils.RedBuilder("Error", "Dieser Member ist bereits gemutet", member, txtChannel, false, 2);
                        }
                    } else {
                        JDAUtils.RedBuilder("Error", "Bitte erwähne einen Member mit @", member, txtChannel, false, 2);
                    }

                } else {
                    JDAUtils.RedBuilder("Error", "Dazu hast du keine Rechte", member, txtChannel, false, 2);
                }

            } else if (args[0].equalsIgnoreCase("&unmute")) {
                if (DataStorageClass.moderators.contains(member)) {
                    if (message.getMentionedMembers().size() == 0) {
                        if (DataStorageClass.muted.containsKey(message.getMentionedMembers().get(0))) {
                            DataStorageClass.muted.remove(message.getMentionedMembers().get(0));
                            JDAUtils.GreenBuilder("Erfolg", "Der Member " + message.getMentionedMembers().get(0).getEffectiveName() + " wurde entmutet.", member, txtChannel, false, 0);
                        } else {
                            JDAUtils.RedBuilder("Error", "Dieser Member ist nicht gemutet", member, txtChannel, false, 2);
                        }
                    } else {
                        JDAUtils.RedBuilder("Error", "Bitte erwähne einen Member mit @", member, txtChannel, false, 2);
                    }
                } else {
                    JDAUtils.RedBuilder("Error", "Dazu hast du keine Rechte", member, txtChannel, false, 2);
                }
            } else if (args[0].equalsIgnoreCase("&clear")) {
                int i = 0;
                List<Message> toDelte = JDAUtils.getChannelMessages(txtChannel, Integer.parseInt(args[1]) + 1);
                while (toDelte.size() > i) {
                    toDelte.get(i).delete().complete();
                    i++;
                }
            }
        }
    }

    public Date timeToBan(String[] args) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
        ZonedDateTime hereAndNow = ZonedDateTime.now();
        String testDate = dateTimeFormatter.format(hereAndNow);
        String currentDate = testDate.replaceAll(",", "");
        SimpleDateFormat format3 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = null;

        try {
            date = format3.parse(currentDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        long timeToAddToDate = 0;

        int timeToBan = 0;
        if (args.length > 2) {
            for (String arg : args) {
                if (arg.matches("\\d*y")) {
                    timeToAddToDate += Long.parseLong(arg.replaceAll("[^\\d]", "")) * 525600;
                } else if (arg.matches("\\d*mo")) {
                    timeToAddToDate += Long.parseLong(arg.replaceAll("[^\\d]", "")) * 43800;
                } else if (arg.matches("\\d*w")) {
                    timeToAddToDate += Long.parseLong(arg.replaceAll("[^\\d]", "")) * 10080;
                } else if (arg.matches("\\d*d") || arg.matches("\\d*day")) {
                    timeToAddToDate += Long.parseLong(arg.replaceAll("[^\\d]", "")) * 1440;
                } else if (arg.matches("\\d*h")) {
                    timeToAddToDate += Long.parseLong(arg.replaceAll("[^\\d]", ""));
                } else if (arg.matches("\\d*m") || arg.matches("\\d*min")) {
                    timeToAddToDate += Long.parseLong(arg.replaceAll("[^\\d]", ""));
                } else if (arg.matches("\\d*s") || arg.matches("\\d*sec")) {
                    date.setSeconds(Integer.parseInt(arg.replaceAll("[^\\d]", "")) + date.getSeconds());
                }


                date.setMinutes(Math.toIntExact(timeToAddToDate) + date.getMinutes());
            }
        } else {
            date.setYear(2100);
        }


        return date;
    }
}
