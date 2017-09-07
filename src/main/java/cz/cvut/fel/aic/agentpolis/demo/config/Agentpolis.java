package cz.cvut.fel.aic.agentpolis.demo.config;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.util.HashMap;

public class Agentpolis {
  public Boolean useTripCache;

  public String preprocessedTrips;

  public String edgePairsFilePath;

  public String tripCacheFile;

  public String preprocessorPath;

  public Integer startTime;

  public String tripsPath;

  public String edgesFilePath;

  public Integer simulationDurationInMillis;

  public Boolean showVisio;

  public Boolean simplifyGraph;

  public Boolean ridesharing;

  public Statistics statistics;

  public Agentpolis(HashMap agentpolis) {
    this.useTripCache = (Boolean) agentpolis.get("use_trip_cache");
    this.preprocessedTrips = (String) agentpolis.get("preprocessed_trips");
    this.edgePairsFilePath = (String) agentpolis.get("edge_pairs_file_path");
    this.tripCacheFile = (String) agentpolis.get("trip_cache_file");
    this.preprocessorPath = (String) agentpolis.get("preprocessor_path");
    this.startTime = (Integer) agentpolis.get("start_time");
    this.tripsPath = (String) agentpolis.get("trips_path");
    this.edgesFilePath = (String) agentpolis.get("edges_file_path");
    this.simulationDurationInMillis = (Integer) agentpolis.get("simulation_duration_in_millis");
    this.showVisio = (Boolean) agentpolis.get("show_visio");
    this.simplifyGraph = (Boolean) agentpolis.get("simplify_graph");
    this.ridesharing = (Boolean) agentpolis.get("ridesharing");
    this.statistics = new Statistics((HashMap) agentpolis.get("statistics"));
  }
}
