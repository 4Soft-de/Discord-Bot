package de.foursoft.discordbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("de.foursoft")
public class FourSoftDiscordBot {

    public static void main(String[] args) {
        SpringApplication.run(FourSoftDiscordBot.class, args);
    }

}
