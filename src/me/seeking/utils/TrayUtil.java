package me.seeking.utils;

import java.awt.*;
import java.io.IOException;

public class TrayUtil {
    public static TrayIcon trayIcon;
    public static void init() throws AWTException, IOException {
        if(SystemTray.isSupported()){
            SystemTray tray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(""), "Tray Demo");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("Seeking Client");
            tray.add(trayIcon);
        }
    }
}
