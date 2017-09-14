/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import cz.cvut.fel.aic.agentpolis.demo.config.Config;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.GraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simulator.MapData;
import cz.cvut.fel.aic.agentpolis.simulator.MapDataGenerator;
import cz.cvut.fel.aic.agentpolis.demo.graphbuilder.SimulationNodeFactory;
import cz.cvut.fel.aic.agentpolis.demo.graphbuilder.SimulationEdgeFactory;
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

/**
 * @author david
 */
public class MapInitializer {

    private static final Logger LOGGER = Logger.getLogger(MapInitializer.class);

    private final File mapFile;

    private final String geojsonEdges;

    private final String geojsonNodes;

    private final Transformer projection;

    private final Set<TransportMode> allowedOsmModes;

    @Inject
    public MapInitializer(Transformer projection, @Named("osm File") File mapFile, @Named("geojson Edges") String geojsonEdges,
                          @Named("geojson Nodes") String geojsonNodes, Set<TransportMode> allowedOsmModes) {
        this.mapFile = mapFile;
        this.projection = projection;
        this.allowedOsmModes = allowedOsmModes;
        this.geojsonEdges = geojsonEdges;
        this.geojsonNodes = geojsonNodes;
    }

    /**
     * init map
     *
     * @return map data with simulation graph
     */
    public MapData getMap() {
        Map<GraphType, Graph<SimulationNode, SimulationEdge>> graphs = new HashMap<>();
        removeTemporaryFiles();

        Importer importer = null;
        if (geojsonEdges != null && geojsonNodes != null && new File(geojsonEdges).exists() && new File(geojsonNodes).exists()) {
            importer = new GeoJSONReader(geojsonEdges, geojsonNodes, projection);
        }
        if (importer == null) {
            importer = new OsmImporter(mapFile, allowedOsmModes, projection);
        }

        GraphCreator<SimulationNode, SimulationEdge> graphCreator = new GraphCreator(projection,
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
