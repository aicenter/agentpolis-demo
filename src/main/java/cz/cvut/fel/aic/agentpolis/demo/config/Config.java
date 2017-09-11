package cz.cvut.fel.aic.agentpolis.demo.config;

import ninja.fido.config.GeneratedConfig;

import java.lang.Integer;
import java.lang.String;
import java.util.HashMap;

public class Config implements GeneratedConfig {
    public Double vehicleSpeedInMeters;

    public String experimentName;

    public String tripsFilename;

    public Images images;

    public Agentpolis agentpolis;

    public Shapefiles shapefiles;

    public Stations stations;

    public Analysis analysis;

    public Integer srid;

    public String mapFilePath;

    public String geojsonEdges;

    public String geojsonNodes;

    public String amodsimExperimentDir;

    public String amodsimDataDir;

    public Double tripsMultiplier;

    public String tripsFilePath;

    public Integer tripsLimit;

    public Config() {
    }

    public Config fill(HashMap config) {
        this.vehicleSpeedInMeters = (Double) config.get("vehicle_speed_in_meters");
        this.experimentName = (String) config.get("experiment_name");
        this.tripsFilename = (String) config.get("trips_filename");
        this.images = new Images((HashMap) config.get("images"));
        this.agentpolis = new Agentpolis((HashMap) config.get("agentpolis"));
        this.shapefiles = new Shapefiles((HashMap) config.get("shapefiles"));
        this.stations = new Stations((HashMap) config.get("stations"));
        this.analysis = new Analysis((HashMap) config.get("analysis"));
        this.srid = (Integer) config.get("srid");

        this.mapFilePath = (String) config.get("map_file_path");
        this.geojsonEdges = (String) config.get("map_geojson_edges");
        this.geojsonNodes = (String) config.get("map_geojson_nodes");

        this.amodsimExperimentDir = (String) config.get("amodsim_experiment_dir");
        this.amodsimDataDir = (String) config.get("amodsim_data_dir");
        this.tripsMultiplier = (Double) config.get("trips_multiplier");
        this.tripsFilePath = (String) config.get("trips_file_path");
        this.tripsLimit = (Integer) config.get("trips_limit");
        return this;
    }
}

