package me.seeking.ui.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class FontLoaders {
    public static FontRender font14;
    public static FontRender font16;
    public static FontRender font18;
    public static FontRender fontBig18;
    public static FontRender iconFont18;
    public static FontRender pFont16;

    public static void init() throws Throwable {
        font14 = createByResource("normal.ttf",14f,true);
        font16 = createByResource("normal.ttf",16f,true);
        font18 = createByResource("normal.ttf",18f,true);
        fontBig18 = createByResource("big.otf",18f,true);
        iconFont18 = createByResource("ico.ttf",18f,true);
	pFont16 = createByResource("ProductSans-Regular.ttf",16f,true);
    }

    private static FontRender createByName(String fontName, int size, boolean antiAliasing) {
        return new FontRender(new Font(fontName,Font.PLAIN,size), antiAliasing);
    }

    public static FontRender createByResource(String resourceName, float size, boolean antiAliasing) throws Throwable {
        return new FontRender(
                Font.createFont(
                        Font.TRUETYPE_FONT,
                        Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("seeking/font/"+resourceName)).getInputStream()
                ).deriveFont(Font.PLAIN, size), antiAliasing
        );
    }
}
