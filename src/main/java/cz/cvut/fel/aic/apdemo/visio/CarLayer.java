/*
 */
package cz.cvut.fel.aic.apdemo.visio;

import com.google.inject.Inject;
import cz.cvut.fel.aic.apdemo.DriveAgent;
import cz.cvut.fel.aic.apdemo.DriveAgentStorage;

import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.EntityLayer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import javax.vecmath.Point2d;

/**
 *
 * @author F-I-D-O
 */
public class CarLayer extends EntityLayer<DriveAgent> {

    private Path2D CAR_REPRESENTATION_SHAPE;

    @Inject
    public CarLayer(DriveAgentStorage driveAgentStorage) {
        super(driveAgentStorage);
    }

    @Override
    protected Point2d getEntityPosition(DriveAgent entity) {
        Vehicle vehicle = entity.getVehicle();
        if (vehicle == null) {
            return positionUtil.getCanvasPosition(entity);
        }
        return positionUtil.getCanvasPositionInterpolatedForVehicle(vehicle);
    }

    @Override
    protected Color getEntityDrawColor(DriveAgent driveAgent) {
        return Color.MAGENTA;
    }

    // not used
    @Override
    protected int getEntityDrawRadius(DriveAgent driveAgent) {
        return 0;
    }

    @Override
    protected void drawEntities(ArrayList<DriveAgent> entities, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
        DriveAgent representative = entities.get(0);

        if (CAR_REPRESENTATION_SHAPE == null) {
            CAR_REPRESENTATION_SHAPE = createCarShape((float) representative.getVehicle().getLength());
        }

        Color color = getEntityDrawColor(entities.get(0));
        canvas.setColor(color);
        double radius = Vis.transW(getEntityDrawRadius(entities.get(0)));
        int width = (int) Math.round(radius * 2);

        int x1 = (int) (entityPosition.getX() - radius);
        int y1 = (int) (entityPosition.getY() - radius);
        int x2 = (int) (entityPosition.getX() + radius);
        int y2 = (int) (entityPosition.getY() + radius);

        if (x2 > 0 && x1 < dim.width && y2 > 0 && y1 < dim.height) {
            SimulationNode target = representative.getTargetNode();

            double angle = 0;
            if (target != null) {
                SimulationNode position = representative.getPosition();
                angle = getAngle(position, target);
            }

            AffineTransform trans
                    = AffineTransform.getTranslateInstance(entityPosition.getX(), entityPosition.getY());
            trans.rotate(-angle);

            Shape s = CAR_REPRESENTATION_SHAPE.createTransformedShape(trans);
            canvas.fill(s);
        }

        if (entities.size() > 1) {
            VisioUtils.printTextWithBackgroud(canvas, Integer.toString(entities.size()),
                    new Point((int) (x1 - DEFAULT_TEXT_MARGIN_BOTTOM), y1 - (y2 - y1) / 2), color,
                    DEFAULT_TEXT_BACKGROUND_COLOR);
        }
    }


    private static Path2D createCarShape(float s) {
        s = 2 * s;
        final GeneralPath p0 = new GeneralPath();
        p0.moveTo(-s, 0f);
        p0.lineTo(0, 4f);
        p0.lineTo(-s, 8f);
        p0.closePath();
        return p0;
    }

    private double getAngle(SimulationNode position, SimulationNode target) {
        double dy = target.getLatitudeProjected1E2() - position.getLatitudeProjected1E2();
        double dx = target.getLongitudeProjected1E2() - position.getLongitudeProjected1E2();
        return Math.atan2(dy, dx);
    }

}