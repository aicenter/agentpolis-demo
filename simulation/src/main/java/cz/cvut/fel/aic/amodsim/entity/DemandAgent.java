/*
 */
package cz.cvut.fel.aic.amodsim.entity;

import cz.cvut.fel.aic.amodsim.entity.vehicle.OnDemandVehicle;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import cz.cvut.fel.aic.amodsim.DemandData;
import cz.cvut.fel.aic.amodsim.OnDemandVehicleStationsCentral;
import cz.cvut.fel.aic.amodsim.event.OnDemandVehicleStationsCentralEvent;
import cz.cvut.fel.aic.amodsim.io.TimeTrip;
import cz.cvut.fel.aic.amodsim.storage.DemandStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.Agent;
import cz.cvut.fel.aic.agentpolis.simmodel.agent.TransportEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.TransportableEntity;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.agents.alite.common.event.Event;
import cz.agents.alite.common.event.EventHandler;
import cz.agents.alite.common.event.EventProcessor;
import cz.cvut.fel.aic.amodsim.DemandSimulationEntityType;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author F-I-D-O
 */
public class DemandAgent extends Agent implements EventHandler, TransportableEntity {

    private final int simpleId;

    private final TimeTrip<Long> osmNodeTrip;

    private final OnDemandVehicleStationsCentral onDemandVehicleStationsCentral;

    private final EventProcessor eventProcessor;

    private final DemandStorage demandStorage;

    private final Map<Long, SimulationNode> nodesMappedByNodeSourceIds;

    private DemandAgentState state;

    private OnDemandVehicle onDemandVehicle;

    private TransportEntity transportEntity;

 

    public DemandAgentState getState() {
        return state;
    }



    public OnDemandVehicle getOnDemandVehicle() {
        return onDemandVehicle;
    }

    @Inject
    public DemandAgent(OnDemandVehicleStationsCentral onDemandVehicleStationsCentral, EventProcessor eventProcessor,
            DemandStorage demandStorage, Map<Long, SimulationNode> nodesMappedByNodeSourceIds, 
             @Assisted String agentId, @Assisted int id,
            @Assisted TimeTrip<Long> osmNodeTrip) {
        super(agentId, nodesMappedByNodeSourceIds.get(osmNodeTrip.getLocations().get(0)));
        this.simpleId = id;
        this.osmNodeTrip = osmNodeTrip;
        this.onDemandVehicleStationsCentral = onDemandVehicleStationsCentral;
        this.eventProcessor = eventProcessor;
        this.demandStorage = demandStorage;
        this.nodesMappedByNodeSourceIds = nodesMappedByNodeSourceIds;
        state = DemandAgentState.WAITING;
    }

    @Override
    public void born() {
        demandStorage.addEntity(this);
        eventProcessor.addEvent(OnDemandVehicleStationsCentralEvent.DEMAND, onDemandVehicleStationsCentral, null,
                new DemandData(osmNodeTrip.getLocations(), this));
    }

    @Override
    public void die() {
        demandStorage.removeEntity(this);
    }

    @Override
    public EventProcessor getEventProcessor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleEvent(Event event) {
        onDemandVehicle = (OnDemandVehicle) event.getContent();
    }

    public void tripEnded() {
        if (!getPosition().equals(nodesMappedByNodeSourceIds.get(osmNodeTrip.getLocations().getLast()))) {
            try {
                throw new Exception("Demand not served properly");
            } catch (Exception ex) {
                Logger.getLogger(DemandAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        die();
    }

    public void tripStarted() {
        state = DemandAgentState.RIDING;
    }

    @Override
    public EntityType getType() {
        return DemandSimulationEntityType.DEMAND;
    }

    @Override
    public <T extends TransportEntity> T getTransportingEntity() {
        return (T) transportEntity;
    }

    @Override
    public <T extends TransportEntity> void setTransportingEntity(T transportingEntity) {
        this.transportEntity = transportingEntity;
    }

    public interface DemandAgentFactory {

        public DemandAgent create(String agentId, int id, TimeTrip<Long> osmNodeTrip);
    }

}
