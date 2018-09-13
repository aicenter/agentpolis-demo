package cz.cvut.fel.aic.apdemo.config;

import java.lang.Integer;
import java.lang.String;
import java.util.Map;

import cz.cvut.fel.aic.agentpolis.config.ADModel;
import ninja.fido.config.GeneratedConfig;

public class ApdemoConfig implements GeneratedConfig {
    public String mapGeojsonEdges;

    public Agentpolis agentpolis;

    public String mapFilePath;

    public String mapGeojsonNodes;

    public Integer srid;

    public ADModel adModel;

    public ApdemoConfig() {

    }
    public ApdemoConfig fill(Map apdemoConfig) {
        this.mapGeojsonEdges = (String) apdemoConfig.get("map_geojson_edges");
        this.agentpolis = new Agentpolis((Map) apdemoConfig.get("agentpolis"));
        this.mapFilePath = (String) apdemoConfig.get("map_file_path");
        this.mapGeojsonNodes = (String) apdemoConfig.get("map_geojson_nodes");
        this.srid = (Integer) apdemoConfig.get("srid");
        return this;
    }


}
