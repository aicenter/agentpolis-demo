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

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.VehicleStorage;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VehicleLayer;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 *
 * @author F-I-D-O
 */
public class CarLayer extends VehicleLayer<PhysicalVehicle> {

	private Path2D CAR_REPRESENTATION_SHAPE;

	@Inject
	public CarLayer(VehicleStorage vehicleStorage, AgentpolisConfig agentpolisConfig) {
		super(vehicleStorage, agentpolisConfig);
	}

	@Override
	protected Color getEntityDrawColor(PhysicalVehicle vehicle) {
		return Color.MAGENTA;
	}
   
	@Override
	protected float getVehicleStaticLength(PhysicalVehicle vehicle) {
		return (float) vehicle.getLengthM();
	}
	
	@Override
	protected float getVehicleStaticWidth(PhysicalVehicle vehicle) {
		return 2;
	}
	
	@Override
	protected float getVehicleWidth(PhysicalVehicle vehicle) {
		return 2;
	}

	@Override
	protected float getVehicleLength(PhysicalVehicle vehicle) {
		return (float) vehicle.getLengthM();
	}

}
