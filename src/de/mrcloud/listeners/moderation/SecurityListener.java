package de.mrcloud.listeners.moderation;

import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.HashMap;

public class SecurityListener extends ListenerAdapter {
    public HashMap<String, Date> bannedUser = new HashMap<>();
    public HashMap<String, Date> bannedUser2 = new HashMap<>();
    public HashMap<String, Date> bannedUser3 = new HashMap<>();
    public HashMap<String, Date> bannedUser4 = new HashMap<>();
    public HashMap<String, Date> bannedUser5 = new HashMap<>();

    @Override
    public void onGuildBan(@Nonnull GuildBanEvent e) {
        super.onGuildBan(e);

        Date currentTime = new Date();

        User banned = e.getUser();
        Guild server = e.getGuild();
        String bannerID = server.retrieveAuditLogs().type(ActionType.BAN).complete().get(0).getUser().getId();

        if (bannedUser.containsKey(bannerID)) {
            if (bannedUser.get(bannerID).getMinutes() + 15 <= new Date().getMinutes()) {
                bannedUser.remove(bannerID);
                if (bannedUser2.containsKey(bannerID)) {
                    bannedUser2.remove(bannerID);
                    if (bannedUser3.containsKey(bannerID)) {
                        bannedUser3.remove(bannerID);
                        if (bannedUser4.containsKey(bannerID)) {
                            bannedUser4.remove(bannerID);
                            if (bannedUser5.containsKey(bannerID)) {
                                bannedUser5.remove(bannerID);
                            }
                        }
                    }
                }
            } else if (!bannedUser2.containsKey(bannerID)) {
                bannedUser2.put(bannerID, currentTime);
                System.out.println("added to second list");
            } else if (!bannedUser3.containsKey(bannerID)) {
                bannedUser3.put(bannerID, currentTime);
                System.out.println("added to third list");
            } else if (!bannedUser4.containsKey(bannerID)) {
                bannedUser4.put(bannerID, currentTime);
                System.out.println("added to fourth list");
            } else if (!bannedUser5.containsKey(bannerID)) {
                bannedUser5.put(bannerID, currentTime);
                System.out.println("added to fifth list");
                server.getMemberById(bannerID).ban(0, "You have been banned do to banning to many members. Please contact an admin or the owner if you think this is a mistake.").queue();
                server.getMemberById(bannerID).getUser().openPrivateChannel().queue((privateChannel -> {
                    privateChannel.sendMessage("You have been banned do to banning to many members. Please contact an admin or the owner if you think this is a mistake.").queue();
                }));
            } else {
                server.getMemberById(bannerID).getUser().openPrivateChannel().queue((privateChannel -> {
                    privateChannel.sendMessage("You have been banned do to banning to many members. Please contact an admin or the owner if you think this is a mistake.").queue();
                }));
                server.getMemberById(bannerID).ban(0, "You have been banned do to banning to many members. Please contact an admin or the owner if you think this is a mistake.").queue();
            }


        } else {
            bannedUser.put(bannerID, currentTime);
            System.out.println("added to first list");
        }


    }
}
