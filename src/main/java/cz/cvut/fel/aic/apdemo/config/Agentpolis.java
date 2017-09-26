package cz.cvut.fel.aic.apdemo.config;

import java.lang.Integer;
import java.util.HashMap;

public class Agentpolis {
  public Integer simulationDurationInMillis;

  public Agentpolis(HashMap agentpolis) {
    this.simulationDurationInMillis = (Integer) agentpolis.get("simulation_duration_in_millis");
  }
}
