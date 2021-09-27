package de.maggiwuerze.xdccloader.events;

import org.springframework.context.ApplicationEvent;

public class EntityUpdateEvent<T> extends ApplicationEvent {
    private final T payload;

    public EntityUpdateEvent(Object source, T payload) {
        super(source);
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }
}