package cz.cvut.fel.aic.demo.config;

import java.lang.String;
import java.util.HashMap;

public class OnDemandVehicleStatistic {
  public String leaveStationFilePath;

  public String reachNearestStationFilePath;

  public String pickupFilePath;

  public String dropOffFilePath;

  public String finishRebalancingFilePath;

  public String dirPath;

  public String startRebalancingFilePath;

  public OnDemandVehicleStatistic(HashMap onDemandVehicleStatistic) {
    this.leaveStationFilePath = (String) onDemandVehicleStatistic.get("leave_station_file_path");
    this.reachNearestStationFilePath = (String) onDemandVehicleStatistic.get("reach_nearest_station_file_path");
    this.pickupFilePath = (String) onDemandVehicleStatistic.get("pickup_file_path");
    this.dropOffFilePath = (String) onDemandVehicleStatistic.get("drop_off_file_path");
    this.finishRebalancingFilePath = (String) onDemandVehicleStatistic.get("finish_rebalancing_file_path");
    this.dirPath = (String) onDemandVehicleStatistic.get("dir_path");
    this.startRebalancingFilePath = (String) onDemandVehicleStatistic.get("start_rebalancing_file_path");
  }
}
