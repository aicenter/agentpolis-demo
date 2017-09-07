package cz.cvut.fel.aic.agentpolis.demo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import cz.cvut.fel.aic.agentpolis.simmodel.entity.EntityType;

/**
 *
 * @author fido
 */
public enum DemoType implements EntityType {
    VEHICLE,
    DRIVER;

    @Override
    public String getDescriptionEntityType() {
        return "";
    }
}
