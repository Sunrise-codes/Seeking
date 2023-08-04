package me.seeking.module.player;

import me.seeking.event.EventTarget;
import me.seeking.event.events.EventPacket;
import me.seeking.event.events.EventStrafe;
import me.seeking.module.Module;
import me.seeking.utils.PlayerUtil;
import me.seeking.value.Option;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.MathHelper;

/**
 * This file is a part of Seeking Client
 */
public class PacketFix extends Module
{
    public Option cancelC0A = new Option("Cancel C0A", true);
    public Option fixCollisionBoundingBox = new Option("Fix CollisionBoundingBox", true);
    public Option fixLadder = new Option("Fix Ladder", true);
    public Option fixHitbox = new Option("Fix Hitbox", true);
    public Option fixMove = new Option("Fix Move", true);
    public Option fixStrafe = new Option("Fix Strafe", true);
    public PacketFix(){
        super("PacketFix", ModuleType.Player);
        addValues(cancelC0A, fixCollisionBoundingBox, fixLadder, fixHitbox, fixMove, fixStrafe);
    }

    @EventTarget
    public void onPacket(EventPacket e){
        //Cancel C0A
        if(e.getPacket() instanceof C0APacketAnimation && cancelC0A.getValue()){
            e.setCancelled(true);
        }
    }

    @EventTarget
    public void runStrafeFixLoop(EventStrafe event) {
        if(fixStrafe.getValue()) {
            //val (yaw) = RotationUtils.targetRotation ?: return
            float strafe = event.strafe;
            float forward = event.forward;
            float friction = event.friction;
            float factor = strafe * strafe + forward * forward;

            int angleDiff = (int) ((MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw - 0 - 22.5f - 135.0f) + 180.0) / (45.0));
            float calcYaw = 45.0f * angleDiff;

            float calcMoveDir = Math.max(Math.abs(strafe), Math.abs(forward));
            calcMoveDir = calcMoveDir * calcMoveDir;
            float calcMultiplier = MathHelper.sqrt_float(calcMoveDir / Math.min(1.0f, calcMoveDir * 2.0f));

            switch (angleDiff) {
                case 1: {
                    if ((Math.abs(forward) > 0.005 || Math.abs(strafe) > 0.005) && !(Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005)) {
                        friction = friction / calcMultiplier;
                    } else if (Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005) {
                        friction = friction * calcMultiplier;
                    }
                }
                case 3: {
                    if ((Math.abs(forward) > 0.005 || Math.abs(strafe) > 0.005) && !(Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005)) {
                        friction = friction / calcMultiplier;
                    } else if (Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005) {
                        friction = friction * calcMultiplier;
                    }
                }
                case 5: {
                    if ((Math.abs(forward) > 0.005 || Math.abs(strafe) > 0.005) && !(Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005)) {
                        friction = friction / calcMultiplier;
                    } else if (Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005) {
                        friction = friction * calcMultiplier;
                    }
                }
                case 7: {
                    if ((Math.abs(forward) > 0.005 || Math.abs(strafe) > 0.005) && !(Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005)) {
                        friction = friction / calcMultiplier;
                    } else if (Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005) {
                        friction = friction * calcMultiplier;
                    }
                }
                case 9: {
                    if ((Math.abs(forward) > 0.005 || Math.abs(strafe) > 0.005) && !(Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005)) {
                        friction = friction / calcMultiplier;
                    } else if (Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005) {
                        friction = friction * calcMultiplier;
                    }
                }
            }
            if (factor >= 1.0E-4F) {
                factor = MathHelper.sqrt_float(factor);

                if (factor < 1.0F) {
                    factor = 1.0F;
                }

                factor = friction / factor;
                strafe *= factor;
                forward *= factor;

                float yawSin = MathHelper.sin((float) (calcYaw * Math.PI / 180F));
                float yawCos = MathHelper.cos((float) (calcYaw * Math.PI / 180F));

                mc.thePlayer.motionX += strafe * yawCos - forward * yawSin;
                mc.thePlayer.motionZ += forward * yawCos + strafe * yawSin;
            }
            event.setCancelled(true);
        }
    }
}
