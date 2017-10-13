package cz.cvut.fel.aic.apdemo;

import com.google.common.collect.Sets;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioInitializer;
import cz.cvut.fel.aic.agentpolis.system.StandardAgentPolisModule;
import cz.cvut.fel.aic.apdemo.config.ApdemoConfig;
import cz.cvut.fel.aic.apdemo.visio.DemoVisioInItializer;
import cz.cvut.fel.aic.geographtools.TransportMode;

import java.io.File;
import java.util.Set;

public class MainModule extends StandardAgentPolisModule {

    private final ApdemoConfig config;

    public MainModule(ApdemoConfig apdemoConfig, File localConfigFile) {
        super(apdemoConfig, localConfigFile, "agentpolis");
        this.config = apdemoConfig;
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

        bind(new TypeLiteral<Set<TransportMode>>() {
        }).toInstance(Sets.immutableEnumSet(TransportMode.CAR));
        bind(ApdemoConfig.class).toInstance(this.config);
    }
}
