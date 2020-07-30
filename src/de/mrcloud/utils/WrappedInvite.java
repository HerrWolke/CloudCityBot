package de.mrcloud.utils;

import net.dv8tion.jda.api.entities.Invite;

public class WrappedInvite {

    int usageCount;
    String inviteCode;


    public WrappedInvite(Invite invite) {
        usageCount = invite.getUses();
        inviteCode = invite.getCode();
    }

    public int getUses() {
        return usageCount;
    }

    public String getInviteCode() {
        return inviteCode;
    }
}
