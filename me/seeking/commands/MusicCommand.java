package me.seeking.commands;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.*;
import java.net.*;

import me.miliblue.netease.MusicAPI;
import me.seeking.managers.Command;
import me.seeking.utils.PlayerUtil;
import static me.drqzszn.ChineseURLEncoder.containsChinese;
import static me.drqzszn.ChineseURLEncoder.encodeChineseToURL;

/**
 * This file is a part of Seeking Client.
 */
public class MusicCommand implements Command {
    public static Player player;
    @Override
    public boolean run(String[] args) {
        if(args.length == 3) {
            if (args[1].equals("playByName")) {
                String name1 = args[2];
                String name = encodeChineseToURL(String.valueOf(name1));
                StringBuilder name2 =new StringBuilder(name);
                if (args.length > 2) {
                    for (int i = 2; i < args.length; i++) {
                        name2.append(args[i] + " ");
                    }
                }
                try {
                    play(getUrl(MusicAPI.getIdByName(name.toString())));
                    PlayerUtil.tellPlayer("Now Playing:" + name1);
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
            }else if (args[1].equals("search")) {
                String name123 = args[2];
                String url = "https://music.163.com/api/search/get/web?csrf_token=hlpretag=&hlposttag=&s=" + encodeChineseToURL(name123) + "&type=1&offset=0&total=true&limit=5";
                try {
                    String jsonResponse = sendGetRequest(url);
                    JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);
                    JsonObject result = jsonObject.getAsJsonObject("result");
                    JsonArray songs = result.getAsJsonArray("songs");

                    for (JsonElement songElement : songs) {
                        JsonObject song = songElement.getAsJsonObject();
                        String name = song.get("name").getAsString();
                        String id = song.get("id").getAsString();

                        PlayerUtil.tellPlayer("Name: " + name + " ID: " + id);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
                return false;
        } else if (args.length == 2) {
             if (args[1].equals("help")) {
                PlayerUtil.tellPlayer("-music help 查看帮助\n-music stop停止播放\n-music play播放音乐\n-music search music_name 搜索音乐\n-music playByName music_name 播放名字对应的音乐\n-music playById music_id 播放id对应的音乐");
            }
             else if (args[1].equals("stop")) {
                 if (player != null) {
                     player.close();
                     player = null;
                 } else
                     PlayerUtil.tellPlayer("我操你妈的 播放器都没实例你stop你妈生命？");
             }
        }
        else
            return false;
        return true;
    }

    @Override
    public String usage() {
        return "-music help查看帮助";
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
    private static String sendGetRequest(String url) throws IOException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();

        try {
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return response.toString();
    }

    public static String getUrl(String id) {

        return "http://music.163.com/song/media/outer/url?id=" + id + ".mp3";
    }
}
