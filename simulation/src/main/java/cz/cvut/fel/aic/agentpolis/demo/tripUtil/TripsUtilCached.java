/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo.tripUtil;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.TripsUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.Log;
import cz.cvut.fel.aic.agentpolis.demo.entity.DemandAgent;
import cz.cvut.fel.aic.agentpolis.demo.jackson.MyModule;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.TripPlannerException;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.ShortestPathPlanner;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.path.ShortestPathPlanners;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.NearestElementUtils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationFinishedListener;
import cz.cvut.fel.aic.agentpolis.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author fido
 */
@Singleton
public class TripsUtilCached extends TripsUtil implements SimulationFinishedListener {

    private static final int OUTPUT_BATCH_SIZE = 10000;

    private final HashMap<StartTargetNodePair, SimpleJsonTrip> tripCache;

    private final File tripCacheFolder;

    private final ObjectMapper mapper;

    private HashMap<StartTargetNodePair, SimpleJsonTrip> newTrips;

    private int cacheFileCounter;

    private ShortestPathPlanner pathPlanner;

    @Inject
    public TripsUtilCached(ShortestPathPlanners pathPlanners, NearestElementUtils nearestElementUtils,
            HighwayNetwork network, SimulationCreator simulationCreator, Config configuration) throws IOException {
        super(pathPlanners, nearestElementUtils, network);

        mapper = new ObjectMapper();
        mapper.registerModule(new MyModule());

        tripCacheFolder = getCacheFolder(configuration);

        tripCache = new HashMap<>();
        if (tripCacheFolder.exists()) {
            loadTripCache();
        } else {
            tripCacheFolder.mkdir();
            cacheFileCounter = 0;
        }

        newTrips = new HashMap<>();

        simulationCreator.addSimulationFinishedListener(this);
    }

    @Override
    public VehicleTrip createTrip(int startNodeId, int targetNodeId, PhysicalVehicle vehicle) {
        if (startNodeId == targetNodeId) {
            try {
                throw new Exception("Start node cannot be the same as end node");
            } catch (Exception ex) {
                Logger.getLogger(TripsUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        ShortestPathPlanner pathPlanner = pathPlanners.getPathPlanner(GRAPH_TYPES);

        StartTargetNodePair tripStartTargetPair = new StartTargetNodePair(startNodeId, targetNodeId);

        VehicleTrip finalTrip = null;

        if (tripCache.containsKey(tripStartTargetPair)) {
            finalTrip
                    = new VehicleTrip(tripCache.get(tripStartTargetPair).getLocations(), EGraphType.HIGHWAY, vehicle.getId());
        } else {
            try {
                finalTrip = pathPlanner.findTrip(vehicle.getId(), startNodeId, targetNodeId);
                tripCache.put(tripStartTargetPair, new SimpleJsonTrip(finalTrip.getLocations()));
                newTrips.put(tripStartTargetPair, new SimpleJsonTrip(finalTrip.getLocations()));
                if (newTrips.size() > OUTPUT_BATCH_SIZE) {
                    saveNewTrips();
                }
            } catch (TripPlannerException ex) {
                Logger.getLogger(DemandAgent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(TripsUtilCached.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return finalTrip;
    }

    @Override
    public void simulationFinished() {
        try {
            saveNewTrips();
        } catch (IOException ex) {
            Logger.getLogger(TripsUtilCached.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTripCache() throws IOException {
        TypeReference<HashMap<StartTargetNodePair, SimpleJsonTrip>> typeRef
                = new TypeReference<HashMap<StartTargetNodePair, SimpleJsonTrip>>() {
        };

        Log.log(this, Level.INFO, "Loading cache start");
        for (final File file : tripCacheFolder.listFiles()) {
            HashMap<StartTargetNodePair, SimpleJsonTrip> tripCachePart = mapper.readValue(file, typeRef);
            tripCache.putAll(tripCachePart);
            cacheFileCounter++;
        }
        Log.log(this, Level.INFO, "Loading cache finished - {0} trips loaded", tripCache.size());

    }

    private File getCacheFolder(Config config) {
        String filename = config.agentpolis.tripCacheFile;
        if (config.agentpolis.simplifyGraph) {
            filename += "-simplified";
        }
        return new File(filename);
    }

    private void saveNewTrips() throws IOException {
        File outputFile = new File(tripCacheFolder + File.separator + cacheFileCounter + ".json");
        cacheFileCounter++;
        mapper.writeValue(outputFile, newTrips);
        newTrips = new HashMap<>();
    }

}
