package cz.cvut.fel.aic.amodsim.config;


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
