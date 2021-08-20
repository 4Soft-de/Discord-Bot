package de.foursoft.discordbot.eventconsumer;

import net.dv8tion.jda.api.events.Event;

public abstract class EventConsumer<T extends Event> {

    public abstract Class<T> getClassOfT();

    public abstract void execute(T event);
}
