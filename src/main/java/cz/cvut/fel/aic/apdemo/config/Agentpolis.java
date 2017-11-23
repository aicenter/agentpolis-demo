package cz.cvut.fel.aic.apdemo.config;

import java.lang.Integer;
import java.util.Map;

public class Agentpolis {
  public Integer simulationDurationInMillis;

  public Agentpolis(Map agentpolis) {
    this.simulationDurationInMillis = (Integer) agentpolis.get("simulation_duration_in_millis");
  }
}
