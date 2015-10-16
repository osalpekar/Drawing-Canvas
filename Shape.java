import java.awt.*;
import java.io.*;

public abstract class Shape implements Serializable, Cloneable {
    public Shape(Point start, DrawingCanvas dcanvas) {
        isSelected = false;
        canvas = dcanvas;
        bounds = new Rectangle(start);
    }

    public abstract Shape clone();

    protected void setBoundingRect(Rectangle newBounds) {
        Rectangle oldBounds = bounds;
        bounds = newBounds;
        updateCanvas(oldBounds.union(bounds));
    }

    public void translate(int dx, int dy) {
        Rectangle newRect = new Rectangle(bounds);
        newRect.translate(dx, dy);
        setBoundingRect(newRect);
    }

    public void setSelected(boolean newState) {
        isSelected = newState;
        updateCanvas(bounds, true);
    }

    protected void updateCanvas(Rectangle areaOfChange) {
        updateCanvas(areaOfChange, isSelected);
    }

    protected void updateCanvas(Rectangle areaOfChange, boolean enlargeForKnobs) {
        Rectangle toRedraw = new Rectangle(areaOfChange);
        if (enlargeForKnobs) {
            toRedraw.grow(3, 3);
        }
        
        canvas.repaint(toRedraw);
    }

    public abstract void resize(Point point, Point point1);

    public void resizeStart(Point point)
    {
    }

    public void resizeEnd(Point point)
    {
    }

    public abstract void draw(Graphics g, Rectangle rectangle);

    protected void drawKnobs(Graphics g, Rectangle regionToDraw) {
        if (isSelected) {
            Rectangle knobs[] = getKnobRects();
            if (knobs != null) {
                for(int i = 0; i < knobs.length; i++) {
                    g.setColor(Color.BLACK);
                    g.fillRect(knobs[i].x, knobs[i].y, knobs[i].width, knobs[i].height);
                }
            }
        }
    }

    public boolean insideBoundingRect(Point pt) {
        return bounds.contains(pt);
    }

    public boolean inside(Point pt) {
        return insideBoundingRect(pt);
    }

    protected Rectangle[] getKnobRects() {
        Rectangle knobs[] = new Rectangle[4];
        knobs[0] = new Rectangle(bounds.x - 3, bounds.y - 3, 6, 6);
        knobs[1] = new Rectangle(bounds.x - 3, (bounds.y + bounds.height) - 3, 6, 6);
        knobs[2] = new Rectangle((bounds.x + bounds.width) - 3, (bounds.y + bounds.height) - 3, 6, 6);
        knobs[3] = new Rectangle((bounds.x + bounds.width) - 3, bounds.y - 3, 6, 6);
        return knobs;
    }

    protected int getKnobContainingPoint(Point pt) {
        if(!isSelected)
            return -1;
        Rectangle knobs[] = getKnobRects();
        for(int i = 0; i < knobs.length; i++)
            if(knobs[i].contains(pt))
                return i;

        return -1;
    }

    protected Point getAnchorForResize(Point mouseLocation) {
        int whichKnob = getKnobContainingPoint(mouseLocation);
        if(whichKnob == -1)
            return null;
        switch(whichKnob) {
        case 0: // '\0'
            return new Point(bounds.x + bounds.width, bounds.y + bounds.height);

        case 3: // '\003'
            return new Point(bounds.x, bounds.y + bounds.height);

        case 1: // '\001'
            return new Point(bounds.x + bounds.width, bounds.y);

        case 2: // '\002'
            return new Point(bounds.x, bounds.y);
        }
        return null;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public static final long serialVersionUID = 1L;
    public DrawingCanvas canvas;
    protected Rectangle bounds;
    protected Color color;
    protected boolean isSelected;
    protected static final int KNOB_SIZE = 6;
    protected static final int NONE = -1;
    protected static final int NW = 0;
    protected static final int SW = 1;
    protected static final int SE = 2;
    protected static final int NE = 3;
    protected static final int START = 0;
    protected static final int FINISH = 1;
}