package cz.cvut.fel.aic.amodsim.entity;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.VehicleTrip;

/**
 *
 * @author F.I.D.O.
 */
public interface PlanningAgent {

    public VehicleTrip getCurrentPlan();
}
