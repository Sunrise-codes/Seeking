package me.seeking.mixin.inject;

import me.seeking.Seeking;
import me.seeking.event.events.EventClickMouse;
import me.seeking.event.events.EventKeyboard;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(
            method = "startGame",
            at = { @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift = At.Shift.AFTER) }
    )
    public void startGameHook(CallbackInfo ci){
        Seeking.instance.start();
    }

    @Inject(
            method = "shutdownMinecraftApplet",
            at = @At("HEAD")
    )
    public void stopGameHook(CallbackInfo ci){
        Seeking.instance.stop();
    }

    @Inject(
            method = "dispatchKeypresses",
            at = @At("HEAD")
    )
    public void hookKeyboard(CallbackInfo ci){
        new EventKeyboard(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() : Keyboard.getEventKey()).call();
    }

    @Inject(
            method = "clickMouse",
            at = @At("HEAD")
    )
    public void hookClickLeftMouse(CallbackInfo ci){
        new EventClickMouse(EventClickMouse.MouseType.Left).call();
    }
}
