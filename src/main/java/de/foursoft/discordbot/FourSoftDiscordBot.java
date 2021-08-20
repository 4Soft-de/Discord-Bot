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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.security.auth.login.LoginException;

@SpringBootApplication
@ComponentScan("de.foursoft")
public class FourSoftDiscordBot {

    public static void main(String[] args) {
        SpringApplication.run(FourSoftDiscordBot.class, args);
    }

}
