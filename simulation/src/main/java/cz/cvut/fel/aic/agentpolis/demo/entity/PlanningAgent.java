package cz.cvut.fel.aic.agentpolis.demo.entity;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.VehicleTrip;

/**
 *
 * @author F.I.D.O.
 */
public interface PlanningAgent {

    public VehicleTrip getCurrentPlan();
}
