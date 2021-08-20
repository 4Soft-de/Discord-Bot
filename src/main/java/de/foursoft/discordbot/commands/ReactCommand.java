package de.foursoft.discordbot.commands;

import de.foursoft.discordbot.FourSoftDiscordBot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReactCommand extends GuildMessageReceivedCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactCommand.class);

    private static final String THUMBS_UP_UNICODE = "\uD83D\uDC4D";

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void execute(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        message.addReaction(THUMBS_UP_UNICODE).queue(success -> {
            LOGGER.debug("Reaction worked!");
        }, failure -> {
            LOGGER.error("Reaction failed!", failure);
        });
    }
}
