package cz.cvut.fel.aic.amodsim.config;

import java.util.HashMap;

public class Shapefiles {

    public String border;

    public String rivers;

    public String towns;

    public String motorways;

    public String roads;

    public String dir;

    public Shapefiles(HashMap shapefiles) {
        this.border = (String) shapefiles.get("border");
        this.rivers = (String) shapefiles.get("rivers");
        this.towns = (String) shapefiles.get("towns");
        this.motorways = (String) shapefiles.get("motorways");
        this.roads = (String) shapefiles.get("roads");
        this.dir = (String) shapefiles.get("dir");
    }
}
