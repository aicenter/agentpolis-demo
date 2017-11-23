/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.apdemo.config;

import java.lang.Integer;
import java.lang.String;
import java.util.Map;
import ninja.fido.config.GeneratedConfig;

public class ApdemoConfig implements GeneratedConfig {
  public String mapGeojsonEdges;

  public Agentpolis agentpolis;

  public String mapFilePath;

  public String mapGeojsonNodes;

  public Integer srid;

  public ApdemoConfig() {
  }

  public ApdemoConfig fill(Map apdemoConfig) {
    this.mapGeojsonEdges = (String) apdemoConfig.get("map_geojson_edges");
    this.agentpolis = new Agentpolis((Map) apdemoConfig.get("agentpolis"));
    this.mapFilePath = (String) apdemoConfig.get("map_file_path");
    this.mapGeojsonNodes = (String) apdemoConfig.get("map_geojson_nodes");
    this.srid = (Integer) apdemoConfig.get("srid");
    return this;
  }
}
