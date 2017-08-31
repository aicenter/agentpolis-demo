package cz.cvut.fel.aic.agentpolis.demo.io;

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
