package cz.cvut.fel.aic.agentpolis.demo.config;

import java.lang.Integer;
import java.lang.String;
import java.util.HashMap;

public class Stations {
  public String demandFilePath;

  public String distanceMatrixPath;

  public Integer regions;

  public Integer timestamps;

  public String distanceCalculatorPath;

  public String dir;

  public String centroidsFilePath;

  public String stationsFilePath;

  public String distanceMatrixOutputPath;

  public String smoothedDemandFilePath;

  public Stations(HashMap stations) {
    this.demandFilePath = (String) stations.get("demand_file_path");
    this.distanceMatrixPath = (String) stations.get("distance_matrix_path");
    this.regions = (Integer) stations.get("regions");
    this.timestamps = (Integer) stations.get("timestamps");
    this.distanceCalculatorPath = (String) stations.get("distance_calculator_path");
    this.dir = (String) stations.get("dir");
    this.centroidsFilePath = (String) stations.get("centroids_file_path");
    this.stationsFilePath = (String) stations.get("stations_file_path");
    this.distanceMatrixOutputPath = (String) stations.get("distance_matrix_output_path");
    this.smoothedDemandFilePath = (String) stations.get("smoothed_demand_file_path");
  }
}
