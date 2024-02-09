package me.seeking.github;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import me.miliblue.rintaro.Loader;
import me.seeking.Seeking;
import me.seeking.ui.GuiResetSession;
import me.seeking.utils.TrayUtil;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

public class GithubLogin {
    private static final String CLIENT_ID = "37a2ab0f3b6fc3f21bd6";
    private static final String REDIRECT_URI = "http://localhost:1337/oauth";
    private static final String SECRET = "1488bc4be8bb8961201441aeed9c08ba5cb733c8";

    public static final String URL = "https://github.com/login/oauth/authorize?client_id=<client_id>&redirect_uri=<redirect_uri>"
            .replace("<client_id>",CLIENT_ID)
            .replace("<redirect_uri>",REDIRECT_URI);

    private static HttpServer httpServer;
    private static MyHandler handler;
    public String token;

    public GithubLogin() throws IOException {
        handler = new MyHandler();
        httpServer = HttpServer.create(new InetSocketAddress("localhost",1337),0);
        httpServer.createContext("/oauth",handler);
        httpServer.start();
    }

    public void login() throws Exception {
        browse(URL);
    }

    private static void browse(String url) throws Exception {
        // 获取操作系统的名字
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Mac OS")) {
            // 苹果的打开方式
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL",
                    new Class[] { String.class });
            openURL.invoke(null, new Object[] { url });
        } else if (osName.startsWith("Windows")) {
            // windows的打开方式。
            Runtime.getRuntime().exec(
                    "rundll32 url.dll,FileProtocolHandler " + url);
        } else {
            // Unix or Linux的打开方式
            String[] browsers = { "firefox", "opera", "konqueror", "epiphany",
                    "mozilla", "netscape" };
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++)
                // 执行代码，在brower有值后跳出，
                // 这里是如果进程创建成功了，==0是表示正常结束。
                if (Runtime.getRuntime()
                        .exec(new String[] { "which", browsers[count] })
                        .waitFor() == 0)
                    browser = browsers[count];
            if (browser == null)
                throw new Exception("Could not find web browser");
            else
                // 这个值在上面已经成功的得到了一个进程。
                Runtime.getRuntime().exec(new String[] { browser, url });
        }
    }

    private String read(InputStream stream) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        final StringBuilder stringBuilder = new StringBuilder();
        String s;
        while ((s = reader.readLine())!=null){
            stringBuilder.append(s);
        }

        stream.close();
        reader.close();

        return stringBuilder.toString();
    }


    private class MyHandler implements HttpHandler {

        private boolean got=false;
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if(!got){
                String queryUri = exchange.getRequestURI().getQuery();
                if(queryUri.contains("code=")){
                    final String code = queryUri.split("code=")[1];
                    GuiResetSession.state = "Got code";
                    got = true;

                    System.out.println("requestToken = "+code);

                    final URL url = new URL("https://github.com/login/oauth/access_token?client_id="+CLIENT_ID+"&client_secret="+SECRET+"&code="+code);
                    final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");

                    final String read = read(connection.getInputStream());
                    token = read.split("access_token=")[1].split("&")[0];
                    System.out.println("Token = "+token);
                    GuiResetSession.state = "Got token";

                    final URL profileURL = new URL("https://api.github.com/user");
                    final HttpURLConnection profileConnection = (HttpURLConnection) profileURL.openConnection();
                    profileConnection.setDoInput(true);
                    profileConnection.setRequestMethod("GET");
                    profileConnection.setRequestProperty("Authorization", "Bearer " + token);
                    final String profile = read(profileConnection.getInputStream());

                    //FUCK
                    final String username = profile.replace("{\"login\":\"", "").split("\"")[0];
                    final String avatarURL = profile.split("\"avatar_url\":\"")[1].split("\"")[0];
                    final String uid = profile.split("id\":")[1].split(",")[0];

                    Seeking.instance.user = new GithubUser(username, avatarURL, Integer.parseInt(uid));

                    GuiResetSession.state = "Welcome, "+username;

                    if(TrayUtil.trayIcon != null){
                        TrayUtil.trayIcon.displayMessage("Seeking Client", "Welcome use Seeking Client, "+username+" nya~", TrayIcon.MessageType.INFO);
                    }
                }
            }
        }
    }
}
