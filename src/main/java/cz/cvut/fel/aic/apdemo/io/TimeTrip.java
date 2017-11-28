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
package cz.cvut.fel.aic.apdemo.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import java.util.LinkedList;

/**
 *
 * @author F-I-D-O
 * @param <L> location type
 */
public class TimeTrip<L> extends Trip<L> {

    @JsonCreator
    public TimeTrip(@JsonProperty("locations") LinkedList<L> locations, @JsonProperty("startTime") long startTime,
            @JsonProperty("endTime") long endTime) {
        super(locations);
    }

    public TimeTrip(L startLocation, L endLocation, long startTime) {
        super(startLocation, endLocation);

    }

    @Override
    @JsonIgnore
    public L getAndRemoveFirstLocation() {
        return super.getAndRemoveFirstLocation();
    }

    @JsonIgnore
    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

}
