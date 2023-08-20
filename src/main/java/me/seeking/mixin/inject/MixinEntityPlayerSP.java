package me.seeking.mixin.inject;

import me.seeking.Seeking;
import me.seeking.event.Event;
import me.seeking.event.events.EventUpdate;
import me.seeking.managers.CommandManager;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {
    @Inject(
            method = "onUpdateWalkingPlayer",
            at = @At("HEAD")
    )
    public void hookPreUpdate(CallbackInfo ci){
        new EventUpdate(Event.Type.PRE).call();
    }

    @Inject(
            method = "onUpdateWalkingPlayer",
            at = @At("RETURN")
    )
    public void hookPostUpdate(CallbackInfo ci){
        new EventUpdate(Event.Type.POST).call();
    }

    @Inject(
            method = "sendChatMessage",
            at = @At("HEAD"),
            cancellable = true
    )
    public void sendMessageHook(String message,CallbackInfo ci){
        if(Objects.requireNonNull(Seeking.instance.getCommandManager()).processCommand(message)){
            ci.cancel();
        }
    }
}
