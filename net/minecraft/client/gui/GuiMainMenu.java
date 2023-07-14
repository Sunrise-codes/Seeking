package net.minecraft.client.gui;

import me.seeking.Seeking;
import me.seeking.ui.GuiResetSession;
import me.seeking.ui.MainMenuButton;
import me.seeking.ui.font.FontLoaders;
import me.seeking.utils.RenderUtil;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

import java.awt.*;
import java.net.URI;

import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveFormat;

import java.util.Date;
import java.util.Calendar;
import net.minecraft.client.settings.GameSettings;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.OpenGlHelper;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.io.Charsets;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.DynamicTexture;

import java.util.Random;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GLContext;

import java.util.concurrent.atomic.AtomicInteger;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback
{
    private static final AtomicInteger field_175373_f;
    private static final Logger logger;
    private static final Random RANDOM;
    private float updateCounter;
    private String splashText;
    private GuiButton buttonResetDemo;
    private int panoramaTimer;
    private DynamicTexture viewportTexture;
    private boolean field_175375_v;
    private final Object threadLock;
    private String openGLWarning1;
    private String openGLWarning2;
    private String openGLWarningLink;
    private static final ResourceLocation splashTexts;
    private static final ResourceLocation minecraftTitleTextures;
    private static final ResourceLocation[] titlePanoramaPaths;
    public static final String field_96138_a;
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation backgroundTexture;
    private GuiButton realmsButton;
    private boolean field_183502_L;
    private GuiScreen field_183503_M;
    public static int frame = 1;
    public GuiMainMenu() {
        this.field_175375_v = true;
        this.threadLock = new Object();
        this.openGLWarning2 = GuiMainMenu.field_96138_a;
        this.field_183502_L = false;
        this.splashText = "missingno";
        BufferedReader bufferedreader = null;
        try {
            final List<String> list = Lists.newArrayList();
            bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(GuiMainMenu.splashTexts).getInputStream(), Charsets.UTF_8));
            String s;
            while ((s = bufferedreader.readLine()) != null) {
                s = s.trim();
                if (!s.isEmpty()) {
                    list.add(s);
                }
            }
            if (!list.isEmpty()) {
                do {
                    this.splashText = list.get(GuiMainMenu.RANDOM.nextInt(list.size()));
                } while (this.splashText.hashCode() == 125780783);
            }
        }
        catch (IOException ex) {}
        finally {
            if (bufferedreader != null) {
                try {
                    bufferedreader.close();
                }
                catch (IOException ex2) {}
            }
        }
        this.updateCounter = GuiMainMenu.RANDOM.nextFloat();
        this.openGLWarning1 = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
            this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    private boolean func_183501_a() {
        return Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && this.field_183503_M != null;
    }

    public void updateScreen() {
        ++this.panoramaTimer;
        if (this.func_183501_a()) {
            this.field_183503_M.updateScreen();
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        }
        else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
            this.splashText = "Happy new year!";
        }
        else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }
        final int i = 24;
        final int j = this.height / 4 + 48;
        this.addSingleplayerMultiplayerButtons(j, 24);
        this.buttonList.add(new MainMenuButton(0, this.width / 2 - 50, new ScaledResolution(mc).getScaledHeight() / 2 + 40, 98, 20, I18n.format("Options", new Object[0]),false,false));
        this.buttonList.add(new MainMenuButton(4, this.width / 2 - 50, new ScaledResolution(mc).getScaledHeight() / 2 + 60, 98, 20, I18n.format("Quit", new Object[0]),true,false));
        synchronized (this.threadLock) {
            this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
            this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
            final int k = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (this.width - k) / 2;
            this.field_92021_u = this.buttonList.get(0).yPosition - 24;
            this.field_92020_v = this.field_92022_t + k;
            this.field_92019_w = this.field_92021_u + 24;
        }
        if (this.func_183501_a()) {
            this.field_183503_M.setGuiSize(this.width, this.height);
            this.field_183503_M.initGui();
        }
    }

    private void addSingleplayerMultiplayerButtons(final int p_73969_1_, final int p_73969_2_) {
        this.buttonList.add(new MainMenuButton(1, this.width / 2 - 50, new ScaledResolution(mc).getScaledHeight() / 2 - 20, 98, 20,I18n.format("Singleplayer", new Object[0]), false, true));
        this.buttonList.add(new MainMenuButton(2, this.width / 2 - 50, new ScaledResolution(mc).getScaledHeight() / 2, 98, 20,I18n.format("Multiplayer", new Object[0]), false, false));
        //this.buttonList.add(new GuiButton(6, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("fml.menu.mods", new Object[0])));
        this.buttonList.add(this.realmsButton = new MainMenuButton(14, this.width / 2 - 50, new ScaledResolution(mc).getScaledHeight() / 2 + 20, 98, 20,"Alt Login", false, false));
    }

    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
        }
        if (button.id == 5) {
            this.mc.displayGuiScreen((GuiScreen)new GuiLanguage((GuiScreen)this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen((GuiScreen)new GuiSelectWorld((GuiScreen)this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
        }
        if (button.id == 14) {
            this.mc.displayGuiScreen((GuiScreen)new GuiResetSession());
        }
        if (button.id == 4) {
            this.mc.shutdown();
        }
        if (button.id == 6) {
            // this.mc.displayGuiScreen((GuiScreen)new GuiModList((GuiScreen)this));
        }
        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }
        if (button.id == 12) {
            final ISaveFormat isaveformat = this.mc.getSaveLoader();
            final WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
            if (worldinfo != null) {
                final GuiYesNo guiyesno = GuiSelectWorld.makeDeleteWorldYesNo((GuiYesNoCallback)this, worldinfo.getWorldName(), 12);
                this.mc.displayGuiScreen((GuiScreen)guiyesno);
            }
        }
    }

    public void confirmClicked(final boolean result, final int id) {
        if (result && id == 12) {
            final ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen((GuiScreen)this);
        }
        else if (id == 13) {
            if (result) {
                try {
                    final Class<?> oclass = Class.forName("java.awt.Desktop");
                    final Object object = oclass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI(this.openGLWarningLink));
                }
                catch (Throwable throwable) {
                    GuiMainMenu.logger.error("Couldn't open link", throwable);
                }
            }
            this.mc.displayGuiScreen((GuiScreen)this);
        }
    }
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawGradientRect(0, 0, this.width, this.height, 0x00FFFFFF, 0x00FFFFFF);
        Seeking.instance.si.renderShader(this.width, this.height);
        GlStateManager.enableAlpha();
        Gui.drawRect(this.width / 2 - 54, new ScaledResolution(mc).getScaledHeight() / 2 - 25, 105 + this.width / 2 - 54, 110 + new ScaledResolution(mc).getScaledHeight() / 2 - 25, new Color(255, 255, 255, 210).getRGB());
        RenderUtil.drawShadow(this.width / 2 - 54, new ScaledResolution(mc).getScaledHeight() / 2 - 25, 105, 110);
        final String s2 = "Seeking 0.1";
        FontLoaders.font18.drawStringWithShadow(s2, this.width - FontLoaders.font18.getStringWidth(s2) - 2, this.height - 10, -1);
        if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0) {
            drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
            this.drawString(this.fontRendererObj, this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
            this.drawString(this.fontRendererObj, this.openGLWarning2, (this.width - this.field_92024_r) / 2, this.buttonList.get(0).yPosition - 12, -1);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        synchronized (this.threadLock) {
            if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
                final GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink((GuiYesNoCallback)this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen((GuiScreen)guiconfirmopenlink);
            }
        }
    }

    public void onGuiClosed() {
        if (this.field_183503_M != null) {
            this.field_183503_M.onGuiClosed();
        }
    }

    static {
        field_175373_f = new AtomicInteger(0);
        logger = LogManager.getLogger();
        RANDOM = new Random();
        splashTexts = new ResourceLocation("texts/splashes.txt");
        minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
        titlePanoramaPaths = new ResourceLocation[] { new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png") };
        field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    }
}