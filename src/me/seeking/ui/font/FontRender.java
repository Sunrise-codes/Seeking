package me.seeking.ui.font;

import me.seeking.utils.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class FontRender {
    private static final HashMap<Integer, Font> MONOSPACED_MAP = new HashMap<>();
    private static final Color NO_COLOR = new Color(0,0,0,0);
    private static final String COLOR_CODE = "0123456789abcdefklmnor";
    private static final int[] COLORS = new int[32];
    private static final int SHADOW_COLOR = new Color(0,0,0,50).getRGB();

    private final Glyph[] glyphs = new Glyph[65536];
    private final Font font;
    private final Font monospacedFont;
    private final int fontSize;
    private final int imageSize;
    private final int halfHeight;
    private final boolean antiAliasing;

    static {
        for (int i = 0; i < COLORS.length; i++) {
            final int offset = (i >> 3 & 1) * 85;

            int red = (i >> 2 & 1) * 170 + offset;
            int green = (i >> 1 & 1) * 170 + offset;
            int blue = (i & 1) * 170 + offset;

            if (i == 6) {
                red += 85;
            }

            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }

            COLORS[i] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }
    }

    public FontRender(Font font, boolean antiAliasing) {
        this.font = font;
        this.fontSize = font.getSize();
        this.imageSize = font.getSize() + 4;
        this.halfHeight = font.getSize() / 2;
        this.antiAliasing = antiAliasing;
        this.monospacedFont = getMonospacedFont();
    }

    private Font getMonospacedFont() {
        Font monospaced = MONOSPACED_MAP.get(fontSize);

        if (monospaced == null) {
            monospaced = new Font(Font.MONOSPACED,Font.PLAIN,fontSize);

            MONOSPACED_MAP.put(fontSize,monospaced);
        }

        return monospaced;
    }

    public int getStringWidth(String s) {
        s = EnumChatFormatting.getTextWithoutFormattingCodes(s);

        int ret = 0;

        for (int i = 0; i < s.length(); i++) {
            ret += getGlyph(s.charAt(i)).halfWidth;
        }

        return ret;
    }

    public int getHeight() {
        return fontSize;
    }

    public int getHalfHeight() {
        return halfHeight;
    }

    public void drawCenteredStringWithShadow(String s, double x, double y, int color) {
        drawStringWithShadow(s,x - (getStringWidth(s) / 2.0),y,color);
    }

    public void drawStringWithShadow(String s, double x, double y, int color) {
        drawString(s,x + 1,y + 1,SHADOW_COLOR,true);
        drawString(s,x,y,color,false);
    }

    public void drawStringWithShadowDirect(String s, double x, double y, int color) {
        drawStringDirect(s,x + 1,y + 1,SHADOW_COLOR);
        drawStringDirect(s,x,y,color);
    }

    public void drawCenteredString(String s, double x, double y, int color) {
        drawString(s,x - getStringWidth(s) / 2.0,y,color);
    }

    public void drawStringDirect(String s,double x,double y,int color) {
        preDraw();
        // GLUtils.color(color);

        x *= 2.0;
        y *= 2.0;

        for (int i = 0; i < s.length(); i++) {
            final Glyph glyph = getGlyph(s.charAt(i));

            glyph.draw(x,y,false);

            x += glyph.width;
        }

        postDraw();
    }

    public void drawString(String s,double x,double y,int color) {
        drawString(s, x, y, color, false);
    }

    public void drawString(String s,double x,double y,int color,boolean shadow) {
        s = filterEmoji(s);
        y -= 0.5;
        preDraw();
        //  GLUtils.color(color);
        //GlStateManager.color(ColorUtil.getRed(color), ColorUtil.getGreen(color), ColorUtil.getBlue(color), ColorUtil.getAlpha(color));
        GlStateManager.color((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, 200);

        x *= 2.0;
        y *= 2.0;

        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;

        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);

            if (c == '§') {
                i++;

                if (i >= s.length()) {
                    final Glyph glyph = getGlyph('§');

                    drawGlyph(glyph,x,y,bold,strikethrough,underline,italic);

                    x += glyph.width;
                }

                if (!shadow) {
                    int colorIndex = 21;

                    if (i < s.length()) {
                        colorIndex = COLOR_CODE.indexOf(s.charAt(i));
                    }

                    switch (colorIndex) {
                        case 17:        // 加粗
                            bold = true;

                            break;
                        case 18:        // 删除线
                            strikethrough = true;

                            break;
                        case 19:        // 下划线
                            underline = true;

                            break;
                        case 20:        // 斜体
                            italic = true;

                            break;
                        case 21:        // 重置
                            bold = false;
                            italic = false;
                            underline = false;
                            strikethrough = false;

                            //GLUtils.color(color);
                            GlStateManager.color(ColorUtil.getRed(color), ColorUtil.getGreen(color), ColorUtil.getBlue(color), ColorUtil.getAlpha(color));
                            break;
                        default:
                            if (colorIndex < 16) {
                                if (colorIndex == -1) {
                                    colorIndex = 15;
                                }

                                final int finalColor = COLORS[colorIndex];

                                GlStateManager.color(ColorUtil.getRed(finalColor), ColorUtil.getGreen(finalColor), ColorUtil.getBlue(finalColor), ColorUtil.getAlpha(color));
                            }

                            break;
                    }
                }
            } else {
                final Glyph glyph = getGlyph(c);

                drawGlyph(glyph,x,y,bold,strikethrough,underline,italic);

                x += glyph.width;
            }
        }

        postDraw();
    }
    public static boolean isBlank(String s) {
        int strLen;

        if (s != null && (strLen = s.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    @SuppressWarnings("ConstantConditions")
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
    public static String filterEmoji(String source) {
        if (isBlank(source)) {
            return source;
        }

        StringBuilder buf = null;
        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }

                buf.append(codePoint);
            }
        }

        if (buf == null) {
            return source;
        } else {
            if (buf.length() == len) {
                return source;
            } else {
                return buf.toString();
            }
        }
    }

    private void drawGlyph(Glyph glyph,double x,double y,boolean bold,boolean strikethrough,boolean underline,boolean italic) {
        if (bold) {
            glyph.draw(x + 1,y,italic);
        }

        glyph.draw(x,y,italic);

        if (strikethrough) {
            final double mid = y + fontSize / 2.0;

            drawLine(x, mid - 1, x + glyph.width, mid + 1);
        }

        if (underline) {
            drawLine(x, y + fontSize - 1, x + glyph.width, y + fontSize + 1);
        }
    }

    private void preDraw() {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableTexture2D();
        GlStateManager.scale(0.5,0.5,0.5);
    }

    private void postDraw() {
        GlStateManager.disableBlend();
        //GlStateManager.disableTexture2D();
        GlStateManager.popMatrix();
    }

    private Glyph getGlyph(char c) {
        Glyph glyph = glyphs[c];

        if (glyph == null) {
            final BufferedImage image = new BufferedImage(imageSize,imageSize,BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g = image.createGraphics();

            if (font.canDisplay(c)) {
                setRenderingHints(g,antiAliasing);
                g.setFont(font);
            } else {
                setRenderingHints(g,false);
                g.setFont(monospacedFont);
            }

            final FontMetrics fontMetrics = g.getFontMetrics();

            g.setColor(NO_COLOR);
            g.fillRect(0,0,imageSize,imageSize);

            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(c),0,15);

            g.dispose();

            glyph = new Glyph(new DynamicTexture(image),fontMetrics.getStringBounds(String.valueOf(c), g).getBounds().width);

            glyphs[c] = glyph;
        }

        return glyph;
    }

    private static void setRenderingHints(Graphics2D g,boolean antiAliasing) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        if (antiAliasing) {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    @SuppressWarnings("DuplicatedCode")
    private static void drawLine(double left, double top, double right, double bottom) {
        if (left < right)
        {
            final double i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            final double j = top;
            top = bottom;
            bottom = j;
        }

        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer bufferBuilder = tessellator.getWorldRenderer();
        GlStateManager.disableTexture2D();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(left, bottom, 0.0D).endVertex();
        bufferBuilder.pos(right, bottom, 0.0D).endVertex();
        bufferBuilder.pos(right, top, 0.0D).endVertex();
        bufferBuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
    }

    private final class Glyph {
        public final DynamicTexture texture;
        public final int width;
        public final int halfWidth;

        public Glyph(DynamicTexture texture, int width) {
            this.texture = texture;
            this.width = width;
            this.halfWidth = width / 2;
        }

        public void draw(double x, double y,boolean italic) {
            GlStateManager.bindTexture(texture.getGlTextureId());

            final double offset = italic ? 2.0 : 0.0;

            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
            GL11.glTexCoord2d(0, 0);
            GL11.glVertex3d(x + offset, y, 0.0F);
            GL11.glTexCoord2d(0, 1);
            GL11.glVertex3d(x - offset, y + imageSize, 0.0F);
            GL11.glTexCoord2d(1, 0);
            GL11.glVertex3d(x + imageSize + offset, y, 0.0F);
            GL11.glTexCoord2d(1, 1);
            GL11.glVertex3d(x + imageSize - offset, y + imageSize, 0.0F);

            GL11.glEnd();
        }
    }
}
