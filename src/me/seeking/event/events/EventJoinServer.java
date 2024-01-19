package me.seeking.event.events;

import me.seeking.event.Event;

/**
 * This file is a part of Seeking Client.
 */
public class EventJoinServer extends Event {
    public long time;

    public EventJoinServer(long time) {
        super(Type.PRE);
        this.time = time;
    }
}
