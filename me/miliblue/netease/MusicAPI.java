package me.miliblue.netease;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This file is a part of Seeking Client.
 */
public class MusicAPI {
    public static String getIdByName(String name) throws IOException {
        URL url = new URL(new String("http://music.163.com/api/search/get/web?csrf_token=hlpretag=&hlposttag=&s="+name+"&type=1&offset=0&total=true&limit=2").replace(" ", "%20"));
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setConnectTimeout(3000);
        httpCon.setDoInput(true);
        httpCon.setRequestMethod("GET");

        String jsonStr;
        if(httpCon.getResponseCode() == 200){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            // 将输入流转移到内存输出流中
            while ((len = httpCon.getInputStream().read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, len);
            }
            // 将内存流转换为字符串
            jsonStr = new String(out.toByteArray());
            System.out.printf(jsonStr+"\n");
            Gson gson = new Gson();
            JsonObject jo = gson.fromJson(jsonStr, JsonObject.class);
            return (gson.fromJson(gson.fromJson(gson.fromJson(jo.get("result"), JsonObject.class).get("songs"), JsonArray.class).get(0), JsonObject.class).get("id").toString());
        }
        return null;
    }
}
