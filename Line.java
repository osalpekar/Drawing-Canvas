import java.awt.*;
import java.io.Serializable;

public class Line extends Shape
    implements Serializable, Cloneable {

    public Line(Point start, DrawingCanvas dcanvas) {
        super(start, dcanvas);
        this.start = start;
        finish = start;
        setBoundingRect(getCurrentRect());
    }

    public Line(Point start, Point end, DrawingCanvas dcanvas) {
        super(start, dcanvas);
        this.start = start;
        finish = end;
        setBoundingRect(getCurrentRect());
    }

    public Line clone() {
        Line line = new Line(new Point(bounds.x, bounds.y), canvas);
        line.start = start;
        line.finish = finish;
        line.setColor(color);
        line.setBoundingRect(bounds);
        return line;
    }

    public Rectangle getCurrentRect() {
        Point rectStart = start;
        Point rectEnd = finish;
        if(finish.x < start.x || finish.y < start.y) {
            Point tmp = rectStart;
            rectStart = rectEnd;
            rectEnd = tmp;
        }
        int lowestX = rectStart.x >= rectEnd.x ? rectEnd.x : rectStart.x;
        int lowestY = rectStart.y >= rectEnd.y ? rectEnd.y : rectStart.y;
        int highestX = rectStart.x <= rectEnd.x ? rectEnd.x : rectStart.x;
        int highestY = rectStart.y <= rectEnd.y ? rectEnd.y : rectStart.y;
        int width = highestX - lowestX;
        int height = highestY - lowestY;
        return new Rectangle(lowestX, lowestY, width, height);
    }

    public void resize(Point anchor, Point end) {
        if(isSelected)
            finish = end;
        setBoundingRect(getCurrentRect());
    }

    public void resizeStart(Point start) {
        if(isSelected)
            this.start = start;
        setBoundingRect(getCurrentRect());
    }

    public void resizeEnd(Point end) {
        resize(null, end);
    }

    public void translate(int dx, int dy) {
        start = new Point(start.x + dx, start.y + dy);
        finish = new Point(finish.x + dx, finish.y + dy);
        super.translate(dx, dy);
    }

    public boolean inside(Point pt) {
        Rectangle boundingBox = new Rectangle(bounds);
        Point begin = new Point(start);
        Point end = new Point(finish);
        int dx = end.x - begin.x;
        int dy = end.y - begin.y;
        if(Math.abs(dx) >= Math.abs(dy)) {
            float slope = (float)dy / (float)dx;
            double intercept = (float)end.y - (float)end.x * slope;
            for(int i = -10; i <= 10; i++)
                if((long)pt.y == Math.round((double)(slope * (float)pt.x) + intercept + (double)i) && pt.x >= boundingBox.x && pt.x <= boundingBox.x + boundingBox.width)
                    return true;

        }
        if(Math.abs(dx) < Math.abs(dy)) {
            float slope = (float)dx / (float)dy;
            double intercept = (float)end.x - (float)end.y * slope;
            for(int j = -10; j <= 10; j++)
                if((long)pt.x == Math.round((double)(slope * (float)pt.y) + intercept + (double)j) && pt.y >= boundingBox.y && pt.y <= boundingBox.y + boundingBox.height)
                    return true;

        }
        return false;
    }

    public void draw(Graphics g, Rectangle regionToDraw) {
        g.setColor(color);
        g.drawLine(start.x, start.y, finish.x, finish.y);
        drawKnobs(g, regionToDraw);
    }

    public Rectangle[] getKnobRects() {
        Rectangle knobs[] = new Rectangle[2];
        knobs[0] = new Rectangle(start.x - 3, start.y - 3, 6, 6);
        knobs[1] = new Rectangle(finish.x - 3, finish.y - 3, 6, 6);
        return knobs;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point pt) {
        start = pt;
        setBoundingRect(getCurrentRect());
    }

    public Point getFinish() {
        return finish;
    }

    public void setFinish(Point pt) {
        finish = pt;
        setBoundingRect(getCurrentRect());
    }

    public static final long serialVersionUID = 1L;
    private final int tolerance = 10;
    private Point start;
    private Point finish;
}