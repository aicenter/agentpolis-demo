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
import com.google.inject.Singleton;

import cz.cvut.fel.aic.apdemo.io.TimeTrip;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.StandardDriveFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.Graph;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import cz.cvut.fel.aic.geographtools.util.DistanceUtil;
import cz.cvut.fel.aic.geographtools.util.GPSLocationTools;


@Singleton
public class EventInitializer {

    private static final int RANDOM_SEED = 1;
    private final EventProcessor eventProcessor;
    private final DemandEventHandler demandEventHandler;
    private final AgentpolisConfig config;
    private final Graph<SimulationNode, SimulationEdge> graph;

    @Inject
    public EventInitializer(EventProcessor eventProcessor, AgentpolisConfig config,
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

        /*for ( int i = 0; i < nodes.size(); i++ ) {
            for ( int j = i + 1; j < nodes.size(); j++ ) {
                SimulationNode from = (SimulationNode) nodes.get(i);
                SimulationNode to = (SimulationNode) nodes.get(j);
                double distance = DistanceUtil.computeEuclideanDistance(from.getLongitudeProjected(), from.getLatitudeProjected(), to.getLongitudeProjected(), to.getLatitudeProjected());
                System.out.println("Distance from " + i + " to " + j + " is: " + distance);
            }
        }*/


        for (int i = 0; i < NUMBER_OF_TRIPS; i++) {
            long startTime = 1 + rand.nextInt(config.simulationDurationInMillis + 1);

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

            PhysicalVehicle vehicle = new PhysicalVehicle("Test vehicle " + COUNTER_ID, DemoType.VEHICLE, 5, EGraphType.HIGHWAY, startNode, 15);
            DriveAgent driveAgent = new DriveAgent("Test driver " + COUNTER_ID, startNode);

            congestedDriveFactory.create(driveAgent, vehicle, finishNode).run();
            driveAgentStorage.addEntity(driveAgent);
            COUNTER_ID++;
        }
    }
}
