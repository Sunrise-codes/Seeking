package me.seeking.mixin.inject;

import me.seeking.event.events.EventRender2D;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {
    @Inject(
            method = "renderTooltip",
            at = @At("HEAD")
    )
    public void render2DHook(CallbackInfo ci){
        new EventRender2D().call();
    }
}
