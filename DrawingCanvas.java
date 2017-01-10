/* 
 * credit: Julie Zelenski and Ian A. Mason
 * @see       javax.swing.JComponent
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DrawingCanvas extends JComponent
    implements Serializable
{
    protected class CanvasMouseHandler extends MouseAdapter
        implements MouseMotionListener
    {

        public void mousePressed(MouseEvent event)
        {
            Shape clicked = null;
            Point curPt = event.getPoint();
            if(toolbar.getCurrentTool() == 0)
            {
                if(selectedShape != null && (dragAnchor = selectedShape.getAnchorForResize(curPt)) != null)
                {
                    if((selectedShape instanceof Line) || (selectedShape instanceof Scribble))
                        lineScribbleEnd = selectedShape.getKnobContainingPoint(curPt);
                    dragStatus = 2;
                } else
                if((clicked = shapeContainingPoint(curPt)) != null)
                {
                    setSelectedShape(clicked);
                    dragStatus = 3;
                    dragAnchor = curPt;
                } else
                {
                    setSelectedShape(null);
                    dragStatus = 0;
                }
            } else
            {
                Shape newShape = null;
                switch(toolbar.getCurrentTool())
                {
                case 1: // '\001'
                    newShape = new Rect(curPt, DrawingCanvas.this);
                    break;

                case 2: // '\002'
                    newShape = new Oval(curPt, DrawingCanvas.this);
                    break;

                case 3: // '\003'
                    newShape = new Line(curPt, DrawingCanvas.this);
                    break;

                case 4: // '\004'
                    newShape = new Scribble(curPt, DrawingCanvas.this);
                    break;
                }
                if(newShape != null)
                {
                    newShape.setColor(toolbar.getCurrentColor());
                    allShapes.add(newShape);
                    setSelectedShape(newShape);
                    dragStatus = 1;
                    dragAnchor = curPt;
                    checkSize();
                }
            }
        }

        public void mouseDragged(MouseEvent event)
        {
            Point curPt = event.getPoint();
            switch(dragStatus)
            {
            default:
                break;

            case 3: // '\003'
                selectedShape.translate(curPt.x - dragAnchor.x, curPt.y - dragAnchor.y);
                dragAnchor = curPt;
                break;

            case 1: // '\001'
                if(selectedShape instanceof Scribble)
                {
                    selectedShape.resize(dragAnchor, curPt);
                    break;
                }
                // fall through

            case 2: // '\002'
                if((selectedShape instanceof Scribble) || (selectedShape instanceof Line))
                {
                    if(lineScribbleEnd == 0)
                    {
                        selectedShape.resizeStart(curPt);
                        break;
                    }
                    if(lineScribbleEnd == 1)
                        selectedShape.resizeEnd(curPt);
                } else
                {
                    selectedShape.resize(dragAnchor, curPt);
                }
                break;
            }
            checkSize();
            toolbar.setMouseAt(event.getPoint());
        }

        public void mouseMoved(MouseEvent e)
        {
            toolbar.setMouseAt(e.getPoint());
        }

        public void mouseReleased(MouseEvent e)
        {
            dragStatus = 0;
        }

        private Point dragAnchor;
        private int dragStatus;
        private int lineScribbleEnd;

        protected CanvasMouseHandler()
        {
        	super();
        }
    }


    public void cut()
    {
        if(selectedShape != null)
        {
            clipboard.storeObject(selectedShape);
            allShapes.remove(selectedShape);
            checkSize();
            repaint();
        }
    }

    public void copy()
    {
        if(selectedShape != null)
            clipboard.storeObject(selectedShape.clone());
    }

    public void paste()
    {
        if(clipboard.retrieveObject() != null)
        {
            allShapes.add(((Shape)clipboard.retrieveObject()).clone());
            checkSize();
            repaint();
        }
    }

    public void delete()
    {
        if(selectedShape != null)
        {
            allShapes.remove(selectedShape);
            checkSize();
            repaint();
        }
    }

    public void clearAll()
    {
        allShapes.removeAllElements();
        checkSize();
        repaint();
    }

    public void loadFile()
    {
        SimpleObjectReader reader = SimpleObjectReader.openFileForReading(filenameChosenByUser(true));
        DrawingCanvas storedCanvas = (DrawingCanvas)reader.readObject();
        allShapes = storedCanvas.allShapes;
        reader.close();
        setSelectedShape(null);
        repaint();
    }

    public void saveToFile()
    {
        SimpleObjectWriter writer = SimpleObjectWriter.openFileForWriting((new StringBuilder(String.valueOf(filenameChosenByUser(false)))).append(".jd").toString());
        writer.writeObject(this);
        writer.close();
    }

    public void exportPNG()
    {
        String file = (new StringBuilder(String.valueOf(filenameChosenByUser(false)))).append(".png").toString();
        if(file != null)
            try
            {
                ImageIO.write(drawToImage(), "png", new File(file));
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
    }

    public void bringToFront()
    {
        if(selectedShape != null)
        {
            Shape obj = selectedShape;
            allShapes.remove(selectedShape);
            allShapes.add(obj);
            repaint();
        }
    }

    public void sendToBack()
    {
        if(selectedShape != null)
        {
            Shape obj = selectedShape;
            allShapes.remove(selectedShape);
            allShapes.add(0, obj);
            repaint();
        }
    }

    private void checkSize()
    {
        int maxWidth = 0;
        int maxHeight = 0;
        for(int i = 0; i < allShapes.size(); i++)
        {
            Rectangle bbox = ((Shape)allShapes.get(i)).getBounds();
            if(maxWidth < bbox.x + bbox.width)
                maxWidth = bbox.x + bbox.width;
            if(maxHeight < bbox.y + bbox.height)
                maxHeight = bbox.y + bbox.height;
        }

        maxWidth++;
        maxHeight++;
        setPreferredSize(new Dimension(maxWidth, maxHeight));
        setSize(new Dimension(maxWidth, maxHeight));
    }

    public DrawingCanvas(Toolbar tb, int width, int height)
    {
        clipboard = new clipboard();
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.white);
        toolbar = tb;
        toolbar.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e)
            {
                if(selectedShape != null)
                {
                    selectedShape.setColor(toolbar.getCurrentColor());
                    repaint();
                }
            }
        }
);
        allShapes = new Vector();
        selectedShape = null;
        CanvasMouseHandler handler = new CanvasMouseHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
    }

    public void paintComponent(Graphics g)
    {
        Rectangle regionToRedraw = g.getClipBounds();
        g.setColor(getBackground());
        g.fillRect(regionToRedraw.x, regionToRedraw.y, regionToRedraw.width, regionToRedraw.height);
        for(Iterator iter = allShapes.iterator(); iter.hasNext(); ((Shape)iter.next()).draw(g, regionToRedraw));
    }

    public BufferedImage drawToImage()
    {
        BufferedImage image = new BufferedImage(getSize().width, getSize().height, 5);
        Graphics x = image.getGraphics();
        x.setColor(getBackground());
        x.fillRect(0, 0, getSize().width, getSize().height);
        for(Iterator iter = allShapes.iterator(); iter.hasNext();)
        {
            Shape shape = (Shape)iter.next();
            if(shape.isSelected)
            {
                shape.setSelected(false);
                shape.draw(x, getBounds());
                shape.setSelected(true);
            } else
            {
                shape.draw(x, getBounds());
            }
        }

        return image;
    }

    protected void setSelectedShape(Shape shapeToSelect)
    {
        if(selectedShape != shapeToSelect)
        {
            if(selectedShape != null)
                selectedShape.setSelected(false);
            selectedShape = shapeToSelect;
            if(selectedShape != null)
            {
                shapeToSelect.setSelected(true);
                toolbar.setCurrentColor(selectedShape.color);
            }
        }
    }

    protected Shape shapeContainingPoint(Point pt)
    {
        for(int i = allShapes.size() - 1; i >= 0; i--)
        {
            Shape r = (Shape)allShapes.elementAt(i);
            if(r.inside(pt))
                return r;
        }

        return null;
    }

    protected String filenameChosenByUser(boolean forOpen)
    {
        JFileChooser fc = new JFileChooser((new StringBuilder(String.valueOf(System.getProperty("user.dir")))).append(File.separator).append("Documents").toString());
        int result = forOpen ? fc.showOpenDialog(this) : fc.showSaveDialog(this);
        File chosenFile = fc.getSelectedFile();
        if(result == 0 && chosenFile != null)
            return chosenFile.getPath();
        else
            return null;
    }

    static final long serialVersionUID = 1L;
    static final int DRAG_NONE = 0;
    static final int DRAG_CREATE = 1;
    static final int DRAG_RESIZE = 2;
    static final int DRAG_MOVE = 3;
    protected Vector allShapes;
    protected transient Shape selectedShape;
    protected transient Toolbar toolbar;
    private transient clipboard clipboard;

}
