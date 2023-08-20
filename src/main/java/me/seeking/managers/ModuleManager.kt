package me.seeking.managers

import me.seeking.module.Module
import me.seeking.module.player.Sprint
import me.seeking.module.render.*

class ModuleManager {
    var modules: ArrayList<Module> = ArrayList<Module>()

    /**
     * 这个方法用来初始化Module List的
     */
    fun addModules() {
        Thread {
            modules.add(Sprint())
//            modules.add(ClickGui())
            modules.add(FPSDisplay())
            modules.add(CPSDisplay())
            modules.add(KeyDisplay())
            modules.add(InfoDisplay())
//            modules.add(AutoGG())
//            modules.add(Fullbright())
//            modules.add(PingDisplay())
//            modules.add(SessionInfo())
            modules.add(ChinaHat())
        }.start()
    }

    /**
     * 这个方法用来返回Module实例的
     * @param name
     */
    fun getModuleByName(name: String?): Module? {
        for (m in modules) {
            if (!m.getName()?.toLowerCase().equals(name)) continue
            return m
        }
        return null
    }

    /**
     * 这个方法用来返回开启的Module的
     * @return 开启的module
     */
    fun getEnableModules(): ArrayList<Module>? {
        val temp: ArrayList<Module> = ArrayList<Module>()
        for (m in modules) {
            if (m.isEnable()) {
                temp.add(m)
            }
        }
        return temp
    }
}