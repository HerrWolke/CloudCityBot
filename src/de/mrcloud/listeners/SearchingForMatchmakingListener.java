package de.mrcloud.listeners;

import de.mrcloud.SQL.SqlMain;
import de.mrcloud.utils.JDAUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchingForMatchmakingListener extends ListenerAdapter {
    HashMap<String, Integer> compare;


    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent e) {
        super.onGuildVoiceJoin(e);
        compare = new HashMap<>();
        int rankNumber = 0;

        Guild server = e.getGuild();
        VoiceChannel voiceChannelJoined = e.getChannelJoined();
        Member member = e.getMember();
        JDAUtils utils = new JDAUtils();

        int i = 0;
        int i2 = 0;
        int getRole;
        int getRole2 = 0;
        double durschnittsRang;

        String friendCodeToSend = utils.sqlGetCollum(Objects.requireNonNull(SqlMain.mariaDB()), member, "FriendCode");

        //Only for Rank Comparing
        compare.put("Silver Ⅰ", 1);
        compare.put("Silver Ⅱ", 2);
        compare.put("Silver Ⅲ", 3);
        compare.put("Silver Ⅳ", 4);
        compare.put("Silver Elite", 5);
        compare.put("Silver Elite Master", 6);
        compare.put("Gold Nova Ⅰ", 7);
        compare.put("Gold Nova Ⅱ", 8);
        compare.put("Gold Nova Ⅲ", 9);
        compare.put("Gold Nova Ⅳ", 10);
        compare.put("Master Guardian Ⅰ", 11);
        compare.put("Master Guardian Ⅱ", 12);
        compare.put("Master Guardian Elite", 13);
        compare.put("Distinguished Master Guardian", 14);
        compare.put("Legendary Eagle", 15);
        compare.put("Legendary Eagle Master", 16);
        compare.put("Supreme Master First Class", 17);
        compare.put("Global Elite", 18);
        //---------------------------

        int rankNumber2 = 0;
        String roleName2;
        while (member.getRoles().size() > getRole2) {
            roleName2 = member.getRoles().get(getRole2).getName();

            if (roleName2.equals("╚═══ Wettkampf Rang ═══╗")) {
                rankNumber2 = compare.get(member.getRoles().get((getRole2 + 1)).getName());

            }
            getRole2++;
        }
        if (voiceChannelJoined.getName().equals("Searching-For-Matchmaking")) {


            List<VoiceChannel> list = server.getVoiceChannelCache().applyStream(it ->
                    it.filter(channel -> channel.getName().matches("Matchmaking \\d*"))
                            .collect(Collectors.toList())
            );

            assert list != null;
            while (list.size() > i) {

                List<Member> membersInVoice = list.get(i).getMembers();


                if (!(membersInVoice.size() >= 5)) {

                    while (membersInVoice.size() > i2) {

                        getRole = 0;


                        while (membersInVoice.get(i2).getRoles().size() > getRole) {


                            String roleName = membersInVoice.get(i2).getRoles().get(getRole).getName();
                            getRole++;


                            if (roleName.equals("╚═══ Wettkampf Rang ═══╗")) {
                                rankNumber += compare.get(membersInVoice.get(i2).getRoles().get(getRole).getName());
                                getRole = 30;

                            }
                        }
                        i2++;
                        if (membersInVoice.size() == i2 || membersInVoice.size() == 1) {
                            //noinspection IntegerDivisionInFloatingPointContext
                            durschnittsRang = Math.ceil(rankNumber / membersInVoice.size());

                            if (durschnittsRang > rankNumber2 && durschnittsRang < (rankNumber2 + 3)) {
                                server.moveVoiceMember(member, list.get(i)).queue();
                                //Checks if a friend code exists in the db
                                if (!friendCodeToSend.isEmpty()) {
                                    AutoCreateChannels.channelOwner.get(list.get(i)).getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("The friendcode of " + member.getNickname() + " is " + friendCodeToSend).queue());
                                }
                            } else if (durschnittsRang < rankNumber2 && durschnittsRang > (rankNumber2 - 3)) {
                                server.moveVoiceMember(member, list.get(i)).queue();
                                //Checks if a friend code exists in the db
                                if (!friendCodeToSend.isEmpty()) {
                                    AutoCreateChannels.channelOwner.get(list.get(i)).getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("The friendcode of " + member.getNickname() + " is " + friendCodeToSend).queue());
                                }
                            } else if (durschnittsRang == rankNumber2) {
                                server.moveVoiceMember(member, list.get(i)).queue();
                                //Checks if a friend code exists in the db
                                if (!friendCodeToSend.isEmpty()) {
                                    AutoCreateChannels.channelOwner.get(list.get(i)).getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("The friendcode of " + member.getNickname() + " is " + friendCodeToSend).queue());
                                }

                            }
                        }
                    }

                }
                i++;
            }

        }
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent e) {
        super.onGuildVoiceMove(e);

        compare = new HashMap<>();
        int rankNumber = 0;

        Guild server = e.getGuild();
        VoiceChannel voiceChannelJoined = e.getChannelJoined();
        Member member = e.getMember();
        JDAUtils utils = new JDAUtils();
        int i = 0;
        int i2 = 0;
        int getRole;
        int getRole2 = 0;
        double durschnittsRang;

        String friendCodeToSend = utils.sqlGetCollum(Objects.requireNonNull(SqlMain.mariaDB()), member, "FriendCode");

        //Only for Rank Comparing
        compare.put("Silver Ⅰ", 1);
        compare.put("Silver Ⅱ", 2);
        compare.put("Silver Ⅲ", 3);
        compare.put("Silver Ⅳ", 4);
        compare.put("Silver Elite", 5);
        compare.put("Silver Elite Master", 6);
        compare.put("Gold Nova Ⅰ", 7);
        compare.put("Gold Nova Ⅱ", 8);
        compare.put("Gold Nova Ⅲ", 9);
        compare.put("Gold Nova Ⅳ", 10);
        compare.put("Master Guardian Ⅰ", 11);
        compare.put("Master Guardian Ⅱ", 12);
        compare.put("Master Guardian Elite", 13);
        compare.put("Distinguished Master Guardian", 14);
        compare.put("Legendary Eagle", 15);
        compare.put("Legendary Eagle Master", 16);
        compare.put("Supreme Master First Class", 17);
        compare.put("Global Elite", 18);
        //---------------------------

        int rankNumber2 = 0;
        String roleName2;
        while (member.getRoles().size() > getRole2) {
            roleName2 = member.getRoles().get(getRole2).getName();

            if (roleName2.equals("╚═══ Wettkampf Rang ═══╗")) {
                rankNumber2 = compare.get(member.getRoles().get((getRole2 + 1)).getName());

            }
            getRole2++;
        }
        if (voiceChannelJoined.getName().equals("Searching-For-Matchmaking")) {
            System.out.println("Mm Search");

            List<VoiceChannel> list = server.getVoiceChannelCache().applyStream(it ->
                    it.filter(channel -> channel.getName().matches("Matchmaking \\d*"))
                            .collect(Collectors.toList())
            );

            assert list != null;
            while (list.size() > i) {

                List<Member> membersInVoice = list.get(i).getMembers();


                if (!(membersInVoice.size() >= 5)) {

                    while (membersInVoice.size() > i2) {

                        getRole = 0;


                        while (membersInVoice.get(i2).getRoles().size() > getRole) {


                            String roleName = membersInVoice.get(i2).getRoles().get(getRole).getName();
                            getRole++;


                            if (roleName.equals("╚═══ Wettkampf Rang ═══╗")) {
                                rankNumber += compare.get(membersInVoice.get(i2).getRoles().get(getRole).getName());
                                System.out.println(membersInVoice.get(i2).getRoles().get(getRole).getName());
                                getRole = 30;

                            }
                        }
                        i2++;
                        if (membersInVoice.size() == i2 || membersInVoice.size() == 1) {
                            //noinspection IntegerDivisionInFloatingPointContext
                            durschnittsRang = Math.ceil(rankNumber / membersInVoice.size());

                            if (durschnittsRang > rankNumber2 && durschnittsRang < (rankNumber2 + 3)) {
                                server.moveVoiceMember(member, list.get(i)).queue();
                                //Checks if a friend code exists in the db
                                if (!friendCodeToSend.isEmpty()) {
                                    AutoCreateChannels.channelOwner.get(list.get(i)).getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("The friendcode of " + member.getNickname() + " is " + friendCodeToSend).queue());
                                }
                            } else if (durschnittsRang < rankNumber2 && durschnittsRang > (rankNumber2 - 3)) {
                                server.moveVoiceMember(member, list.get(i)).queue();
                                //Checks if a friend code exists in the db
                                if (!friendCodeToSend.isEmpty()) {
                                    AutoCreateChannels.channelOwner.get(list.get(i)).getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("The friendcode of " + member.getNickname() + " is " + friendCodeToSend).queue());
                                }
                            } else if (durschnittsRang == rankNumber2) {
                                server.moveVoiceMember(member, list.get(i)).queue();
                                //Checks if a friend code exists in the db
                                if (!friendCodeToSend.isEmpty()) {
                                    AutoCreateChannels.channelOwner.get(list.get(i)).getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("The friendcode of " + member.getNickname() + " is " + friendCodeToSend).queue());
                                }
                            }
                        }
                    }

                }
                i++;
            }

        }
    }
}



