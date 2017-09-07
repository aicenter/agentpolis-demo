package cz.cvut.fel.aic.agentpolis.demo;

import ninja.fido.config.Configuration;
import com.google.inject.Injector;
import cz.cvut.fel.aic.agentpolis.demo.config.Config;
import cz.cvut.fel.aic.agentpolis.system.AgentPolisInitializer;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import java.net.MalformedURLException;

/**
 * @author David Fiedler
 */
public class OnDemandVehiclesSimulation {

    public static void main(String[] args) throws MalformedURLException {
        new OnDemandVehiclesSimulation().run();
    }

    public void run() {
        Config config = Configuration.load(new Config());

        Injector injector = new AgentPolisInitializer(new MainModule(config)).initialize();

        SimulationCreator creator = injector.getInstance(SimulationCreator.class);

        //fixed
        creator.prepareSimulation(injector.getInstance(MapInitializer.class).getMap());

        //new
        injector.getInstance(EventInitializer.class).initialize();

        // start it up
        creator.startSimulation();

    }
}
