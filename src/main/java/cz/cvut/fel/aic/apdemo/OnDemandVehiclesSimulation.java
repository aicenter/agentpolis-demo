package cz.cvut.fel.aic.apdemo;

import cz.cvut.fel.aic.apdemo.config.Config;
import ninja.fido.config.Configuration;
import com.google.inject.Injector;
import cz.cvut.fel.aic.agentpolis.system.AgentPolisInitializer;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import java.net.MalformedURLException;

public class OnDemandVehiclesSimulation {

    public static void main(String[] args) throws MalformedURLException {
        new OnDemandVehiclesSimulation().run();
    }

    public void run() {
        Config config = Configuration.load(new Config());

        Injector injector = new AgentPolisInitializer(new MainModule(config)).initialize();
        SimulationCreator creator = injector.getInstance(SimulationCreator.class);
        creator.prepareSimulation(injector.getInstance(MapInitializer.class).getMap());
        injector.getInstance(EventInitializer.class).initialize();

        creator.startSimulation();
    }
}
