package cz.cvut.fel.aic.agentpolis.demo.config;

import java.lang.Boolean;
import java.lang.Double;
import java.lang.Integer;
import java.lang.String;
import java.util.HashMap;

public class Rebalancing {
  public Integer maxWaitInQueue;

  public Boolean loadShapes;

  public String filePath;

  public Double vehCoef;

  public Boolean useSmoothedDemand;

  public String policyFilePath;

  public String method;

  public Integer timestep;

  public Integer vehicleLimit;

  public String type;

  public Rebalancing(HashMap rebalancing) {
    this.maxWaitInQueue = (Integer) rebalancing.get("max_wait_in_queue");
    this.loadShapes = (Boolean) rebalancing.get("load_shapes");
    this.filePath = (String) rebalancing.get("file_path");
    this.vehCoef = (Double) rebalancing.get("veh_coef");
    this.useSmoothedDemand = (Boolean) rebalancing.get("use_smoothed_demand");
    this.policyFilePath = (String) rebalancing.get("policy_file_path");
    this.method = (String) rebalancing.get("method");
    this.timestep = (Integer) rebalancing.get("timestep");
    this.vehicleLimit = (Integer) rebalancing.get("vehicle_limit");
    this.type = (String) rebalancing.get("type");
  }
}
