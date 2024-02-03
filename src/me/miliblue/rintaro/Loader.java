package me.miliblue.rintaro;

import me.seeking.Seeking;
import me.seeking.managers.Command;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Loader {
    private static Loader self = new Loader();
    public static int modCount = 0;
    public static Loader getInstance(){
        return self;
    }

    public void loadMods(String modPath) throws CutsomException, NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        //Load Mods from a Folder
        File folder = new File(modPath);
        if(folder.exists()) {
            if (folder.isDirectory()) {//Anti sb
                File[] files = folder.listFiles();
                if(Objects.isNull(files) || files.length == 0){
                    System.out.printf("[Rintaro Loader Warming] Not found any mod in the folder.\n");
                    return;
                }
                for (File f : files){
                    if(f.getName().toLowerCase().endsWith(".jar") && !f.isDirectory()){
                        //get method instance
                        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                        URLClassLoader classLoader = null;
                        try {
                            method.setAccessible(true);
                            classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                            //define classes into jvm
                            method.invoke(classLoader, f.toURI().toURL());
                        } finally {
                            method.setAccessible(false);
                        }

                        modCount += 1;
                        Display.setTitle("Seeking 0.2 - (Minecraft 1.8.9) - Loaded "+Loader.modCount+" Plugins. - 您可能是正版卡网的受害者");
                    }
                    for (Class c : getClassesNameByJar(f.getPath())){
                        if(PluginBase.class.isAssignableFrom(c)) {
                            Method method = c.getDeclaredMethod("init");
                            method.setAccessible(true);
                            method.invoke(c.newInstance());
                        }
                        if(Command.class.isAssignableFrom(c)){
                            Seeking.instance.commandManager.addCommand((Command) c.newInstance());
                        }
                    }
                }
            } else {
                throw new CutsomException("Not a directory");
            }
        }else {
            //Try to create folder
            folder.mkdir();
            System.out.printf("[Rintaro Loader] Folder Not Found! Created a new one. Path:"+modPath+"\n");
        }
    }

    public List<Class> getClassesNameByJar(String jarPath) throws IOException, ClassNotFoundException {
        List<Class> lc = new ArrayList<>();
        List<String> ln = new ArrayList<>();
        JarFile jf = new JarFile(jarPath);
        Enumeration entries = jf.entries();
        while (entries.hasMoreElements()){
            JarEntry je = (JarEntry) entries.nextElement();
            String n = je.getName();
            if(n != null && n.endsWith(".class")){
                n = n.replace("/", ".").substring(0, n.lastIndexOf("."));
                ln.add(n);
            }
        }

        if(ln.size() > 0){
            for (String s : ln){
                Class c = ClassLoader.getSystemClassLoader().loadClass(s);
                lc.add(c);
            }
        }
        return lc;
    }
}
