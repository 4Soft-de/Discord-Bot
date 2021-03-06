package de.foursoft.discordbot.listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;
import java.util.function.Consumer;

public class DadListener implements Consumer<GuildMessageReceivedEvent> {

    private static final Set<String> VALID_KEY_PHRASES;

    static {
        VALID_KEY_PHRASES = new HashSet<>();
        VALID_KEY_PHRASES.add("im");
        VALID_KEY_PHRASES.add("i am");
        VALID_KEY_PHRASES.add("ich bin");
        VALID_KEY_PHRASES.add("mein name ist");
    }

    @Override
    public void accept(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String contentRaw = message.getContentRaw();

        String command = contentRaw.toLowerCase(Locale.ROOT).trim();
        if (command.isEmpty()){
            return;
        }

        if (VALID_KEY_PHRASES.stream()
                .anyMatch(keyPhrase -> command.startsWith(keyPhrase + " ")))  {

            Optional<String> optionalName = VALID_KEY_PHRASES.stream()
                    .filter(command::startsWith)
                    .map(c -> command.replaceFirst(c, ""))
                    .findFirst();

            optionalName.ifPresent(name -> {
                TextChannel channel = event.getChannel();
                channel.sendMessage("Hi " + name.trim() + ", I'm dad")
                        .queue();  // IMPORTANT - .queue is needed when request is made
            });
        }
    }
}
