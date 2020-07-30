package de.mrcloud.utils;

import net.dv8tion.jda.api.entities.Member;

import java.util.*;

public class DataStorageClass {
    public static boolean cloudHasDisableMove = false;
    public static List<Member> moderators = new ArrayList<>();
    public static List<Long> moderationRolesIDs = new ArrayList<>(Arrays.asList(709128492842745887L, 675326650879049746L, 712231870829690892L, 731937228661457037L, 515187921394073600L, 716047553405386775L, 714874334707188087L));
    public static List<WrappedInvite> invitesList = new ArrayList<>();
    public static LinkedHashMap<Member, Date> muted = new LinkedHashMap<>();
    public static LinkedHashMap<String,Integer> muteReasonsTime = new LinkedHashMap<>();
    public static LinkedHashMap<Integer,Double> muteTimeMultiplier = new LinkedHashMap<>();
    public static List<String> muteReasonsList = new ArrayList<>();
}
