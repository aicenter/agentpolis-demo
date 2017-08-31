package cz.cvut.fel.aic.agentpolis.demo.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.demo.entity.vehicle.OnDemandVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.EntityStorage;

/**
 *
 * @author F.I.D.O.
 */
@Singleton
public class OnDemandVehicleStorage extends EntityStorage<OnDemandVehicle> {

    @Inject
    public OnDemandVehicleStorage() {
        super();
    }

}
