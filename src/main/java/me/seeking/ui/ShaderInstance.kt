package me.seeking.ui

import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.opengl.GL20

class ShaderInstance : GLSLSandBox("shader.frag") {
    private var time = 0f
    override fun setupUniforms() {
        setupUniform("resolution")
        setupUniform("time")
    }

    override fun updateUniforms() {
        val scaledResolution = ScaledResolution(mc)
        val resolutionID = getUniform("resolution")!!
        if (resolutionID > -1) {
            GL20.glUniform2f(
                resolutionID,
                scaledResolution.scaledWidth.toFloat() * 2,
                scaledResolution.scaledHeight.toFloat() * 2
            )
        }
        val timeID = getUniform("time")!!
        if (timeID > -1) {
            GL20.glUniform1f(timeID, time)
        }
        time += 0.02f
    }
}
