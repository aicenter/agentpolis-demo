package cz.cvut.fel.aic.apdemo;

import com.google.inject.Injector;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.cvut.fel.aic.agentpolis.system.AgentPolisInitializer;
import cz.cvut.fel.aic.apdemo.config.ApdemoConfig;

import java.io.File;
import java.net.MalformedURLException;

public class DemoSimulation {

    public static void main(String[] args) throws MalformedURLException {
        new DemoSimulation().run(args);
    }

    public void run(String[] args) {

        ApdemoConfig config = new ApdemoConfig();

        File localConfigFile = null;
        if (args.length > 0) {
            localConfigFile = new File(args[0]);
        }

        Injector injector = new AgentPolisInitializer(new MainModule(config, localConfigFile)).initialize();

        SimulationCreator creator = injector.getInstance(SimulationCreator.class);
        creator.prepareSimulation(injector.getInstance(MapInitializer.class).getMap());
        injector.getInstance(EventInitializer.class).initialize();

        creator.startSimulation();
    }
}
