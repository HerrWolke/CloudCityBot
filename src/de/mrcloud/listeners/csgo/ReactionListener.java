package de.mrcloud.listeners.csgo;

import de.mrcloud.utils.JDAUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

public class ReactionListener extends ListenerAdapter {
    public static HashMap<Member, Date> hasCalledSuppoort = new HashMap<>();
    public List<Permission> allow = Arrays.asList(Permission.VOICE_CONNECT);

    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent e) {
        super.onGuildMessageReactionAdd(e);
        Guild server = e.getGuild();
        TextChannel txtChannel = e.getChannel();
        Member member = e.getMember();
        MessageReaction.ReactionEmote reacEmote = e.getReactionEmote();
        JDAUtils utils = new JDAUtils();
        User user = e.getUser();
        String messageID = e.getMessageId();


        if (!e.getUser().isBot()) {
            if (txtChannel.getName().matches("channel-settings-\\d*"))
                //Aka w symbol / wingman channel
                if (reacEmote.getName().equals("\uD83C\uDDFC")) {
                    int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                    VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("Matchmaking " + channelNumber, true).get(0);
                    matchmakingChannel.getManager().setUserLimit(2).queue();
                    getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
                    //Aka m symbol / matchmaking channel
                } else if (reacEmote.getName().equals("\uD83C\uDDF2")) {
                    int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                    VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("Matchmaking " + channelNumber, true).get(0);
                    matchmakingChannel.getManager().setUserLimit(5).queue();
                    getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
                    //Aka green circle / open channel
                } else if (reacEmote.getName().equals("\uD83D\uDFE2")) {
                    int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                    VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("Matchmaking " + channelNumber, true).get(0);
                    matchmakingChannel.getManager().putPermissionOverride(server.getRoleById(514511396491231233L), allow, null).queue();
                    getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
                    //Aka red circle / close channel
                } else if (reacEmote.getName().equals("\uD83D\uDD34")) {
                    int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                    VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("Matchmaking " + channelNumber, true).get(0);
                    matchmakingChannel.getManager().setUserLimit(5).queue();
                    matchmakingChannel.getManager().putPermissionOverride(server.getRoleById(514511396491231233L), null, allow).queue();
                    getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
                } else if (reacEmote.getName().equals("📞")) {
                    //Formats current time in a normal format
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
                    ZonedDateTime hereAndNow = ZonedDateTime.now();
                    String test = dateTimeFormatter.format(hereAndNow);
                    String stopDate = test.replaceAll(",", "");

                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                    //---------------------------------------

                    Date d2 = null;
                    try {
                        //Formats the channel join and leave time
                        d2 = format.parse(stopDate);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }


                    if (hasCalledSuppoort.containsKey(member)) {
                        System.out.println(hasCalledSuppoort.get(member).getMinutes());
                        if (hasCalledSuppoort.get(member).getMinutes() + 5 <= d2.getMinutes() || hasCalledSuppoort.get(member).getDay() != d2.getDay() || hasCalledSuppoort.get(member).getHours() != d2.getHours()) {
                            int i = 0;
                            List<Member> modTeamMembers = utils.getMembersWithRole(server, "Mod-Team");
                            while (modTeamMembers.size() > i) {
                                modTeamMembers.get(i).getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("A user in the Voice Channel " + Objects.requireNonNull(member.getVoiceState()).getChannel().getName() + " needs support!").queue());
                                i++;
                                hasCalledSuppoort.remove(member);
                                hasCalledSuppoort.put(member, d2);
                            }
                            getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
                        } else {
                            System.out.println("older than 5 min ");
                            member.getUser().openPrivateChannel().queue(privateChannel -> {
                                privateChannel.sendMessage("Bitte nutze diesen Command nur alle 5 Minuten").queue();
                                getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
                            });
                        }
                    } else {
                        System.out.println("doesnt contain");
                        int i = 0;
                        List<Member> modTeamMembers = utils.getMembersWithRole(server, "Mod-Team");
                        while (modTeamMembers.size() > i) {
                            modTeamMembers.get(i).getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("A user in the Voice Channel " + Objects.requireNonNull(member.getVoiceState()).getChannel().getName() + " needs support!").queue());
                            i++;

                        }
                        getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
                        hasCalledSuppoort.put(member, d2);
                    }

                }
        }
    }

    public List<Message> getMessageToRemoveReaction(MessageChannel channel, String messageID) {
        return channel.getIterableHistory().stream()
                .limit(4)
                .filter(m -> m.getId().equals(messageID))
                .collect(Collectors.toList());
    }
}
