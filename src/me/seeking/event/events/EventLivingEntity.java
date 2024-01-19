package me.seeking.event.events;

import me.seeking.event.Event;
import net.minecraft.entity.EntityLivingBase;

public class EventLivingEntity extends Event {
    public EventLivingEntity(EntityLivingBase entity){
        super(Type.POST);
        this.entity = entity;
    }
    EntityLivingBase entity;

    public EntityLivingBase getEntity() {
        return entity;
    }

    public void setEntity(EntityLivingBase entity) {
        this.entity = entity;
    }
}
