package me.seeking.ui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.Session;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * This file is a part of Seeking Client.
 */
public class GuiResetSession extends GuiScreen {
    private List<GuiTextField> textFieldList = Lists.<GuiTextField>newArrayList();

    private ScaledResolution scaledResolution;
    @Override
    public void initGui() {
        super.initGui();
        scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        textFieldList.clear();
        this.textFieldList.add(new GuiTextField(0, fontRendererObj, scaledResolution.getScaledWidth() / 2 - 100, scaledResolution.getScaledHeight() / 5, 200, 20));
        this.textFieldList.add(new GuiTextField(1, fontRendererObj, scaledResolution.getScaledWidth() / 2 - 100, scaledResolution.getScaledHeight() / 5 + 32, 200, 20));
        this.textFieldList.add(new GuiTextField(2, fontRendererObj, scaledResolution.getScaledWidth() / 2 - 100, scaledResolution.getScaledHeight() / 5 + 64, 200, 20));
        this.textFieldList.add(new GuiTextField(3, fontRendererObj, scaledResolution.getScaledWidth() / 2 - 100, scaledResolution.getScaledHeight() / 5 + 96, 200, 20));
        this.textFieldList.add(new GuiTextField(4, fontRendererObj, scaledResolution.getScaledWidth() / 2 - 100, scaledResolution.getScaledHeight() / 5 + 128, 200, 20));
        for (GuiTextField gtf : textFieldList){
            gtf.setMaxStringLength(32767);
        }
        for (GuiTextField tf : textFieldList) {
            String username = mc.getSession().getUsername();
            String playerID = mc.getSession().getPlayerID();
            String token = mc.getSession().getToken();
            String sessiontype = mc.getSession().getSessionType().toString();

            if (tf.getId() == 0) {
                tf.setText(username);
            }

            if (tf.getId() == 1) {
                tf.setText(playerID);
            }
            if (tf.getId() == 2) {
                tf.setText(token);
            }
            if (tf.getId() == 3) {
                tf.setText(sessiontype);
            }

            if(tf.getId() == 4){
                String text =  "-" + "username-" + username + "-username" + "-" + "playerID-" + playerID + "-playerID" + "-" + "token-" + token + "-token" + "-" + "sessiontype-" + sessiontype + "-sessiontype" + "-";
                tf.setText(text);
            }
        }

        this.buttonList.add(new GuiButton(0, scaledResolution.getScaledWidth() / 2 - 100, scaledResolution.getScaledHeight() / 5 + 160, "OK"));
        this.buttonList.add(new GuiButton(1, scaledResolution.getScaledWidth() / 2 - 100, scaledResolution.getScaledHeight() / 5 + 184, "Cancel"));
    }

    @Override
    public void onGuiClosed() {
    }

