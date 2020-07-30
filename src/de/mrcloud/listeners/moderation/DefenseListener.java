package de.mrcloud.listeners.moderation;

import de.mrcloud.utils.DataStorageClass;
import de.mrcloud.utils.Static;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.audit.AuditLogOption;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.List;

public class DefenseListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceGuildMute(@Nonnull GuildVoiceGuildMuteEvent e) {
        super.onGuildVoiceGuildMute(e);

        Member muted = e.getMember();
        Guild server = e.getGuild();
        if(muted.getId().equals(Static.CLOUD_ID_STRING) && !e.getMember().getUser().isBot()) {
            String bannerID = server.retrieveAuditLogs().type(ActionType.MEMBER_UPDATE).complete().get(0).getUser().getId();
            if(bannerID.equals(Static.CLOUD_ID_STRING)) {
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

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent e) {
        super.onGuildVoiceMove(e);
        Member moved = e.getMember();
        Guild server = e.getGuild();




        if(moved.getId().equals(Static.CLOUD_ID_STRING)) {
           LinkedHashMap<User, Integer> movers = new LinkedHashMap<>();
            System.out.println();
            List<AuditLogEntry> lastMovers = server.retrieveAuditLogs().type(ActionType.MEMBER_VOICE_MOVE).complete();
            
            String movedAmountAsString = server.retrieveAuditLogs().type(ActionType.MEMBER_VOICE_MOVE).complete().get(0).getOption(AuditLogOption.COUNT);
            User mover = server.retrieveAuditLogs().type(ActionType.MEMBER_VOICE_MOVE).complete().get(0).getUser();



            System.out.println(DataStorageClass.cloudHasDisableMove);
            if(DataStorageClass.cloudHasDisableMove && moved.getUser() != mover && !mover.isBot()) {
                server.moveVoiceMember(moved,e.getChannelLeft()).queue();
            }
        }
    }
}
