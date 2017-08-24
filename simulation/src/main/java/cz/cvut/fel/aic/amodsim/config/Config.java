package cz.cvut.fel.aic.amodsim.config;

import java.util.HashMap;
import ninja.fido.config.GeneratedConfig;

public class Config implements GeneratedConfig {

    public Double vehicleSpeedInMeters;

    public String experimentName;

    public String tripsFilename;

    public Images images;

    public Agentpolis agentpolis;

    public Shapefiles shapefiles;

    public Stations stations;

    public Analysis analysis;

    public String pythonExperimentDir;

    public Integer srid;

    public String mapFilePath;

    public String amodsimExperimentDir;

    public String amodsimDataDir;

    public Double tripsMultiplier;

    public Double criticalDensity;

    public String pythonDataDir;

    public String tripsFilePath;

    public Integer tripsLimit;

    public Db db;

    public TutmProjectionCentre tutmProjectionCentre;

    public Rebalancing rebalancing;

    public Config() {
    }

    @Override
    public Config fill(HashMap config) {
        this.vehicleSpeedInMeters = (Double) config.get("vehicle_speed_in_meters");
      //  this.experimentName = (String) config.get("experiment_name");
     //   this.tripsFilename = (String) config.get("trips_filename");
        this.images = new Images((HashMap) config.get("images"));
        this.agentpolis = new Agentpolis((HashMap) config.get("agentpolis"));
        this.shapefiles = new Shapefiles((HashMap) config.get("shapefiles"));
     //   this.stations = new Stations((HashMap) config.get("stations"));
      //  this.analysis = new Analysis((HashMap) config.get("analysis"));
     //   this.pythonExperimentDir = (String) config.get("python_experiment_dir");
        this.srid = (Integer) config.get("srid");
        this.mapFilePath = (String) config.get("map_file_path");
    //    this.amodsimExperimentDir = (String) config.get("amodsim_experiment_dir");
        this.amodsimDataDir = (String) config.get("amodsim_data_dir");
        this.tripsMultiplier = (Double) config.get("trips_multiplier");
     //   this.criticalDensity = (Double) config.get("critical_density");
        this.pythonDataDir = (String) config.get("python_data_dir");
    //    this.tripsFilePath = (String) config.get("trips_file_path");
        this.tripsLimit = (Integer) config.get("trips_limit");
        this.db = new Db((HashMap) config.get("db"));
        this.tutmProjectionCentre = new TutmProjectionCentre((HashMap) config.get("tutm_projection_centre"));
    //    this.rebalancing = new Rebalancing((HashMap) config.get("rebalancing"));
        return this;
    }
}
