package cz.cvut.fel.aic.apdemo.config;

import java.lang.Integer;
import java.lang.String;
import java.util.HashMap;
import ninja.fido.config.GeneratedConfig;

public class Config implements GeneratedConfig {
  public String mapGeojsonEdges;

  public Agentpolis agentpolis;

  public String mapFilePath;

  public String mapGeojsonNodes;

  public Integer srid;

  public Config() {
  }

  public Config fill(HashMap config) {
    this.mapGeojsonEdges = (String) config.get("map_geojson_edges");
    this.agentpolis = new Agentpolis((HashMap) config.get("agentpolis"));
    this.mapFilePath = (String) config.get("map_file_path");
    this.mapGeojsonNodes = (String) config.get("map_geojson_nodes");
    this.srid = (Integer) config.get("srid");
    return this;
  }
}
