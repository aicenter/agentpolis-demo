/* 
 * Copyright (C) 2017 Czech Technical University in Prague.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.apdemo.visio;

import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.HighwayLayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.elements.SimulationEdge;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.transportnetwork.networks.HighwayNetwork;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.VisioPositionUtil;
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
	public BufferedHighwayLayer(HighwayNetwork highwayNetwork, VisioPositionUtil positionUtil, GraphSpec2D mapSpecification) {
		super(highwayNetwork, positionUtil);
		scale = DEFAULT_SCALE;
		this.mapSpecification = mapSpecification;
	}

	@Override
	protected void paintGraph(Graphics2D canvas, Rectangle2D drawingRectangle) {
		if (cachedHighwayNetwork == null) {
			int imageWidth = mapSpecification.getWidth() / scale;
			int imageHeight = mapSpecification.getHeight() / scale;

			cachedHighwayNetwork = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_BINARY);

			Graphics2D newCanvas = cachedHighwayNetwork.createGraphics();

			// background
			newCanvas.setColor(Color.WHITE);
			newCanvas.fillRect(0, 0, imageWidth, imageHeight);

			// graph
			newCanvas.setColor(Color.BLACK);
			newCanvas.setStroke(new BasicStroke(8));

			for (SimulationEdge edge : graph.getAllEdges()) {
				Point2d from = getPositionOnImage(edge.fromNode.getId());
				Point2d to = getPositionOnImage(edge.toNode.getId());
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
