package de.foursoft.discordbot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {

    @Bean
    public EventWaiter eventWaiter(){
        return new EventWaiter();
    }
}