    @Override
    public void updateScreen() {
        for (GuiTextField tf : textFieldList) {
            tf.updateCursorCounter();
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        fontRendererObj.drawString("Username", this.width / 2 - 100, scaledResolution.getScaledHeight() / 5 - 10, new Color(128, 128, 128).getRGB());
        fontRendererObj.drawString("PlayerID", this.width / 2 - 100, scaledResolution.getScaledHeight() / 5 + 32 - 10, new Color(128, 128, 128).getRGB());
        fontRendererObj.drawString("Token", this.width / 2 - 100, scaledResolution.getScaledHeight() / 5 + 64 - 10, new Color(128, 128, 128).getRGB());
        fontRendererObj.drawString("SessionType (legacy/mojang)", this.width / 2 - 100, scaledResolution.getScaledHeight() / 5 + 96 - 10, new Color(128, 128, 128).getRGB());
        fontRendererObj.drawString("Code", this.width / 2 - 100, scaledResolution.getScaledHeight() / 5 + 128 - 10, new Color(128, 128, 128).getRGB());

        for (GuiTextField tf : textFieldList) {
            tf.drawTextBox();
        }

        for (GuiButton button : buttonList) {
            button.drawButton(mc, mouseX, mouseY);
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {

            String username = null;
            String playerID = null;
            String token = null;
            String sessiontype = null;

            for (GuiTextField tf : textFieldList) {
                if (tf.getId() == 0) {
                    username = tf.getText();
                }
                if (tf.getId() == 1) {
                    playerID = tf.getText();
                }
                if (tf.getId() == 2) {
                    token = tf.getText();
                }
                if (tf.getId() == 3) {
                    sessiontype = tf.getText();
                }
            }

             mc.setSession(new Session(username, playerID, token, sessiontype));
            mc.displayGuiScreen(new GuiMainMenu());
        }
        if (button.id == 1) {
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    @Override
    protected void keyTyped(char keyChar, int keyCode) throws IOException {
        super.keyTyped(keyChar, keyCode);

        for (GuiTextField tf : textFieldList) {

            if (tf.isFocused()) {

                if(tf.getId() != 4){

                    if(tf.getText().equals("Fuck You! Error! Input code is shit wrong!")){
                        String username = mc.getSession().getUsername();
                        String playerID = mc.getSession().getPlayerID();
                        String token = mc.getSession().getToken();
                        String sessiontype = mc.getSession().getSessionType().toString();

                        if (tf.getId() == 0) {
                            tf.setText(username);
                        }

                        if (tf.getId() == 1) {
                            tf.setText(playerID);
                        }
                        if (tf.getId() == 2) {
                            tf.setText(token);
                        }
                        if (tf.getId() == 3) {
                            tf.setText(sessiontype);
                        }
                    }else if(keyChar != '-' && keyChar != ' '){
                        tf.textboxKeyTyped(keyChar, keyCode);
                    }

                    String username = null;
                    String playerID = null;
                    String token = null;
                    String sessiontype = null;

                    for (GuiTextField tf1 : textFieldList) {

                        if (tf1.getId() == 0) {
                            username = tf1.getText();
                        }
                        if (tf1.getId() == 1) {
                            playerID = tf1.getText();
                        }
                        if (tf1.getId() == 2) {
                            token = tf1.getText();
                        }
                        if (tf1.getId() == 3) {
                            sessiontype = tf1.getText();
                        }

                        if(tf1.getId() == 4){
                            String text =  "-" + "username-" + username + "-username" + "-" + "playerID-" + playerID + "-playerID" + "-" + "token-" + token + "-token" + "-" + "sessiontype-" + sessiontype + "-sessiontype" + "-";
                            tf1.setText(text);
                        }
                    }

                }else{
                    tf.textboxKeyTyped(keyChar, keyCode);

                    String text = tf.getText();

                    String username = getSubString(text,"-username-","-username-");
                    String playerID = getSubString(text,"-playerID-","-playerID-");;
                    String token = getSubString(text,"-token-","-token-");;
                    String sessiontype = getSubString(text,"-sessiontype-","-sessiontype-");;

                    for (GuiTextField tf1 : textFieldList) {

                        if(username == null || playerID == null || token == null || sessiontype == null){
                            if(tf1.getId() != 4){
                                tf1.setText("Fuck You! Error! Input code is shit wrong!");
                            }
                            continue;
                        }

                        if (tf1.getId() == 0) {
                            tf1.setText(username);
                        }
                        if (tf1.getId() == 1) {
                            tf1.setText(playerID);
                        }
                        if (tf1.getId() == 2) {
                            tf1.setText(token);
                        }
                        if (tf1.getId() == 3) {
                            tf1.setText(sessiontype);
                        }
                    }
                }

            }

        }
    }

    public static String getSubString(String text, String left, String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            return null;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                return null;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right.isEmpty()) {
            yLen = text.length();
            return null;
        }
        result = text.substring(zLen, yLen);
        return result;
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (GuiTextField tf : textFieldList) {
            tf.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
}
