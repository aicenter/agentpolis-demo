/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo.entity;

import cz.cvut.fel.aic.agentpolis.demo.entity.vehicle.OnDemandVehicle;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.vividsolutions.jts.geom.Coordinate;
import cz.cvut.fel.aic.agentpolis.demo.DemandData;
import cz.cvut.fel.aic.agentpolis.demo.DemandSimulationEntityType;
import cz.cvut.fel.aic.agentpolis.demo.storage.OnDemandvehicleStationStorage;
import cz.cvut.fel.aic.agentpolis.demo.io.TimeTrip;
import cz.cvut.fel.aic.agentpolis.demo.storage.OnDemandVehicleStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.AgentPolisEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.NearestElementUtils;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.agentpolis.demo.OnDemandVehicleStationsCentral;
import cz.cvut.fel.aic.agentpolis.config.Config;
import cz.cvut.fel.aic.agentpolis.demo.entity.vehicle.OnDemandVehicleFactory;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.Node;
import cz.cvut.fel.aic.geographtools.util.NearestElementUtil;
import cz.cvut.fel.aic.geographtools.util.NearestElementUtilPair;
import cz.cvut.fel.aic.geographtools.util.Transformer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fido
 */
public class OnDemandVehicleStation extends AgentPolisEntity implements EventHandler {

    private final LinkedList<OnDemandVehicle> parkedVehicles;

    private final EventProcessor eventProcessor;

    private final LinkedList<DepartureCard> departureCards;

    private final Transformer transformer;

    private final PositionUtil positionUtil;

    private final OnDemandVehicleStationsCentral onDemandVehicleStationsCentral;

    private final Map<Long, SimulationNode> nodesMappedByNodeSourceIds;

    private final Config config;

    @Inject
    public OnDemandVehicleStation(Config config, EventProcessor eventProcessor,
            OnDemandVehicleFactory onDemandVehicleFactory, NearestElementUtils nearestElementUtils,
            OnDemandvehicleStationStorage onDemandVehicleStationStorage, OnDemandVehicleStorage onDemandVehicleStorage,
            @Assisted String id, @Assisted SimulationNode node,
            @Assisted int initialVehicleCount, Transformer transformer, PositionUtil positionUtil,
            OnDemandVehicleStationsCentral onDemandVehicleStationsCentral, Map<Long, SimulationNode> nodesMappedByNodeSourceIds) {
        super(id, node);
        this.eventProcessor = eventProcessor;
        parkedVehicles = new LinkedList<>();
        for (int i = 0; i < initialVehicleCount; i++) {
            String onDemandVehicelId = String.format("%s-%d", id, i);
            OnDemandVehicle newVehicle = onDemandVehicleFactory.create(onDemandVehicelId, getPosition());
            parkedVehicles.add(newVehicle);
            onDemandVehicleStorage.addEntity(newVehicle);
        }
        onDemandVehicleStationStorage.addEntity(this);
        this.departureCards = new LinkedList<>();
        this.transformer = transformer;
        this.positionUtil = positionUtil;
        this.onDemandVehicleStationsCentral = onDemandVehicleStationsCentral;
        this.nodesMappedByNodeSourceIds = nodesMappedByNodeSourceIds;
        this.config = config;
    }

    @Override
    public EntityType getType() {
        return DemandSimulationEntityType.ON_DEMAND_VEHICLE_STATION;
    }

    @Override
    public EventProcessor getEventProcessor() {
        return null;
    }

    @Override
    public void handleEvent(Event event) {

    }

