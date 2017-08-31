/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo.entity.vehicle;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.cvut.fel.aic.agentpolis.demo.entity.DemandAgent;

/**
 *
 * @author fido
 */
public class Demand {

    final DemandAgent demandAgent;

    final VehicleTrip<TripItem> demandTrip;

    public DemandAgent getDemandAgent() {
        return demandAgent;
    }

    public VehicleTrip getDemandTrip() {
        return demandTrip;
    }

    public Demand(DemandAgent demandAgent, VehicleTrip demandTrip) {
        this.demandAgent = demandAgent;
        this.demandTrip = demandTrip;
    }

    public int getStartNodeId() {
        return demandTrip.getLocations().getFirst().tripPositionByNodeId;
    }

    public int getTargetNodeId() {
        return demandTrip.getLocations().getLast().tripPositionByNodeId;
    }
}
