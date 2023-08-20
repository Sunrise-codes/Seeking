package me.seeking.mixin.inject;

import me.seeking.mixin.interfaces.IRenderManager;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderManager.class)
public class MixinRenderManager implements IRenderManager {
    @Shadow
    private double renderPosX;

    @Shadow
    private double renderPosY;

    @Shadow
    private double renderPosZ;
    @Override
    public double renderPosX() {
        return renderPosX;
    }

    @Override
    public double renderPosY() {
        return renderPosY;
    }

    @Override
    public double renderPosZ() {
        return renderPosZ;
    }
}
