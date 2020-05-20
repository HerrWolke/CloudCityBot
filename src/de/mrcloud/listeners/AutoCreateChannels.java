package de.mrcloud.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AutoCreateChannels extends ListenerAdapter {
    public HashMap<VoiceChannel, Member> channelOwner = new HashMap<>();

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent e) {
        super.onGuildVoiceJoin(e);


        Guild server = e.getGuild();
        VoiceChannel voiceChannel = e.getChannelJoined();
        Category category = e.getChannelJoined().getParent();
        Member member = e.getMember();


        if (voiceChannel.getId().equals("709796589253820577")) {
            server.getVoiceChannels();
            List<VoiceChannel> list = server.getVoiceChannelCache().applyStream(it ->
                    it.filter(channel -> channel.getName().matches("Matchmaking \\d*"))
                            .collect(Collectors.toList())
            );


            int matchmakingChannels = list.size() + 1;
            VoiceChannel newChannel = category.createVoiceChannel("Matchmaking " + (list.size() + 1)).complete();
            newChannel.getManager().setUserLimit(5).queue();
            server.moveVoiceMember(member, newChannel).queue();

            channelOwner.put(newChannel, member);

            TextChannel newTextChannel = category.createTextChannel("Channel Settings " + matchmakingChannels).complete();
            EmbedBuilder embBuilder = new EmbedBuilder();
            embBuilder.setTitle("Channel Typ");
            embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
            embBuilder.setColor(Color.decode("#00a8ff"));
            embBuilder.addField("Gamemode", "\uD83C\uDDF2 Matchamking \n \uD83C\uDDFC Wingman", true);

            newTextChannel.sendMessage(embBuilder.build()).queue();

        }

    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent e) {
        super.onGuildVoiceMove(e);

        Guild server = e.getGuild();
        VoiceChannel voiceChannel = e.getChannelJoined();
        VoiceChannel voiceChannelLeft = e.getChannelLeft();
        Category category = e.getChannelJoined().getParent();
        Member member = e.getMember();


        if (voiceChannel.getId().equals("709796589253820577")) {
            server.getVoiceChannels();
            List<VoiceChannel> list = server.getVoiceChannelCache().applyStream(it ->
                    it.filter(channel -> channel.getName().matches("Matchmaking \\d*"))
                            .collect(Collectors.toList())
            );
            int matchmakingChannels = list.size() + 1;
            VoiceChannel newChannel = category.createVoiceChannel("Matchmaking " + (list.size() + 1)).complete();
            newChannel.getManager().setUserLimit(5).queue();
            server.moveVoiceMember(member, newChannel).queue();

            channelOwner.put(newChannel, member);

            TextChannel newTextChannel = category.createTextChannel("Channel Settings " + matchmakingChannels).complete();
            EmbedBuilder embBuilder = new EmbedBuilder();
            embBuilder.setTitle("Channel Typ");
            embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
            embBuilder.setColor(Color.decode("#00a8ff"));
            embBuilder.addField("Gamemode", "\uD83C\uDDF2 Matchamking \n \uD83C\uDDFC Wingman", true);

            newTextChannel.sendMessage(embBuilder.build()).queue((message) -> {
                message.addReaction("\uD83C\uDDF2").queue();
                message.addReaction("\uD83C\uDDFC").queue();
            });
        }

        if (voiceChannelLeft.getName().matches("Matchmaking \\d*")) {
            if (voiceChannelLeft.getMembers().size() == 0) {
                voiceChannelLeft.delete().queue();
                server.getTextChannelsByName("channel-settings-" + voiceChannelLeft.getName().split("\\s")[1], true).get(0).delete().complete();
            }
        }
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent e) {
        super.onGuildVoiceLeave(e);

        Guild server = e.getGuild();
        VoiceChannel voiceChannel = e.getChannelLeft();
        System.out.println(voiceChannel.getName());
        Member member = e.getMember();

        if (voiceChannel.getName().matches("Matchmaking \\d*")) {
            if (voiceChannel.getMembers().size() == 0) {
                server.getTextChannelsByName("channel-settings-" + voiceChannel.getName().split("\\s")[1], true).get(0).delete().complete();
                voiceChannel.delete().queue();
            }
        }
    }
}
