package de.foursoft.discordbot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;

@Component
public class BotInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(FourSoftDiscordBot.class);

    @Autowired
    private EventWaiter eventWaiter;
    @Autowired
    private EventListener eventListener;

    @PostConstruct
    private void init(){
        Configuration config = new Configuration("config.properties");

        try {
            JDABuilder.createDefault(config.getToken())  // init default settings
                    .setChunkingFilter(ChunkingFilter.ALL)  // load all members
                    .enableIntents(
                            GatewayIntent.GUILD_MEMBERS, // allow access to guild members
                            GatewayIntent.GUILD_PRESENCES  // access to online status and activities
                    )
                    .setMemberCachePolicy(MemberCachePolicy.ALL)  // always cache members
                    .addEventListeners(eventListener)
                    .addEventListeners(eventWaiter)  // waits for events
                    .build().awaitReady();  // wait until the API is ready
        } catch (LoginException e) {
            LOGGER.error("Could not start the Bot, invalid Token!");
            System.exit(-1);
        } catch (InterruptedException e) {
            LOGGER.error("Could not start the Bot, interrupted while waiting!");
            System.exit(-1);
        }
    }
}
