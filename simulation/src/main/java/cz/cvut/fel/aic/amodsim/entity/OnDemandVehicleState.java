/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.amodsim.entity;

/**
 *
 * @author fido
 */
public enum OnDemandVehicleState {
    WAITING,
    DRIVING_TO_START_LOCATION,
    DRIVING_TO_TARGET_LOCATION,
    DRIVING_TO_STATION,
    REBALANCING;
}
