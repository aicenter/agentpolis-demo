/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo.entity.vehicle;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.IdGenerator;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.StandardDriveFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.VehicleStorage;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.agentpolis.demo.OnDemandVehicleStationsCentral;
import cz.cvut.fel.aic.agentpolis.config.Config;
import cz.cvut.fel.aic.agentpolis.demo.entity.DemandAgent;
import cz.cvut.fel.aic.agentpolis.demo.entity.OnDemandVehicleState;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.TripsUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.geographtools.Node;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fido
 */
public class RideSharingOnDemandVehicle extends OnDemandVehicle {

    private final LinkedList<Node> startNodes;

    private final LinkedList<Node> targetNodes;

    private final LinkedList<Demand> demands;

    private final LinkedList<Demand> pickedDemands;

    private final Map<DemandAgent, Demand> demandsData;

    private final PositionUtil positionUtil;

    private Demand currentlyServedDemmand;

    @Inject
    public RideSharingOnDemandVehicle(Map<Long, SimulationNode> nodesMappedByNodeSourceIds, VehicleStorage vehicleStorage,
            TripsUtil tripsUtil, OnDemandVehicleStationsCentral onDemandVehicleStationsCentral,
            StandardDriveFactory driveActivityFactory, PositionUtil positionUtil, EventProcessor eventProcessor,
            StandardTimeProvider timeProvider, @Named("precomputedPaths") boolean precomputedPaths,
            IdGenerator rebalancingIdGenerator, Config config, @Assisted String vehicleId,
            @Assisted SimulationNode startPosition) {
        super(nodesMappedByNodeSourceIds, vehicleStorage, tripsUtil, onDemandVehicleStationsCentral,
                driveActivityFactory, positionUtil, eventProcessor, timeProvider, precomputedPaths,
                rebalancingIdGenerator, config, vehicleId, startPosition);
        this.positionUtil = positionUtil;
        startNodes = new LinkedList<>();
        targetNodes = new LinkedList<>();
        demandsData = new HashMap<>();
        demands = new LinkedList<>();
        pickedDemands = new LinkedList<>();
    }

    public boolean hasFreeCapacity() {
        return targetNodes.size() < vehicle.getCapacity();
    }

    @Override
    public void handleEvent(Event event) {
        cz.cvut.fel.aic.agentpolis.demo.DemandData demandData = (cz.cvut.fel.aic.agentpolis.demo.DemandData) event.getContent();
        List<Long> locations = demandData.locations;

        Node startNode = nodesMappedByNodeSourceIds.get(locations.get(0));
        Node targetNode = nodesMappedByNodeSourceIds.get(locations.get(locations.size() - 1));

        startNodes.add(startNode);
        targetNodes.add(targetNode);

        // for the UseVehicleAsPassangerAction
        VehicleTrip demandTrip = tripsUtil.createTrip(startNode.id, targetNode.id, vehicle);
        demands.add(new Demand(demandData.demandAgent, demandTrip));

        // release from station in case of full car
        if (demands.size() + pickedDemands.size() == vehicle.getCapacity()) {
            departureStation.release(this);
        }

        if (state == OnDemandVehicleState.WAITING) {
            state = OnDemandVehicleState.DRIVING_TO_START_LOCATION;

            chooseTarget();
            driveToDemandStartLocation();
        }
    }

    @Override
    protected void driveToDemandStartLocation() {
        if (getPosition().id == currentlyServedDemmand.getStartNodeId()) {
            finishedDriving();
            return;
        } else {
            currentTrip = tripsUtil.createTrip(getPosition().id,
                    currentlyServedDemmand.getStartNodeId(), vehicle);
            metersToStartLocation += positionUtil.getTripLengthInMeters(currentTrip);
        }

        driveFactory.runActivity(this, vehicle, vehicleTripToTrip(currentTrip));
    }

    @Override
    protected void driveToTargetLocation() {
        if (getPosition().id == currentlyServedDemmand.getTargetNodeId()) {
            finishedDriving();
            return;
        }

        currentTrip = tripsUtil.createTrip(getPosition().id,
                currentlyServedDemmand.getTargetNodeId(), vehicle);

        metersWithPassenger += positionUtil.getTripLengthInMeters(currentTrip);

        driveFactory.runActivity(this, vehicle, vehicleTripToTrip(currentTrip));
    }

    @Override
    protected void driveToNearestStation() {
        targetStation = onDemandVehicleStationsCentral.getNearestStation(getPosition());

        if (getPosition().equals(targetStation.getPosition())) {
            currentTrip = null;
        } else {
            currentTrip = tripsUtil.createTrip(getPosition().id,
                    targetStation.getPosition().getId(), vehicle);
            metersToStation += positionUtil.getTripLengthInMeters(currentTrip);
        }

        if (currentTrip == null) {
            finishedDriving();
            return;
        }

        driveFactory.runActivity(this, vehicle, vehicleTripToTrip(currentTrip));
    }

    // change to find the nearest
    private void chooseTarget() {

        LinkedList<Demand> collection = demands.isEmpty() ? pickedDemands : demands;
        currentlyServedDemmand = collection.poll();
    }

    @Override
    public void finishedDriving() {
        switch (state) {
            case DRIVING_TO_START_LOCATION:
                currentlyServedDemmand.getDemandAgent().tripStarted();
                pickedDemands.add(currentlyServedDemmand);
                boolean departure = demands.isEmpty();
                chooseTarget();
                if (departure) {
                    state = OnDemandVehicleState.DRIVING_TO_TARGET_LOCATION;

                    // release from station if not done yet
                    if (pickedDemands.size() < vehicle.getCapacity()) {
                        departureStation.release(this);
                    }
                    driveToTargetLocation();
                } else {
                    driveToDemandStartLocation();
                }
                break;
            case DRIVING_TO_TARGET_LOCATION:
                currentlyServedDemmand.demandAgent.tripEnded();
                chooseTarget();
                if (currentlyServedDemmand == null) {
                    state = OnDemandVehicleState.DRIVING_TO_STATION;
                    driveToNearestStation();
                } else {
                    driveToTargetLocation();
                }

                break;
            case DRIVING_TO_STATION:
            case REBALANCING:
                waitInStation();
                break;
        }
    }

    @Override
    public VehicleTrip getDemandTrip(DemandAgent agent) {
        return demandsData.get(agent).demandTrip;
    }

}
