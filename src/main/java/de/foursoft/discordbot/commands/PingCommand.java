package de.foursoft.discordbot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PingCommand extends Command {
    @Override
    String getName() {
        return "ping";
    }

    @Override
    void execute(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Pong!")
                .queue();  // IMPORTANT - .queue is needed when request is made
    }

}
