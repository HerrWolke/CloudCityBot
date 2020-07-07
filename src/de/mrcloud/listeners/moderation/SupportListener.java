package de.mrcloud.listeners.moderation;

import de.mrcloud.utils.JDAUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        if(e.getMessage().getContentRaw().split("\\s++")[0].equalsIgnoreCase("&ticket")) {
            Guild server = e.getGuild();
            Member member = Objects.requireNonNull(e.getMember());
            Message message = e.getMessage();
            String messageContent = message.getContentRaw();
            TextChannel txtChannel = e.getChannel();
            JDAUtils utils = new JDAUtils();

            String[] messageSplit = messageContent.split("\\s++");
            if(messageSplit.length == 4) {
                TextChannel supportChannel = server.getTextChannelsByName("\uD83D\uDEABtickets-support\uD83D\uDEAB",true).get(0);
                if(messageSplit[1].equalsIgnoreCase("create")) {
                    String contentOfTicket = "";
                    int i = 2;
                    while (messageSplit.length > i) {
                        contentOfTicket += messageSplit[i];
                                i++;
                    }

                    EmbedBuilder embBuilder = new EmbedBuilder();
                    embBuilder.setTitle("New ticket created by " + member.getEffectiveName());
                    embBuilder.setAuthor(e.getAuthor().getName() + "'s Profile", e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
                    embBuilder.setColor(Color.decode("#2ecc71"));
                    embBuilder.addField("Title", messageSplit[2], true);
                    embBuilder.addField("Content", contentOfTicket, true);
                    supportChannel.sendMessage(embBuilder.build()).complete();

                    utils.GreenBuilder("Erfolgreich","Deine Nachricht wurde an dein Support Team weitergeleitet. Du wirst benachrichtigt sobald sie bearbeitet wird.",member,txtChannel,true,15);
                } if(messageSplit[1].equalsIgnoreCase("close")) {

                } if(messageSplit[1].equalsIgnoreCase("reopen")) {

                }
            } else {
                utils.UsageHelp("Use: &ticket create/close/reopen [TITLE] [INHALT]",member,txtChannel);
            }

        }


    }
}
