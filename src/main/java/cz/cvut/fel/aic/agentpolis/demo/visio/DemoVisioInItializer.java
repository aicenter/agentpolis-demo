/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo.visio;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.RailwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.BikewayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TramwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.MetrowayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.AllNetworkNodes;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.PedestrianNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.AgentStorage;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.VehicleStorage;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationCreator;
import cz.cvut.fel.aic.alite.simulation.Simulation;
import cz.cvut.fel.aic.alite.vis.VisManager;
import cz.cvut.fel.aic.alite.vis.layer.VisLayer;
import cz.cvut.fel.aic.alite.vis.layer.common.ColorLayer;

import java.awt.Color;

@Singleton
public class DemoVisioInItializer extends DefaultVisioInitializer {

    protected final NodeIdLayer nodeIdLayer;
    protected final HighwayLayer highwayLayer;
    private final VisLayer backgroundLayer;
    private final CarLayer carLayer;
    private final MapTilesLayer mapTitlesLayer;

    @Inject
    public DemoVisioInItializer(PedestrianNetwork pedestrianNetwork, BikewayNetwork bikewayNetwork,
                                HighwayNetwork highwayNetwork, TramwayNetwork tramwayNetwork, MetrowayNetwork metrowayNetwork,
                                RailwayNetwork railwayNetwork, NodeIdLayer nodeIdLayer, HighwayLayer highwayLayer,
                                SimulationControlLayer simulationControlLayer, GridLayer gridLayer, CarLayer carLayer, MapTilesLayer mapTitles) {
        super(pedestrianNetwork, bikewayNetwork, highwayNetwork, tramwayNetwork, metrowayNetwork, railwayNetwork,
                simulationControlLayer, gridLayer);
        this.nodeIdLayer = nodeIdLayer;
        this.highwayLayer = highwayLayer;
        this.carLayer = carLayer;
        this.backgroundLayer = ColorLayer.create(Color.white);
        this.mapTitlesLayer = mapTitles;
    }

    @Override
    protected void initEntityLayers(Simulation simulation) {

        VisManager.registerLayer(carLayer);
    }

    @Override
    protected void initLayersBeforeEntityLayers() {
    }

    @Override
    protected void initLayersAfterEntityLayers() {

    }

    @Override
    protected void initGraphLayers() {
        VisManager.registerLayer(backgroundLayer);
        VisManager.registerLayer(mapTitlesLayer);
        VisManager.registerLayer(highwayLayer);
    }

}
