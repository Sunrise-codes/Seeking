package me.seeking.module;

import me.seeking.Seeking;
import me.seeking.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

/**
 * This file is a part of Seeking Client.
 * @author MiLiBlue
 */
public class Module {
    private String name;
    private ModuleType moduleType;
    private int key;
    private boolean enable = false;
    protected Minecraft mc = Minecraft.getMinecraft();
    private double x, y, width, height;
    private boolean needNoti;
    private boolean dragging = false;
    public ArrayList<Value> valuesList = new ArrayList<>();

    public Module(String name, ModuleType moduleType) {
        this.name = name;
        this.moduleType = moduleType;
    }

    public ArrayList<Value> getValues() {
        return valuesList;
    }

    /**
     * 返回Module的名字
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 返回Module的类型
     * @return module's type
     */
    public ModuleType getModuleType() {
        return moduleType;
    }

    /**
     * 返回Module的按键
     * @return key
     */
    public int getKey() {
        return key;
    }

    /**
     * 设置Module的按键.
     * @param key
     */
    public void setKey(int key) {
        this.key = key;
    }

    /**
     * 获取Module的开启状态
     * @return enable state
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * 设置Module的开启状态
     * @param enable
     */
    public void setEnable(boolean enable) {
        this.enable = enable;

        if(enable){
            onEnable();
            Seeking.instance.eventManager.register(this);
            if(needNoti){

            }
        }else {
            onDisable();
            Seeking.instance.eventManager.unregister(this);
            if(needNoti){

            }
        }
    }

    public void addValues(Value... values){
        for(Value v : values){
            valuesList.add(v);
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
    public void mouseClick(int mouseX, int mouseY, int button) {
        if (mouseX > getX() - 2 && mouseX < getX() + getWidth() && mouseY >getY() - 2 && mouseY < getY() + height && isEnable()) {
            if (button == 0) {
                this.dragging = true;
                this.x = mouseX + x;
                this.y = mouseY - y;
            }
        }
    }

    public void doGrag(int mouseX, int mouseY){
        if(this.dragging){
            if (!Mouse.isButtonDown(0)) {
                this.dragging = false;
            }
            setX(mouseX);
            setY(mouseY);
        }
    }


    /**
     * 应在每个Module实例中覆盖此方法。（如果您需要在Module启动时执行一些东西）
     */
    public void onEnable(){}

    /**
     * 应在每个Module实例中覆盖此方法。（如果您需要在Module关闭时执行一些东西）
     */
    public void onDisable(){}

    /**
     * 模块类型的enum，不解释
     */
    public enum ModuleType{
        Render, Player, Fun
    }
}
