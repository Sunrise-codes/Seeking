package me.seeking.module.render;

import me.seeking.module.Module;
import me.seeking.ui.clickgui.ClickGraphicsUi;
import me.seeking.ui.clickgui.ClickUi;
import org.lwjgl.input.Keyboard;

/**
 * This file is a part of Seeking Client.
 * @author MiLiBlue
 */
public class ClickGui extends Module {
    public ClickGui(){
        super("ClickGui", ModuleType.Render);
        this.setKey(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable(){
        mc.displayGuiScreen(new ClickGraphicsUi());
    }
}
