package de.foursoft.discordbot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class Command {

    public abstract String getName();

    public abstract void execute(GuildMessageReceivedEvent event);
}
