package me.seeking.event.events;

import me.seeking.event.Event;

public class EventRenderWorld extends Event {
    private float partialTicks;
    public EventRenderWorld(float partialTicks) {
        super(Type.POST);
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
