/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.apdemo.graphbuilder;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.EdgeShape;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.GraphBuilder;
import cz.cvut.fel.aic.graphimporter.structurebuilders.client.EdgeFactory;
import cz.cvut.fel.aic.graphimporter.structurebuilders.internal.InternalEdge;

/**
 * @author fido
 */
public class SimulationEdgeFactory implements EdgeFactory<SimulationNode, SimulationEdge> {

	@Override
	public SimulationEdge createEdge(InternalEdge internalEdge, GraphBuilder<SimulationNode,SimulationEdge> graphBuilder) {
		EdgeShape shape = new EdgeShape(internalEdge.get("coordinateList"));
		SimulationNode fromNode = graphBuilder.getNode(internalEdge.fromNode.getId());
		SimulationNode toNode = graphBuilder.getNode(internalEdge.toNode.getId());
		return new SimulationEdge(fromNode, toNode, internalEdge.get("wayID"),
				internalEdge.get("uniqueWayID"), internalEdge.get("oppositeWayUniqueId"), internalEdge.getLength(),
				internalEdge.get("allowedMaxSpeedInMpS"),
				internalEdge.get("lanesCount"), shape);
	}

}
