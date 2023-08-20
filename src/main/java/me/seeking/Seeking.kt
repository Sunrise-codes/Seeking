package me.seeking

import me.seeking.event.EventManager
import me.seeking.event.EventTarget
import me.seeking.event.events.EventKeyboard
import me.seeking.managers.CommandManager
import me.seeking.managers.FileManager
import me.seeking.managers.ModuleManager
import me.seeking.ui.ShaderInstance
import me.seeking.ui.font.CFontRender
import me.seeking.ui.font.FontLoaders
import org.lwjgl.opengl.Display

class Seeking {
    var moduleManager: ModuleManager? = null
    var eventManager: EventManager? = null
    var fileManager: FileManager? = null
    var commandManager: CommandManager? = null
    var si: ShaderInstance? = null

    /**
     * 当方块人启动的时候会调用这个方法
     */
    fun start() {
        FontLoaders.font14 = CFontRender(FontLoaders.getFont(14), true, true)
        FontLoaders.font18 = CFontRender(FontLoaders.getFont(18), true, true)
        FontLoaders.font16 = CFontRender(FontLoaders.getFont(16), true, true)
        FontLoaders.fontBig18 = CFontRender(FontLoaders.getBigFont(18), true, true)
        FontLoaders.iconFont18 = CFontRender(FontLoaders.getIconFont(18), true, true)
        moduleManager = ModuleManager()
        moduleManager!!.addModules()
        eventManager = EventManager()
        eventManager!!.register(this)
        fileManager = FileManager()
        commandManager = CommandManager()
        commandManager!!.loadCommands()
        loadCFG()
        si = ShaderInstance()
        //Set Title
        Display.setTitle("Seeking 0.3 - (Minecraft 1.8.9)")
    }

    /**
     * 当方块人关闭的时候会调用这个方法
     */
    fun stop() {
        saveCFG()
    }

    /**
     * 这个方法用来监听按键的，事件系统会自动调用这个几把方法
     */
    @EventTarget
    fun keyListener(e: EventKeyboard) {
        for (m in moduleManager!!.modules) {
            if (e.key === m.getKey()) {
                m.setEnable(!m.isEnable())
            }
        }
    }

    /**
     * 这个方法用来保存配置的
     */
    fun saveCFG() {
        //Build Content
        val sb = StringBuilder()
        sb.append("[ENABLE]")
        for (m in moduleManager!!.getEnableModules()!!) {
            sb.append(m.getName() + "|")
        }
        sb.append("\n")
        sb.append("[KEY]")
        for (m in moduleManager!!.modules) {
            sb.append((m.getName() + ":" + m.getKey()).toString() + "|")
        }
        sb.append("\n")
        sb.append("[X]")
        for (m in moduleManager!!.modules) {
            sb.append((m.getName() + ":" + m.getX()).toString() + "|")
        }
        sb.append("\n")
        sb.append("[Y]")
        for (m in moduleManager!!.modules) {
            sb.append((m.getName() + ":" + m.getY()).toString() + "|")
        }
        sb.append("\n")

        //Save CFG
        fileManager!!.saveFile("SeekingCFG.seeking", sb.toString())
    }

    /**
     * 这个方法用来加载配置的
     */
    fun loadCFG() {
        //Read CFG File
        val list: List<String>? = fileManager!!.loadFile("SeekingCFG.seeking")
        if (list != null) {
            for (s in list) {
                if (s.startsWith("[ENABLE]")) {
                    var temps = s.replace("[ENABLE]", "")
                    if (s !== "") {
                        val iq = temps.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        for (st in iq) {
                            if (st != null) if (moduleManager!!.getModuleByName(st) != null && !st.equals(
                                    "ClickGui",
                                    ignoreCase = true
                                )
                            ) moduleManager!!.getModuleByName(st)!!.setEnable(true)
                        }
                    }
                }
                if (s.startsWith("[KEY]")) {
                    var temps = s.replace("[KEY]", "")
                    if (temps !== "") {
                        val iq = temps.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        for (st in iq) {
                            val wow = st.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (moduleManager!!.getModuleByName(wow[0]) != null) moduleManager!!.getModuleByName(wow[0])!!
                                .setKey(
                                    wow[1].toInt()
                                )
                        }
                    }
                }
                if (s.startsWith("[X]")) {
                    var temps = s.replace("[X]", "")
                    if (temps !== "") {
                        val iq = temps.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        for (st in iq) {
                            val wow = st.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (moduleManager!!.getModuleByName(wow[0]) != null) moduleManager!!.getModuleByName(wow[0])!!
                                .setX(wow[1].toDouble())
                        }
                    }
                }
                if (s.startsWith("[Y]")) {
                    var temps = s.replace("[Y]", "")
                    if (temps !== "") {
                        val iq = temps.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        for (st in iq) {
                            val wow = st.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (moduleManager!!.getModuleByName(wow[0]) != null) moduleManager!!.getModuleByName(wow[0])!!
                                .setY(wow[1].toDouble())
                        }
                    }
                }
            }
        }
    }

    companion object {
        val instance: Seeking = Seeking()
    }
}