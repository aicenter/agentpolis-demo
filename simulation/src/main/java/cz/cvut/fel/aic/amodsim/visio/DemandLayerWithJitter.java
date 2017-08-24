/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.amodsim.visio;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.amodsim.entity.DemandAgent;
import cz.cvut.fel.aic.amodsim.storage.DemandStorage;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Random;
import javax.vecmath.Point2d;

/**
 *
 * @author fido
 */
@Singleton
public class DemandLayerWithJitter extends DemandLayer {

    private static final double JITTER_DEVIATION = 8;

    private final HashMap<DemandAgent, Point2d> waitingAgentCachedPosition;

    private final Random random;

    @Inject
    public DemandLayerWithJitter(DemandStorage demandStorage) {
        super(demandStorage);
        waitingAgentCachedPosition = new HashMap<>();
        random = new Random();
    }

    @Override
    protected Point2d getWaitingAgentPosition(DemandAgent demandAgent, Dimension drawingDimension) {
        Point2d agentPosition = positionUtil.getPosition(demandAgent.getPosition());
        Point2d agentJitter;
        if (waitingAgentCachedPosition.containsKey(demandAgent)) {
            agentJitter = waitingAgentCachedPosition.get(demandAgent);
        } else {
            agentJitter = getJitter();
            waitingAgentCachedPosition.put(demandAgent, agentJitter);
        }

        jitte(agentPosition, agentJitter);

        return positionUtil.getCanvasPosition(agentPosition);
    }

    @Override
    protected Point2d getDrivingAgentPosition(DemandAgent demandAgent) {
        if (waitingAgentCachedPosition.containsKey(demandAgent)) {
            waitingAgentCachedPosition.remove(demandAgent);
        }
        return super.getDrivingAgentPosition(demandAgent);
    }

    private Point2d getJitter() {
        double jittX = random.nextGaussian();
        double jittY = random.nextGaussian();

        return new Point2d(jittX, jittY);
    }

    private void jitte(Point2d agentPosition, Point2d agentJitter) {
        agentPosition.x = agentPosition.x + JITTER_DEVIATION * agentJitter.x;
        agentPosition.y = agentPosition.y + JITTER_DEVIATION * agentJitter.y;
    }

}
