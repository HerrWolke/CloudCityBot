package de.mrcloud.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
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


    public void RedBuilder(String Title, GuildMessageReceivedEvent e, TextChannel txtChannel, String ErrorText, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(e.getAuthor().getName(), e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
        embBuilder.setColor(Color.decode("#d63031"));
        embBuilder.setDescription(ErrorText);
        txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(deleteAfter, TimeUnit.SECONDS);

    }

    public void YellowBuilder (String Title,GuildMessageReceivedEvent e, TextChannel txtChannel, String InfoText, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(e.getAuthor().getName(), e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
        embBuilder.setColor(Color.decode("#f9ca24"));
        embBuilder.setDescription(InfoText);
        txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(deleteAfter, TimeUnit.SECONDS);

    }

    public void GreenBuilder(GuildMessageReceivedEvent e, String Title, TextChannel txtChannel, String InfoText, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(e.getAuthor().getName(), e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
        embBuilder.setColor(Color.decode("#2ecc71"));
        embBuilder.setDescription(InfoText);
        txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(deleteAfter, TimeUnit.SECONDS);

    }


    public void Generell(GuildMessageReceivedEvent e, TextChannel txtChannel, String MessageColor, String Title, String Desc, boolean delete, int toDeleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(e.getAuthor().getName(), e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
        embBuilder.setColor(Color.decode(MessageColor));
        embBuilder.setDescription(Desc);
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(toDeleteAfter, TimeUnit.SECONDS);
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }

    }

    public void PrivGenerell(GuildMessageReceivedEvent e, PrivateChannel txtChannel, String MessageColor, String Title, String Desc, boolean delete, int toDeleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(e.getAuthor().getName(), e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
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
}