package cz.cvut.fel.aic.apdemo;

import com.google.common.collect.Sets;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import cz.cvut.fel.aic.apdemo.config.Config;
import cz.cvut.fel.aic.apdemo.visio.DemoVisioInItializer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.cvut.fel.aic.agentpolis.system.StandardAgentPolisModule;
import cz.cvut.fel.aic.geographtools.util.Transformer;
import cz.cvut.fel.aic.geographtools.TransportMode;

import java.util.Set;

public class MainModule extends StandardAgentPolisModule {

    private final Config config;

    public MainModule(Config config) {
        super();
        this.config = config;
    }

    @Override
    protected void bindVisioInitializer() {
        bind(VisioInitializer.class).to(DemoVisioInItializer.class);
    }

    @Override
    protected void configureNext() {
      
        bind(String.class).annotatedWith(Names.named("osm File")).toInstance(this.config.mapFilePath);
        bind(String.class).annotatedWith(Names.named("geojson Edges")).toInstance(this.config.mapGeojsonEdges);
        bind(String.class).annotatedWith(Names.named("geojson Nodes")).toInstance(this.config.mapGeojsonNodes);

        bind(new TypeLiteral<Set<TransportMode>>() {}).toInstance(Sets.immutableEnumSet(TransportMode.CAR));
        bind(Config.class).toInstance(this.config);
    }
}
