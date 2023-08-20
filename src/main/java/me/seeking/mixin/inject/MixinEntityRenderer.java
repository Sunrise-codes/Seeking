package me.seeking.mixin.inject;

import me.seeking.event.events.EventRenderWorld;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {
    @Inject(
            method = "renderWorldPass",
            at = @At("RETURN")
    )
    public void renderWorldHook(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci){
        new EventRenderWorld(partialTicks).call();
    }
}
