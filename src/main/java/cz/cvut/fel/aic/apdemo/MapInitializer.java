/* 
 * Copyright (C) 2019 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.apdemo;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import cz.cvut.fel.aic.agentpolis.simulator.MapDataGenerator;
import cz.cvut.fel.aic.apdemo.graphbuilder.SimulationNodeFactory;
import cz.cvut.fel.aic.apdemo.graphbuilder.SimulationEdgeFactory;
import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.util.Transformer;
import cz.cvut.fel.aic.graphimporter.GraphCreator;
import cz.cvut.fel.aic.geographtools.TransportMode;
import cz.cvut.fel.aic.graphimporter.Importer;
import cz.cvut.fel.aic.graphimporter.geojson.GeoJSONReader;
import cz.cvut.fel.aic.graphimporter.osm.OsmImporter;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapInitializer {

	private static final Logger LOGGER = Logger.getLogger(MapInitializer.class);
	private final String geojsonEdges;
	private final String geojsonNodes;
	private final Transformer projection;
	private final Set<TransportMode> allowedOsmModes;

	@Inject
	public MapInitializer(Transformer projection, @Named("geojson Edges") String geojsonEdges,
			@Named("geojson Nodes") String geojsonNodes, Set<TransportMode> allowedOsmModes) {
		this.projection = projection;
		this.allowedOsmModes = allowedOsmModes;
		this.geojsonEdges = geojsonEdges;
		this.geojsonNodes = geojsonNodes;
	}

	public MapData getMap() {
		Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphs = new HashMap<>();
		removeTemporaryFiles();

		Importer importer = null;
		if (geojsonEdges != null && geojsonNodes != null) {
			File geojsonEdgesFile = new File(geojsonEdges);
			File geojsonNodesFile = new File(geojsonNodes);
			importer = new GeoJSONReader(geojsonEdges, geojsonNodes, projection);
		}

		GraphCreator<SimulationNode, SimulationEdge> graphCreator = new GraphCreator(
				true, true, importer, new SimulationNodeFactory(), new SimulationEdgeFactory());

		graphs.put(EGraphType.HIGHWAY, graphCreator.getMap());

		LOGGER.info("Graphs imported, highway graph details: " + graphs.get(EGraphType.HIGHWAY));
		return MapDataGenerator.getMap(graphs);
	}

	private String getExtension(String filename) {
		return filename.substring(filename.length() - 3, filename.length());
	}

	private void deleteTempFile(String filename) {
		try {
			File file = new File(filename);

			if (file.delete()) {
				LOGGER.info(file.getName() + " is deleted!");
			} else {
				LOGGER.info("Delete operation is failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removeTemporaryFiles() {
		//System.out.println("Working Directory = " + System.getProperty("user.dir"));
		File folder = new File(System.getProperty("user.dir"));
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			//  System.out.println(listOfFiles[i].getName());
			String extension = getExtension(listOfFiles[i].getName());
			// System.out.println(extension);
			if ("ser".equals(extension)) {
				String absolutePath = folder + "/" + listOfFiles[i].getName();
				//System.out.println(absolutePath);
				deleteTempFile(absolutePath);
			}
		}
	}

}
