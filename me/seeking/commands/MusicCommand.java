package me.seeking.commands;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.*;
import java.net.*;

import me.miliblue.netease.MusicAPI;
import me.seeking.managers.Command;
import me.seeking.utils.PlayerUtil;

/**
 * This file is a part of Seeking Client.
 */
public class MusicCommand implements Command {
    public static Player player;
    @Override
    public boolean run(String[] args) {
        if(args.length > 1) {
            if (args[1].equals("playByName")) {
                StringBuilder name = new StringBuilder();
                if (args.length > 2) {
                    for (int i = 2; i < args.length; i++) {
                        name.append(args[i] + " ");
                    }
                }
                try {
                    play(getUrl(MusicAPI.getIdByName(name.toString())));
                    PlayerUtil.tellPlayer("Now Playing:" + name);
                } catch (IOException e) {
                    PlayerUtil.tellPlayer("There is a exception when API trying to get MusicID:" + e.getMessage());
                    e.printStackTrace();
                }
            } else if (args[1].equals("playById")) {
                try {
                    play(getUrl(args[2]));
                } catch (MalformedURLException e) {
                    PlayerUtil.tellPlayer("ERROR IN EXECUTE PLAY METHOD, MESSAGE:" + e.getMessage());
                }
            } else if (args[1].equals("stop")) {
                if (player != null) {
                    player.close();
                    player = null;
                } else
                    PlayerUtil.tellPlayer("我操你妈的 播放器都没实例你stop你妈生命？");
            } else
                return false;
        }else
            return false;
        return true;
    }

    @Override
    public String usage() {
        return "-play 播放 | -stop 停止播放";
    }
    //播放url音乐
    public static void play(String url) throws MalformedURLException {
        URL url1 = new URL(url);
        new Thread(() -> {
            BufferedInputStream buffer = null;

            try {
                URLConnection connection = url1.openConnection();

                buffer = new BufferedInputStream(connection.getInputStream());

                if (buffer.read() == -1) {
                    PlayerUtil.tellPlayer("不可用的url:" + url);
                }

                player = new Player(buffer);
                player.play();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            } finally {
                if (buffer != null) {
                    try {
                        buffer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static String getUrl(String id) {

        return "http://music.163.com/song/media/outer/url?id=" + id + ".mp3";
    }
}
