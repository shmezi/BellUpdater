package me.alexirving;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class BellUpdater {
  public static void main(String[] args) throws IOException {
    Utils utils = new Utils();
    utils.copyFile("settings.properties");
    if (!new File("latest.txt").exists()) {
      new File("latest.txt").createNewFile();
    }

    Properties properties = new Properties();
    properties.load(new FileInputStream("settings.properties"));
    URL url = new URL(properties.getProperty("URL"));

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            try {
              if (!utils.isLatest(properties)) {
                System.out.println("Getting the new song!");
                if (properties.getProperty("AUTO_KILL").equals("ON")) {
                  Runtime.getRuntime().exec("taskkill /F /IM " + properties.getProperty("APPNAME"));
                }
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));

                FileOutputStream outputStream =
                    new FileOutputStream(properties.getProperty("NAME"));
                outputStream.write(Base64.getDecoder().decode(reader.readLine()));
                outputStream.close();
                if (properties.getProperty("AUTO_KILL").equals("ON")) {
                  Desktop.getDesktop().open(new File(properties.getProperty("APP_PATH")));
                }
              }else{
                System.out.println("Latest song already present!");
              }

            } catch (IOException ignored) {
            }
          }
        },
        0,
        Integer.decode(properties.getProperty("DELAY")));
  }
}
