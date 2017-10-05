package cz.cvut.fel.aic.apdemo;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.PhysicalVehicleDriveFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.NearestElementUtils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.apdemo.config.Config;
import cz.cvut.fel.aic.apdemo.io.TimeTrip;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.Graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


@Singleton
public class EventInitializer {

    private static final int RANDOM_SEED = 1;
    private final EventProcessor eventProcessor;
    private final DemandEventHandler demandEventHandler;
    private final Config config;
    private final Graph<SimulationNode, SimulationEdge> graph;
    private final NearestElementUtils nearestElementUtils;

    @Inject
    public EventInitializer(EventProcessor eventProcessor, Config config,
                            DemandEventHandler demandEventHandler, TransportNetworks network, NearestElementUtils nearestElementUtils) {
        this.eventProcessor = eventProcessor;
        this.demandEventHandler = demandEventHandler;
        this.config = config;
        this.nearestElementUtils = nearestElementUtils;
        this.graph = network.getGraph(EGraphType.HIGHWAY);
    }

    private static final int NUMBER_OF_TRIPS = 200000;

    public void initialize() {

        Random rand = new Random(RANDOM_SEED);
        List nodes = (List) this.graph.getAllNodes();
        loadTrips("trips.txt");
//        for (int i = 0; i < NUMBER_OF_TRIPS; i++) {
//            long startTime = 1 + rand.nextInt(config.agentpolis.simulationDurationInMillis + 1);
//
//            SimulationNode startNode;
//            SimulationNode destNode;
//            do {
//                startNode = (SimulationNode) nodes.get(rand.nextInt(nodes.size()));
//                destNode = (SimulationNode) nodes.get(rand.nextInt(nodes.size()));
//            } while (startNode.equals(destNode));
//
//            eventProcessor.addEvent(null, demandEventHandler, null, new TimeTrip<SimulationNode>(startNode, destNode, startTime), startTime);
//        }
    }

    private void loadTrips(String inputFile) {

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                GPSLocation startLocation
                        = new GPSLocation(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), 0, 0);
                GPSLocation targetLocation
                        = new GPSLocation(Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), 0, 0);
                SimulationNode startNode = nearestElementUtils.getNearestElement(startLocation, EGraphType.HIGHWAY);
                SimulationNode destNode = nearestElementUtils.getNearestElement(targetLocation, EGraphType.HIGHWAY);
                long startTime = Long.parseLong(parts[0].split("\\.")[0]);
                //TODO remove hack
//                if (allowedCommodity.size() < 500) {
//                }
                if (startNode != destNode)
                    eventProcessor.addEvent(null, demandEventHandler, null, new TimeTrip<SimulationNode>(startNode, destNode, startTime), startTime);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class DemandEventHandler extends EventHandlerAdapter {

        private final PhysicalVehicleDriveFactory congestedDriveFactory;
        private final DriveAgentStorage driveAgentStorage;
        private static int COUNTER_ID = 0;

        @Inject
        public DemandEventHandler(
                PhysicalVehicleDriveFactory driveFactory, DriveAgentStorage driveAgentStorage) {
            this.congestedDriveFactory = driveFactory;
            this.driveAgentStorage = driveAgentStorage;
        }

        @Override
        public void handleEvent(Event event) {
            Trip<SimulationNode> trip = (Trip) event.getContent();
            LinkedList nodes = trip.getLocations();
            SimulationNode startNode = (SimulationNode) nodes.get(0);
            SimulationNode finishNode = (SimulationNode) nodes.get(1);

            PhysicalVehicle vehicle = new PhysicalVehicle("Test vehicle " + COUNTER_ID, DemoType.VEHICLE, 5, 2, EGraphType.HIGHWAY, startNode, 15);
            DriveAgent driveAgent = new DriveAgent("Test driver " + COUNTER_ID, startNode);

            congestedDriveFactory.create(driveAgent, vehicle, finishNode).run();
            driveAgentStorage.addEntity(driveAgent);
            COUNTER_ID++;
        }
    }
}
