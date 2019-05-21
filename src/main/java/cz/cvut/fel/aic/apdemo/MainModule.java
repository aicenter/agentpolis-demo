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

		bind(String.class).annotatedWith(Names.named("geojson Edges")).toInstance(this.config.mapGeojsonEdges);
		bind(String.class).annotatedWith(Names.named("geojson Nodes")).toInstance(this.config.mapGeojsonNodes);

		bind(new TypeLiteral<Set<TransportMode>>() {
		}).toInstance(Sets.immutableEnumSet(TransportMode.CAR));
		bind(ApdemoConfig.class).toInstance(this.config);
	}
}
