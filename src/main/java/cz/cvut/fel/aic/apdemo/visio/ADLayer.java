package cz.cvut.fel.aic.apdemo.visio;

import com.google.inject.Inject;
import cz.cvut.fel.aic.agentpolis.config.AgentpolisConfig;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.ADPhysicalVehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.Vehicle;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.Network;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.XMLReader;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationNode;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.EntityLayer;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioUtils;
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.apdemo.ADDriveAgent;
import cz.cvut.fel.aic.apdemo.AgentdriveAgentStorage;
import cz.cvut.fel.aic.geographtools.GPSLocation;

import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class ADLayer extends EntityLayer<ADDriveAgent> {

    private Path2D CAR_REPRESENTATION_SHAPE;

    @Inject
    public ADLayer(AgentdriveAgentStorage driveAgentStorage, AgentpolisConfig apc) {
        super(driveAgentStorage, apc);
    }

    @Override
    protected Point2d getEntityPosition(ADDriveAgent entity) {
        ADPhysicalVehicle vehicle = entity.getVehicle();
        if (vehicle == null) {
            return new Point2d(entity.getPosX(), entity.getPosY());
        }
        double x = vehicle.getPosX();
        double y = vehicle.getPosY();
        if (x == Double.MAX_VALUE && y == Double.MAX_VALUE)
            return positionUtil.getCanvasPosition(vehicle);
        Point2d p2d = new Point2d(Vis.transX(((x - Network.OFFSET_X))), Vis.transY(-(y - Network.OFFSET_Y)));
        return p2d;
    }


    @Override
    protected Point2d getEntityPositionInTime(ADDriveAgent entity, long time) {
        return getEntityPosition(entity);
    }

    @Override
    protected Color getEntityDrawColor(ADDriveAgent driveAgent) {
        return Color.blue;
    }

    @Override
    protected int getEntityTransformableRadius(ADDriveAgent entity) {
        return 20;
    }

    @Override
    protected double getEntityStaticRadius(ADDriveAgent entity) {
        return 20;
    }

    // not used
    // @Override
    protected int getEntityDrawRadius(ADDriveAgent driveAgent) {
        return 20;
    }

    protected void drawEntity(ADDriveAgent entity, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
        ADDriveAgent representative = entity;

        if (CAR_REPRESENTATION_SHAPE == null) {
            CAR_REPRESENTATION_SHAPE = createCarShape((float) representative.getVehicle().getLength());
        }

        Color color = getEntityDrawColor(entity);
        canvas.setColor(color);
        double radius = Vis.transW(getEntityDrawRadius(entity));
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

            Shape s =  CAR_REPRESENTATION_SHAPE.createTransformedShape(trans);
            canvas.fill(s);

        }
    }
    @Override
    protected void drawEntities(ArrayList<ADDriveAgent> entities, Point2d entityPosition, Graphics2D canvas, Dimension dim) {
        ADDriveAgent representative = entities.get(0);

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

            Shape s =  CAR_REPRESENTATION_SHAPE.createTransformedShape(trans);
            canvas.fill(s);

        }

        if (entities.size() > 1) {
            VisioUtils.printTextWithBackgroud(canvas, Integer.toString(entities.size()),
                    new Point((int) (x1 - DEFAULT_TEXT_MARGIN_BOTTOM), y1 - (y2 - y1) / 2), color,
                    DEFAULT_TEXT_BACKGROUND_COLOR);
        }
    }


    private static Path2D createCarShape(float s) {
        s = 5 * s;
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
