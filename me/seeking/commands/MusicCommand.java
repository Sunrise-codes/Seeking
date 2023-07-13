package me.seeking.commands;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.*;
import java.net.*;
import me.seeking.managers.Command;
import me.seeking.utils.PlayerUtil;

/**
 * This file is a part of Seeking Client.
 */
public class MusicCommand implements Command {
    public static Player player;
    @Override
    public boolean run(String[] args) {
        PlayerUtil.tellPlayer(args[1]);
        if(args[1].equals("search")){
            String[] temp = geMusicIdArray(args[2]);
            for (String s : temp){
                PlayerUtil.tellPlayer("Found: "+s);
            }
        }
        if(args[1].equals("play")){
            try {
                play(getUrl(args[2]));
            } catch (MalformedURLException e) {
                PlayerUtil.tellPlayer("ERROR IN EXECUTE PLAY METHOD, MESSAGE:"+e.getMessage());
            }
        }
        if(args[1].equals("stop")){
            if(player != null)
                player.close();
            else
                PlayerUtil.tellPlayer("我操你妈的 播放器都没实例你stop你妈生命？");
        }
        return true;
    }

    @Override
    public String usage() {
        return "-search <name> 搜索Music | -play 播放 | -stop 停止播放";
    }

    public static String[] geMusicIdArray(String musicName){

        String jsonStr = null;
        String id[] = null;

        try {
            String str = URLEncoder.encode(musicName, "utf-8").replaceAll("\\+", "");
            // 转换成encode
            URL url = new URL("http://music.163.com/api/search/pc?s=" + str + "&type=1");
            //拼接url
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(3000);
            httpCon.setDoInput(true);
            httpCon.setRequestMethod("GET");
            PlayerUtil.tellPlayer("GET SEND");
            // 获取相应码
            int respCode = httpCon.getResponseCode();
            if (respCode == 200) {
                // ByteArrayOutputStream相当于内存输出流
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                // 将输入流转移到内存输出流中
                while ((len = httpCon.getInputStream().read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                }
                // 将内存流转换为字符串
                jsonStr = new String(out.toByteArray());
                System.out.println(jsonStr);
                Gson gson = new Gson();
                JsonArray jo = gson.fromJson(gson.fromJson(gson.fromJson(jsonStr, JsonObject.class).get("result"), JsonObject.class).get("songs"), JsonArray.class);
                id = new String[10];
                for (int i = 0; i < jo.size(); i++){
                    id[i] = gson.fromJson(jo.get(i), JsonObject.class).get("id").getAsString();
                    System.out.printf("[DEBUG PLAYER] "+id[i]+"\n");
                }
            } else {
                PlayerUtil.tellPlayer("网易云错误，错误码为" + respCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
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
