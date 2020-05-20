package de.mrcloud.listeners;

import de.mrcloud.utils.JDAUtils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ReactionListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent e) {
        super.onGuildMessageReactionAdd(e);
        Guild server = e.getGuild();
        TextChannel txtChannel = e.getChannel();
        Member member = e.getMember();
        MessageReaction.ReactionEmote reacEmote = e.getReactionEmote();
        JDAUtils utils = new JDAUtils();

        if (txtChannel.getName().matches("channel-settings-\\d*"))
            if (reacEmote.getName().equals("\uD83C\uDDFC")) {
                int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("Matchmaking " + channelNumber, true).get(0);
                matchmakingChannel.getManager().setUserLimit(2).queue();
            }
    }
}
