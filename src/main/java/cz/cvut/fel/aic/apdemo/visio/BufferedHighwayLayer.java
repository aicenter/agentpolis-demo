/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.aic.apdemo.visio;

import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.HighwayLayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.PositionUtil;
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.geographtools.GPSLocation;
import cz.cvut.fel.aic.geographtools.GraphSpec2D;

import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author fido
 */
@Singleton
public class BufferedHighwayLayer extends HighwayLayer {

    private static final int DEFAULT_SCALE = 5;

    BufferedImage cachedHighwayNetwork;

    private final int scale;

    private final GraphSpec2D mapSpecification;

    @Inject
    public BufferedHighwayLayer(HighwayNetwork highwayNetwork, PositionUtil positionUtil, GraphSpec2D mapSpecification) {
        super(highwayNetwork, positionUtil);
        scale = DEFAULT_SCALE;
        this.mapSpecification = mapSpecification;
    }

    @Override
    protected void paintGraph(Graphics2D canvas, Rectangle2D drawingRectangle) {
        if (cachedHighwayNetwork == null) {
            int imageWidth = positionUtil.getWorldWidth() / scale;
            int imageHeight = positionUtil.getWorldHeight() / scale;

            cachedHighwayNetwork = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_BINARY);

            Graphics2D newCanvas = cachedHighwayNetwork.createGraphics();

            // background
            newCanvas.setColor(Color.WHITE);
            newCanvas.fillRect(0, 0, imageWidth, imageHeight);

            // graph
            newCanvas.setColor(Color.BLACK);
            newCanvas.setStroke(new BasicStroke(8));

            for (SimulationEdge edge : graph.getAllEdges()) {
                Point2d from = getPositionOnImage(edge.fromId);
                Point2d to = getPositionOnImage(edge.toId);
                Line2D line2d = new Line2D.Double(from.x, from.y, to.x, to.y);
                newCanvas.draw(line2d);
            }
        }

        canvas.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        canvas.drawImage(cachedHighwayNetwork, Vis.transX(mapSpecification.minLon),
                Vis.transY(mapSpecification.maxLat), Vis.transW(cachedHighwayNetwork.getWidth() * scale),
                Vis.transH(cachedHighwayNetwork.getHeight() * scale), null);

    }

    private Point2d getPositionOnImage(int nodeId) {
        GPSLocation location = graph.getNode(nodeId);
        double x = (location.getLongitudeProjected() - mapSpecification.minLon) / scale;
        double y = (mapSpecification.maxLat - location.getLatitudeProjected()) / scale;

        return new Point2d(x, y);
    }

}
