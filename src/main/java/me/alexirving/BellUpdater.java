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
                if (properties.getProperty("AUTO_KILL").equals("ON")) {
                  System.out.println("Killing the process");
                  Runtime.getRuntime().exec("taskkill /F /IM " + properties.getProperty("APPNAME"));
                } else {
                  System.out.println("Not killing the process since Auto kill was turned off.");
                }

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                BufferedReader reader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));
                System.out.println(
                    "Trying to put song at path: "
                        + new File(properties.getProperty("NAME")).getAbsolutePath());

                FileOutputStream outputStream =
                    new FileOutputStream(properties.getProperty("NAME"));

                outputStream.write(Base64.getDecoder().decode(reader.readLine()));
                outputStream.close();

                if (properties.getProperty("AUTO_KILL").equals("ON")) {
                  System.out.println("ReOpening the app.");
                  Desktop.getDesktop().open(new File(properties.getProperty("APP_PATH")));
                }
              } else {
                System.out.println("Latest song already present!");
              }

            } catch (IOException e) {
              System.out.println("ERROR While getting the new song!");
              e.printStackTrace();
            }
          }
        },
        0,
        Integer.decode(properties.getProperty("DELAY")));
  }
}
