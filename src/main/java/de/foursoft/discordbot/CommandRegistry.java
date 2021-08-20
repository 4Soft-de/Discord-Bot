package de.foursoft.discordbot;

import de.foursoft.discordbot.commands.Command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, Command> commands = new HashMap<>();

    public CommandRegistry addCommand(String name, Command command)  {
        commands.put(name, command);
        return this;
    }

    public Command getCommand(String name)  {
        return commands.get(name);
    }

}
