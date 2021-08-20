package de.foursoft.discordbot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.foursoft.discordbot.commands.PingCommand;
import de.foursoft.discordbot.commands.ReactCommand;
import de.foursoft.discordbot.commands.ResetCommand;
import de.foursoft.discordbot.commands.SecretCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class FourSoftDiscordBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(FourSoftDiscordBot.class);

    private final JDA discordApi;

    private final EventWaiter eventWaiter = new EventWaiter();

    private final CommandRegistry commandRegistry = new CommandRegistry();

    public FourSoftDiscordBot() {
        Configuration config = new Configuration("config.properties");

        // init commands
        commandRegistry.addCommand("ping", new PingCommand())
                .addCommand("react", new ReactCommand())
                .addCommand("secret", new SecretCommand(eventWaiter))
                .addCommand("reset", new ResetCommand());

        JDA tmpJda = null;
        try {
            tmpJda = JDABuilder.createDefault(config.getToken())  // init default settings
                    .setChunkingFilter(ChunkingFilter.ALL)  // load all members
                    .enableIntents(
                            GatewayIntent.GUILD_MEMBERS, // allow access to guild members
                            GatewayIntent.GUILD_PRESENCES  // access to online status and activities
                    )
                    .setMemberCachePolicy(MemberCachePolicy.ALL)  // always cache members
                    .addEventListeners(new BotListener(commandRegistry))  // enable the class to receive events
                    .addEventListeners(eventWaiter)  // waits for events
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
