package de.mrcloud.listeners;

import de.mrcloud.utils.JDAUtils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RoleListener extends ListenerAdapter {
    static List<Member> hasMatchMakingRole= new ArrayList<>();
    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent e) {
        super.onGuildMessageReactionAdd(e);
        Guild server = e.getGuild();
        TextChannel txtChannel = e.getChannel();
        Member member = e.getMember();
        MessageReaction.ReactionEmote reacEmote = e.getReactionEmote();
        JDAUtils utils = new JDAUtils();

        if (txtChannel.getId().equals("709136263944339516")) {
            if (e.getMessageId().equals("712705608251342879")) {
                server.addRoleToMember(member, server.getRolesByName("╚═══ Wettkampf Rang ═══╗", true).get(0)).queue();
                hasMatchMakingRole.add(member);

            } else if (e.getMessageId().equals("709156105015787600")) {
                server.addRoleToMember(member, server.getRolesByName("╚═══ Wingman Rang ═══╗", true).get(0)).queue();
                if(hasMatchMakingRole.contains(member)) {
                    utils.addRoleToMember(server,member,"Clouds ☁️");
                    hasMatchMakingRole.remove(member);
                }
            } else if (e.getMessageId().equals("709192438392029313")) {
                server.addRoleToMember(member, server.getRolesByName("╚═════ Info Rang  ═════╗", true).get(0)).queue();
                utils.addRoleToMember(server,member,"Clouds ☁️");
                hasMatchMakingRole.remove(member);
            }
        }
    }
}
