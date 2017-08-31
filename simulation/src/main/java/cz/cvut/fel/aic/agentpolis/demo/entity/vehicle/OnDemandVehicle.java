/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo.entity.vehicle;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import cz.cvut.fel.aic.agentpolis.demo.DemandSimulationEntityType;
import cz.cvut.fel.aic.agentpolis.demo.OnDemandVehicleStationsCentral;
import cz.cvut.fel.aic.agentpolis.demo.entity.PlanningAgent;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.TripsUtil;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.TripItem;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.cvut.fel.aic.agentpolis.simmodel.Activity;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.IdGenerator;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.PhysicalVehicleDriveFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.Driver;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.VehicleStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.EGraphType;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.agentpolis.demo.DemandData;
import cz.cvut.fel.aic.agentpolis.config.Config;
import cz.cvut.fel.aic.agentpolis.demo.entity.DemandAgent;
import cz.cvut.fel.aic.agentpolis.demo.entity.OnDemandVehicleState;
import cz.cvut.fel.aic.agentpolis.demo.entity.OnDemandVehicleStation;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.DelayData;
import cz.cvut.fel.aic.geographtools.Node;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalTransportVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;

/**
 *
 * @author fido
 */
public class OnDemandVehicle extends Agent implements EventHandler, PlanningAgent,
        Driver<PhysicalTransportVehicle> {

    private static final double LENGTH = 4;

    private static final int CAPACITY = 5;

    protected PhysicalTransportVehicle vehicle;

    protected final Map<Long, SimulationNode> nodesMappedByNodeSourceIds;

    protected final TripsUtil tripsUtil;

    private final boolean precomputedPaths;

    protected final OnDemandVehicleStationsCentral onDemandVehicleStationsCentral;

    protected final PhysicalVehicleDriveFactory driveFactory;

    private final PositionUtil positionUtil;

    private final EventProcessor eventProcessor;

    private final StandardTimeProvider timeProvider;

    private final IdGenerator rebalancingIdGenerator;

    private final Config config;

    private List<Node> demandNodes;

    protected OnDemandVehicleState state;

    protected OnDemandVehicleStation departureStation;

    protected OnDemandVehicleStation targetStation;

    protected VehicleTrip currentTrip;

    private VehicleTrip demandTrip;

    protected VehicleTrip tripToStation;

    private VehicleTrip completeTrip;

    protected int metersWithPassenger;

    protected int metersToStartLocation;

    protected int metersToStation;

    private int metersRebalancing;

    private SimulationNode targetNode;

    private DelayData delayData;

    protected DemandData currentlyServedDemmand;

    private int currentRebalancingId;

    public VehicleTrip getCurrentTrips() {
        return currentTrip;
    }

    public VehicleTrip getDemandTrip(DemandAgent agent) {
        return demandTrip.clone();
    }

    public OnDemandVehicleState getState() {
        return state;
    }

    public int getMetersWithPassenger() {
        return metersWithPassenger;
    }

    public int getMetersToStartLocation() {
        return metersToStartLocation;
    }

    public int getMetersToStation() {
        return metersToStation;
    }

    public int getMetersRebalancing() {
        return metersRebalancing;
    }

    // remove in future to be more agent-like
    public void setDepartureStation(OnDemandVehicleStation departureStation) {
        this.departureStation = departureStation;
    }

    @Inject
    public OnDemandVehicle(Map<Long, SimulationNode> nodesMappedByNodeSourceIds, VehicleStorage vehicleStorage,
            TripsUtil tripsUtil, OnDemandVehicleStationsCentral onDemandVehicleStationsCentral,
            PhysicalVehicleDriveFactory driveFactory, PositionUtil positionUtil, EventProcessor eventProcessor,
            StandardTimeProvider timeProvider, @Named("precomputedPaths") boolean precomputedPaths,
            IdGenerator rebalancingIdGenerator, Config config, @Assisted String vehicleId,
            @Assisted SimulationNode startPosition) {
        super(vehicleId + " - autonomus agent", startPosition);
        this.nodesMappedByNodeSourceIds = nodesMappedByNodeSourceIds;
        this.tripsUtil = tripsUtil;
        this.precomputedPaths = precomputedPaths;
        this.onDemandVehicleStationsCentral = onDemandVehicleStationsCentral;
        this.driveFactory = driveFactory;
        this.positionUtil = positionUtil;
        this.eventProcessor = eventProcessor;
        this.timeProvider = timeProvider;
        this.rebalancingIdGenerator = rebalancingIdGenerator;
        this.config = config;

        vehicle = new PhysicalTransportVehicle(vehicleId + " - vehicle",
                DemandSimulationEntityType.VEHICLE, LENGTH, CAPACITY, EGraphType.HIGHWAY, startPosition,
                config.vehicleSpeedInMeters);

        vehicleStorage.addEntity(vehicle);
        state = OnDemandVehicleState.WAITING;

        metersWithPassenger = 0;
        metersToStartLocation = 0;
        metersToStation = 0;
        metersRebalancing = 0;
    }

    @Override
    public EventProcessor getEventProcessor() {
        return null;
    }

    public String getVehicleId() {
        return vehicle.getId();
    }

    @Override
    public void handleEvent(Event event) {
        currentlyServedDemmand = (DemandData) event.getContent();
        List<Long> locations = currentlyServedDemmand.locations;

        demandNodes = new ArrayList<>();
        if (precomputedPaths) {
            for (Long location : locations) {
                demandNodes.add(nodesMappedByNodeSourceIds.get(location));
            }
        } else {
            demandNodes.add(nodesMappedByNodeSourceIds.get(locations.get(0)));
            demandNodes.add(nodesMappedByNodeSourceIds.get(locations.get(locations.size() - 1)));
        }

        driveToDemandStartLocation();
    }

    public void finishedDriving() {
        switch (state) {
            case DRIVING_TO_START_LOCATION:
                driveToTargetLocation();
                break;
            case DRIVING_TO_TARGET_LOCATION:
                dropOffDemand();
                driveToNearestStation();
                break;
            case DRIVING_TO_STATION:

                waitInStation();
                break;
            case REBALANCING:

                waitInStation();
                break;
        }
    }

    protected void driveToDemandStartLocation() {

        if (getPosition() == demandNodes.get(0)) {
            currentTrip = null;
        } else {
            currentTrip = tripsUtil.createTrip(getPosition().id,
                    demandNodes.get(0).getId(), vehicle);
            metersToStartLocation += positionUtil.getTripLengthInMeters(currentTrip);
        }
        if (precomputedPaths) {
            demandTrip = tripsUtil.locationsToVehicleTrip(demandNodes, precomputedPaths, vehicle);
        } else {
            demandTrip = tripsUtil.createTrip(demandNodes.get(0).getId(), demandNodes.get(1).getId(), vehicle);
        }
        metersWithPassenger += positionUtil.getTripLengthInMeters(demandTrip);

        Node demandEndNode = demandNodes.get(demandNodes.size() - 1);

        targetStation = onDemandVehicleStationsCentral.getNearestStation(demandEndNode);

        if (demandEndNode.getId() == targetStation.getPosition().getId()) {
            tripToStation = null;
        } else {
            tripToStation = tripsUtil.createTrip(demandEndNode.getId(),
                    targetStation.getPosition().getId(), vehicle);
            metersToStation += positionUtil.getTripLengthInMeters(tripToStation);
        }

        completeTrip = TripsUtil.mergeTripsOld(currentTrip, demandTrip, tripToStation);

        if (currentTrip == null) {
            driveToTargetLocation();
            return;
        }

        state = OnDemandVehicleState.DRIVING_TO_START_LOCATION;

        driveFactory.runActivity(this, vehicle, vehicleTripToTrip(currentTrip));
    }

    protected void driveToTargetLocation() {
        state = OnDemandVehicleState.DRIVING_TO_TARGET_LOCATION;
        pickupDemand();

        departureStation.release(this);

        currentTrip = demandTrip;

        driveFactory.runActivity(this, vehicle, vehicleTripToTrip(currentTrip));

    }

    protected void driveToNearestStation() {
        if (tripToStation == null) {
            waitInStation();
            return;
        }

        state = OnDemandVehicleState.DRIVING_TO_STATION;

        currentTrip = tripToStation;

        driveFactory.runActivity(this, vehicle, vehicleTripToTrip(currentTrip));
    }

    protected void waitInStation() {
        targetStation.parkVehicle(this);
        state = OnDemandVehicleState.WAITING;
        completeTrip = null;
    }

    @Override
    public VehicleTrip getCurrentPlan() {
        return completeTrip;
    }

    public Node getDemandTarget() {
        if (demandNodes != null) {
            return demandNodes.get(demandNodes.size() - 1);
        }
        return null;
    }

    public void startRebalancing(OnDemandVehicleStation targetStation) {
        state = OnDemandVehicleState.REBALANCING;
        currentRebalancingId = rebalancingIdGenerator.getId();

        currentTrip = tripsUtil.createTrip(getPosition().id,
                targetStation.getPosition().getId(), vehicle);
        metersRebalancing += positionUtil.getTripLengthInMeters(currentTrip);

        completeTrip = currentTrip.clone();

        this.targetStation = targetStation;

        driveFactory.runActivity(this, vehicle, vehicleTripToTrip(currentTrip));
    }

    @Override
    public PhysicalTransportVehicle getVehicle() {
        return vehicle;
    }

    @Override
    public double getVelocity() {
        return config.vehicleSpeedInMeters;
    }

    @Override
    public void setTargetNode(SimulationNode targetNode) {
        this.targetNode = targetNode;
    }

    @Override
    public SimulationNode getTargetNode() {
        return targetNode;
    }

    private void pickupDemand() {
        currentlyServedDemmand.demandAgent.tripStarted();
        vehicle.pickUp(currentlyServedDemmand.demandAgent);

    }

    protected void dropOffDemand() {
        currentlyServedDemmand.demandAgent.tripEnded();
        vehicle.dropOff(currentlyServedDemmand.demandAgent);

    }

    // todo - repair path planner and remove this
    protected Trip<Node> vehicleTripToTrip(VehicleTrip<TripItem> vehicleTrip) {
        LinkedList<Node> locations = new LinkedList<>();
        for (TripItem tripItem : vehicleTrip.getLocations()) {
            locations.add(positionUtil.getNode(tripItem.tripPositionByNodeId));
        }
        Trip<Node> trip = new Trip<>(locations);

        return trip;
    }

    @Override
    protected void onActivityFinish(Activity activity) {
        super.onActivityFinish(activity);
        finishedDriving();
    }

    @Override
    public EntityType getType() {
        return DemandSimulationEntityType.ON_DEMAND_VEHICLE;
    }

    @Override
    public void startDriving(PhysicalTransportVehicle vehicle) {
        this.vehicle = vehicle;
        vehicle.setDriver(this);
    }

    @Override
    public void setDelayData(DelayData delayData) {
        this.delayData = delayData;
    }

    @Override
    public DelayData getDelayData() {
        return delayData;
    }

    @Override
    public void endDriving() {

    }

}
