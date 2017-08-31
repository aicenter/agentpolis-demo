/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.agentpolis.demo.visio;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.EntityLayer;
import cz.cvut.fel.aic.agentpolis.demo.entity.DemandAgent;
import cz.cvut.fel.aic.agentpolis.demo.entity.DemandAgentState;
import cz.cvut.fel.aic.agentpolis.demo.storage.DemandStorage;
import java.awt.Color;
import java.awt.Dimension;
import javax.vecmath.Point2d;

/**
 *
 * @author fido
 */
@Singleton
public class DemandLayer extends EntityLayer<DemandAgent> {

    private static final Color DEMAND_COLOR = Color.RED;

    private static final int SIZE = 3;

    @Inject
    public DemandLayer(DemandStorage demandStorage) {
        super(demandStorage);
    }

    protected Point2d getDrivingAgentPosition(DemandAgent demandAgent) {
        return positionUtil.getCanvasPositionInterpolated(demandAgent.getOnDemandVehicle());
    }

    protected Point2d getWaitingAgentPosition(DemandAgent demandAgent, Dimension drawingDimension) {
        return positionUtil.getCanvasPosition(demandAgent.getPosition());
    }

    @Override
    protected Point2d getEntityPosition(DemandAgent demandAgent) {
        if (demandAgent.getState() == DemandAgentState.RIDING) {
            return getDrivingAgentPosition(demandAgent);
        } else {
            return getWaitingAgentPosition(demandAgent, dim);
        }
    }

    @Override
    protected Color getEntityDrawColor(DemandAgent demandAgent) {
        return DEMAND_COLOR;
    }

    @Override
    protected int getEntityDrawRadius(DemandAgent demandAgent) {
        return SIZE;
    }

}
