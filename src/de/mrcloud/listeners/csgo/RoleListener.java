package de.mrcloud.listeners.csgo;

import de.mrcloud.listeners.SearchingForMatchmakingListener;
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

            int getRoleMM = 0;
            String roleNameMM;
            boolean hasMatchmakingRole = false;
            while (member.getRoles().size() > getRoleMM) {
                roleNameMM = member.getRoles().get(getRoleMM).getName();

                if (roleNameMM.equals("╚═══ Wettkampf Rang ═══╗")) {
                    if(SearchingForMatchmakingListener.compare.containsKey(member.getRoles().get(getRoleMM + 1).getName()))
                        hasMatchmakingRole = true;
                }
                getRoleMM++;
            }
            int getRoleWM = 0;
            String roleNameWM;
            boolean hasWingmanRole = false;
            while (member.getRoles().size() > getRoleWM) {
                roleNameWM = member.getRoles().get(getRoleWM).getName();

                if (roleNameWM.equals("╚═══ Wingman Rang ═══╗")) {
                    if(SearchingForMatchmakingListener.compare.containsKey(member.getRoles().get(getRoleWM + 1).getName()))
                        hasWingmanRole = true;
                }
                getRoleWM++;
            }
            
            

            if (!member.getUser().isBot()) {
                //Aka matchmaing rank message
                if (e.getMessageId().equals("716726166618243142")) {
                      if(!hasMatchmakingRole) {
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
                            utils.addRoleToMember(server, member, "Clouds ☁️");

                            break;
                        case "unranked":
                            toGiveToMember = server.getRolesByName("unranked", true).get(0);
                            server.addRoleToMember(member, toGiveToMember).queue();
                            break;
                    }
                }
                    //aka wingman rank message
                } else if (e.getMessageId().equals("716726167586996284")) {
                    if(!hasWingmanRole) {
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
                            case "unranked":
                                toGiveToMember = server.getRolesByName("unranked", true).get(0);
                                server.addRoleToMember(member, toGiveToMember).queue();
                                break;
                        }
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
                } else if (e.getMessageId().equals("716726168627183616")) {
                    switch (emoteName) {
                        //Thumbs up
                        case "\\uD83D\\uDC4D":
                            server.addRoleToMember(member, server.getRolesByName("CommunityNotify", false).get(0)).queue();
                            break;

                        case "\\uD83D\\uDC4E":
                            member.getUser().openPrivateChannel().queue(PrivateChannel -> {
                                PrivateChannel.sendMessage("Ok. Du kannst jederzeit dir den Rang noch geben.").queue();
                            });
                            break;
                    }
                }


            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent e) {
        super.onGuildMessageReactionRemove(e);

        Guild server = e.getGuild();
        TextChannel txtChannel = e.getChannel();
        Member member = e.getMember();
        MessageReaction.ReactionEmote reacEmote = e.getReactionEmote();
        String emoteName = reacEmote.getName();
        JDAUtils utils = new JDAUtils();

        if (e.getMessageId().equals("716726166618243142")) {
            utils.removeRoleFromMemberByString(member, server, SearchingForMatchmakingListener.compareEmojiToRole.get(emoteName), 0, true);
        } else if(e.getMessageId().equals("716726167586996284")) {
            utils.removeRoleFromMemberByString(member, server, SearchingForMatchmakingListener.compareEmojiToRole.get(emoteName), 1, true);
        } else if (e.getMessageId().equals("716726168627183616")) {
            utils.removeRoleFromMemberByString(member,server,"CommunityNotify",0,true);
        }
    }

}


