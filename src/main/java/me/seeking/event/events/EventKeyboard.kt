package me.seeking.event.events

import me.seeking.event.Event

class EventKeyboard:Event {
    var key = 0;
    constructor(key:Int) : super(Type.PRE) {
        this.key=key;
    }
}