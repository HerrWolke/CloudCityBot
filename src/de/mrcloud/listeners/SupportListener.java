package de.mrcloud.listeners;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class SupportListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent e) {
        super.onGuildVoiceJoin(e);

        Guild server = e.getGuild();
        VoiceChannel voiceChannelJoined = e.getChannelJoined();
        VoiceChannel voiceChannelLeft = e.getChannelLeft();
        Category category = e.getChannelJoined().getParent();
        Member member = e.getMember();
        int i = 0;

        if (voiceChannelJoined.getName().equals("Support-Room")) {
            while (server.getMembersWithRoles(server.getRolesByName("Mod-Team", true).get(0)).size() > i) {
                Member toNotify = server.getMembersWithRoles(server.getRolesByName("Mod-Team", true).get(0)).get(i);


                toNotify.getUser().openPrivateChannel().queue((privateChannel -> {
                    privateChannel.sendMessage("Ein User wartet im Support Raum auf dich.").queue();
                }));
            }
        }
    }
}
