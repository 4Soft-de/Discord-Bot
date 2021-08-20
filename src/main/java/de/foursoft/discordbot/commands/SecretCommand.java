package de.foursoft.discordbot.commands;

import static de.foursoft.discordbot.GuildUtilsAndConstantsAndOtherUsefulStuffAndShit.PASSWORD_CATEGORY_ID;
import static de.foursoft.discordbot.GuildUtilsAndConstantsAndOtherUsefulStuffAndShit.deleteChannel;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SecretCommand extends GuildMessageReceivedCommand {
    private static final Map<String, Long> PASSWORD_TO_CHANNELS = new HashMap<>();
    private static final long FAIL_CHANNEL = 852912833024491520L;
    private final EventWaiter eventWaiter;

    {
        PASSWORD_TO_CHANNELS.put("1234", 852912770969370624L);
        PASSWORD_TO_CHANNELS.put("1337", 852912856814845962L);
    }

    public SecretCommand(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;
    }

    @Override
    public String getName() {
        return "secret";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        User user = event.getAuthor();
        SelfUser selfUser = event.getJDA().getSelfUser();
        final Category pwCategory = guild.getCategoryById(PASSWORD_CATEGORY_ID);
        guild.createTextChannel("enter-the-password-" + user.getName(), pwCategory)
                .addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, Collections.singletonList(Permission.VIEW_CHANNEL))
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
    }

    private void handlePasswordResponse(GuildMessageReceivedEvent userResponse) {
        final TextChannel channel = userResponse.getChannel();

        Long targetChannelId = PASSWORD_TO_CHANNELS.get(userResponse.getMessage()
                .getContentRaw());

        long channelId;
        String responseMessage;
        if (targetChannelId == null) {
            channelId = FAIL_CHANNEL;
            responseMessage = "password incorrect!";
        } else {
            channelId = targetChannelId;
            responseMessage = "password correct, you have permission to enter the <#" + targetChannelId + ">";
        }
        TextChannel targetChannel = userResponse.getGuild().getTextChannelById(channelId);

        if (targetChannel == null) {
            userResponse.getGuild().getOwner().getUser().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage("Channel with id " + channelId + " does not exist anymore!").queue();
            });

            channel.sendMessage("Internal Server Error, please try again later.").queue();
            return;
        }

        targetChannel
                .upsertPermissionOverride(userResponse.getMember())
                .setAllow(Permission.VIEW_CHANNEL)
                .queue();
        channel.sendMessage(responseMessage).queue();
    }
}
