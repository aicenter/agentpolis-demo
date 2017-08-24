/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.amodsim;

import com.google.common.collect.Sets;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import cz.cvut.fel.aic.amodsim.entity.DemandAgent;
import cz.cvut.fel.aic.amodsim.entity.DemandAgent.DemandAgentFactory;
import cz.cvut.fel.aic.amodsim.entity.vehicle.OnDemandVehicle;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.TripsUtil;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.PhysicalVehicleDriveFactory;
import cz.cvut.fel.aic.agentpolis.simmodel.activity.activityFactory.StandardDriveFactory;
import cz.cvut.fel.aic.amodsim.tripUtil.TripsUtilCached;
import cz.cvut.fel.aic.amodsim.visio.DemandLayer;
import cz.cvut.fel.aic.amodsim.visio.DemandLayerWithJitter;
import cz.cvut.fel.aic.amodsim.visio.AmodsimVisioInItializer;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.EntityStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.VehicleStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.cvut.fel.aic.amodsim.config.Config;
import cz.cvut.fel.aic.amodsim.entity.vehicle.OnDemandVehicleFactorySpec;
import cz.cvut.fel.aic.amodsim.entity.vehicle.RideSharingOnDemandVehicle;
import cz.cvut.fel.aic.agentpolis.system.StandardAgentPolisModule;
import cz.cvut.fel.aic.geographtools.util.Transformer;
import cz.cvut.fel.aic.geographtools.TransportMode;
import java.io.File;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author fido
 */
public class MainModule extends StandardAgentPolisModule {

    private final Config amodsimConfig;

    public MainModule(Config amodsimConfig) {
        super();
        this.amodsimConfig = amodsimConfig;
    }

    @Override
    protected void bindVisioInitializer() {
        bind(VisioInitializer.class).to(AmodsimVisioInItializer.class);
    }

    @Override
    protected void configureNext() {
        bindConstant().annotatedWith(Names.named("precomputedPaths")).to(false);

        bind(File.class).annotatedWith(Names.named("osm File")).toInstance(new File(amodsimConfig.mapFilePath));

        bind(new TypeLiteral<Set<TransportMode>>() {
        }).toInstance(Sets.immutableEnumSet(TransportMode.CAR));
        bind(Config.class).toInstance(amodsimConfig);
        bind(Transformer.class).toInstance(new Transformer(amodsimConfig.srid));

        bind(EntityStorage.class).to(VehicleStorage.class);

        if (amodsimConfig.agentpolis.useTripCache) {
            bind(TripsUtil.class).to(TripsUtilCached.class);
        }
        bind(DemandLayer.class).to(DemandLayerWithJitter.class);

        bind(PhysicalVehicleDriveFactory.class).to(StandardDriveFactory.class);

        if (amodsimConfig.agentpolis.ridesharing) {
            install(new FactoryModuleBuilder().implement(OnDemandVehicle.class, RideSharingOnDemandVehicle.class)
                    .build(OnDemandVehicleFactorySpec.class));
        } else {
            install(new FactoryModuleBuilder().implement(OnDemandVehicle.class, OnDemandVehicle.class)
                    .build(OnDemandVehicleFactorySpec.class));
        }

        install(new FactoryModuleBuilder().implement(DemandAgent.class, DemandAgent.class)
                .build(DemandAgentFactory.class));
    }

    @Provides
    @Singleton
    Map<Long, SimulationNode> provideNodesMappedByNodeSourceIds(HighwayNetwork highwayNetwork, AllNetworkNodes allNetworkNodes) {
        Map<Long, Integer> nodeIdsMappedByNodeSourceIds = highwayNetwork.getNetwork().createSourceIdToNodeIdMap();
        Map<Long, SimulationNode> nodesMappedByNodeSourceIds = new HashMap<>();

        for (Map.Entry<Long, Integer> entry : nodeIdsMappedByNodeSourceIds.entrySet()) {
            Long key = entry.getKey();
            Integer value = entry.getValue();
            nodesMappedByNodeSourceIds.put(key, allNetworkNodes.getNode(value));
        }

        return nodesMappedByNodeSourceIds;
    }

}
