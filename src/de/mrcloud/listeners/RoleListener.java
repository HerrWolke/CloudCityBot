package de.mrcloud.listeners;

import de.mrcloud.utils.JDAUtils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RoleListener extends ListenerAdapter {
    static List<Member> hasMatchMakingRole = new ArrayList<>();

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent e) {
        super.onGuildMessageReactionAdd(e);


        if (e.getChannel().getId().equals("709136263944339516")) {
            Guild server = e.getGuild();
            TextChannel txtChannel = e.getChannel();
            Member member = e.getMember();
            MessageReaction.ReactionEmote reacEmote = e.getReactionEmote();
            String emoteName = reacEmote.getName();
            Role toGiveToMember = null;
            JDAUtils utils = new JDAUtils();

            if (!member.getUser().isBot()) {
                //Aka matchmaing rank message
                if (e.getMessageId().equals("716726166618243142")) {
                    int getRole2 = 0;
                    boolean hasMMRole;
                    String roleName2;
                    while (member.getRoles().size() > getRole2) {
                        roleName2 = member.getRoles().get(getRole2).getName();

                        if (roleName2.equals("╚═══ Wettkampf Rang ═══╗")) {
                            if(SearchingForMatchmakingListener.compare.containsKey(member.getRoles().get((getRole2 + 1)).getName())) {

                            }

                        }
                        getRole2++;
                    }
                    switch (reacEmote.getName()) {
                        case "s1":
                        case "global":
                        case "s2":
                        case "s3":
                        case "s4":
                        case "se":
                        case "supreme":
                        case "lem":
                        case "le":
                        case "dmg":
                        case "mge":
                        case "mg2":
                        case "mg":
                        case "gn4":
                        case "gn3":
                        case "gn2":
                        case "gn1":
                        case "sem":
                            toGiveToMember = server.getRolesByName(SearchingForMatchmakingListener.compareEmojiToRole.get(emoteName), true).get(0);
                            server.addRoleToMember(member, toGiveToMember).queue();

                            server.addRoleToMember(member, server.getRolesByName("╚═══ Wettkampf Rang ═══╗", true).get(0)).queue();
                            hasMatchMakingRole.add(member);

                            break;
                        case"unranked":
                            toGiveToMember = server.getRolesByName("unranked", true).get(0);
                            server.addRoleToMember(member, toGiveToMember).queue();
                            break;
                    }
                    //aka wingman rank message
                } else if (e.getMessageId().equals("716726167586996284")) {
                    switch (reacEmote.getName()) {
                        case "s1":
                        case "global":
                        case "s2":
                        case "s3":
                        case "s4":
                        case "se":
                        case "supreme":
                        case "lem":
                        case "le":
                        case "dmg":
                        case "mge":
                        case "mg2":
                        case "mg":
                        case "gn4":
                        case "gn3":
                        case "gn2":
                        case "gn1":
                        case "sem":
                            //needs to be one because of the way discords sorts the role when getting them
                            toGiveToMember = server.getRolesByName(SearchingForMatchmakingListener.compareEmojiToRole.get(emoteName), true).get(1);
                            server.addRoleToMember(member, toGiveToMember).queue();

                            server.addRoleToMember(member, server.getRolesByName("╚═══ Wingman Rang ═══╗", true).get(0)).queue();
                            if (hasMatchMakingRole.contains(member)) {
                                utils.addRoleToMember(server, member, "Clouds ☁️");
                                hasMatchMakingRole.remove(member);
                            }
                            break;
                        case"unranked":
                            toGiveToMember = server.getRolesByName("unranked", true).get(0);
                            server.addRoleToMember(member, toGiveToMember).queue();
                            break;
                    }
                } else if (e.getMessageId().equals("716726168627183616")) {
                    switch (emoteName) {
                        case "hackengrn":
                            server.addRoleToMember(member, server.getRolesByName("prime ✔", false).get(0)).queue();
                            break;
                        case "x":
                            server.addRoleToMember(member, server.getRolesByName("none prime ❌", false).get(0)).queue();
                            break;
                    }
                    server.addRoleToMember(member, server.getRolesByName("╚═════ Info Rang  ═════╗", true).get(0)).queue();
                    utils.addRoleToMember(server, member, "Clouds ☁️");
                    hasMatchMakingRole.remove(member);
                }


            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent e) {
        super.onGuildMessageReactionRemove(e);
    }
}


