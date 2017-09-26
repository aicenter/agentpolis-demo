package cz.cvut.fel.aic.apdemo;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.apdemo.config.Config;
import cz.cvut.fel.aic.apdemo.io.TimeTrip;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.StandardDriveFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.geographtools.Graph;
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

    @Inject
    public EventInitializer(EventProcessor eventProcessor, Config config,
            DemandEventHandler demandEventHandler, TransportNetworks network) {
        this.eventProcessor = eventProcessor;
        this.demandEventHandler = demandEventHandler;
        this.config = config;
        this.graph = network.getGraph(EGraphType.HIGHWAY);
    }

    private static final int NUMBER_OF_TRIPS = 2000;

    public void initialize() {

        Random rand = new Random(RANDOM_SEED);
        List nodes = (List) this.graph.getAllNodes();

        for (int i = 0; i < NUMBER_OF_TRIPS; i++) {
            long startTime = 1 + rand.nextInt(config.agentpolis.simulationDurationInMillis + 1);

            SimulationNode startNode;
            SimulationNode destNode;
            do {
                startNode = (SimulationNode) nodes.get(rand.nextInt(nodes.size()));
                destNode = (SimulationNode) nodes.get(rand.nextInt(nodes.size()));
            } while(startNode.equals(destNode));

            eventProcessor.addEvent(null, demandEventHandler, null, new TimeTrip<SimulationNode>(startNode, destNode, startTime), startTime);
        }
    }

    public static class DemandEventHandler extends EventHandlerAdapter {

        private final StandardDriveFactory congestedDriveFactory;
        private final DriveAgentStorage driveAgentStorage;
        private static int COUNTER_ID = 0;

        @Inject
        public DemandEventHandler(
                 StandardDriveFactory congestedDriveFactory, DriveAgentStorage driveAgentStorage) {
            this.congestedDriveFactory = congestedDriveFactory;
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
