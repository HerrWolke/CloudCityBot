package de.mrcloud.listeners;

import de.mrcloud.utils.Static;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class DefenseListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceGuildMute(@Nonnull GuildVoiceGuildMuteEvent e) {
        super.onGuildVoiceGuildMute(e);

        Member muted = e.getMember();
        Guild server = e.getGuild();
        if(muted.getId().equals(Static.CLOUD_ID_STRING) && !e.getMember().getUser().isBot()) {
            String bannerID = server.retrieveAuditLogs().type(ActionType.MEMBER_UPDATE).complete().get(0).getUser().getId();
            if(bannerID == Static.CLOUD_ID_STRING) {
                server.mute(muted, false).complete();
            } else {
                server.mute(muted, false).complete();
                server.mute(server.getMemberById(bannerID),true).complete();
            }

        }
    }

    @Override
    public void onGuildVoiceGuildDeafen(@Nonnull GuildVoiceGuildDeafenEvent e) {
        super.onGuildVoiceGuildDeafen(e);

        Member muted = e.getMember();
        Guild server = e.getGuild();
        if(muted.getId().equals(Static.CLOUD_ID_STRING) && !e.getMember().getUser().isBot()) {
            String bannerID = server.retrieveAuditLogs().type(ActionType.MEMBER_UPDATE).complete().get(0).getUser().getId();
            server.deafen(muted,false).complete();
            server.deafen(server.getMemberById(bannerID),true).complete();
        }
    }
}
