package me.seeking.ui;

import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

/**
 * This file is a part of Seeking Client.
 */
public class ShaderInstance extends GLSLSandBox {
    private float time;

    public ShaderInstance() {
        super("shader.frag");
    }

    @Override
    public void setupUniforms() {
        setupUniform("resolution");
        setupUniform("time");
    }

    @Override
    public void updateUniforms() {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);

        final int resolutionID = getUniform("resolution");
        if (resolutionID > -1) {
            GL20.glUniform2f(resolutionID, (float) scaledResolution.getScaledWidth() * 2, (float) scaledResolution.getScaledHeight() * 2);
        }

        final int timeID = getUniform("time");
        if (timeID > -1) {
            GL20.glUniform1f(timeID, time);
        }
        this.time += 0.02f;
    }

}
