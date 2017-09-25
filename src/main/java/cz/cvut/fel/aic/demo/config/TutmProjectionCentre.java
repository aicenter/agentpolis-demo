package cz.cvut.fel.aic.demo.config;

import java.lang.Double;
import java.util.HashMap;

public class TutmProjectionCentre {
  public Double latitude;

  public Double longitude;

  public TutmProjectionCentre(HashMap tutmProjectionCentre) {
    this.latitude = (Double) tutmProjectionCentre.get("latitude");
    this.longitude = (Double) tutmProjectionCentre.get("longitude");
  }
}
