
package com.claymg;



import java.util.Iterator;
import java.util.ArrayList;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Container;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.awt.Polygon;

import java.awt.geom.Line2D;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;





/**
 *
 * @author woody
 */
public class RbItem extends JPanel 
                    implements MouseListener,
                               MouseMotionListener
{

    RbItem item = this;
    RbMainComponent view = null;
    ArrayList cornerList = new ArrayList();

    Color clrEdge = Color.magenta;

    boolean bButton_1_down = false;
    boolean bButton_1_dragged = false;
	Point ptScreen = new Point();

    private Color bkg = Color.white;


    public RbItem(RbMainComponent view) {

        super(null, true);

        this.view = view;

        addMouseListener(this);
        addMouseMotionListener(this);

		setOpaque(false);

    }//----------------------------------



    //////////////////////////////////////////////////////


    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {


		Object obj = e.getSource();
		Point loc = getLocation();

        //Point p = e.getPoint();
        //System.out.println("RbItem.mousePressed (" +p.x +", " +p.y +")");

        /*
		if (obj instanceof RbWall) {
            RbWall wall = (RbWall)obj;
			Point wallLoc = ((JPanel)obj).getLocation();
			if (e.getButton() == MouseEvent.BUTTON3) {
          		JPopupMenu menu = getPopupMenu(wall);
				menu.show(wall, wallLoc.x +e.getX(), wallLoc.y +e.getY());
          		return;
			}
		}
        */

        if (e.getButton() == MouseEvent.BUTTON1) {
			bButton_1_down = true;
	        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			ptScreen = e.getLocationOnScreen();
		}
		else if (e.getButton() == MouseEvent.BUTTON3) {
            RbPopupMenu menu = RbPopupMenu.getPopupMenu();
            menu.show(this, e.getX(), e.getY());
        }
    }//----------------------------------------------------

    @Override
    public void mouseReleased(MouseEvent e) {
        //System.out.println("RbItem.mouseReleased");
		if (e.getButton() == MouseEvent.BUTTON1) {
            bButton_1_down = false;

			if (bButton_1_dragged) {
				if (view != null && getParent() == view) {
					RbFloor floor = view.isHoveringOverFloor(item);
					if (floor != null)
						floor.addItem(item);
				}
			}

            bButton_1_dragged = false;
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//-------------------------------------------------------

    @Override
    public void mouseDragged(MouseEvent e) {

		if (bButton_1_down == true) {
            bButton_1_dragged = true;

			Point loc = getLocation();
			Point pscreen = e.getLocationOnScreen();

			int dx = pscreen.x -ptScreen.x;
			int dy = pscreen.y -ptScreen.y;

			Container parent = getParent();
            if (parent != null && parent instanceof RbFloor) {

				//System.out.println("mouseDragged ");
				//System.out.println("location (" +loc.x +", " +loc.y +")");

                RbFloor floor = (RbFloor)parent;

                //loc.x = Math.max(0, Math.min(loc.x +dx, parent.getWidth() -getWidth() -1));
                //loc.y = Math.max(0, Math.min(loc.y +dy, parent.getHeight() -getHeight() -1));

                if (floor.isValidLocation(dx, dy, item)) {
                    setLocation(loc.x +dx, loc.y +dy);
				}
				
				ptScreen = pscreen;
                return;
            }
            loc.x = Math.max(0, Math.min(loc.x +dx, parent.getWidth() -getWidth() -1));
            loc.y = Math.max(0, Math.min(loc.y +dy, parent.getHeight() -getHeight() -1));
            setLocation(loc.x, loc.y);

			ptScreen = pscreen;
		}
    }//-------------------------------------------






    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

		Color org = g.getColor();
		g.setColor(clrEdge);

        try {
            Point p0 = new Point(0, 0);
            Point p1 = new Point(0, 0);
            Point p2 = new Point(0, 0);

            Iterator itr = cornerList.iterator();
            if (itr.hasNext()) {

                p0 = (Point)itr.next();

                p1 = p0;
                while (itr.hasNext()) {
                    p2 = (Point)itr.next();
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                    p1 = p2;
                }

                if (!p2.equals(p0)) {
                    g.drawLine(p2.x, p2.y, p0.x, p0.y);
                }
            }
        }
        catch(Exception x) {}

		if (org != null)
			g.setColor(org);

    }//-----------------------------------------



    public boolean intersectsItem(Line2D itemEdges[]) {
        try {




        } catch(Exception x) {}

        return false;
    }//----------------------------------------------------




    
    public Point[] getCornersOffset (Point offset) {

        Point pt;
        Point p[] = new Point[cornerList.size()];
        Iterator itr = cornerList.iterator();
        int index = 0;
        while(itr.hasNext()) {
            pt = (Point)itr.next();
            p[index] = (Point)pt.clone();
			p[index].x += offset.x;
			p[index].y += offset.y;
			index++;
        }
        
        return p;

    }//-------------------------------------------
    

	
    public Polygon getPolygon() {
		return getPolygonOffset(new Point());
	}//-------------------------------	

		
	public Polygon getPolygonOffset(Point offset) {

        int len = cornerList.size();
        int x[] = new int[len];
        int y[] = new int[len];

        Iterator itr = cornerList.iterator();
        int index = 0;
        Point p;
        while(itr.hasNext()) {
            p = (Point)itr.next();

            x[index] = p.x +offset.x;
            y[index] = p.y +offset.y;

            index++;
        }

        return new Polygon(x, y, len);

    }//--------------------------------



    public Line2D[] getEdgesOffset(Point loc) {
        try {
            Point p0;
            Point p1;
            Point p2;

            Line2D edges[] = new Line2D[cornerList.size()];

            Iterator itr = cornerList.iterator();
            if (itr.hasNext()) {
                p0 = (Point)itr.next();
                p1 = p0;
                int index = 0;
                while (itr.hasNext()) {
                    p2 = (Point)itr.next();
                    edges[index] = new Line2D.Double((double)p1.x +(double)loc.x, (double)p1.y +loc.y,
													 (double)p2.x +(double)loc.x, (double)p2.y +loc.y);
                    index++;
                    p1 = p2;
                }
                edges[index] = new Line2D.Double((double)p1.x +(double)loc.x, (double)p1.y +loc.y,
												 (double)p0.x +(double)loc.x, (double)p0.y +loc.y);
            }

            return edges;
        } catch(Exception x) {}

        return new Line2D[0];

    }//----------------------------------------------







    public void addCorners(ArrayList list) {
        try {
            Point lt = Util.getLeftTop(list.iterator());
            Point rb = Util.getRightBottom(list.iterator());

            int len = list.size();
            int x[] = new int[len];
            int y[] = new int[len];
            Iterator itr = list.iterator();
            int index = 0;
            Point p = null;
            while (itr.hasNext()) {
                p = (Point)itr.next();

                p.x -= lt.x;
                p.y -= lt.y;

                x[index] = p.x;
                y[index] = p.y;
                cornerList.add(new Point(p.x, p.y));

                index++;
            }

            setBounds(lt.x, lt.y, rb.x -lt.x +1, rb.y -lt.y +1);
            setVisible(true);

            repaint();

        } catch(Exception x) {}
    }//---------------------------------


}//end class (RbItem)
