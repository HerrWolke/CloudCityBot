package de.mrcloud.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class FunListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        Guild server = e.getGuild();
        Member member = e.getMember();
        Message message = e.getMessage();
        String messageContent = message.getContentRaw();
        TextChannel txtChannel = e.getChannel();

        if(messageContent.equals("\uD83E\uDD21")) {
            message.addReaction("\uD83C\uDDF8").queue();
            message.addReaction("\uD83C\uDDF9").queue();
            message.addReaction("\uD83C\uDDEB").queue();
            message.addReaction("\uD83C\uDDFA").queue();
        } else if(messageContent.equalsIgnoreCase("simp")) {
            message.addReaction("\uD83C\uDDF8").queue();
            message.addReaction("\uD83C\uDDF9").queue();
            message.addReaction("\uD83C\uDDEB").queue();
            message.addReaction("\uD83C\uDDFA").queue();
        }
    }
}
