package me.seeking.event.events;

import me.seeking.event.Event;

/**
 * This file is a part of Seeking Client
 */
public class EventStrafe extends Event {
    public float strafe;
    public float forward;
    public float friction;

    public EventStrafe(float strafe, float forward, float friction) {
        super(Type.PRE);
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
    }
}
