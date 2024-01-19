package me.seeking.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DONT_CARE;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;
import static org.lwjgl.opengl.GL20.*;
/**
 * This file is a part of Seeking Client.
 */
public class DrawUtil {

    private static final FloatBuffer WND_POS_BUFFER = GLAllocation.createDirectFloatBuffer(4);
    private static final IntBuffer VIEWPORT_BUFFER = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer MODEL_MATRIX_BUFFER = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION_MATRIX_BUFFER = GLAllocation.createDirectFloatBuffer(16);
    private static final IntBuffer SCISSOR_BUFFER = GLAllocation.createDirectIntBuffer(16);


    private DrawUtil() {
    }

    public static double animateProgress(final double current, final double target, final double speed) {
        if (current < target) {
            final double inc = 1.0 / Minecraft.getDebugFPS() * speed;
            if (target - current < inc) {
                return target;
            } else {
                return current + inc;
            }
        } else if (current > target) {
            final double inc = 1.0 / Minecraft.getDebugFPS() * speed;
            if (current - target < inc) {
                return target;
            } else {
                return current - inc;
            }
        }

        return current;
    }

    public static double bezierBlendAnimation(double t) {
        return t * t * (3.0 - 2.0 * t);
    }

    public static void glDrawTriangle(final double x, final double y,
                                      final double x1, final double y1,
                                      final double x2, final double y2,
                                      final int colour) {
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Enable blending
        final boolean restore = glEnableBlend();
        // Enable anti-aliasing
        glEnable(GL_POLYGON_SMOOTH);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);

        glColour(colour);

        // Start drawing a triangle
        glBegin(GL_TRIANGLES);
        {
            glVertex2d(x, y);
            glVertex2d(x1, y1);
            glVertex2d(x2, y2);
        }
        glEnd();

        // Enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Disable blending
        glRestoreBlend(restore);
        // Disable anti-aliasing
        glDisable(GL_POLYGON_SMOOTH);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
    }

    public static void glDrawFramebuffer(final int framebufferTexture, final int width, final int height) {
        // Bind the texture of our framebuffer
        glBindTexture(GL_TEXTURE_2D, framebufferTexture);
        // Disable alpha testing so fading out outline works
        glDisable(GL_ALPHA_TEST);
        // Make sure blend is enabled
        final boolean restore = DrawUtil.glEnableBlend();
        // Draw the frame buffer texture upside-down
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 1);
            glVertex2f(0, 0);

            glTexCoord2f(0, 0);
            glVertex2f(0, height);

            glTexCoord2f(1, 0);
            glVertex2f(width, height);

            glTexCoord2f(1, 1);
            glVertex2f(width, 0);
        }
        glEnd();
        // Restore blend
        DrawUtil.glRestoreBlend(restore);
        // Restore alpha test
        glEnable(GL_ALPHA_TEST);
    }

    public static void glDrawPlusSign(final double x,
                                      final double y,
                                      final double size,
                                      final double rotation,
                                      final int colour) {
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Enable blending
        final boolean restore = glEnableBlend();
        // Enable anti-aliasing
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        // Set line width
        glLineWidth(1.f);
        // Push new matrix
        glPushMatrix();
        // Translate matrix
        glTranslated(x, y, 0);
        // Rotate matrix by rotation value (do after translation
        glRotated(rotation, 0, 1, 1);
        // Disable depth
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);

        glColour(colour);

        // Start drawing a triangle
        glBegin(GL_LINES);
        {
            // Horizontal stroke
            glVertex2d(-(size / 2.0), 0);
            glVertex2d(size / 2.0, 0);
            // Vertical stroke
            glVertex2d(0, -(size / 2.0));
            glVertex2d(0, size / 2.0);
        }
        glEnd();

        // Enable depth
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        // Pop off old matrix (restore)
        glPopMatrix();
        // Enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Disable blending
        glRestoreBlend(restore);
        // Disable anti-aliasing
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
    }

    public static void glDrawFilledEllipse(final double x,
                                           final double y,
                                           final double radius,
                                           final int startIndex,
                                           final int endIndex,
                                           final int polygons,
                                           final boolean smooth,
                                           final int colour) {
        // Enable blending
        final boolean restore = glEnableBlend();

        if (smooth) {
            // Enable anti-aliasing
            glEnable(GL_POLYGON_SMOOTH);
            glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        }
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set color
        glColour(colour);
        // Required because of minecraft optimizations
        glDisable(GL_CULL_FACE);

        // Begin triangle fan
        glBegin(GL_POLYGON);
        {
            // Specify center vertex
            glVertex2d(x, y);

            for (double i = startIndex; i <= endIndex; i++) {
                final double theta = 2.0 * Math.PI * i / polygons;
                // Specify triangle fan vertices in a circle (size=radius) around x & y
                glVertex2d(x + radius * Math.cos(theta), y + radius * Math.sin(theta));
            }
        }
        // Draw the triangle fan
        glEnd();

        // Disable blending
        glRestoreBlend(restore);

        if (smooth) {
            // Disable anti-aliasing
            glDisable(GL_POLYGON_SMOOTH);
            glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
        }
        // See above
        glEnable(GL_CULL_FACE);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static void glDrawFilledEllipse(final double x,
                                           final double y,
                                           final float radius,
                                           final int colour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Enable anti-aliasing
        glEnable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set color
        glColour(colour);
        // See the point size aka radius
        glPointSize(radius);

        glBegin(GL_POINTS);
        {
            glVertex2d(x, y);
        }
        glEnd();

        // Disable blending
        glRestoreBlend(restore);
        // Disable anti-aliasing
        glDisable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_DONT_CARE);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static void glScissorBox(final double x, final double y,
                                    final double width, final double height,
                                    final ScaledResolution scaledResolution) {
        if (!glIsEnabled(GL_SCISSOR_TEST))
            glEnable(GL_SCISSOR_TEST);

        final int scaling = scaledResolution.getScaleFactor();

        glScissor((int) (x * scaling),
                (int) ((scaledResolution.getScaledHeight() - (y + height)) * scaling),
                (int) (width * scaling),
                (int) (height * scaling));
    }

    public static void glRestoreScissor() {
        if (!glIsEnabled(GL_SCISSOR_TEST))
            glEnable(GL_SCISSOR_TEST);

        // Restore the last saved scissor box
        glScissor(SCISSOR_BUFFER.get(0), SCISSOR_BUFFER.get(1),
                SCISSOR_BUFFER.get(2), SCISSOR_BUFFER.get(3));
    }

    public static void glEndScissor() {
        glDisable(GL_SCISSOR_TEST);
    }
    public static void glColour(final int color) {
        glColor4ub((byte) (color >> 16 & 0xFF),
                (byte) (color >> 8 & 0xFF),
                (byte) (color & 0xFF),
                (byte) (color >> 24 & 0xFF));
    }

    public static void glDrawLine(final double x,
                                  final double y,
                                  final double x1,
                                  final double y1,
                                  final float lineWidth,
                                  final boolean smoothed,
                                  final int colour) {
        // Enable blending (required for anti-aliasing)
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set line width
        glLineWidth(lineWidth);

        if (smoothed) {
            // Enable line anti-aliasing
            glEnable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        }

        glColour(colour);

        // Begin line
        glBegin(GL_LINES);
        {
            // Start
            glVertex2d(x, y);
            // End
            glVertex2d(x1, y1);
        }
        // Draw the line
        glEnd();

        // Restore blend
        glRestoreBlend(restore);
        if (smoothed) {
            // Disable line anti-aliasing
            glDisable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        }
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static void glDrawPlayerFace(final double x,
                                        final double y,
                                        final double width,
                                        final double height,
                                        final ResourceLocation skinLocation) {
        // Bind skin texture
        Minecraft.getMinecraft().getTextureManager().bindTexture(skinLocation);
        // Colour solid
        glColor4f(1, 1, 1, 1);
        final float eightPixelOff = 1.0F / 8;

        glBegin(GL_QUADS);
        {
            glTexCoord2f(eightPixelOff, eightPixelOff);
            glVertex2d(x, y);

            glTexCoord2f(eightPixelOff, eightPixelOff * 2);
            glVertex2d(x, y + height);

            glTexCoord2f(eightPixelOff * 2, eightPixelOff * 2);
            glVertex2d(x + width, y + height);

            glTexCoord2f(eightPixelOff * 2, eightPixelOff);
            glVertex2d(x + width, y);
        }
        glEnd();
    }

    public static void glDrawSidewaysGradientRect(final double x,
                                                  final double y,
                                                  final double width,
                                                  final double height,
                                                  final int startColour,
                                                  final int endColour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Enable vertex colour changing
        glShadeModel(GL_SMOOTH);

        // Begin rect
        glBegin(GL_QUADS);
        {
            // Start fade
            glColour(startColour);
            glVertex2d(x, y);
            glVertex2d(x, y + height);
            // End fade
            glColour(endColour);
            glVertex2d(x + width, y + height);
            glVertex2d(x + width, y);
        }
        // Draw the rect
        glEnd();

        // Restore shade model
        glShadeModel(GL_FLAT);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Disable blending
        glRestoreBlend(restore);
    }

    public static void glDrawFilledRect(final double x,
                                        final double y,
                                        final double x1,
                                        final double y1,
                                        final int startColour,
                                        final int endColour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Enable vertex colour changing
        glShadeModel(GL_SMOOTH);

        // Begin rect
        glBegin(GL_QUADS);
        {
            // Start fade
            glColour(startColour);
            glVertex2d(x, y);
            glColour(endColour);
            glVertex2d(x, y1);
            // End fade
            glVertex2d(x1, y1);
            glColour(startColour);
            glVertex2d(x1, y);
        }
        // Draw the rect
        glEnd();

        // Restore shade model
        glShadeModel(GL_FLAT);

        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Disable blending
        glRestoreBlend(restore);
    }

    public static void glDrawOutlinedQuad(final double x,
                                          final double y,
                                          final double width,
                                          final double height,
                                          final float thickness,
                                          final int colour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set color
        glColour(colour);

        glLineWidth(thickness);

        // Begin rect
        glBegin(GL_LINE_LOOP);
        {
            glVertex2d(x, y);
            glVertex2d(x, y + height);
            glVertex2d(x + width, y + height);
            glVertex2d(x + width, y);
        }
        // Draw the rect
        glEnd();

        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Disable blend
        glRestoreBlend(restore);
    }

    public static void drawHollowRoundedRect(double x,
                                             double y,
                                             double width,
                                             double height,
                                             double cornerRadius,
                                             boolean smoothed,
                                             Color color) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_BLEND);
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255.0F, color.getAlpha() / 255F);
        glLineWidth(1.0f);
        glBegin(GL_LINE_LOOP);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        for (int i = 0; i <= 90; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        glEnd();
        cornerX = x + width - cornerRadius;
        cornerY = y + cornerRadius;
        glBegin(GL_LINE_LOOP);
        for (int i = 90; i <= 180; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        glEnd();
        cornerX = x + cornerRadius;
        cornerY = y + cornerRadius;
        glBegin(GL_LINE_LOOP);
        for (int i = 180; i <= 270; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        glEnd();
        cornerX = x + cornerRadius;
        cornerY = y + height - cornerRadius;
        glBegin(GL_LINE_LOOP);
        for (int i = 270; i <= 360; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        glEnd();
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glDrawLine(x + cornerRadius, y, x + width - cornerRadius, y, 1.0f, smoothed, color.getRGB());
        glDrawLine(x + cornerRadius, y + height, x + width - cornerRadius, y + height, 1.0f, smoothed, color.getRGB());
        glDrawLine(x, y + cornerRadius, x, y + height - cornerRadius, 1.0f, smoothed, color.getRGB());
        glDrawLine(x + width, y + cornerRadius, x + width, y + height - cornerRadius, 1.0f, smoothed, color.getRGB());
    }

    public static void glDrawOutlinedQuadGradient(final double x,
                                                  final double y,
                                                  final double width,
                                                  final double height,
                                                  final float thickness,
                                                  final int colour, final int secondaryColour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);

        glLineWidth(thickness);

        // Begin rect
        glShadeModel(GL_SMOOTH);
        glBegin(GL_LINE_LOOP);
        {
            // Set color
            glColour(colour);
            glVertex2d(x, y);
            glVertex2d(x, y + height);
            // Set second color
            glColour(secondaryColour);
            glVertex2d(x + width, y + height);
            glVertex2d(x + width, y);
        }
        // Draw the rect
        glEnd();
        glShadeModel(GL_FLAT);

        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Disable blend
        glRestoreBlend(restore);
    }


    public static void glDrawFilledQuad(final double x,
                                        final double y,
                                        final double width,
                                        final double height,
                                        final int colour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set color
        glColour(colour);

        // Begin rect
        glBegin(GL_QUADS);
        {
            glVertex2d(x, y);
            glVertex2d(x, y + height);
            glVertex2d(x + width, y + height);
            glVertex2d(x + width, y);
        }
        // Draw the rect
        glEnd();

        // Disable blending
        glRestoreBlend(restore);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static void glDrawFilledQuad(final double x,
                                        final double y,
                                        final double width,
                                        final double height,
                                        final int startColour,
                                        final int endColour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);

        glShadeModel(GL_SMOOTH);

        // Begin rect
        glBegin(GL_QUADS);
        {
            glColour(startColour);
            glVertex2d(x, y);

            glColour(endColour);
            glVertex2d(x, y + height);
            glVertex2d(x + width, y + height);

            glColour(startColour);
            glVertex2d(x + width, y);
        }
        // Draw the rect
        glEnd();

        glShadeModel(GL_FLAT);

        // Disable blending
        glRestoreBlend(restore);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static void glDrawFilledRect(final double x,
                                        final double y,
                                        final double x1,
                                        final double y1,
                                        final int colour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set color
        glColour(colour);

        // Begin rect
        glBegin(GL_QUADS);
        {
            glVertex2d(x, y);
            glVertex2d(x, y1);
            glVertex2d(x1, y1);
            glVertex2d(x1, y);
        }
        // Draw the rect
        glEnd();

        // Disable blending
        glRestoreBlend(restore);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }
    public static void glDrawArcOutline(final double x,
                                        final double y,
                                        final float radius,
                                        final float angleStart,
                                        final float angleEnd,
                                        final float lineWidth,
                                        final int colour) {
        // Derive segments from size
        final int segments = (int) (radius * 4);
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set the width of the line
        glLineWidth(lineWidth);
        // Set color
        glColour(colour);
        // Enable triangle smoothing
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        // Translate to centre of arc
        glTranslated(x, y, 0);
        // Begin triangle fan
        glBegin(GL_LINE_STRIP);
        {
            final float[][] vertices = MathUtil.getArcVertices(radius, angleStart, angleEnd, segments);

            for (float[] vertex : vertices) {
                // Specify triangle fan vertices in a circle (size=radius) around x & y
                glVertex2f(vertex[0], vertex[1]);
            }
        }
        // Draw the triangle fan
        glEnd();
        // Restore matrix
        glTranslated(-x, -y, 0);
        // Disable triangle smoothing
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        // Disable blending
        glRestoreBlend(restore);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }


    public static void glDrawPoint(final double x,
                                   final double y,
                                   final float radius,
                                   final ScaledResolution scaledResolution,
                                   final int colour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Enable anti-aliasing
        glEnable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set color
        glColour(colour);
        // See the point size aka radius
        glPointSize(radius * glGetFloat(GL_MODELVIEW_MATRIX) * scaledResolution.getScaleFactor());

        glBegin(GL_POINTS);
        {
            glVertex2d(x, y);
        }
        glEnd();

        // Disable blending
        glRestoreBlend(restore);
        // Disable anti-aliasing
        glDisable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_DONT_CARE);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static void glDrawRoundedOutline(final double x,
                                            final double y,
                                            final double width,
                                            final double height,
                                            final float lineWidth,
                                            final RoundingMode roundingMode,
                                            final float rounding,
                                            final int colour) {
        boolean bLeft = false;
        boolean tLeft = false;
        boolean bRight = false;
        boolean tRight = false;

        switch (roundingMode) {
            case TOP:
                tLeft = true;
                tRight = true;
                break;
            case BOTTOM:
                bLeft = true;
                bRight = true;
                break;
            case FULL:
                tLeft = true;
                tRight = true;
                bLeft = true;
                bRight = true;
                break;
            case LEFT:
                bLeft = true;
                tLeft = true;
                break;
            case RIGHT:
                bRight = true;
                tRight = true;
                break;
            case TOP_LEFT:
                tLeft = true;
                break;
            case TOP_RIGHT:
                tRight = true;
                break;
            case BOTTOM_LEFT:
                bLeft = true;
                break;
            case BOTTOM_RIGHT:
                bRight = true;
                break;
        }

        // Translate matrix to top-left of rect
        glTranslated(x, y, 0);
        // Enable blending
        final boolean restore = glEnableBlend();

        if (tLeft) {
            // Top left
            glDrawArcOutline(rounding, rounding, rounding,
                    270.f, 360.f, lineWidth, colour);
        }

        if (tRight) {
            // Top right
            glDrawArcOutline(width - rounding, rounding, rounding,
                    0.f, 90.f, lineWidth, colour);
        }

        if (bLeft) {
            // Bottom left
            glDrawArcOutline(rounding, height - rounding, rounding,
                    180, 270, lineWidth, colour);
        }

        if (bRight) {
            // Bottom right
            glDrawArcOutline(width - rounding, height - rounding, rounding,
                    90, 180, lineWidth, colour);
        }

        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set colour
        glColour(colour);

        // Begin polygon
        glBegin(GL_LINES);
        {
            if (tLeft) {
                glVertex2d(0.0, rounding);
            } else {
                glVertex2d(0.0, 0.0);
            }

            if (bLeft) {
                glVertex2d(0, height - rounding);
                glVertex2d(rounding, height);
            } else {
                glVertex2d(0.0, height);
                glVertex2d(0.0, height);
            }

            if (bRight) {
                glVertex2d(width - rounding, height);
                glVertex2d(width, height - rounding);
            } else {
                glVertex2d(width, height);
                glVertex2d(width, height);
            }

            if (tRight) {
                glVertex2d(width, rounding);
                glVertex2d(width - rounding, 0.0);
            } else {
                glVertex2d(width, 0.0);
                glVertex2d(width, 0.0);
            }

            if (tLeft) {
                glVertex2d(rounding, 0.0);
            } else {
                glVertex2d(0.0, 0.0);
            }
        }
        // Draw polygon
        glEnd();

        // Disable blending
        glRestoreBlend(restore);
        // Translate matrix back (instead of creating a new matrix with glPush/glPop)
        glTranslated(-x, -y, 0);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    // TODO :: Do this shader (its not hard)

    private static final String CIRCLE_FRAG_SHADER =
            "#version 120\n" +
                    "\n" +
                    "uniform float innerRadius;\n" +
                    "uniform vec4 colour;\n" +
                    "\n" +
                    "void main() {\n" +
                    "   vec2 pixel = gl_TexCoord[0].st;\n" +
                    "   vec2 centre = vec2(0.5, 0.5);\n" +
                    "   float d = length(pixel - centre);\n" +
                    "   float c = smoothstep(d+innerRadius, d+innerRadius+0.01, 0.5-innerRadius);\n" +
                    "   float a = smoothstep(0.0, 1.0, c) * colour.a;\n" +
                    "   gl_FragColor = vec4(colour.rgb, a);\n" +
                    "}\n";

    public static final String VERTEX_SHADER =
            "#version 120 \n" +
                    "\n" +
                    "void main() {\n" +
                    "    gl_TexCoord[0] = gl_MultiTexCoord0;\n" +
                    "    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n" +
                    "}";

    private static final GLShader CIRCLE_SHADER = new GLShader(VERTEX_SHADER, CIRCLE_FRAG_SHADER) {
        @Override
        public void setupUniforms() {
            this.setupUniform("colour");
            this.setupUniform("innerRadius");
        }
    };

    public static void glDrawSemiCircle(final double x, final double y,
                                        final double diameter,
                                        final float innerRadius,
                                        final double percentage,
                                        final int colour) {
        final boolean restore = glEnableBlend();

        final boolean alphaTest = glIsEnabled(GL_ALPHA_TEST);
        if (alphaTest) glDisable(GL_ALPHA_TEST);

        glUseProgram(CIRCLE_SHADER.getProgram());
        glUniform1f(CIRCLE_SHADER.getUniformLocation("innerRadius"), innerRadius);
        glUniform4f(CIRCLE_SHADER.getUniformLocation("colour"),
                (colour >> 16 & 0xFF) / 255.f,
                (colour >> 8 & 0xFF) / 255.f,
                (colour & 0xFF) / 255.f,
                (colour >> 24 & 0xFF) / 255.f);

        glBegin(GL_QUADS);
        {
            glTexCoord2f(0.f, 0.f);
            glVertex2d(x, y);

            glTexCoord2f(0.f, 1.f);
            glVertex2d(x, y + diameter);

            glTexCoord2f(1.f, 1.f);
            glVertex2d(x + diameter, y + diameter);

            glTexCoord2f(1.f, 0.f);
            glVertex2d(x + diameter, y);
        }
        glEnd();

        glUseProgram(0);

        if (alphaTest) glEnable(GL_ALPHA_TEST);

        glRestoreBlend(restore);
    }

    private static final String ROUNDED_QUAD_FRAG_SHADER =
            "#version 120\n" +
                    "uniform float width;\n" +
                    "uniform float height;\n" +
                    "uniform float radius;\n" +
                    "uniform vec4 colour;\n" +
                    "\n" +
                    "float SDRoundedRect(vec2 p, vec2 b, float r) {\n" +
                    "    vec2 q = abs(p) - b + r;\n" +
                    "    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - r;\n" +
                    "}\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec2 size = vec2(width, height);\n" +
                    "    vec2 pixel = gl_TexCoord[0].st * size;\n" +
                    "    vec2 centre = 0.5 * size;\n" +
                    "    float b = SDRoundedRect(pixel - centre, centre, radius);\n" +
                    "    float a = 1.0 - smoothstep(0, 1.0, b);\n" +
                    "    gl_FragColor = vec4(colour.rgb, colour.a * a);\n" +
                    "}";

    private static final GLShader ROUNDED_QUAD_SHADER = new GLShader(VERTEX_SHADER, ROUNDED_QUAD_FRAG_SHADER) {
        @Override
        public void setupUniforms() {
            this.setupUniform("width");
            this.setupUniform("height");
            this.setupUniform("colour");
            this.setupUniform("radius");
        }
    };

    public static void glDrawRoundedQuad(final double x, final double y,
                                         final float width, final float height,
                                         final float radius,
                                         final int colour) {
        final boolean restore = glEnableBlend();

        final boolean alphaTest = glIsEnabled(GL_ALPHA_TEST);
        if (alphaTest) glDisable(GL_ALPHA_TEST);

        glUseProgram(ROUNDED_QUAD_SHADER.getProgram());
        glUniform1f(ROUNDED_QUAD_SHADER.getUniformLocation("width"), width);
        glUniform1f(ROUNDED_QUAD_SHADER.getUniformLocation("height"), height);
        glUniform1f(ROUNDED_QUAD_SHADER.getUniformLocation("radius"), radius);
        glUniform4f(ROUNDED_QUAD_SHADER.getUniformLocation("colour"),
                (colour >> 16 & 0xFF) / 255.f,
                (colour >> 8 & 0xFF) / 255.f,
                (colour & 0xFF) / 255.f,
                (colour >> 24 & 0xFF) / 255.f);

        glDisable(GL_TEXTURE_2D);

        glBegin(GL_QUADS);
        {
            glTexCoord2f(0.f, 0.f);
            glVertex2d(x, y);

            glTexCoord2f(0.f, 1.f);
            glVertex2d(x, y + height);

            glTexCoord2f(1.f, 1.f);
            glVertex2d(x + width, y + height);

            glTexCoord2f(1.f, 0.f);
            glVertex2d(x + width, y);
        }
        glEnd();

        glUseProgram(0);

        glEnable(GL_TEXTURE_2D);

        if (alphaTest) glEnable(GL_ALPHA_TEST);

        glRestoreBlend(restore);
    }

    private static final String RAINBOW_FRAG_SHADER =
            "#version 120\n" +
                    "uniform float width;\n" +
                    "uniform float height;\n" +
                    "uniform float radius;\n" +
                    "uniform float u_time;\n" +
                    "\n" +
                    "float SDRoundedRect(vec2 p, vec2 b, float r) {\n" +
                    "    vec2 q = abs(p) - b + r;\n" +
                    "    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - r;\n" +
                    "}\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec2 size = vec2(width, height);\n" +
                    "    vec2 pixel = gl_TexCoord[0].st * size;\n" +
                    "    vec2 centre = 0.5 * size;\n" +
                    "    float b = SDRoundedRect(pixel - centre, centre, radius);\n" +
                    "    float a = 1.0 - smoothstep(0, 1.0, b);\n" +
                    "    vec3 colour = 0.5 + 0.5*cos(u_time+gl_TexCoord[0].st.x+vec3(0,2,4));\n" +
                    "    gl_FragColor = vec4(colour, a);\n" +
                    "}";

    private static final GLShader GL_COLOUR_SHADER = new GLShader(VERTEX_SHADER, RAINBOW_FRAG_SHADER) {

        private final long initTime = System.currentTimeMillis();

        @Override
        public void setupUniforms() {
            this.setupUniform("width");
            this.setupUniform("height");
            this.setupUniform("radius");
            this.setupUniform("u_time");
        }

        @Override
        public void updateUniforms() {
            glUniform1f(glGetUniformLocation(getProgram(), "u_time"), (System.currentTimeMillis() - initTime) / 1000.0f);
        }
    };

    public static void glDrawRoundedQuadRainbow(final double x, final double y, final float width, final float height, final float radius) {
        final boolean restore = glEnableBlend();

        final boolean alphaTest = glIsEnabled(GL_ALPHA_TEST);
        if (alphaTest) glDisable(GL_ALPHA_TEST);

        GL_COLOUR_SHADER.use();
        glUniform1f(GL_COLOUR_SHADER.getUniformLocation("width"), width);
        glUniform1f(GL_COLOUR_SHADER.getUniformLocation("height"), height);
        glUniform1f(GL_COLOUR_SHADER.getUniformLocation("radius"), radius);

        glDisable(GL_TEXTURE_2D);

        glBegin(GL_QUADS);
        {
            glTexCoord2f(0.f, 0.f);
            glVertex2d(x, y);

            glTexCoord2f(0.f, 1.f);
            glVertex2d(x, y + height);

            glTexCoord2f(1.f, 1.f);
            glVertex2d(x + width, y + height);

            glTexCoord2f(1.f, 0.f);
            glVertex2d(x + width, y);
        }

        glEnd();

        glUseProgram(0);

        glEnable(GL_TEXTURE_2D);

        if (alphaTest) glEnable(GL_ALPHA_TEST);

        glRestoreBlend(restore);
    }


    public static void glDrawRoundedRect(final double x,
                                         final double y,
                                         final double width,
                                         final double height,
                                         final RoundingMode roundingMode,
                                         final float rounding,
                                         final float scaleFactor,
                                         final int colour) {
        boolean bLeft = false;
        boolean tLeft = false;
        boolean bRight = false;
        boolean tRight = false;

        switch (roundingMode) {
            case TOP:
                tLeft = true;
                tRight = true;
                break;
            case BOTTOM:
                bLeft = true;
                bRight = true;
                break;
            case FULL:
                tLeft = true;
                tRight = true;
                bLeft = true;
                bRight = true;
                break;
            case LEFT:
                bLeft = true;
                tLeft = true;
                break;
            case RIGHT:
                bRight = true;
                tRight = true;
                break;
            case TOP_LEFT:
                tLeft = true;
                break;
            case TOP_RIGHT:
                tRight = true;
                break;
            case BOTTOM_LEFT:
                bLeft = true;
                break;
            case BOTTOM_RIGHT:
                bRight = true;
                break;
        }

        final float alpha = (colour >> 24 & 0xFF) / 255.f;

        // Enable blending
        final boolean restore = glEnableBlend();

        // Set colour
        DrawUtil.glColour(colour);

        // Translate matrix to top-left of rect
        glTranslated(x, y, 0);
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);

        // Begin polygon
        glBegin(GL_POLYGON);
        {
            if (tLeft) {
                glVertex2d(rounding, rounding);
                glVertex2d(0, rounding);
            } else {
                glVertex2d(0, 0);
            }

            if (bLeft) {
                glVertex2d(0, height - rounding);
                glVertex2d(rounding, height - rounding);
                glVertex2d(rounding, height);
            } else {
                glVertex2d(0, height);
            }

            if (bRight) {
                glVertex2d(width - rounding, height);
                glVertex2d(width - rounding, height - rounding);
                glVertex2d(width, height - rounding);
            } else {
                glVertex2d(width, height);
            }

            if (tRight) {
                glVertex2d(width, rounding);
                glVertex2d(width - rounding, rounding);
                glVertex2d(width - rounding, 0);
            } else {
                glVertex2d(width, 0);
            }

            if (tLeft) {
                glVertex2d(rounding, 0);
            }
        }
        // Draw polygon
        glEnd();

        // Enable anti-aliasing
        glEnable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);

        // Set point size
        glPointSize(rounding * 2.f * glGetFloat(GL_MODELVIEW_MATRIX) * scaleFactor);

        glBegin(GL_POINTS);
        {
            if (tLeft) {
                // Top left
                glVertex2d(rounding, rounding);
            }

            if (tRight) {
                // Top right
                glVertex2d(width - rounding, rounding);
            }

            if (bLeft) {
                // Bottom left
                glVertex2d(rounding, height - rounding);
            }

            if (bRight) {
                // Bottom right
                glVertex2d(width - rounding, height - rounding);
            }
        }
        glEnd();

        // Disable anti-aliasing
        glDisable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_DONT_CARE);
        // Disable blending
        glRestoreBlend(restore);
        // Translate matrix back (instead of creating a new matrix with glPush/glPop)
        glTranslated(-x, -y, 0);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }


    public static void glDrawRoundedRectEllipse(final double x,
                                                final double y,
                                                final double width,
                                                final double height,
                                                final RoundingMode roundingMode,
                                                final int roundingDef,
                                                final double roundingLevel,
                                                final int colour) {
        boolean bLeft = false;
        boolean tLeft = false;
        boolean bRight = false;
        boolean tRight = false;

        switch (roundingMode) {
            case TOP:
                tLeft = true;
                tRight = true;
                break;
            case BOTTOM:
                bLeft = true;
                bRight = true;
                break;
            case FULL:
                tLeft = true;
                tRight = true;
                bLeft = true;
                bRight = true;
                break;
            case LEFT:
                bLeft = true;
                tLeft = true;
                break;
            case RIGHT:
                bRight = true;
                tRight = true;
                break;
            case TOP_LEFT:
                tLeft = true;
                break;
            case TOP_RIGHT:
                tRight = true;
                break;
            case BOTTOM_LEFT:
                bLeft = true;
                break;
            case BOTTOM_RIGHT:
                bRight = true;
                break;
        }

        // Translate matrix to top-left of rect
        glTranslated(x, y, 0);
        // Enable triangle anti-aliasing
        glEnable(GL_POLYGON_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        // Enable blending
        final boolean restore = glEnableBlend();

        if (tLeft) {
            // Top left
            glDrawFilledEllipse(roundingLevel, roundingLevel, roundingLevel,
                    (int) (roundingDef * 0.5), (int) (roundingDef * 0.75),
                    roundingDef, false, colour);
        }

        if (tRight) {
            // Top right
            glDrawFilledEllipse(width - roundingLevel, roundingLevel, roundingLevel,
                    (int) (roundingDef * 0.75), roundingDef,
                    roundingDef, false, colour);
        }

        if (bLeft) {
            // Bottom left
            glDrawFilledEllipse(roundingLevel, height - roundingLevel, roundingLevel,
                    (int) (roundingDef * 0.25), (int) (roundingDef * 0.5),
                    roundingDef, false, colour);
        }

        if (bRight) {
            // Bottom right
            glDrawFilledEllipse(width - roundingLevel, height - roundingLevel, roundingLevel,
                    0, (int) (roundingDef * 0.25),
                    roundingDef, false, colour);
        }

        // Enable triangle anti-aliasing (to save performance on next poly draw)
        glDisable(GL_POLYGON_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);

        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set colour
        glColour(colour);

        // Begin polygon
        glBegin(GL_POLYGON);
        {
            if (tLeft) {
                glVertex2d(roundingLevel, roundingLevel);
                glVertex2d(0, roundingLevel);
            } else {
                glVertex2d(0, 0);
            }

            if (bLeft) {
                glVertex2d(0, height - roundingLevel);
                glVertex2d(roundingLevel, height - roundingLevel);
                glVertex2d(roundingLevel, height);
            } else {
                glVertex2d(0, height);
            }

            if (bRight) {
                glVertex2d(width - roundingLevel, height);
                glVertex2d(width - roundingLevel, height - roundingLevel);
                glVertex2d(width, height - roundingLevel);
            } else {
                glVertex2d(width, height);
            }

            if (tRight) {
                glVertex2d(width, roundingLevel);
                glVertex2d(width - roundingLevel, roundingLevel);
                glVertex2d(width - roundingLevel, 0);
            } else {
                glVertex2d(width, 0);
            }

            if (tLeft) {
                glVertex2d(roundingLevel, 0);
            }
        }
        // Draw polygon
        glEnd();

        // Disable blending
        glRestoreBlend(restore);
        // Translate matrix back (instead of creating a new matrix with glPush/glPop)
        glTranslated(-x, -y, 0);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static boolean glEnableBlend() {
        final boolean wasEnabled = glIsEnabled(GL_BLEND);

        if (!wasEnabled) {
            glEnable(GL_BLEND);
            glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        }

        return wasEnabled;
    }

    public static void glRestoreBlend(final boolean wasEnabled) {
        if (!wasEnabled) {
            glDisable(GL_BLEND);
        }
    }

    public static float interpolate(final float old, final float now, final float progress) {
        return old + (now - old) * progress;
    }

    public static double interpolate(final double old, final double now, final double progress) {
        return old + (now - old) * progress;
    }

    public static Vec3 interpolate(final Vec3 old, final Vec3 now, final double progress) {
        final Vec3 difVec = now.subtract(old);
        return new Vec3(old.xCoord + difVec.xCoord * progress,
                old.yCoord + difVec.yCoord * progress,
                old.zCoord + difVec.zCoord * progress);
    }

    public static double[] interpolate(final Entity entity, final float partialTicks) {
        return new double[]{
                interpolate(entity.prevPosX, entity.posX, partialTicks),
                interpolate(entity.prevPosY, entity.posY, partialTicks),
                interpolate(entity.prevPosZ, entity.posZ, partialTicks),
        };
    }

    public static AxisAlignedBB interpolate(final Entity entity,
                                            final AxisAlignedBB boundingBox,
                                            final float partialTicks) {
        final float invertedPT = 1.0f - partialTicks;
        return boundingBox.offset(
                (entity.posX - entity.prevPosX) * -invertedPT,
                (entity.posY - entity.prevPosY) * -invertedPT,
                (entity.posZ - entity.prevPosZ) * -invertedPT
        );
    }

    public static void glDrawBoundingBox(final AxisAlignedBB bb,
                                         final float lineWidth,
                                         final boolean filled) {
        if (filled) {
            // 4 sides
            glBegin(GL_QUAD_STRIP);
            {
                glVertex3d(bb.minX, bb.minY, bb.minZ);
                glVertex3d(bb.minX, bb.maxY, bb.minZ);

                glVertex3d(bb.maxX, bb.minY, bb.minZ);
                glVertex3d(bb.maxX, bb.maxY, bb.minZ);

                glVertex3d(bb.maxX, bb.minY, bb.maxZ);
                glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

                glVertex3d(bb.minX, bb.minY, bb.maxZ);
                glVertex3d(bb.minX, bb.maxY, bb.maxZ);

                glVertex3d(bb.minX, bb.minY, bb.minZ);
                glVertex3d(bb.minX, bb.maxY, bb.minZ);
            }
            glEnd();

            // Bottom
            glBegin(GL_QUADS);
            {
                glVertex3d(bb.minX, bb.minY, bb.minZ);
                glVertex3d(bb.maxX, bb.minY, bb.minZ);
                glVertex3d(bb.maxX, bb.minY, bb.maxZ);
                glVertex3d(bb.minX, bb.minY, bb.maxZ);
            }
            glEnd();

            glCullFace(GL_FRONT);

            // Top
            glBegin(GL_QUADS);
            {
                glVertex3d(bb.minX, bb.maxY, bb.minZ);
                glVertex3d(bb.maxX, bb.maxY, bb.minZ);
                glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
                glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            }
            glEnd();

            glCullFace(GL_BACK);
        }


        if (lineWidth > 0) {
            glLineWidth(lineWidth);

            glEnable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);

            glBegin(GL_LINE_STRIP);
            {
                // Bottom
                glVertex3d(bb.minX, bb.minY, bb.minZ);
                glVertex3d(bb.maxX, bb.minY, bb.minZ);
                glVertex3d(bb.maxX, bb.minY, bb.maxZ);
                glVertex3d(bb.minX, bb.minY, bb.maxZ);
                glVertex3d(bb.minX, bb.minY, bb.minZ);

                // Top
                glVertex3d(bb.minX, bb.maxY, bb.minZ);
                glVertex3d(bb.maxX, bb.maxY, bb.minZ);
                glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
                glVertex3d(bb.minX, bb.maxY, bb.maxZ);
                glVertex3d(bb.minX, bb.maxY, bb.minZ);
            }
            glEnd();

            glBegin(GL_LINES);
            {
                glVertex3d(bb.maxX, bb.minY, bb.minZ);
                glVertex3d(bb.maxX, bb.maxY, bb.minZ);

                glVertex3d(bb.maxX, bb.minY, bb.maxZ);
                glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

                glVertex3d(bb.minX, bb.minY, bb.maxZ);
                glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            }
            glEnd();

            glDisable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        }
    }

    public enum RoundingMode {
        TOP_LEFT,
        BOTTOM_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,

        LEFT,
        RIGHT,

        TOP,
        BOTTOM,

        FULL
    }
}
