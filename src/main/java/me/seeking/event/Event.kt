package me.seeking.event

import me.seeking.Seeking
import me.seeking.utils.PlayerUtil
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer

abstract class Event(val type: Type) {
    var isCancelled = false

    enum class Type {
        PRE, POST
    }

    fun call() {
        isCancelled = false
        val dataList: CopyOnWriteArrayList<Data>? = Seeking.instance.eventManager!![this.javaClass]
        if (dataList != null) {
            if(dataList.isEmpty()){
                PlayerUtil.tellPlayer("NULL:"+this.javaClass.name)
                return
            }
        }
        dataList?.forEach(Consumer<Data> { data: Data ->
            try {
                data.target.invoke(data.source, this)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        })
    }
}