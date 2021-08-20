package de.foursoft.discordbot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
public class PingCommand extends GuildMessageReceivedCommand {

    public String getName() {
        return "ping";
    }

    @Override
    public void doExecute(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Pong!")
                .queue();  // IMPORTANT - .queue is needed when request is made
    }

}
