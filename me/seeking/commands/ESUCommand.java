package me.seeking.commands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.seeking.managers.Command;
import me.seeking.utils.PlayerUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ESUCommand implements Command {

    @Override
    public boolean run(String[] args) {
        if (args.length == 2) {
            String qq_number = args[1];
            try {
                URL url = new URL("https://zy.xywlapi.cc/qqapi?qq=" + qq_number);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // 解析 JSON 数据
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);

                // 提取 phone 值
                String phone = jsonObject.get("phone").getAsString();

                PlayerUtil.tellPlayer("Phone:" + phone);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public String usage() {
        return "USAGE: -qq <QQ_Number>";
    }
}
