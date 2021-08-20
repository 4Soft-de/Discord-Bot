package de.foursoft.discordbot.eventconsumer;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReactionAddEventConsumer extends EventConsumer<GuildMessageReactionAddEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactionAddEventConsumer.class);

    @Override
    public Class<GuildMessageReactionAddEvent> getClassOfT() {
        return GuildMessageReactionAddEvent.class;
    }

    @Override
    public void execute(GuildMessageReactionAddEvent event) {
        Member member = event.getMember();
        long msgId = event.getMessageIdLong();
        MessageReaction.ReactionEmote reaction = event.getReactionEmote();  // can be emoji or emote

        boolean isEmoji = reaction.isEmoji();  // default emojis like :D
        boolean isEmote = reaction.isEmote();  // emote = custom emoji, is bound to a server

        LOGGER.info("{} reacted to Message with Id {}", member.getUser().getAsTag(), msgId);
    }
}
