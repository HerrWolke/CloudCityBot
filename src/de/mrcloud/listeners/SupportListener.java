package de.mrcloud.listeners;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SupportListener extends ListenerAdapter {
    static List<Member> toMove = new ArrayList<>();
    public List<Permission> allow = Arrays.asList(Permission.VIEW_CHANNEL);

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent e) {
        super.onGuildVoiceJoin(e);

        //Variables
        Guild server = e.getGuild();
        VoiceChannel voiceChannelJoined = e.getChannelJoined();
        Category category = e.getChannelJoined().getParent();
        Member member = e.getMember();
        int i = 0;
        //-------------

        //Checks if the channel is the right channel
        if (voiceChannelJoined.getName().equals("Support-Waiting-Room")) {
            while (server.getMembersWithRoles(server.getRolesByName("Mod-Team", true).get(0)).size() > i) {
                Member toNotify = server.getMembersWithRoles(server.getRolesByName("Mod-Team", true).get(0)).get(i);
                toNotify.getUser().openPrivateChannel().queue((privateChannel -> {
                    privateChannel.sendMessage("Ein User wartet im Support Raum auf dich.").queue();
                }));
                toMove.add(member);
            }

            //Checks if a mod joined a mod support channel
        }
        if (voiceChannelJoined.getName().equals("Support-Room-Mod")) {
            assert category != null;
            if (!toMove.isEmpty()) {
                category.createVoiceChannel("Belegter Support Raum").setUserlimit(2).addRolePermissionOverride(514511396491231233L, null, allow).complete();
                server.moveVoiceMember(member, server.getVoiceChannelsByName("Belegter Support Raum", true).get(0)).queue();
                server.moveVoiceMember(toMove.get(0), server.getVoiceChannelsByName("Belegter Support Raum", true).get(0)).queue();
                toMove.remove(0);
            }
        }
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent e) {
        super.onGuildVoiceLeave(e);

        Guild server = e.getGuild();
        VoiceChannel voiceChannelLeft = e.getChannelLeft();
        Member member = e.getMember();


        if (voiceChannelLeft.getName().equals("Support-Room")) {
            toMove.remove(member);
        }

    }
}
