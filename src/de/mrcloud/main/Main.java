package de.mrcloud.main;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import de.mrcloud.SQL.SqlMain;
import de.mrcloud.listeners.*;
import de.mrcloud.listeners.csgo.AutoCreateChannels;
import de.mrcloud.listeners.csgo.GuildJoinLeaveListener;
import de.mrcloud.listeners.csgo.ReactionListener;
import de.mrcloud.listeners.csgo.RoleListener;
import de.mrcloud.listeners.moderation.*;
import de.mrcloud.listeners.statistiks.ActivityListener;
import de.mrcloud.listeners.statistiks.Twitch;
import de.mrcloud.utils.DataStorageClass;
import de.mrcloud.utils.JDAUtils;
import de.mrcloud.utils.Static;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {

    public static ShardManager shardMan;
    ActivityListener activityListener = new ActivityListener();
    TwitchClient twitchClient;

    public Main() throws LoginException {
        JDAUtils utils = new JDAUtils();
        try {
            SqlMain.mariaDB().createStatement();
            SqlMain.mariaDB().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Sets the gateway intends
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.create(Static.TOKEN, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS,GatewayIntent.DIRECT_MESSAGES);


        //Registers all event listeners
        builder.addEventListeners(new RoleListener());
        builder.addEventListeners(new AutoCreateChannels());
        builder.addEventListeners(new TabsenListener());
        builder.addEventListeners(new ActivityListener());
        builder.addEventListeners(new CommandListener());
        builder.addEventListeners(new GuildJoinLeaveListener());
        builder.addEventListeners(new SupportListener());
        builder.addEventListeners(new ReactionListener());
        builder.addEventListeners(new FunListener());
        builder.addEventListeners(new SearchingForMatchmakingListener());
        builder.addEventListeners(new SecurityListener());
        builder.addEventListeners(new TextListener());
        builder.addEventListeners(new DefenseListener());
        builder.addEventListeners(new DataStorageClass());
        builder.addEventListeners(new TimeUnbanEtcListener());
        //-----------------------------

        //builder settings
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("you and your stats"));
        builder.setMaxReconnectDelay(32);
        builder.setAutoReconnect(true);
        builder.setRequestTimeoutRetry(true);
        //----------------------------

        shardMan = builder.build();

        CredentialManager credentialManager = CredentialManagerBuilder.builder().build();
        credentialManager.registerIdentityProvider(new TwitchIdentityProvider("xfxosioihzjrye0d7v6d7x7pgm23jk", "znzvcykz3pwr9pxjcgauvyrlzfn945", "http://localhost/"));
        credentialManager.save();

        EventManager eventManager = new EventManager();
        eventManager.registerEventHandler(new SimpleEventHandler());
        Twitch myEventHandler = new Twitch();
        eventManager.getEventHandler(SimpleEventHandler.class).registerListener(myEventHandler);

        twitchClient = TwitchClientBuilder.builder()
                .withCredentialManager(credentialManager)
                .withDefaultAuthToken(new OAuth2Credential("twitch", "uxufslax9oq87ufujrnyxfvnbb79od"))
                .withEnableHelix(true)
                .withEventManager(eventManager)
                .build();

        twitchClient.getClientHelper().enableStreamEventListener("actualmrcloud");


        channelTime();
        TurnOffListener();


    }

    public static void main(String[] args) throws LoginException {
        new Main();

        //Only for Rank Comparing
        SearchingForMatchmakingListener.compare.put("unranked", 0);
        SearchingForMatchmakingListener.compare.put("Silver Ⅰ", 1);
        SearchingForMatchmakingListener.compare.put("Silver Ⅱ", 2);
        SearchingForMatchmakingListener.compare.put("Silver Ⅲ", 3);
        SearchingForMatchmakingListener.compare.put("Silver Ⅳ", 4);
        SearchingForMatchmakingListener.compare.put("Silver Elite", 5);
        SearchingForMatchmakingListener.compare.put("Silver Elite Master", 6);
        SearchingForMatchmakingListener.compare.put("Gold Nova Ⅰ", 7);
        SearchingForMatchmakingListener.compare.put("Gold Nova Ⅱ", 8);
        SearchingForMatchmakingListener.compare.put("Gold Nova Ⅲ", 9);
        SearchingForMatchmakingListener.compare.put("Gold Nova Ⅳ", 10);
        SearchingForMatchmakingListener.compare.put("Master Guardian Ⅰ", 11);
        SearchingForMatchmakingListener.compare.put("Master Guardian Ⅱ", 12);
        SearchingForMatchmakingListener.compare.put("Master Guardian Elite", 13);
        SearchingForMatchmakingListener.compare.put("Distinguished Master Guardian", 14);
        SearchingForMatchmakingListener.compare.put("Legendary Eagle", 15);
        SearchingForMatchmakingListener.compare.put("Legendary Eagle Master", 16);
        SearchingForMatchmakingListener.compare.put("Supreme Master First Class", 17);
        SearchingForMatchmakingListener.compare.put("Global Elite", 18);
        //---------------------------

        //Only for Rank Comparing
        SearchingForMatchmakingListener.compareReverse.put(1, "Silver Ⅰ");
        SearchingForMatchmakingListener.compareReverse.put(2, "Silver Ⅱ");
        SearchingForMatchmakingListener.compareReverse.put(3, "Silver Ⅲ");
        SearchingForMatchmakingListener.compareReverse.put(4, "Silver Ⅳ");
        SearchingForMatchmakingListener.compareReverse.put(5, "Silver Elite");
        SearchingForMatchmakingListener.compareReverse.put(6, "Silver Elite Master");
        SearchingForMatchmakingListener.compareReverse.put(7, "Gold Nova Ⅰ");
        SearchingForMatchmakingListener.compareReverse.put(8, "Gold Nova Ⅱ");
        SearchingForMatchmakingListener.compareReverse.put(9, "Gold Nova Ⅲ");
        SearchingForMatchmakingListener.compareReverse.put(10, "Gold Nova Ⅳ");
        SearchingForMatchmakingListener.compareReverse.put(11, "Master Guardian Ⅰ");
        SearchingForMatchmakingListener.compareReverse.put(12, "Master Guardian Ⅱ");
        SearchingForMatchmakingListener.compareReverse.put(13, "Master Guardian Elite");
        SearchingForMatchmakingListener.compareReverse.put(14, "Distinguished Master Guardian");
        SearchingForMatchmakingListener.compareReverse.put(15, "Legendary Eagle");
        SearchingForMatchmakingListener.compareReverse.put(16, "Legendary Eagle Master");
        SearchingForMatchmakingListener.compareReverse.put(17, "Supreme Master First Class");
        SearchingForMatchmakingListener.compareReverse.put(18, "Global Elite");
        //---------------------------

        //Only for Rank Comparing
        SearchingForMatchmakingListener.compareEmojiToRole.put("unranked", "unranked");
        SearchingForMatchmakingListener.compareEmojiToRole.put("s1", "Silver Ⅰ");
        SearchingForMatchmakingListener.compareEmojiToRole.put("s2", "Silver Ⅱ");
        SearchingForMatchmakingListener.compareEmojiToRole.put("s3", "Silver Ⅲ");
        SearchingForMatchmakingListener.compareEmojiToRole.put("s4", "Silver Ⅳ");
        SearchingForMatchmakingListener.compareEmojiToRole.put("se", "Silver Elite");
        SearchingForMatchmakingListener.compareEmojiToRole.put("sem", "Silver Elite Master");
        SearchingForMatchmakingListener.compareEmojiToRole.put("gn1", "Gold Nova Ⅰ");
        SearchingForMatchmakingListener.compareEmojiToRole.put("gn2", "Gold Nova Ⅱ");
        SearchingForMatchmakingListener.compareEmojiToRole.put("gn3", "Gold Nova Ⅲ");
        SearchingForMatchmakingListener.compareEmojiToRole.put("gn4", "Gold Nova Ⅳ");
        SearchingForMatchmakingListener.compareEmojiToRole.put("mg", "Master Guardian Ⅰ");
        SearchingForMatchmakingListener.compareEmojiToRole.put("mg2", "Master Guardian Ⅱ");
        SearchingForMatchmakingListener.compareEmojiToRole.put("mge", "Master Guardian Elite");
        SearchingForMatchmakingListener.compareEmojiToRole.put("dmg", "Distinguished Master Guardian");
        SearchingForMatchmakingListener.compareEmojiToRole.put("le", "Legendary Eagle");
        SearchingForMatchmakingListener.compareEmojiToRole.put("lem", "Legendary Eagle Master");
        SearchingForMatchmakingListener.compareEmojiToRole.put("supreme", "Supreme Master First Class");
        SearchingForMatchmakingListener.compareEmojiToRole.put("global", "Global Elite");
        //---------------------------

        DataStorageClass.muteReasonsTime.put("Voice Spam", 15);
        DataStorageClass.muteReasonsTime.put("Text Spam", 5);
        DataStorageClass.muteReasonsTime.put("Racism", 60);
        DataStorageClass.muteReasonsTime.put("NSFW", 1);

        DataStorageClass.muteTimeMultiplier.put(0, 1.0);
        DataStorageClass.muteTimeMultiplier.put(1, 2.0);
        DataStorageClass.muteTimeMultiplier.put(2, 2.5);
        DataStorageClass.muteTimeMultiplier.put(3, 3.0);
        DataStorageClass.muteTimeMultiplier.put(4, 5.0);
        DataStorageClass.muteTimeMultiplier.put(5, 7.0);
        DataStorageClass.muteTimeMultiplier.put(6, 12.0);

        Object[] set = DataStorageClass.muteReasonsTime.keySet().toArray();
        int i = 0;
        while (DataStorageClass.muteReasonsTime.keySet().size() > i) {
            DataStorageClass.muteReasonsList.add(set[i].toString());
            i++;
        }

        DataStorageClass.moderators.addAll(shardMan.getGuildById("514511396491231233").getMembersWithRoles(shardMan.getGuildById("514511396491231233").getRoles().stream().filter(role -> DataStorageClass.moderationRolesIDs.contains(role.getIdLong())).collect(Collectors.toList())));

    }

    //Waiting for someone to write "stop" to stop
    public void TurnOffListener() {
        new Thread(() -> {
            String line = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            try {
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("Stop")) {
                        Object[] set = ActivityListener.timeInChannel.keySet().toArray();
                        System.out.println(set[0]);
                        int i = 0;
                        while (ActivityListener.timeInChannel.keySet().iterator().hasNext()) {
                            activityListener.saveChannelTime(Objects.requireNonNull(shardMan.getGuildById("514511396491231233")).getMemberById(set[i].toString()), ActivityListener.timeInChannel);
                            System.out.println("Worked");
                            i++;
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
    //-------------------------------------------

    //Waits for a 4 seconds
    public void channelTime() {
        int i = 0;
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //-------------------

        //Saves the channel time on shutdown
        while ((Objects.requireNonNull(shardMan.getGuildById("514511396491231233"))).getVoiceStates().size() > i) {
            if (Objects.requireNonNull(shardMan.getGuildById("514511396491231233")).getVoiceStates().get(i).inVoiceChannel()) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
                ZonedDateTime hereAndNow = ZonedDateTime.now();
                String test = dateTimeFormatter.format(hereAndNow);
                String date = test.replaceAll(",", "");
                ActivityListener.timeInChannel.put(Objects.requireNonNull(shardMan.getGuildById("514511396491231233")).getVoiceStates().get(i).getMember().getId(), date);
            }
            i++;
        }
        //------------------------------------
    }

}


