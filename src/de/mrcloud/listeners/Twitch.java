package de.mrcloud.listeners;

import com.github.philippheuer.events4j.api.domain.IEvent;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.common.events.channel.ChannelGoLiveEvent;
import de.mrcloud.main.Main;
import de.mrcloud.utils.Static;

public class Twitch implements IEventHandler {

    @EventSubscriber
    public void hasStartedStreaming(ChannelGoLiveEvent event) {
        System.out.println(event.getChannel().getName());
        Main.shardMan.getGuildById("514511396491231233").getTextChannelById("712264677698306068").sendMessage("Hi. Mr Cloud hat angefangen zu streamen! Schaut jetzt zu über " +
                " https://www.twitch.tv/" + event.getChannel().getName()).queue();
    }

    @Override
    public void publish(IEvent iEvent) {

    }

    @Override
    public void close() throws Exception {
    }
}
