package cz.cvut.fel.aic.agentpolis.demo.config;

import java.lang.String;
import java.util.HashMap;

public class Images {
  public String mainMap;

  public String imagesDir;

  public String tripStartHistogram;

  public String trafficDensityCurrent;

  public String trafficDensityFutureDetail;

  public String trafficDensityFutureDetailStacked;

  public String trafficDensityCurrentDetail;

  public Images(HashMap images) {
    this.mainMap = (String) images.get("main_map");
    this.imagesDir = (String) images.get("images_dir");
    this.tripStartHistogram = (String) images.get("trip_start_histogram");
    this.trafficDensityCurrent = (String) images.get("traffic_density_current");
    this.trafficDensityFutureDetail = (String) images.get("traffic_density_future_detail");
    this.trafficDensityFutureDetailStacked = (String) images.get("traffic_density_future_detail_stacked");
    this.trafficDensityCurrentDetail = (String) images.get("traffic_density_current_detail");
  }
}
