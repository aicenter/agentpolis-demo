/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.apdemo.graphbuilder;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.Lane;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.LaneBuilder;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.graphimporter.structurebuilders.client.EdgeFactory;
import cz.cvut.fel.aic.graphimporter.structurebuilders.internal.InternalEdge;

import java.util.List;

/**
 * @author fido
 */
public class SimulationEdgeFactory extends EdgeFactory<SimulationEdge> {
    private LaneBuilder laneBuilder = new LaneBuilder();

    @Override
    public SimulationEdge createEdge(InternalEdge internalEdge) {
        int uniqueID = internalEdge.get("uniqueWayID");
        int lanesCount = internalEdge.get("lanesCount");
        EdgeShape shape = new EdgeShape(internalEdge.get("coordinateList"));
        List<Lane> lanes = laneBuilder.createLanes(uniqueID, internalEdge.get("lanesTurn"), lanesCount);

        return new SimulationEdge(internalEdge.fromId, internalEdge.toId,
                internalEdge.get("uniqueWayID"),
                internalEdge.get("oppositeWayUniqueId"),
                internalEdge.getLength(),
                internalEdge.get("allowedMaxSpeedInMpS"),
                lanesCount,
                shape,
                lanes);
    }
}
