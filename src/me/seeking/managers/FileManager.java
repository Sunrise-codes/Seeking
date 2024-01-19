package me.seeking.managers;

import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * This file is a part of Seeking Client.
 * @author MiLiBlue
 */
public class FileManager {
    public File path = new File(Minecraft.getMinecraft().mcDataDir.getPath());

    /**
     * 这个方法用来保存文件的
     * @param file
     * @param content
     * @return result, true=succeed, false=failed.
     */
    public boolean saveFile(String file, String content){
        try (FileWriter fileWriter = new FileWriter(path.getPath()+"/"+file)) {
            fileWriter.append(content);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 这个方法用来加载文件的
     * @param file
     * @return File Content
     */
    public List<String> loadFile(String file){
        try {
            return Files.readAllLines(Paths.get(path.toPath() +"/"+file));
        } catch (IOException e) {
            return null;
        }
    }
}
