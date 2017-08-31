/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo.tripUtil;


import cz.cvut.fel.aic.agentpolis.siminfrastructure.planner.trip.Trip;
import java.util.LinkedList;

/**
 *
 * @author fido
 */
public class SimpleJsonTrip extends Trip<JsonTripItem> {

    public static LinkedList<JsonTripItem> getLocationList(int[] locationsArray) {
        LinkedList<JsonTripItem> locationList = new LinkedList<>();
        for (int i = 0; i < locationsArray.length; i++) {
            locationList.add(new JsonTripItem(locationsArray[i]));
        }
        return locationList;
    }

    public SimpleJsonTrip(LinkedList<JsonTripItem> locations) {
        super(locations);
    }


}
