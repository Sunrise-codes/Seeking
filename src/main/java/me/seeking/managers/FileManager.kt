package me.seeking.managers

import net.minecraft.client.Minecraft
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * This file is a part of Seeking Client.
 * @author MiLiBlue
 */
class FileManager {
    var path = File(Minecraft.getMinecraft().mcDataDir.path)

    /**
     * 这个方法用来保存文件的
     * @param file
     * @param content
     * @return result, true=succeed, false=failed.
     */
    fun saveFile(file: String, content: String?): Boolean {
        try {
            FileWriter(path.path + "/" + file).use { fileWriter ->
                fileWriter.append(content)
                return true
            }
        } catch (e: IOException) {
            return false
        }
    }

    /**
     * 这个方法用来加载文件的
     * @param file
     * @return File Content
     */
    fun loadFile(file: String): List<String>? {
        return try {
            Files.readAllLines(Paths.get(path.toPath().toString() + "/" + file))
        } catch (e: IOException) {
            null
        }
    }
}
