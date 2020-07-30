package de.mrcloud.listeners;

import de.mrcloud.SQL.SqlMain;
import de.mrcloud.utils.DataStorageClass;
import de.mrcloud.utils.JDAUtils;
import de.mrcloud.utils.WrappedInvite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;

public class DataListeners extends ListenerAdapter {
    @Override
    public void onGuildMemberRoleAdd(@Nonnull GuildMemberRoleAddEvent e) {
        super.onGuildMemberRoleAdd(e);

        Member member = e.getMember();
        List<Role> roles = e.getRoles();

        for(Role role : roles) {
            if(DataStorageClass.moderationRolesIDs.contains(role.getIdLong())) {
                DataStorageClass.moderators.add(member);
                JDAUtils.setSQLCollumBoolean(SqlMain.mariaDB(),member.getId(),"isMod",true);
            }
        }
    }


    @Override
    public void onGuildMemberRoleRemove(@Nonnull GuildMemberRoleRemoveEvent e) {
        super.onGuildMemberRoleRemove(e);

        Member member = e.getMember();
        List<Role> roles = e.getRoles();

        for(Role role : roles) {
            if(DataStorageClass.moderationRolesIDs.contains(role.getIdLong())) {
                DataStorageClass.moderators.remove(member);
                JDAUtils.setSQLCollumBoolean(SqlMain.mariaDB(),member.getId(),"isMod",false);
            }
        }
    }

    @Override
    public void onGuildInviteCreate(@Nonnull GuildInviteCreateEvent e) {
        super.onGuildInviteCreate(e);

        DataStorageClass.invitesList.add(new WrappedInvite(e.getInvite()));
        System.out.println("Successfully added new invite to list");
    }

    @Override
    public void onGuildInviteDelete(@Nonnull GuildInviteDeleteEvent e) {
        super.onGuildInviteDelete(e);
        
    }
}
