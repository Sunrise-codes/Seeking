package me.seeking.mixin.inject;

import me.seeking.Seeking;
import me.seeking.module.Module;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public class MixinGuiChat {
    @Inject(
            method = "mouseClicked",
            at = @At("RETURN")
    )
    public void mouseClickedHook(int mouseX, int mouseY, int mouseButton, CallbackInfo ci){
        for (Module m : Seeking.instance.getModuleManager().getModules()){
            m.mouseClick(mouseX, mouseY, mouseButton);
        }
    }

    @Inject(
            method = "drawScreen",
            at = @At("RETURN")
    )
    public void dragHook(int mouseX, int mouseY, float partialTicks, CallbackInfo ci){
        for (Module m : Seeking.instance.getModuleManager().getModules()){
            m.doGrag(mouseX, mouseY);
        }
    }
}
