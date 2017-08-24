package cz.cvut.fel.aic.amodsim.config;

import java.util.HashMap;

public class TutmProjectionCentre {

    public Double latitude;

    public Double longitude;

    public TutmProjectionCentre(HashMap tutmProjectionCentre) {
        this.latitude = (Double) tutmProjectionCentre.get("latitude");
        this.longitude = (Double) tutmProjectionCentre.get("longitude");
    }
}
