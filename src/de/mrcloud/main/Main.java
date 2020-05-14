package de.mrcloud.main;

import de.mrcloud.listeners.*;
import de.mrcloud.utils.Static;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class Main {

    public ShardManager shardMan;
    public Main() throws LoginException {


        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.create(Static.TOKEN, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS);
        builder.addEventListeners(new RoleListener());
        builder.addEventListeners(new AutoCreateChannels());
        builder.addEventListeners(new TabsenListener());
        builder.addEventListeners(new ActivityListener());
        builder.addEventListeners(new CommandListener());
        builder.addEventListeners(new GuildJoinListener());
        builder.addEventListeners(new SupportListener());
        builder.addEventListeners(new ReactionListener());
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("you and your stats"));
        builder.setMaxReconnectDelay(32);
        builder.setAutoReconnect(true);
        builder.setRequestTimeoutRetry(true);
        shardMan = builder.build();

    }

    public static void main(String[] args) throws LoginException {
        new Main();
    }
}

