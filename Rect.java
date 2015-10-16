import java.awt.*;
import java.io.Serializable;

public class Rect extends Shape
    implements Serializable, Cloneable {

    public Rect(Point start, DrawingCanvas dcanvas) {
        super(start, dcanvas);
    }

    public Rect clone() {
        Rect rect = new Rect(new Point(bounds.x, bounds.y), canvas);
        rect.setBoundingRect(new Rectangle(bounds));
        rect.setColor(color);
        return rect;
    }

    public void resize(Point anchor, Point end) {
        Rectangle newRect = new Rectangle(anchor);
        newRect.add(end);
        setBoundingRect(newRect);
    }

    public void draw(Graphics g, Rectangle regionToDraw) {
        if(!bounds.intersects(regionToDraw)) {
            return;
        } else {
            g.setColor(color);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            drawKnobs(g, regionToDraw);
            return;
        }
    }
    public static final long serialVersionUID = 1L;
}