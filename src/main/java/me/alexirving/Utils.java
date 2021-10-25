package me.alexirving;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class Utils {
  public void copyFile(String name) {
    try {
      if (!new File(name).exists()) {
        Files.copy(
            this.getClass().getClassLoader().getResourceAsStream("settings.properties"),
            new File("settings.properties").toPath(),
            StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean isLatest(Properties properties) {
    try {
      URL url = new URL(properties.getProperty("TIMEURL"));
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(connection.getInputStream()));

      if (Files.readAllLines(new File("latest.txt").toPath()).get(0).equals(reader.readLine())) {
        return true;

      } else {
        Files.write(
            new File("latest.txt").toPath(),
            reader.readLine().getBytes(),
            StandardOpenOption.WRITE);
        return false;
      }

    } catch (Exception ignored) {
      return false;
    }
  }
}
