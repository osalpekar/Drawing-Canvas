/**
 *-------------------------------------------------------------- 80 columns ---|
 * This is the main class for the application. It is built along the same
 * lines as the Editor class of project 1. It has a constructor, two instance
 * methods, and a static main()  that kicks the whole thing off.
 * The two instance methods are a menu creation method, and a menuItems 
 * initialisation method.  The constructor instantiates the window 
 * and sets up its internals by creating and installing the drawing canvas, 
 * toolbar, and menus. All of the code works correctly as is and should 
 * require no changes. 
 *
 * @version      1.2 9/24/10
 * @author       Julie Zelenski 
 * @author       Restructured by Ian A. Mason
 * @see       javax.swing.JFrame
 * @see       javax.swing.JMenuBar
 * @see       javax.swing.JMenuItem
 * @see       javax.swing.JMenu
 */

import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class JavaDraw extends JFrame {
    private JavaDraw() {
        super("JavaDraw!");
        canvas = new DrawingCanvas(toolbar, 350, 350);
        scrollArea = new JScrollPane(canvas, 20, 30);
        editMenuMasks = (new int[] {
            menuMask, menuMask, menuMask, 0
        });
        getContentPane().add(toolbar, "South");
        getContentPane().add(scrollArea, "Center");
        createMenus();
        setJMenuBar(mb);
        setSize(600, 600);
        setExtendedState(6);
        setVisible(true);
        setDefaultCloseOperation(3);
    }

    public static void main(String args[]) {
        new JavaDraw();
    }

    private void initMenus(JMenu m, String miLabel, int keyCode, int menuMask, ActionListener al) {
        JMenuItem mi = new JMenuItem(miLabel);
        m.add(mi);
        mi.addActionListener(al);
        mi.setAccelerator(KeyStroke.getKeyStroke(keyCode, menuMask));
    }

    private void createMenus() {
        JMenu m = new JMenu("File");
        for(int i = 0; i < fileMenuItems.length; i++)
            initMenus(m, fileMenuItems[i], fileKeyCodes[i], menuMask, fileActionListeners[i]);

        mb.add(m);
        m = new JMenu("Edit");
        for(int i = 0; i < editMenuItems.length; i++)
            initMenus(m, editMenuItems[i], editKeyCodes[i], editMenuMasks[i], editActionListeners[i]);

        mb.add(m);
        m = new JMenu("Layering");
        for(int i = 0; i < layeringMenuItems.length; i++)
            initMenus(m, layeringMenuItems[i], layeringKeyCodes[i], menuMask, layeringActionListeners[i]);

        mb.add(m);
    }

    static final long serialVersionUID = 1L;
    final Toolbar toolbar = new Toolbar();
    final DrawingCanvas canvas;
    final JScrollPane scrollArea;
    final int menuMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    final JMenuBar mb = new JMenuBar();
    final String fileMenuItems[] = {
        "Clear all", "Load file", "Save to file", "Export as PNG", "Quit"
    };
    final int fileKeyCodes[] = {
        78, 79, 83, 71, 81
    };
    final ActionListener fileActionListeners[] = {
        new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                canvas.clearAll();
            }
        }
, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                canvas.loadFile();
            }
        }
, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                canvas.saveToFile();
            }
        }
, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                canvas.exportPNG();
            }
        }
, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }

    };
    final String editMenuItems[] = {
        "Cut", "Copy", "Paste", "Delete"
    };
    final int editKeyCodes[] = {
        88, 67, 86, 8
    };
    final int editMenuMasks[];
    final ActionListener editActionListeners[] = {
        new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                canvas.cut();
            }
        }
, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                canvas.copy();
            }
        }
, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                canvas.paste();
            }
        }
, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                canvas.delete();
            }
        }

    };
    final String layeringMenuItems[] = {
        "Bring to front", "Send to back"
    };
    final int layeringKeyCodes[] = {
        70, 66
    };
    final ActionListener layeringActionListeners[] = {
        new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                canvas.bringToFront();
            }
        }
, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                canvas.sendToBack();
            }
        }

    };
}

