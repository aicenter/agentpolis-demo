/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.apdemo;

import com.google.inject.Injector;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.cvut.fel.aic.agentpolis.system.AgentPolisInitializer;
import cz.cvut.fel.aic.apdemo.config.ApdemoConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.NodeCoordinateInformation;
import cz.cvut.fel.aic.geographtools.util.GPSLocationTools;

import java.util.HashMap;
import java.util.Map;

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
        //localConfigFile = new File("topkek.cfg");

        Injector injector = new AgentPolisInitializer(new MainModule(config, localConfigFile)).initialize();

        SimulationCreator creator = injector.getInstance(SimulationCreator.class);
        creator.prepareSimulation(injector.getInstance(MapInitializer.class).getMap());
        injector.getInstance(EventInitializer.class).initialize();



        //NodeCoordinateInformation ci = new NodeCoordinateInformation(injector.getInstance(MapInitializer.class).getMap());
        /*Map <Integer, SimulationNode> nodes = injector.getInstance()
        for (Integer key1: nodes.keySet() ) {
            for (Integer key2: nodes.keySet() ) {
                double distance = GPSLocationTools.computeDistanceAsDouble(nodes.get(key1), nodes.get(key2));
                System.out.println( distance + "\n" );
            }
        }*/



        creator.startSimulation();
    }
}
