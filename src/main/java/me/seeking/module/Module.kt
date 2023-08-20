package me.seeking.module

import me.seeking.Seeking
import me.seeking.value.Value
import net.minecraft.client.Minecraft
import org.lwjgl.input.Mouse

open class Module {
    private var name: String? = null
    private var moduleType: ModuleType? = null
    private var key = 0
    private var enable = false
    protected var mc = Minecraft.getMinecraft()
    private var x = 0.0
    private  var y:Double = 0.0
    private  var width: Double = 0.0
    private  var height: Double = 0.0
    private var dragging = false
    var valuesList: ArrayList<Value<*>> = ArrayList<Value<*>>()

    constructor (name: String?, moduleType: ModuleType?) {
        this.name = name
        this.moduleType = moduleType
    }

    fun getValues(): ArrayList<Value<*>> {
        return valuesList
    }

    /**
     * 返回Module的名字
     * @return name
     */
    fun getName(): String? {
        return name
    }

    /**
     * 返回Module的类型
     * @return module's type
     */
    fun getModuleType(): ModuleType? {
        return moduleType
    }

    /**
     * 返回Module的按键
     * @return key
     */
    fun getKey(): Int {
        return key
    }

    /**
     * 设置Module的按键.
     * @param key
     */
    fun setKey(key: Int) {
        this.key = key
    }

    /**
     * 获取Module的开启状态
     * @return enable state
     */
    fun isEnable(): Boolean {
        return enable
    }

    /**
     * 设置Module的开启状态
     * @param enable
     */
    fun setEnable(enable: Boolean) {
        this.enable = enable
        if (enable) {
            onEnable()
            Seeking.instance.eventManager!!.register(this)
        } else {
            onDisable()
            Seeking.instance.eventManager!!.unregister(this)
        }
    }

    fun addValues(vararg value: Value<*>) {
        for (v in valuesList) {
            valuesList.add(v)
        }
    }

    fun getX(): Double {
        return x
    }

    fun setX(x: Double) {
        this.x = x
    }

    fun getY(): Double {
        return y
    }

    fun setY(y: Double) {
        this.y = y
    }

    fun getWidth(): Double {
        return width
    }

    fun setWidth(width: Double) {
        this.width = width
    }

    fun getHeight(): Double {
        return height
    }

    fun setHeight(height: Double) {
        this.height = height
    }

    var offsetX = 0f
    var offsetY = 0f
    fun mouseClick(mouseX: Int, mouseY: Int, button: Int) {
        if (mouseX > getX() - 2 && mouseX < getX() + getWidth() && mouseY > getY() - 2 && mouseY < getY() + height && isEnable()) {
            if (button == 0) {
                offsetX = (mouseX - x).toFloat()
                offsetY = (mouseY - y).toFloat()
                dragging = true
                x = (mouseX - offsetX).toDouble()
                this.y = (mouseY - offsetY).toDouble()
            }
        }
    }

    fun doGrag(mouseX: Int, mouseY: Int) {
        if (dragging) {
            if (!Mouse.isButtonDown(0)) {
                dragging = false
            }
            setX((mouseX - offsetX).toDouble())
            setY((mouseY - offsetY).toDouble())
        }
    }


    /**
     * 应在每个Module实例中覆盖此方法。（如果您需要在Module启动时执行一些东西）
     */
    fun onEnable() {}

    /**
     * 应在每个Module实例中覆盖此方法。（如果您需要在Module关闭时执行一些东西）
     */
    fun onDisable() {}

    /**
     * 模块类型的enum，不解释
     */
    enum class ModuleType {
        Render, Player, Fun
    }
}