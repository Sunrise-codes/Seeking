package me.seeking.module.player;

import me.seeking.event.EventTarget;
import me.seeking.event.events.EventPacket;
import me.seeking.module.Module;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;

/**
 * This file is a part of Seeking Client.
 */
public class AutoGG extends Module {
    public AutoGG(){
        super("AutoGG", ModuleType.Player);
    }

    @EventTarget
    public void onPacket(EventPacket e){
        if(e.getPacket() instanceof S02PacketChat){
            S02PacketChat s02 = (S02PacketChat) e.getPacket();
        }
    }
}
