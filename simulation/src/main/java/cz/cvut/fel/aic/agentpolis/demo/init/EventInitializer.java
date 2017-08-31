/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo.init;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.demo.io.TimeTrip;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandlerAdapter;
import cz.agents.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.StandardDriveFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.agentpolis.config.Config;
import cz.cvut.fel.aic.agentpolis.demo.init.drive.DriveAgent;
import cz.cvut.fel.aic.agentpolis.demo.init.drive.DriveAgentStorage;
import cz.cvut.fel.aic.agentpolis.demo.init.support.mock.CongestionTestType;
import cz.cvut.fel.aic.geographtools.Graph;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 *
 * @author fido
 */
@Singleton
public class EventInitializer {


    private static final long MAX_EVENTS = 0;

    private static final int RANDOM_SEED = 1;

    private final EventProcessor eventProcessor;

    private final DemandEventHandler demandEventHandler;

    private final Config config;

    private long eventCount;

    private final Graph<SimulationNode, SimulationEdge> graph;

    @Inject
    public EventInitializer(EventProcessor eventProcessor, Config config,
            DemandEventHandler demandEventHandler, TransportNetworks network) {
        this.eventProcessor = eventProcessor;
        this.demandEventHandler = demandEventHandler;
        this.config = config;
        eventCount = 0;
        this.graph = network.getGraph(EGraphType.HIGHWAY);
    }

    private static final int NUMBER_OF_TRIPS = 200;

    public void initialize() {
        //get Graph
        //for-lop
        //generate latlon_id from-to + check distance
        //generate  start_time

        Random rand = new Random(RANDOM_SEED);

        List nodes = (List) this.graph.getAllNodes();
        SimulationNode node1, node2;
        long nodeID1, nodeID2;

        for (int i = 0; i < NUMBER_OF_TRIPS; i++) {
            long startTime = rand.nextInt(10000);// + 2000; //in 2 secs + offset 10 secs

            int randNode = rand.nextInt(nodes.size());
            node1 = (SimulationNode) nodes.get(randNode);
            nodeID1 = node1.id;
            randNode = rand.nextInt(nodes.size());
            node2 = (SimulationNode) nodes.get(randNode);
            nodeID2 = node2.id;
            if (startTime < 1 || startTime > config.agentpolis.simulationDurationInMillis || nodeID1 == nodeID2) {
                i--; //everytime same number of trips must be generated
                continue;
            }

            eventProcessor.addEvent(null, demandEventHandler, null, new TimeTrip<SimulationNode>(node1, node2, startTime), startTime);
            eventCount++;
            if (MAX_EVENTS != 0 && eventCount >= MAX_EVENTS) {
                return;
            }
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
            Trip<SimulationNode> osmNode = (Trip) event.getContent();
            LinkedList nodes = osmNode.getLocations();
            SimulationNode startNode = (SimulationNode) nodes.get(0);
            SimulationNode finishNode = (SimulationNode) nodes.get(1);

            //set id in name
            PhysicalVehicle vehicle = new PhysicalVehicle("Test vehicle " + COUNTER_ID, CongestionTestType.TEST_VEHICLE, 5, 2, EGraphType.HIGHWAY, startNode, 15); //my starting point
            //set id in name
            DriveAgent driveAgent = new DriveAgent("Test driver " + COUNTER_ID, startNode); //my starting point


            congestedDriveFactory.create(driveAgent, vehicle, finishNode).run();

            COUNTER_ID++;

            driveAgentStorage.addEntity(driveAgent);
        }
    }
}
