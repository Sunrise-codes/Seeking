package me.seeking.module.player

import me.seeking.event.EventTarget
import me.seeking.event.events.EventUpdate
import me.seeking.module.Module
import net.minecraft.client.settings.KeyBinding
import net.minecraft.network.play.client.C01PacketChatMessage

class Sprint:Module("Sprint", ModuleType.Player) {
    @EventTarget
    fun onUpdate(e:EventUpdate){
        if(mc.gameSettings.keyBindForward.isKeyDown)
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.keyCode, true)
    }
}