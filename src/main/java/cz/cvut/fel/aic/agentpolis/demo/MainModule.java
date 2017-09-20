/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo;

import com.google.common.collect.Sets;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import cz.cvut.fel.aic.agentpolis.demo.config.Config;
import cz.cvut.fel.aic.agentpolis.demo.visio.AmodsimVisioInItializer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.cvut.fel.aic.agentpolis.system.StandardAgentPolisModule;
import cz.cvut.fel.aic.geographtools.util.Transformer;
import cz.cvut.fel.aic.geographtools.TransportMode;
import java.io.File;

import java.util.Set;
import sun.font.FontUtilities;

/**
 *
 * @author fido
 */
public class MainModule extends StandardAgentPolisModule {

    private final Config config;

    public MainModule(Config config) {
        super();
        this.config = config;
    }

    @Override
    protected void bindVisioInitializer() {
        bind(VisioInitializer.class).to(AmodsimVisioInItializer.class);
    }

    @Override
    protected void configureNext() {
      
        bind(String.class).annotatedWith(Names.named("osm File")).toInstance(this.config.mapFilePath);
        bind(String.class).annotatedWith(Names.named("geojson Edges")).toInstance(this.config.geojsonEdges);
        bind(String.class).annotatedWith(Names.named("geojson Nodes")).toInstance(this.config.geojsonNodes);

        bind(new TypeLiteral<Set<TransportMode>>() {}).toInstance(Sets.immutableEnumSet(TransportMode.CAR));
        bind(Config.class).toInstance(this.config);
        bind(Transformer.class).toInstance(new Transformer(this.config.srid));
    }
}
