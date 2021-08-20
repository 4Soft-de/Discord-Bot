package de.foursoft.discordbot.eventconsumer;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DadEventConsumer extends EventConsumer<GuildMessageReceivedEvent> {

    private static final Set<String> VALID_KEY_PHRASES;

    static {
        VALID_KEY_PHRASES = new HashSet<>();
        VALID_KEY_PHRASES.add("im");
        VALID_KEY_PHRASES.add("i am");
        VALID_KEY_PHRASES.add("ich bin");
        VALID_KEY_PHRASES.add("mein name ist");
    }

    @Override
    public Class<GuildMessageReceivedEvent> getClassOfT() {
        return GuildMessageReceivedEvent.class;
    }

    @Override
    public void execute(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String contentRaw = message.getContentRaw();

        String command = contentRaw.toLowerCase(Locale.ROOT).trim();
        if (command.isEmpty()){
            return;
        }

        if (VALID_KEY_PHRASES.stream().anyMatch(command::startsWith))  {


            Optional<String> optionalName = VALID_KEY_PHRASES.stream()
                    .filter(command::startsWith)
                    .map(c -> command.replace(c, ""))
                    .findFirst();

            optionalName.ifPresent(name -> {
                TextChannel channel = event.getChannel();
                channel.sendMessage("Hi " + name)
                        .queue();  // IMPORTANT - .queue is needed when request is made
            });
        }
    }
}
