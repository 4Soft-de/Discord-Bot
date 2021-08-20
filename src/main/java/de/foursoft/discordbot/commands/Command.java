package de.foursoft.discordbot.commands;

import net.dv8tion.jda.api.events.Event;

public abstract class Command<T extends Event> {

    public abstract String getName();

    public abstract void execute(T event);
}
