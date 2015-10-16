/**
 *-------------------------------------------------------------- 80 columns ---|
 * The Toolbar class sets up the buttons along the bottom of the window
 * that allow the user to choose the current drawing tool as well as the
 * the button that shows the current color and brings up the color chooser
 * to pick a new color.  The code that's here should do pretty much all
 * you need as is. Read it over to get a sense of what it takes to put
 * together and manage some simple UI. You are welcome to futz with this
 * code if you want to make some changes, but it shouldn't be necessary.
 *
 * @version      1.2 9/24/10
 * @author       Julie Zelenski
 * @author      (touched up by Ian A. Mason)
 * @see          javax.swing.JPanel
 * @see          javax.swing.JToggleButton
 * @see          javax.swing.JButton
 */
 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Toolbar extends JPanel
{

    public int getCurrentTool()
    {
        for(int i = 0; i < toolButtons.length; i++)
            if(toolButtons[i].isSelected())
                return i;

        return -1;
    }

    public void setCurrentTool(int toolNum)
    {
        toolButtons[toolNum].setSelected(true);
    }

    public Color getCurrentColor()
    {
        return colorButton.getBackground();
    }

    public void setCurrentColor(Color color)
    {
        colorButton.setBackground(color);
        fireStateChanged();
    }

    public void addChangeListener(ChangeListener l)
    {
        listeners.addElement(l);
    }

    public void removeChangeListener(ChangeListener l)
    {
        listeners.removeElement(l);
    }

    protected void fireStateChanged()
    {
        ChangeEvent changeEvent = new ChangeEvent(this);
        for(int i = 0; i < listeners.size(); i++)
            ((ChangeListener)listeners.elementAt(i)).stateChanged(changeEvent);

    }

    public void setMouseAt(Point pt)
    {
        mouseAt.setText((new StringBuilder("   Mouse at: ")).append(pt.x).append(", ").append(pt.y).append("   ").toString());
    }

    public Toolbar()
    {
        mouseAt = new JLabel("   Mouse at:   ");
        setLayout(new FlowLayout(1, 0, 3));
        Insets noMargin = new Insets(0, 0, 0, 0);
        add(mouseAt);
        ButtonGroup group = new ButtonGroup();
        toolButtons = new JToggleButton[toolNames.length];
        for(int i = 0; i < toolNames.length; i++)
        {
            toolButtons[i] = new JToggleButton(new ImageIcon((new StringBuilder("Images")).append(File.separator).append(toolNames[i]).append(".gif").toString()), i == 0);
            group.add(toolButtons[i]);
            add(toolButtons[i]);
            toolButtons[i].setMargin(noMargin);
        }

        add(new JLabel("   Fill color   "));
        colorButton = new JButton(new ImageIcon((new StringBuilder("Images")).append(File.separator).append("Bucket.gif").toString()));
        colorButton.setBackground(Color.darkGray);
        colorButton.setMargin(noMargin);
        add(colorButton);
        colorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                Color chosenColor;
                if((chosenColor = JColorChooser.showDialog(null, "Choose shape fill color", getCurrentColor())) != null)
                    setCurrentColor(chosenColor);
            }
        }
);
        listeners = new Vector();
    }

    static final long serialVersionUID = 1L;
    public static final int SELECT = 0;
    public static final int RECT = 1;
    public static final int OVAL = 2;
    public static final int LINE = 3;
    public static final int SCRIBBLE = 4;
    protected JToggleButton toolButtons[];
    protected JButton colorButton;
    protected Vector listeners;
    protected static final String toolNames[] = {
        "Select", "Rect", "Oval", "Line", "Scribble"
    };
    private JLabel mouseAt;

}