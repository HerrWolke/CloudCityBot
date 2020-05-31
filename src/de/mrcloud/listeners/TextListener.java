package de.mrcloud.listeners;

import de.mrcloud.utils.JDAUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.HashMap;

public class TextListener extends ListenerAdapter {
    JDAUtils utils = new JDAUtils();
    public HashMap<Member, Date> taggedBot = new HashMap<>();

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);


        if (e.getMessage().getMentionedMembers().contains(e.getGuild().getMemberById("709288877957054465"))) {

            //Variables
            Guild server = e.getGuild();
            Member member = e.getMember();
            Message message = e.getMessage();
            String messageContent = message.getContentRaw();
            TextChannel txtChannel = e.getChannel();
            //-----------

            //Sends a message
            utils.GreenBuilder("Hello", "If you need help please join the suppport channel or mention the Mod-Team. " +
                    "Otherwise, I can also forward a message to a specific member of the mod team, if you wish. " +
                    "To do that, just type [MESSAGE] [@SUPPORT_TEAM_MEMBER] and replace the content of the []-brackets.", member, txtChannel, true, 60);
            //-------

            taggedBot.put(member, new Date());
        } else if (taggedBot.containsKey(e.getMessage().getMember())) {
            //Checks if the request hasnt expired yet
            if (!(taggedBot.get(e.getMember()).getMinutes() + 20 <= new Date().getMinutes())) {
                //Checks if the member is a part of the mod team
                if (utils.getMembersWithRole(e.getGuild(), "Mod-Team").contains(e.getMessage().getMentionedMembers().get(0))) {
                    //Sends the message to the mod and removes the user from the list
                    e.getMessage().getMentionedMembers().get(0).getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("The user " + e.getMember().getUser().getName() + " has send you the following message: " + e.getMessage().getContentRaw()).queue());
                    utils.GreenBuilder(" ", "Your message has been forwarded", e.getMember(), e.getChannel(), true, 20);
                    e.getMessage().delete().queue();
                    taggedBot.remove(e.getMember());
                } else {
                    utils.RedBuilder("Error", "I am sorry, but the mentioned member is not part of our Team!", e.getMember(), e.getChannel(), true, 20);
                }
            } else {
                utils.RedBuilder("Error", "I am sorry, but your period to submit a request is over!", e.getMember(), e.getChannel(), true, 20);
                taggedBot.remove(e.getMember());
            }
        }
    }
}
