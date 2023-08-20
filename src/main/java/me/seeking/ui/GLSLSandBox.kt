package me.seeking.ui

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.apache.commons.io.IOUtils
import org.lwjgl.opengl.*
import java.io.InputStream

abstract class GLSLSandBox(fragmentShader: String) {
    private var program: Int = 0
    private var uniformsMap: MutableMap<String, Int>? = null

    companion object {
        val mc: Minecraft = Minecraft.getMinecraft()
    }

    init {
        var vertexShaderID: Int = 0
        var fragmentShaderID: Int = 0

        try {
            val vertexStream: InputStream = javaClass.getResourceAsStream("/assets/minecraft/seeking/vertex.vert")
            vertexShaderID = createShader(IOUtils.toString(vertexStream), ARBVertexShader.GL_VERTEX_SHADER_ARB)
            IOUtils.closeQuietly(vertexStream)

            val fragmentStream: InputStream = javaClass.getResourceAsStream("/assets/minecraft/seeking/shaders/$fragmentShader")
            fragmentShaderID = createShader(IOUtils.toString(fragmentStream), ARBFragmentShader.GL_FRAGMENT_SHADER_ARB)
            IOUtils.closeQuietly(fragmentStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (!(vertexShaderID == 0 || fragmentShaderID == 0)) {

            program = ARBShaderObjects.glCreateProgramObjectARB()

            if (!(program == 0)) {

                ARBShaderObjects.glAttachObjectARB(program, vertexShaderID)
                ARBShaderObjects.glAttachObjectARB(program, fragmentShaderID)

                ARBShaderObjects.glLinkProgramARB(program)
                ARBShaderObjects.glValidateProgramARB(program)
            }
        }
    }

    fun startShader() {
        GL11.glPushMatrix()
        GL20.glUseProgram(program)

        if (uniformsMap == null) {
            uniformsMap = HashMap()
            setupUniforms()
        }

        updateUniforms()
    }

    fun renderShader(width: Int, height: Int) {
        try {
            startShader()
            GL11.glScalef(1f, 1f, 1f)
            val instance: Tessellator = Tessellator.getInstance()
            val worldRenderer: WorldRenderer = instance.getWorldRenderer()
            worldRenderer.begin(7, DefaultVertexFormats.POSITION)
            worldRenderer.pos(0.0, height.toDouble(), 0.0).endVertex()
            worldRenderer.pos(width.toDouble(), height.toDouble(), 0.0).endVertex()
            worldRenderer.pos(width.toDouble(), 0.0, 0.0).endVertex()
            worldRenderer.pos(0.0, 0.0, 0.0).endVertex()
            instance.draw()
            stopShader()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun stopShader() {
        GL20.glUseProgram(0)
        GL11.glPopMatrix()
    }

    abstract fun setupUniforms()

    abstract fun updateUniforms()

    private fun createShader(shaderSource: String, shaderType: Int): Int {
        var shader = 0

        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType)

            if (shader == 0)
                return 0

            ARBShaderObjects.glShaderSourceARB(shader, shaderSource)
            ARBShaderObjects.glCompileShaderARB(shader)

            if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                throw RuntimeException("Error creating shader: " + getLogInfo(shader))

            return shader
        } catch (e: Exception) {
            ARBShaderObjects.glDeleteObjectARB(shader)
            throw e
        }
    }

    private fun getLogInfo(i: Int): String {
        return ARBShaderObjects.glGetInfoLogARB(
            i,
            ARBShaderObjects.glGetObjectParameteriARB(i, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB)
        )
    }

    fun setUniform(uniformName: String, location: Int) {
        uniformsMap?.put(uniformName, location)
    }

    fun setupUniform(uniformName: String) {
        setUniform(uniformName, GL20.glGetUniformLocation(program, uniformName))
    }

    fun getUniform(uniformName: String): Int? {
        return uniformsMap?.get(uniformName)
    }
}