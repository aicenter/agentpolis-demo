/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.apdemo.graphbuilder;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.graphimporter.structurebuilders.client.NodeFactory;
import cz.cvut.fel.aic.graphimporter.structurebuilders.internal.InternalNode;

/**
 *
 * @author fido
 */
public class SimulationNodeFactory extends NodeFactory<SimulationNode> {

    @Override
    public SimulationNode createNode(InternalNode internalNode) {

        return new SimulationNode(internalNode.id, internalNode.sourceId, internalNode.latE6, internalNode.lonE6,
                internalNode.getLatitudeProjected1E2(), internalNode.getLongitudeProjected1E2(), internalNode.elevation);
    }

}
