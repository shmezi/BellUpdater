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
    if (!new File("settings.properties").exists()) {
      new Utils().makeProperties();

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
              if (properties.getProperty("AUTO_KILL").equals("ON")) {
                Runtime.getRuntime().exec("taskkill /F /IM " + properties.getProperty("APPNAME"));
              }
              HttpURLConnection connection = (HttpURLConnection) url.openConnection();
              connection.setRequestMethod("GET");
              BufferedReader reader =
                  new BufferedReader(new InputStreamReader(connection.getInputStream()));

              FileOutputStream outputStream = new FileOutputStream(properties.getProperty("NAME"));
              outputStream.write(Base64.getDecoder().decode(reader.readLine()));
              outputStream.close();
              if (properties.getProperty("AUTO_KILL").equals("ON")) {
                Desktop.getDesktop().open(new File(properties.getProperty("APP_PATH")));
              }
            } catch (IOException ignored) {
            }
          }
        },
        0,
        Integer.decode(properties.getProperty("DELAY")));
  }
}
