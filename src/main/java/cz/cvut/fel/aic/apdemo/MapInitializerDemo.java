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
package cz.cvut.fel.aic.apdemo;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.AgentDriveModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import cz.cvut.fel.aic.agentpolis.simulator.MapDataGenerator;

import cz.cvut.fel.aic.geographtools.Graph;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import cz.cvut.fel.aic.geographtools.util.Transformer;
import cz.cvut.fel.aic.graphimporter.GraphCreator;
import cz.cvut.fel.aic.geographtools.TransportMode;
import cz.cvut.fel.aic.graphimporter.Importer;
import cz.cvut.fel.aic.graphimporter.geojson.GeoJSONReader;
import cz.cvut.fel.aic.graphimporter.osm.OsmImporter;
import cz.cvut.fel.aic.graphimporter.structurebuilders.EdgeBuilder;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapInitializerDemo {

    private static final Logger LOGGER = Logger.getLogger(MapInitializerDemo.class);
    private final String mapFile;
    private final String geojsonEdges;
    private final String geojsonNodes;
    private final Transformer projection;
    private final Set<TransportMode> allowedOsmModes;

    @Inject
    public MapInitializerDemo(Transformer projection, @Named("osm File") String mapFile, @Named("geojson Edges") String geojsonEdges,
                              @Named("geojson Nodes") String geojsonNodes, Set<TransportMode> allowedOsmModes) {
        this.mapFile = mapFile;
        this.projection = projection;
        this.allowedOsmModes = allowedOsmModes;
        this.geojsonEdges = geojsonEdges;
        this.geojsonNodes = geojsonNodes;
    }

    public MapData getMap() {
        Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphs = new HashMap<>();
        removeTemporaryFiles();

        Importer importer = null;
        if (geojsonEdges != null && geojsonNodes != null && new File(geojsonEdges).exists() && new File(geojsonNodes).exists()) {
            importer = new GeoJSONReader(geojsonEdges, geojsonNodes, projection);
        }
        if (importer == null) {
            importer = new OsmImporter(new File(mapFile), allowedOsmModes, projection);
        }

        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = MapInitializerDemo.getCompleteGraph(10);
        graphs.put(EGraphType.HIGHWAY, graphBuilder.createGraph());

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

    public static GraphBuilder<SimulationNode, SimulationEdge> getCompleteGraph(int nodeCount) {
        GraphBuilder<SimulationNode, SimulationEdge> graphBuilder = new GraphBuilder<>();

        int radius = 1000;

        for (int i = 0; i < nodeCount; i++) {
            double angle = 2 * Math.PI / nodeCount * i;

            int x = (int) Math.round(radius * Math.cos(angle));
            int y = (int) Math.round(radius * Math.sin(angle));


            SimulationNode node = new SimulationNode(i, 0, x, y, x, y, 0);

            graphBuilder.addNode(node);

            for (int j = 0; j < i; j++) {
                SimulationEdge edge1 = new SimulationEdge(graphBuilder.getNode(i), graphBuilder.getNode(j),
                        0, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(graphBuilder.getNode(i), graphBuilder.getNode(j))));
                SimulationEdge edge2 = new SimulationEdge(graphBuilder.getNode(j), graphBuilder.getNode(i),
                        0, 0, 0, 100, 40, 1, new EdgeShape(Arrays.asList(graphBuilder.getNode(j), graphBuilder.getNode(i))));

                graphBuilder.addEdge(edge1);
                graphBuilder.addEdge(edge2);
            }
        }

        return graphBuilder;
    }
}
