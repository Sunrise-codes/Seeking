package me.seeking.event.events

import me.seeking.event.Event

class EventClickMouse(t: MouseType) : Event(Type.PRE) {
    var mtype:MouseType = t

    enum class MouseType{
        Left, Right
    }
}