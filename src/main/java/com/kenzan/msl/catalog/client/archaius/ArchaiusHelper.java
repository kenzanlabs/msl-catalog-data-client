package com.kenzan.msl.catalog.client.archaius;

public class ArchaiusHelper {

  /**
   * Inits properties from Archaius Needs to be executed when getting dynamic properties
   */
  public static void setupArchaius() {
    StringBuilder configPath = new StringBuilder();
    configPath.append("file://").append(System.getProperty("user.dir"));
    configPath.append("/../msl-catalog-data-client-config/data-client-config.properties");
    String additionalUrlsProperty = "archaius.configurationSource.additionalUrls";
    System.setProperty(additionalUrlsProperty, configPath.toString());
  }
}
