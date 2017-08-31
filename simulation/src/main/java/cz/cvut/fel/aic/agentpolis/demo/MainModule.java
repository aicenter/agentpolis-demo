/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo;

import com.google.common.collect.Sets;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import cz.cvut.fel.aic.agentpolis.demo.config.Config;
import cz.cvut.fel.aic.agentpolis.demo.visio.AmodsimVisioInItializer;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.EntityStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.VehicleStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;
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
        bind(File.class).annotatedWith(Names.named("osm File")).toInstance(new File(amodsimConfig.mapFilePath));

        bind(new TypeLiteral<Set<TransportMode>>() {}).toInstance(Sets.immutableEnumSet(TransportMode.CAR));
        bind(Config.class).toInstance(amodsimConfig);
        bind(Transformer.class).toInstance(new Transformer(amodsimConfig.srid));
    }
}
