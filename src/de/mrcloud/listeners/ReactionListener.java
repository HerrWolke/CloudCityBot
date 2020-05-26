package de.mrcloud.listeners;

import de.mrcloud.utils.JDAUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReactionListener extends ListenerAdapter {
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
                    int i = 0;
                    List<Member> modTeamMembers = utils.getMembersWithRole(server, "Mod-Team");
                    while (modTeamMembers.size() > i) {
                        modTeamMembers.get(i).getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("A user in the Voice Channel " + Objects.requireNonNull(member.getVoiceState()).getChannel().getName() + " needs support!").queue());
                        i++;

                    }
                    getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
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
