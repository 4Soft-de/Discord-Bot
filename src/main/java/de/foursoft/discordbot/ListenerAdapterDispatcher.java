package de.foursoft.discordbot;

import de.foursoft.discordbot.eventconsumer.EventConsumer;
import net.dv8tion.jda.api.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ListenerAdapterDispatcher implements InvocationHandler {

    @Autowired
    private List<EventConsumer> allEventConsumers;

    private Map<Class, List<EventConsumer>> consumerByEvent;

    @PostConstruct
    private void init(){
        consumerByEvent = allEventConsumers.stream().collect(Collectors.groupingBy(EventConsumer::getClassOfT));
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if(args.length != 1){
            return null;
        }

        Object event = args[0];

        consumerByEvent.getOrDefault(event.getClass(), Collections.emptyList())
                .forEach(c -> c.execute((Event) event));

        return null;
    }
}
