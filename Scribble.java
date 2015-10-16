import java.awt.*;
import java.io.Serializable;
import java.util.Vector;

public class Scribble extends Shape implements Serializable, Cloneable {

    Point smallest;
    Point largest;
    Vector lines;
    public static final long serialVersionUID = 1L;
    
    public Scribble(Point start, DrawingCanvas dcanvas) {
        super(start, dcanvas);
        smallest = null;
        largest = null;
        lines = new Vector();
        smallest = new Point(start.x, start.y);
        largest = new Point(start.x, start.y);
    }

    public Scribble clone() {
        Scribble scribble = new Scribble(lines.size() <= 0 ? new Point(0, 0) : new Point(((Line)lines.get(0)).getStart()), canvas);
        scribble.setBoundingRect(new Rectangle(bounds));
        scribble.setColor(color);
        scribble.smallest = smallest;
        scribble.largest = largest;
        scribble.lines = new Vector();
        for(int i = 0; i < lines.size(); i++)
            scribble.lines.add(((Line)lines.get(i)).clone());
        return scribble;
    }

    public void resize(Point anchor, Point end) {
        Point start;
        if(lines.size() > 0)
            start = ((Line)lines.get(lines.size() - 1)).getFinish();
        else
            start = anchor;
        lines.add(new Line(start, end, canvas));
        recheckBoundingBox(new Point[] {
            start, end
        });
    }

    private Rectangle rectFromSmallLarge() {
        return new Rectangle(smallest.x, smallest.y, largest.x - smallest.x, largest.y - smallest.y);
    }

    private void recheckBoundingBox(Point points[]) {
        Point apoint[];
        int j = (apoint = points).length;
        for(int i = 0; i < j; i++) {
            Point start = apoint[i];
            if(start.x < smallest.x)
                smallest.x = start.x;
            if(start.y < smallest.y)
                smallest.y = start.y;
            if(start.x > largest.x)
                largest.x = start.x;
            if(start.y > largest.y)
                largest.y = start.y;
        }
        setBoundingRect(rectFromSmallLarge());
    }

    public void resizeStart(Point newPoint) {
        if(lines.size() > 0)
        {
            ((Line)lines.get(0)).setStart(newPoint);
            recheckBoundingBox(new Point[] {
                newPoint
            });
        }
    }

    public void resizeEnd(Point newPoint) {
        if(lines.size() > 0) {
            ((Line)lines.get(lines.size() - 1)).setFinish(newPoint);
            recheckBoundingBox(new Point[] {
                newPoint
            });
        }
    }

    public void translate(int dx, int dy) {
        for(int i = 0; i < lines.size(); i++)  {
            Line line = (Line)lines.get(i);
            line.setStart(new Point(line.getStart().x + dx, line.getStart().y + dy));
            line.setFinish(new Point(line.getFinish().x + dx, line.getFinish().y + dy));
        }
        super.translate(dx, dy);
    }

    public boolean inside(Point pt) {
        if(!super.inside(pt))
            return false;
        for(int i = 0; i < lines.size(); i++) {
            Line line = (Line)lines.get(i);
            if(line.inside(pt))
                return true;
        }
        return false;
    }

    public void draw(Graphics g, Rectangle regionToDraw) {
        for(int i = 0; i < lines.size(); i++) {
            g.setColor(color);
            ((Line)lines.get(i)).draw(g, regionToDraw);
        }
        drawKnobs(g, regionToDraw);
    }

    public Rectangle[] getKnobRects() {
        Rectangle knobs
        [] = new Rectangle[2];
        if(lines.size() > 0) {
            Line lineFirst = (Line)lines.get(0);
            Line lineLast = (Line)lines.get(lines.size() - 1);
            knobs[0] = new Rectangle(lineFirst.getStart().x - 3, lineFirst.getStart().y - 3, 6, 6);
            knobs[1] = new Rectangle(lineLast.getFinish().x - 3, lineLast.getFinish().y - 3, 6, 6);
            return knobs;
        } else {
            return null;
        }
    }
}