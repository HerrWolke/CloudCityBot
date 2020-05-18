package de.mrcloud.main;

import de.mrcloud.listeners.*;
import de.mrcloud.utils.JDAUtils;
import de.mrcloud.utils.Static;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public ShardManager shardMan;
    ActivityListener activityListener = new ActivityListener();
    public Main() throws LoginException {
        JDAUtils utils = new JDAUtils();


        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.create(Static.TOKEN, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS);
        builder.addEventListeners(new RoleListener());
        builder.addEventListeners(new AutoCreateChannels());
        builder.addEventListeners(new TabsenListener());
        builder.addEventListeners(new ActivityListener());
        builder.addEventListeners(new CommandListener());
        builder.addEventListeners(new GuildJoinListener());
        builder.addEventListeners(new SupportListener());
        builder.addEventListeners(new ReactionListener());
        builder.addEventListeners(new FunListener());
        builder.addEventListeners(new SearchingForMatchmakingListener());
        builder.addEventListeners(new SecurityListener());
        builder.addEventListeners(new TextListener());
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("you and your stats"));
        builder.setMaxReconnectDelay(32);
        builder.setAutoReconnect(true);
        builder.setRequestTimeoutRetry(true);
        shardMan = builder.build();
        TurnOffListener();
    }

    public void TurnOffListener() {
        new Thread(() -> {
            String line = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            try {
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("Stop")) {

                        for ( String key : activityListener.timeInChannel.keySet() ) {
                            activityListener.saveChannelTime(shardMan.getGuildById("514511396491231233").getMemberById(key),activityListener.timeInChannel);
                            System.out.println("Worked");
                        }
                        if (shardMan != null) {
                            shardMan.setStatus(OnlineStatus.OFFLINE);
                            shardMan.setActivity(Activity.listening("offline"));
                            shardMan.shutdown();
                            System.out.println("Bot wird heruntergefahren");
                            System.exit(1);

                        }
                    } else {
                        System.out.println("Bitte benutz stop!");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public static void main(String[] args) throws LoginException {
        new Main();

    }

    }


