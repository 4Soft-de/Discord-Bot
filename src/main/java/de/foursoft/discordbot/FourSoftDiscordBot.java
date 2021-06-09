package de.foursoft.discordbot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FourSoftDiscordBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(FourSoftDiscordBot.class);
    
    private final JDA discordApi;
       
    public FourSoftDiscordBot() {
        Configuration config = new Configuration("config.properties");
        
        JDA tmpJda = null;
        try {
            tmpJda = JDABuilder.createDefault(config.getToken())  // init default settings
                        .setChunkingFilter(ChunkingFilter.ALL)  // load all members
                        .enableIntents(
                                GatewayIntent.GUILD_MEMBERS, // allow access to guild members
                                GatewayIntent.GUILD_PRESENCES  // access to online status and activities
                         )  
                        .setMemberCachePolicy(MemberCachePolicy.ALL)  // always cache members
                        .addEventListeners(new BotListener())  // enable the class to receive events
                        .build().awaitReady();  // wait until the API is ready
        } catch (LoginException e) {
            LOGGER.error("Could not start the Bot, invalid Token!");
            System.exit(-1);
        } catch (InterruptedException e) {
            LOGGER.error("Could not start the Bot, interrupted while waiting!");
            System.exit(-1);
        }
        
        discordApi = tmpJda;
    }
    
    public JDA getDiscordApi() {
        return discordApi;
    }

    public static void main(String[] args) {
        new FourSoftDiscordBot();
    }

}
