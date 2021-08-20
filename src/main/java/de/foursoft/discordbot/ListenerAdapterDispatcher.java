package de.foursoft.discordbot;

import de.foursoft.discordbot.commands.Command;
import net.dv8tion.jda.api.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

@Component
public class ListenerAdapterDispatcher implements InvocationHandler {

    @Autowired
    private List<Command> allCommands;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if(args.length != 1){
            return null;
        }

        Object event = args[0];

        allCommands.stream()
                .filter(c -> c.getClassOfT().equals(event.getClass()))
                .forEach(c -> c.execute((Event) event));

        return null;
    }
}
