/* 
 * Copyright (C) 2019 Czech Technical University in Prague.
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
package cz.cvut.fel.aic.apdemo.visio;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.RailwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.BikewayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.TramwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.MetrowayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.PedestrianNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.alite.simulation.Simulation;
import cz.cvut.fel.aic.alite.vis.VisManager;
import cz.cvut.fel.aic.alite.vis.layer.VisLayer;
import cz.cvut.fel.aic.alite.vis.layer.common.ColorLayer;
import cz.cvut.fel.aic.alite.vis.layer.toggle.KeyToggleLayer;

import java.awt.Color;

@Singleton
public class DemoVisioInItializer extends DefaultVisioInitializer {

	protected final NodeIdLayer nodeIdLayer;
	protected final HighwayLayer highwayLayer;
	private final VisLayer backgroundLayer;
	private final CarLayer carLayer;
	private final MapTilesLayer mapTilesLayer;
	private final LayerManagementLayer layerManagementLayer;

	@Inject
	public DemoVisioInItializer(Simulation simulation, PedestrianNetwork pedestrianNetwork, BikewayNetwork bikewayNetwork,
								HighwayNetwork highwayNetwork, TramwayNetwork tramwayNetwork, MetrowayNetwork metrowayNetwork,
								RailwayNetwork railwayNetwork, NodeIdLayer nodeIdLayer, HighwayLayer highwayLayer,
								SimulationControlLayer simulationControlLayer, GridLayer gridLayer, CarLayer carLayer, MapTilesLayer mapTiles, AgentpolisConfig config,
								LayerManagementLayer layerManagementLayer) {
		super(simulation, highwayNetwork, simulationControlLayer, gridLayer);
		this.nodeIdLayer = nodeIdLayer;
		this.highwayLayer = highwayLayer;
		this.carLayer = carLayer;
		this.backgroundLayer = ColorLayer.create(Color.white);
		this.mapTilesLayer = mapTiles;
		this.layerManagementLayer = layerManagementLayer;
	}

	@Override
	protected void initEntityLayers(Simulation simulation) {
		VisManager.registerLayer(layerManagementLayer.createManageableLayer("Maptiles", mapTilesLayer));
		ManageableLayer manageableHighwayLayer = layerManagementLayer.createManageableLayer("Highway layer", highwayLayer);  
		manageableHighwayLayer.toggle();
		VisManager.registerLayer(manageableHighwayLayer);	
		VisManager.registerLayer(layerManagementLayer.createManageableLayer("Vehicles", carLayer));
		ManageableLayer manageableNodeIdLayer = layerManagementLayer.createManageableLayer("Node ID layer", nodeIdLayer);  
		manageableNodeIdLayer.toggle();
		VisManager.registerLayer(manageableNodeIdLayer);
	}

	@Override
	protected void initLayersBeforeEntityLayers() {
	}

	@Override
	protected void initLayersAfterEntityLayers() {
		VisManager.registerLayer(layerManagementLayer);
	}

	@Override
	protected void initGraphLayers() {
		VisManager.registerLayer(backgroundLayer);
		/*
		VisManager.registerLayer(KeyToggleLayer.create("h", true, highwayLayer));
		VisManager.registerLayer(KeyToggleLayer.create("n", true, nodeIdLayer));*/
	}

}
