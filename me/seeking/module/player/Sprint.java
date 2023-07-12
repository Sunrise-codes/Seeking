package me.seeking.module.player;

import me.seeking.event.EventTarget;
import me.seeking.event.events.EventUpdate;
import me.seeking.module.Module;
import org.lwjgl.input.Keyboard;

/**
 * This file is a part of Seeking Client.
 * @author MiLiBlue
 */
public class Sprint extends Module {
    public Sprint(){
        super("Sprint", ModuleType.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate e){
        if(!mc.thePlayer.isSprinting() && mc.gameSettings.keyBindForward.isKeyDown() && mc.thePlayer.getFoodStats().getFoodLevel() > 6 && !(mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown())){
            mc.thePlayer.setSprinting(true);
        }
    }
}
