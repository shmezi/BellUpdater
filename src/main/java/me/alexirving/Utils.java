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
    System.out.println("Checking if it's the latest version!");
    try {
      URL url = new URL(properties.getProperty("TIMEURL"));
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", "Mozilla/5.0");
      System.out.println(
          "Finished with code: "
              + connection.getResponseCode()
              + " And Message: "
              + connection.getResponseMessage());

      BufferedReader reader =
          new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String temp = reader.readLine();

      if (Files.readAllLines(new File("latest.txt").toPath()).toArray().length > 0) {
        if (Files.readAllLines(new File("latest.txt").toPath()).get(0).equals(temp)) {
          System.out.println("Song was the current version of the song!");
          return true;
        } else {
          System.out.println("Song was not the latest version of the song! Getting it now!");
          Files.write(
              new File("latest.txt").toPath(),
              temp.getBytes(),
              StandardOpenOption.TRUNCATE_EXISTING);
          return false;
        }
      } else {
        System.out.println("Song was not the latest version of the song! Getting it now!");
        Files.write(
            new File("latest.txt").toPath(), temp.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        return false;
      }

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
