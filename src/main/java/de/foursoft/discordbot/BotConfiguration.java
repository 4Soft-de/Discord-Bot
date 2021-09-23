package de.foursoft.discordbot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class BotConfiguration {

    @Autowired
    ListenerAdapterDispatcher dispatcher;

    @Bean
    public EventWaiter eventWaiter(){
        return new EventWaiter();
    }

    @Bean
    public EventListener eventListener(){
        return (EventListener) Proxy.newProxyInstance(
                BotConfiguration.class.getClassLoader(),
                new Class[] { EventListener.class },
                dispatcher);
    }
}
