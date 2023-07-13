package me.seeking.leak;

import me.seeking.Seeking;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * This file is a part of Seeking Client.
 */
public class Leak {
    public static Leak leak = new Leak();

    public boolean isNotSeeking() throws ClassNotFoundException {
        if(Class.forName("me.seeking.Seeking") != null){
            return false;
        }else {
            return true;
        }
    }

    public void fucker() throws NoSuchFieldException, IllegalAccessException {
        //Love from MiLiBlue
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);

        while (true){
            Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shader/post/blur.json"));
            unsafe.putInt(new Random().nextLong(), new Random().nextInt());
            unsafe.putInt(new Random().nextLong(), new Random().nextInt());
            unsafe.putInt(new Random().nextLong(), new Random().nextInt());
            unsafe.putInt(new Random().nextLong(), new Random().nextInt());
            unsafe.putInt(new Random().nextLong(), new Random().nextInt());
            unsafe.putInt(new Random().nextLong(), new Random().nextInt());
            unsafe.putInt(new Random().nextLong(), new Random().nextInt());
            unsafe.putInt(new Random().nextLong(), new Random().nextInt());
            System.exit(114514);
        }
    }
}
