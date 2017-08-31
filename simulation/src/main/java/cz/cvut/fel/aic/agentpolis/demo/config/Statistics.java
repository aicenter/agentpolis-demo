package cz.cvut.fel.aic.agentpolis.demo.config;

import java.lang.Integer;
import java.lang.String;
import java.util.HashMap;

public class Statistics {
  public String resultFilePath;

  public Integer statisticIntervalMilis;

  public String transitStatisticFilePath;

  public String allEdgesLoadHistoryFilePath;

  public OnDemandVehicleStatistic onDemandVehicleStatistic;

  public Integer allEdgesLoadIntervalMilis;

  public String tripDistancesFilePath;

  public Statistics(HashMap statistics) {
    this.resultFilePath = (String) statistics.get("result_file_path");
    this.statisticIntervalMilis = (Integer) statistics.get("statistic_interval_milis");
    this.transitStatisticFilePath = (String) statistics.get("transit_statistic_file_path");
    this.allEdgesLoadHistoryFilePath = (String) statistics.get("all_edges_load_history_file_path");
    this.onDemandVehicleStatistic = new OnDemandVehicleStatistic((HashMap) statistics.get("on_demand_vehicle_statistic"));
    this.allEdgesLoadIntervalMilis = (Integer) statistics.get("all_edges_load_interval_milis");
    this.tripDistancesFilePath = (String) statistics.get("trip_distances_file_path");
  }
}
