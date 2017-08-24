/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.amodsim.entity.vehicle;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

/**
 *
 * @author fido
 */
public interface OnDemandVehicleFactorySpec {

    public OnDemandVehicle create(String vehicleId, SimulationNode startPosition);
}
