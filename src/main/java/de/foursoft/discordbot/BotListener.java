package de.foursoft.discordbot;

import de.foursoft.discordbot.commands.Command;
import de.foursoft.discordbot.listener.DadListener;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class BotListener extends ListenerAdapter {

    private static final Pattern WHITESPACES_PATTERN = Pattern.compile("\\s+");
    private static final Logger LOGGER = LoggerFactory.getLogger(BotListener.class);

    private static final String PREFIX = "!";

//    private static final Map<Consumer>
//
//    static {
//        MAP.put(ListenerAdapter::onStageInstanceDelete, DadListener.class);
//    }

    private final CommandRegistry commandRegistry;

    public BotListener(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        Message message = event.getMessage();


        // Mentions will converted to ids, Markdown characters are included
        String contentRaw = message.getContentRaw();

        LOGGER.info("{}: {}", user.getAsTag(), contentRaw);

        //TODO insert into generic event dispatcher
        new DadListener().accept(event);

        if (!contentRaw.startsWith(PREFIX)) {
            return;
        }

        // split at whitespaces
        final String contentWithoutPrefix = contentRaw.substring(PREFIX.length()).trim();
        String[] args = WHITESPACES_PATTERN.split(contentWithoutPrefix);

        String userCommand = args[0].toLowerCase();  // args will never be empty due to the impl of split
        if (userCommand.isEmpty()) {
            return;
        }

        final Command<GuildMessageReceivedEvent> cmd = commandRegistry.getCommand(userCommand);
        if (cmd == null) {
            return;
        }

        cmd.execute(event);

    }


    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        // means a message was edited or (un)pinned
        // NOTE: Previous Content / State is not provided by the API

        Message message = event.getMessage();

        LOGGER.info("[MESSAGE EDIT] {}", message.getContentRaw());
    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        // NOTE: Message Content / State is not provided by the API

        TextChannel channel = event.getChannel();
        long msgId = event.getMessageIdLong();
        LOGGER.info("Message with Id {} was deleted from {}", msgId, channel.getName());
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        Member member = event.getMember();
        long msgId = event.getMessageIdLong();
        ReactionEmote reaction = event.getReactionEmote();  // can be emoji or emote

        boolean isEmoji = reaction.isEmoji();  // default emojis like :D
        boolean isEmote = reaction.isEmote();  // emote = custom emoji, is bound to a server

        LOGGER.info("{} reacted to Message with Id {}", member.getUser().getAsTag(), msgId);
    }


    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
        // Same as above

        LOGGER.info("{} removed reaction from Message with Id {}", event.getUser().getAsTag(), event.getMessageIdLong());
    }

}
