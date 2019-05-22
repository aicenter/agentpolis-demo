package cz.cvut.fel.aic.apdemo.config;

import java.lang.String;
import java.util.Map;
import ninja.fido.config.GeneratedConfig;

public class ApdemoConfig implements GeneratedConfig {
  public String mapGeojsonEdges;

  public String mapGeojsonNodes;

  public ApdemoConfig() {
  }

  public ApdemoConfig fill(Map apdemoConfig) {
    this.mapGeojsonEdges = (String) apdemoConfig.get("map_geojson_edges");
    this.mapGeojsonNodes = (String) apdemoConfig.get("map_geojson_nodes");
    return this;
  }
}
