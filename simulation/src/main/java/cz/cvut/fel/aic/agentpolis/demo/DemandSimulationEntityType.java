/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;

/**
 *
 * @author david
 */
public enum DemandSimulationEntityType implements EntityType {

    TEST_TYPE("testType"),
    DEMAND("demand"),
    ON_DEMAND_VEHICLE("on demand vehicle"),
    VEHICLE("vehicle"),
    ON_DEMAND_VEHICLE_STATION("on demand vehicle station");

    private final String entityType;

    private DemandSimulationEntityType(String entityType) {
        this.entityType = entityType;
    }

    @Override
    public String getDescriptionEntityType() {
        return entityType;
    }

}
