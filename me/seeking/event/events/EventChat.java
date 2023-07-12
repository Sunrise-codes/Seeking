package me.seeking.event.events;

import me.seeking.event.Event;

/**
 * @author: Thr1c0/s
 * @date: 2022/8/3 13:12
 */
public class EventChat extends Event {
    private String message;

    public EventChat(String message) {
        super(Type.PRE);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
