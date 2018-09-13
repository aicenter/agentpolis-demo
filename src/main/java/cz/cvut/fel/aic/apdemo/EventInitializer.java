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
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.AgentdriveActivityFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.ADPhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.util.Pair;
import cz.cvut.fel.aic.apdemo.config.ApdemoConfig;
import cz.cvut.fel.aic.apdemo.io.TimeTrip;
import cz.cvut.fel.aic.alite.common.event.Event;
import cz.cvut.fel.aic.alite.common.event.EventHandlerAdapter;
import cz.cvut.fel.aic.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TransportNetworks;
import cz.cvut.fel.aic.geographtools.Graph;

import java.util.*;


@Singleton
public class EventInitializer {

    private static final int RANDOM_SEED = 1;
    private final EventProcessor eventProcessor;
    private final DemandEventHandler demandEventHandler;
    private final ApdemoConfig config;
    private final Graph<SimulationNode, SimulationEdge> graph;

    public Collection<Integer> agentIds;
    public Map<Integer, Float> departures;


    @Inject
    public EventInitializer(EventProcessor eventProcessor, ApdemoConfig config,
                            DemandEventHandler demandEventHandler, TransportNetworks network) {
        this.eventProcessor = eventProcessor;
        this.demandEventHandler = demandEventHandler;
        this.config = config;
        this.graph = network.getGraph(EGraphType.HIGHWAY);
    }

    private static final int NUMBER_OF_TRIPS = 30;

    public void initialize() {
        departures = new HashMap<>();
        Random rand = new Random(RANDOM_SEED);
        List nodes = (List) this.graph.getAllNodes();

        for (int i = 0; i < NUMBER_OF_TRIPS; i++) {
            long startTime = (i + 1) * 1000;//rand.nextInt(config.agentpolis.simulationDurationInMillis + 1000);

            SimulationNode startNode;
            SimulationNode destNode;
            do {
                startNode = (SimulationNode) nodes.get(rand.nextInt(nodes.size()));
                destNode = (SimulationNode) nodes.get(rand.nextInt(nodes.size()));
            } while (startNode.equals(destNode) || startNode.getId() == 1 || destNode.getId() == 1);
            Pair<Integer, TimeTrip<SimulationNode>> tripForAgent = new Pair<>(i, new TimeTrip<>(startNode, destNode, startTime));
            departures.put(i, ((Long) startTime).floatValue() / 1000);
            eventProcessor.addEvent(null, demandEventHandler, null, tripForAgent, startTime);
        }
    }

    public static class DemandEventHandler extends EventHandlerAdapter {

        private final AgentdriveActivityFactory agentdriveActivityFactory;
        private final AgentdriveAgentStorage driveAgentStorage;

        @Inject
        public DemandEventHandler(
                AgentdriveActivityFactory agentdriveActivityFactory, AgentdriveAgentStorage driveAgentStorage) {
            this.agentdriveActivityFactory = agentdriveActivityFactory;
            this.driveAgentStorage = driveAgentStorage;
        }

        @Override
        public void handleEvent(Event event) {
            Pair<Integer, TimeTrip<SimulationNode>> pair = (Pair) event.getContent();
            Trip<SimulationNode> trip = (Trip) pair.getValue();
            LinkedList nodes = trip.getLocations();
            SimulationNode startNode = (SimulationNode) nodes.get(0);
            SimulationNode finishNode = (SimulationNode) nodes.get(1);

            ADPhysicalVehicle vehicle = new ADPhysicalVehicle(pair.getKey().toString(), DemoType.VEHICLE, 3, EGraphType.HIGHWAY, startNode, 15);
            ADDriveAgent driveAgent = new ADDriveAgent(pair.getKey().toString(), startNode);

            agentdriveActivityFactory.create(driveAgent, vehicle, finishNode).run();
            driveAgentStorage.addEntity(driveAgent);
        }
    }

    public Collection<Integer> getAgentIds() {
        return agentIds;
    }

    public Map<Integer, Float> getDepartures() {
        return departures;
    }
}
