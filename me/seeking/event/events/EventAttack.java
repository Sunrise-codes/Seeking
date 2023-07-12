package me.seeking.event.events;

import me.seeking.event.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

import java.text.DecimalFormat;

/**
 * This file is a part of Seeking Client.
 */
public class EventAttack extends Event {
    private Entity entity;
    private double reach;
    DecimalFormat df = new DecimalFormat("#.00");
    public EventAttack(Entity e){
        super(Type.PRE);
        this.entity = e;
        reach = Double.parseDouble(df.format(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity)));
    }

    public Entity getEntity() {
        return entity;
    }

    public double getReach() {
        return reach;
    }
}