    public void parkVehicle(OnDemandVehicle onDemandVehicle) {
        if (onDemandVehicle.getPosition() != getPosition()) {
            try {
                throw new Exception("Vehicle cannot be parked in station, beacause it's not present in the station!");
            } catch (Exception ex) {
                Logger.getLogger(OnDemandVehicleStation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        parkedVehicles.add(onDemandVehicle);
    }

    public int getParkedVehiclesCount() {
        return parkedVehicles.size();
    }

    public void handleTripRequest(DemandData demandData) {
        Node startLocation = nodesMappedByNodeSourceIds.get(demandData.locations.get(0));
        Node targetLocation = nodesMappedByNodeSourceIds.get(demandData.locations.get(demandData.locations.size() - 1));

        // hack for demands that starts and ends in the same position
        if (startLocation.equals(targetLocation)) {
            try {
                throw new Exception("Start and target location cannot be the same!");
            } catch (Exception ex) {
                Logger.getLogger(OnDemandVehicleStation.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }

        OnDemandVehicle vehicle = getVehicle(startLocation, targetLocation, config.agentpolis.ridesharing);

        if (vehicle != null) {
            vehicle.setDepartureStation(this);
            eventProcessor.addEvent(null, vehicle, null, demandData);
            eventProcessor.addEvent(null, demandData.demandAgent, null, vehicle);
        } else {
            try {
                throw new Exception("Request cannot be handeled - station has not any vehicles available!");
            } catch (Exception ex) {
                Logger.getLogger(OnDemandVehicleStation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean rebalance(TimeTrip<OnDemandVehicleStation> rebalancingTrip) {
        OnDemandVehicle vehicle = getVehicle(this.getPosition(),
                rebalancingTrip.getLocations().getLast().getPosition(), false);
        if (vehicle != null) {
            vehicle.startRebalancing(rebalancingTrip.getLocations().getLast());
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return parkedVehicles.isEmpty();
    }

    private OnDemandVehicle getVehicle(Node startLocation, Node targetLocation, boolean useRideSharing) {
        OnDemandVehicle nearestVehicle;

        OnDemandVehicleStation targetStation
                = onDemandVehicleStationsCentral.getNearestStation(targetLocation);

        if (useRideSharing) {
            NearestElementUtil<OnDemandVehicle> nearestElementUtil
                    = getNearestElementUtilForVehiclesBeforeDeparture(targetStation);

            // ridesharing posibility test
            if (nearestElementUtil != null) {
                nearestVehicle = nearestElementUtil.getNearestElement(startLocation);

                // currently cartesian distance check only!
                if (positionUtil.getPosition(nearestVehicle.getPosition()).distance(positionUtil.getPosition(startLocation))
                        < positionUtil.getPosition(getPosition()).distance(positionUtil.getPosition(startLocation))) {
                    // ridesharing successfully locked
                    return nearestVehicle;
                }
            }
        }

        nearestVehicle = parkedVehicles.poll();

        if (useRideSharing && nearestVehicle != null) {
            departureCards.add(new DepartureCard(nearestVehicle, targetStation));
        }

        return nearestVehicle;
    }

    private NearestElementUtil<OnDemandVehicle> getNearestElementUtilForVehiclesBeforeDeparture(
            OnDemandVehicleStation targetStation) {
        List<NearestElementUtilPair<Coordinate, OnDemandVehicle>> pairs = new ArrayList<>();

        for (DepartureCard departureCard : departureCards) {
            if (departureCard.getTargetStation() == targetStation) {
                GPSLocation location = departureCard.getDemandVehicle().getPosition();

                pairs.add(new NearestElementUtilPair<>(
                        new Coordinate(location.getLongitude(), location.getLatitude(), location.elevation),
                        departureCard.getDemandVehicle()));
            }
        }
        if (pairs.size() > 0) {
            return new NearestElementUtil<>(pairs, transformer, new OnDemandVehicleStation.OnDemandVehicleArrayConstructor());
        } else {
            return null;
        }
    }

    public void release(OnDemandVehicle onDemandVehicle) {
        Iterator<DepartureCard> iterator = departureCards.iterator();
        while (iterator.hasNext()) {
            DepartureCard departureCard = iterator.next();
            if (departureCard.demandVehicle == onDemandVehicle) {
                iterator.remove();
                break;
            }
        }
    }

    public interface OnDemandVehicleStationFactory {

        public OnDemandVehicleStation create(String id, Node node, int initialVehicleCount);
    }

    private class DepartureCard {

        private final OnDemandVehicle demandVehicle;

        private final OnDemandVehicleStation targetStation;

        public OnDemandVehicle getDemandVehicle() {
            return demandVehicle;
        }

        public OnDemandVehicleStation getTargetStation() {
            return targetStation;
        }

        public DepartureCard(OnDemandVehicle demandVehicle, OnDemandVehicleStation targetStation) {
            this.demandVehicle = demandVehicle;
            this.targetStation = targetStation;
        }

    }

    private static class OnDemandVehicleArrayConstructor
            implements NearestElementUtil.SerializableIntFunction<OnDemandVehicle[]> {

        @Override
        public OnDemandVehicle[] apply(int value) {
            return new OnDemandVehicle[value];
        }

    }
}
