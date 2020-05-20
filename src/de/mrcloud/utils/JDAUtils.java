package de.mrcloud.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JDAUtils {
    public boolean hasRole(String roleName, Member member, Guild server) {
        boolean hasRole = false;
        int i = 0;

        while (member.getRoles().size() > i) {
            List<Role> roles = member.getRoles();
            if (roles.get(i).getName().equals("roleName")) {
                hasRole = true;
            }
        }

        return hasRole;
    }


    public void RedBuilder(String Title, Member member, TextChannel txtChannel, String ErrorText, int deleteAfter, boolean delete) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#d63031"));
        embBuilder.setDescription(ErrorText);
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(deleteAfter, TimeUnit.SECONDS);
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }

    }

    public void YellowBuilder(String Title, Member member, TextChannel txtChannel, String InfoText, int deleteAfter, boolean delete) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#f9ca24"));
        embBuilder.setDescription(InfoText);
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(deleteAfter, TimeUnit.SECONDS);
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }

    }

    public void GreenBuilder(String Title, Member member, TextChannel txtChannel, String InfoText, int deleteAfter, boolean delete) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#2ecc71"));
        embBuilder.setDescription(InfoText);
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(deleteAfter, TimeUnit.SECONDS);
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }

    }


    public void Generell(Member member, TextChannel txtChannel, String MessageColor, String Title, String Desc, boolean delete, int toDeleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode(MessageColor));
        embBuilder.setDescription(Desc);
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(toDeleteAfter, TimeUnit.SECONDS);
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }

    }

    public void PrivGenerell(Member member, PrivateChannel txtChannel, String MessageColor, String Title, String Desc, boolean delete, int toDeleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode(MessageColor));
        embBuilder.setDescription(Desc);
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(toDeleteAfter, TimeUnit.SECONDS);
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }

    }

    public void downloaded(GuildMessageReceivedEvent e, TextChannel txtChannel) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle("Success");
        embBuilder.setAuthor(e.getAuthor().getName() + " uploaded a File", e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
        embBuilder.setColor(Color.decode("#2ecc71"));
        embBuilder.setDescription("Downloaded File " + e.getMessage().getAttachments().get(0).getFileName());
        txtChannel.sendMessage(embBuilder.build()).complete();
    }

    public List<Member> getMembersWithRole(Guild server, String roleName) {
        return server.getMembersWithRoles(server.getRolesByName(roleName, true).get(0));
    }

    public void addRoleToMember(Guild server, Member meber, String roleName) {
        server.addRoleToMember(meber, server.getRolesByName(roleName, true).get(0)).queue();
    }
}

