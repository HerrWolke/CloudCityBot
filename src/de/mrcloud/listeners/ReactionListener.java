package de.mrcloud.listeners;

import de.mrcloud.utils.JDAUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

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



        if (!e.getUser().isBot()) {
            if (txtChannel.getName().matches("channel-settings-\\d*"))
                //Aka w symbol / wingman channel
                if (reacEmote.getName().equals("\uD83C\uDDFC")) {
                    int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                    VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("Matchmaking " + channelNumber, true).get(0);
                    matchmakingChannel.getManager().setUserLimit(2).queue();
                    //Aka m symbol / matchmaking channel
                } else if (reacEmote.getName().equals("\uD83C\uDDF2")) {
                    int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                    VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("Matchmaking " + channelNumber, true).get(0);
                    matchmakingChannel.getManager().setUserLimit(5).queue();
                    //Aka green circle / open channel
                } else if (reacEmote.getName().equals("\uD83D\uDFE2")) {
                    int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                    VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("Matchmaking " + channelNumber, true).get(0);
                    matchmakingChannel.getManager().putPermissionOverride(server.getRoleById(514511396491231233L), allow, null).queue();
                    //Aka red circle / close channel
                } else if (reacEmote.getName().equals("\uD83D\uDD34")) {
                    int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                    VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("Matchmaking " + channelNumber, true).get(0);
                    matchmakingChannel.getManager().setUserLimit(5).queue();
                    matchmakingChannel.getManager().putPermissionOverride(server.getRoleById(514511396491231233L), null, allow).queue();
                }
        }
    }
}
