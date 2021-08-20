package de.foursoft.discordbot;

import de.foursoft.discordbot.commands.Command;
import net.dv8tion.jda.api.events.Event;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, Command<? extends Event>> commands = new HashMap<>();

    public CommandRegistry addCommand(String name, Command<? extends Event> command)  {
        commands.put(name, command);
        return this;
    }

    public Command<? extends Event> getCommand(String name)  {
        return commands.get(name);
    }

}
