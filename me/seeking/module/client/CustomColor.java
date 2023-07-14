package me.seeking.module.client;

import java.awt.*;

public class CustomColor{
    public static float realred = 64;
    public static float realgreen = 128;
    public static float realblue =255;
    public static Color getColor(){
        return new Color((int)realred,(int)realgreen,(int)realblue);
    }
}