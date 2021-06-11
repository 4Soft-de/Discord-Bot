package de.foursoft.discordbot;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotListener extends ListenerAdapter {

    private static final Pattern WHITESPACES_PATTERN = Pattern.compile("\\s+");
    private static final Logger LOGGER = LoggerFactory.getLogger(BotListener.class);
    
    private static final String PREFIX = "!";
    private static final String THUMBS_UP_UNICODE = "\uD83D\uDC4D";
    
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        Guild guild = event.getGuild();  // guild = server
        Member member = event.getMember();  // member = user in guild, can have roles
        Message message = event.getMessage();
        
        // Mentions will be converted to ids, Markdown characters are included
        String contentRaw = message.getContentRaw();
        
        LOGGER.info("{}: {}", user.getAsTag(), contentRaw);
        
        if (!contentRaw.startsWith(PREFIX))  {
            return;
        }
        
        // split at whitespaces
        final String contentWithoutPrefix = contentRaw.substring(PREFIX.length()).trim();
        String[] args = WHITESPACES_PATTERN.split(contentWithoutPrefix);

        TextChannel channel = event.getChannel();
        
        String command = args[0].toLowerCase();  // args will never be empty due to the impl of split
        if (command.isEmpty())  {
            return;
        }

        LOGGER.debug("Command: {}", command);
        if (command.equals("ping"))  {
            channel.sendMessage("Pong!")
                .queue();  // IMPORTANT - .queue is needed when request is made
        }  else if (command.equals("react"))  {
            message.addReaction(THUMBS_UP_UNICODE).queue(success -> {
                LOGGER.debug("Reaction worked!");
            }, failure -> {
                LOGGER.error("Reaction failed!", failure);
            });
        }  else if (command.equals("edit"))  {
            channel.sendMessage("I will edit the message in 5 seconds!").queue(sentMessage -> {
                sentMessage.editMessage("New Content!").queueAfter(5, TimeUnit.SECONDS);
            }, failure -> {
                LOGGER.error("Failed to send message!", failure);
            });
        }  else if (command.equals("dm"))  {
            // private channel needs to be opened
            user.openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage("helo").queue(dm -> {
                    channel.sendMessage("Check your DMs. :)").queue();
                }, failure -> {
                    channel.sendMessage("Couldn't sent you a DM. :(").queue();
                });
            });            
        }
        
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
