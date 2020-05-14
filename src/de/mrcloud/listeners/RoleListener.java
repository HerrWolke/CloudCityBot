package de.mrcloud.listeners;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RoleListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
        super.onGuildMessageReactionAdd(e);
        Guild server = e.getGuild();
        TextChannel txtChannel = e.getChannel();
        Member member = e.getMember();
        MessageReaction.ReactionEmote reacEmote = e.getReactionEmote();

        if (txtChannel.getId().equals("709136263944339516")) {
            if (e.getMessageId().equals("709169088131498037")) {
                server.addRoleToMember(member, server.getRolesByName("╚═══ Wettkampf Rang ═══╗", true).get(0)).queue();

            } else if (e.getMessageId().equals("709156105015787600")) {
                server.addRoleToMember(member, server.getRolesByName("╚═══ Wingman Rang ═══╗", true).get(0)).queue();

            } else if (e.getMessageId().equals("709192438392029313")) {
                server.addRoleToMember(member, server.getRolesByName("╚═════ Info Rang  ═════╗", true).get(0)).queue();
            }
        }
    }
}
