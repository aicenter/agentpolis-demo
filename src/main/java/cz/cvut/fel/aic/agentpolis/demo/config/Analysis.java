package cz.cvut.fel.aic.agentpolis.demo.config;

import java.lang.Double;
import java.lang.Integer;
import java.util.HashMap;

public class Analysis {
  public Double tripsMultiplier;

  public Integer chosenWindowStart;

  public Integer chosenWindowEnd;

  public Analysis(HashMap analysis) {
    this.tripsMultiplier = (Double) analysis.get("trips_multiplier");
    this.chosenWindowStart = (Integer) analysis.get("chosen_window_start");
    this.chosenWindowEnd = (Integer) analysis.get("chosen_window_end");
  }
}
