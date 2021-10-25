package me.alexirving;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Utils {
  public void makeProperties() {
    try {
      Files.copy(
          this.getClass().getClassLoader().getResourceAsStream("settings.properties"),
          new File("settings.properties").toPath(),
          StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
