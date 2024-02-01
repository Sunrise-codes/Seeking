package me.seeking.module.player;

import me.seeking.event.EventTarget;
import me.seeking.event.events.EventUpdate;
import me.seeking.module.Module;
import net.minecraft.client.settings.KeyBinding;
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
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
    }
}
