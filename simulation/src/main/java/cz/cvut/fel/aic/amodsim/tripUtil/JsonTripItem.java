/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.amodsim.tripUtil;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.TripItem;

/**
 *
 * @author fido
 */
public class JsonTripItem extends TripItem {

    @JsonCreator
    public JsonTripItem(@JsonProperty("tripPositionByNodeId") int tripPositionByNodeId) {
        super(tripPositionByNodeId);
    }

}
