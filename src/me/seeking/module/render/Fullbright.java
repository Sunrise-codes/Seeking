package me.seeking.module.render;

import me.seeking.event.EventTarget;
import me.seeking.event.events.EventRender2D;
import me.seeking.module.Module;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * This file is a part of Seeking Client.
 */
public class Fullbright extends Module {
    public Fullbright(){
        super("Fullbright", ModuleType.Render);
        setKey(Keyboard.KEY_M);
    }

    @Override
    public void onEnable(){
        mc.gameSettings.gammaSetting = 255;
    }
}
