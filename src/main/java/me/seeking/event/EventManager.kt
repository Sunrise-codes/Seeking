package me.seeking.event

import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

class EventManager {
    private val REGISTRY_MAP: HashMap<Class<out Event?>, CopyOnWriteArrayList<Data>> = HashMap()

    fun register(o: Any) {
        Arrays.stream(o.javaClass.declaredMethods).forEach { method: Method ->
            if (!isMethodBad(
                    method
                )
            ) register(method, o)
        }
        REGISTRY_MAP.values.forEach { flexibleArray: CopyOnWriteArrayList<Data> ->
            flexibleArray.sortWith(Comparator<Data> { o1: Data, o2: Data ->
                o1.priority.value - o2.priority.value
            })
        }
    }

    private fun register(method: Method, o: Any) {
        val clazz: Class<out Event?> = method.parameterTypes[0] as Class<out Event?>
        val eventTargetAnnotation = method.getAnnotation(EventTarget::class.java)

        if (eventTargetAnnotation == null) {
            println("Warning: Event handling method ${method.name} is missing EventTarget annotation.")
            return
        }

        val methodData = Data(o, method, eventTargetAnnotation.priority)
        if (!methodData.target.isAccessible) methodData.target.isAccessible = true
        if (REGISTRY_MAP.containsKey(clazz)) {
            if (!REGISTRY_MAP[clazz]!!.contains(methodData)) REGISTRY_MAP[clazz]!!.add(methodData)
        } else {
            REGISTRY_MAP[clazz] = CopyOnWriteArrayList<Data>(listOf(methodData))
        }
    }

    fun unregister(o: Any?) {
        REGISTRY_MAP.values.forEach { flexibleArray: CopyOnWriteArrayList<Data> ->
            flexibleArray.removeIf { methodData: Data -> methodData.source == o }
        }
        REGISTRY_MAP.entries.removeIf { (_, value): Map.Entry<Class<out Event?>, CopyOnWriteArrayList<Data>> -> value.isEmpty() }
    }

    private fun isMethodBad(method: Method): Boolean {
        return method.parameterTypes.size != 1 || !method.isAnnotationPresent(EventTarget::class.java)
    }

    operator fun get(clazz: Class<out Event?>): CopyOnWriteArrayList<Data>? {
        return REGISTRY_MAP[clazz]
    }
}
