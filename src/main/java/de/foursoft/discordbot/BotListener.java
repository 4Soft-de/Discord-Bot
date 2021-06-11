package de.foursoft.discordbot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.Permission;
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

public class BotListener extends ListenerAdapter {

    private static final Pattern WHITESPACES_PATTERN = Pattern.compile("\\s+");
    private static final Logger LOGGER = LoggerFactory.getLogger(BotListener.class);

    private static final String PREFIX = "!";
    private static final String THUMBS_UP_UNICODE = "\uD83D\uDC4D";

    private static final long PASSWORD_CATEGORY_ID = 852893820983050240L;
    private static final long FAIL_CHANNEL = 852912833024491520L;


    private static final Map<String, Long> PASSWORD_TO_CHANNELS = new HashMap<>();

    private final EventWaiter eventWaiter;

    public BotListener(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;

        PASSWORD_TO_CHANNELS.put("1234", 852912770969370624L);
        PASSWORD_TO_CHANNELS.put("1337", 852912856814845962L);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        Guild guild = event.getGuild();  // guild = server
        Member member = event.getMember();  // member = user in guild, can have roles
        Message message = event.getMessage();
        SelfUser selfUser = event.getJDA()
                .getSelfUser();


        // Mentions will converted to ids, Markdown characters are included
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
        } else if (command.equals("secret"))  {
            final Category pwCategory = guild.getCategoryById(PASSWORD_CATEGORY_ID);
            guild.createTextChannel("enter-the-password-"+user.getName(), pwCategory)
                    .addRolePermissionOverride(guild.getPublicRole().getIdLong(),null, Collections.singletonList(Permission.VIEW_CHANNEL))
                    .addMemberPermissionOverride(user.getIdLong(), Collections.singletonList(Permission.VIEW_CHANNEL), null)
                    .addMemberPermissionOverride(selfUser.getIdLong(), Collections.singletonList(Permission.VIEW_CHANNEL), null)
                    .queue(pwChannel -> {
                        eventWaiter.waitForEvent(GuildMessageReceivedEvent.class,
                                userResponse -> userResponse.getAuthor().equals(user) &&
                                        userResponse.getChannel().equals(pwChannel),
                                userResponse -> {

                                    handlePasswordResponse(userResponse);
                                    deleteChannel(pwChannel, 1, TimeUnit.MINUTES);
                                },
                                1, TimeUnit.MINUTES, () -> {
                                    deleteChannel(pwChannel);
                                });
                    });
        } else if (command.equals("reset")) {
            Category category = guild.getCategoryById(PASSWORD_CATEGORY_ID);
            if (category != null) {
                category.getChannels().forEach(passwordChannel -> {

                    if (guild.getSelfMember().hasPermission(passwordChannel, Permission.VIEW_CHANNEL)) {
                        deleteChannel(passwordChannel);
                    }

                });
            }
        }

    }

    private void handlePasswordResponse(GuildMessageReceivedEvent userResponse) {

        Long targetChannelId = PASSWORD_TO_CHANNELS.get(userResponse.getMessage()
                .getContentRaw());

        if (targetChannelId == null) {

            userResponse.getGuild().getTextChannelById(FAIL_CHANNEL).upsertPermissionOverride(userResponse.getMember()).setAllow(Permission.VIEW_CHANNEL);
        }
        if(userResponse.getMessage().getContentRaw().equals(SUPER_SECRET_PASSWORD)){
            userResponse.getChannel().sendMessage("password correct, you have permission to enter the channel");
        } if else (userResponse.getMessage().getContentRaw().equals(SUPER_SECRET_PASSWORD))

        else {
            userResponse.getChannel().sendMessage("password incorrect!");
        }
    }

    private void deleteChannel(GuildChannel passwordChannel, long timeoutValue, TimeUnit timeUnit) {
        passwordChannel.delete().queueAfter(timeoutValue, timeUnit);
    }

    private void deleteChannel(GuildChannel passwordChannel) {
       deleteChannel(passwordChannel, 0, TimeUnit.MILLISECONDS);
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
