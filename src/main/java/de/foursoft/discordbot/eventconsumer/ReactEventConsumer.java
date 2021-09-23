package de.foursoft.discordbot.eventconsumer;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReactEventConsumer extends GuildMessageReceivedEventConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactEventConsumer.class);

    private static final String THUMBS_UP_UNICODE = "\uD83D\uDC4D";

    public String getName() {
        return "react";
    }

    @Override
    public void doExecute(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        message.addReaction(THUMBS_UP_UNICODE).queue(success -> {
            LOGGER.debug("Reaction worked!");
        }, failure -> {
            LOGGER.error("Reaction failed!", failure);
        });
    }
}
