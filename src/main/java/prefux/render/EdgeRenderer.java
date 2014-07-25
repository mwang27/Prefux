package prefux.render;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.shape.Line;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prefux.data.util.Point2D;
import prefux.visual.EdgeItem;
import prefux.visual.VisualItem;

/**
 * <p>
 * Renderer that draws edges as lines connecting nodes. Both straight and curved
 * lines are supported. Curved lines are drawn using cubic Bezier curves.
 * Subclasses can override the
 * {@link #getCurveControlPoints(EdgeItem, Point2D[], double, double, double, double)}
 * method to provide custom control point assignment for such curves.
 * </p>
 *
 * <p>
 * This class also supports arrows for directed edges. See the
 * {@link #setArrowType(int)} method for more.
 * </p>
 *
 * @version 1.0
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class EdgeRenderer extends AbstractShapeRenderer implements Renderer {

	private static final Logger log = LoggerFactory
	        .getLogger(EdgeRenderer.class);

	public static final String DEFAULT_STYLE_CLASS = "prefux-edge";

	@Override
	public boolean locatePoint(Point2D p, VisualItem item) {
		log.debug("locatePoint " + p + " " + item);
		return false;
	}

	@Override
	protected Node getRawShape(VisualItem item, boolean bind) {
		EdgeItem edge = (EdgeItem) item;
		Line line = new Line();
		if (bind) {
			Platform.runLater(() -> {
				line.startXProperty().bind(edge.getSourceItem().xProperty());
				line.startYProperty().bind(edge.getSourceItem().yProperty());
				line.endXProperty().bind(edge.getTargetItem().xProperty());
				line.endYProperty().bind(edge.getTargetItem().yProperty());
			});
		}
		return line;
	}

	@Override
	public String getDefaultStyle() {
		return DEFAULT_STYLE_CLASS;
	}

} // end of class EdgeRenderer
