package me.seeking.module.render;

import me.seeking.module.Module;
import me.seeking.ui.drag.GuiDrag;

/**
 * This file is a part of Seeking Client.
 */
public class Drag extends Module {
    public Drag(){
        super("Drag", ModuleType.Render);
    }

    @Override
    public void onEnable(){
        mc.displayGuiScreen(new GuiDrag());
        setEnable(false);
    }
}
