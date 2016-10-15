/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.claymg;

import java.util.Iterator;
import java.util.ArrayList;



import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;



/**
 *
 * @author woody
 */
public class RbMainComponent extends JPanel
                           implements ComponentListener,
                                      MouseListener,
                                      MouseMotionListener
{
    static RbMainComponent viewInstance = null;
									  
    JPanel mainPanel;
	RbMainComponent mainComponent = this;

    private Color bkg = Color.white;
    Color fg = Color.black;


    public ArrayList floorList = new ArrayList();
    public ArrayList locatorList = new ArrayList();
    public ArrayList pointlist = new ArrayList();

    RbLocator locator = null;

    boolean bButton_1_down = false;
    boolean bButton_1_dragged = false;

    double mainScale = 4.0;



    public RbMainComponent(JPanel panel) {
        super(null, true);

		this.mainPanel = panel;

        locator= new RbLocator(this);
        locator.setVisible(false);

        setBounds(mainPanel.getBounds());
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(bkg);
        mainPanel.addComponentListener(this);
        //addComponentListener(this);

		floorList.clear();
		
        add(locator);

        setVisible(true);

		Toolkit.getDefaultToolkit().getScreenSize();
		mainComponent.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

    }//-------------------------


    public Color getFG() {


        return fg;
    }



	public static JPanel getRbInstance(JPanel parent) {

		if (parent != null) {
			if (viewInstance == null || viewInstance.getParent() != parent)
				viewInstance = new RbMainComponent(parent);

		}
		return viewInstance;
	}//---------------------------------------------------




    public void clearLocatorList() {
        try {
            RbLocator loc;
            Iterator itr = locatorList.iterator();
            while (itr.hasNext()) {
                loc = (RbLocator)itr.next();
                loc.setVisible(false);
				loc.setAsListed(false);
                remove(loc);
            }
            locatorList.clear();
        } catch(Exception x) {}
    }//----------------------------------



    public Iterator getFloorListItr() {
        return floorList.iterator();
    }//------------------------------------------


	public void addToLocatorList(RbLocator loc) {
		try {
			if (loc != null) {
				locatorList.add(loc);
				loc.setAsListed(true);
				loc.repaint();
			}
		} catch(Exception x) {}
	}//-------------------------------------------


	public void removeFromLocatorList(RbLocator loc) {
		try {
			if (loc != null) {
				if (locatorList.contains(loc)) {
					locatorList.remove((Object)loc);
					loc.setVisible(false);
					loc.setAsListed(false);
					remove(loc);
					loc.repaint();
					loc = null;
				}
			}
		} catch(Exception x) {}
	}//-------------------------------------------




    ////////////////////////////////////////////////////
    // Mouse Listener routines
    //

    public void mouseMoved(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}


    public void mousePressed(MouseEvent e) {
        //System.out.println("mainPanel.mousePressed " +e.getButton() +"--------------------");
        
        int btn = e.getButton();
        int clickCount = e.getClickCount();

        if (btn == MouseEvent.BUTTON1)  {
            if (clickCount == 1) {

                if (locator != null) {
                    clearLocatorList();
                    locator.unsetLocator();
                }

                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

                locator.setAnchor(e.getPoint());
        
                return;
            }
        }
        else if (btn == MouseEvent.BUTTON3){

            if (bButton_1_dragged) {
                if (locator != null && locator.isLocatorSet()) {

                    if (locator.getType() == RbLocator.LINE_TYPE) {
                        locator.setLocator(e.getPoint());
                        RbLocator loc = locator;

						addToLocatorList(loc);
                        //locatorList.add(loc);

                        locator = new RbLocator(this);
                        locator.setType(loc.type);

                        locator.test(loc);

                        locator.setAnchor(e.getPoint());
                    }
                    else {
                        locatorList.clear();
                        locator.setLocator(e.getPoint());
                        RbLocator loc = locator;

						addToLocatorList(loc);
                        //locatorList.add(loc);

                        locator = new RbLocator(this);
                        locator.setType(loc.type);
                        locator.setAnchor(e.getPoint());
                    }
                }


                return;
            }
            else {
                JPopupMenu menu = RbPopupMenu.getPopupMenu();
                menu.show(mainComponent, e.getX(), e.getY());
            }
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }//-----------------------------------------




    public void mouseReleased(MouseEvent e) {

        //System.out.println("mouseReleased " +e.getButton() +"--------------");

        int btn = e.getButton();

        if (btn == MouseEvent.BUTTON1) {
            bButton_1_down = false;
            bButton_1_dragged = false;
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        //repaint();
    }//----------------------------------------




    public void mouseDragged(MouseEvent e) {

        //System.out.println("mouseDragged  -----------------");//(" +e.getPoint().x +", " +e.getPoint().y +")");

        //int btn = e.getButton();
        //System.out.println("MouseEvent.BUTTON1 " +MouseEvent.BUTTON1);
        //System.out.println("MouseEvent.e.getButton() " +e.getButton());

        if (bButton_1_down = true) {
            bButton_1_dragged = true;

            if (locator.getType() == RbLocator.LINE_TYPE) {
                locator.setLocator(e.getPoint());
            }
            else if (locator.getType() == RbLocator.RECT_TYPE) {
                clearLocatorList();
                locator.setLocator(e.getPoint());
            }
        }

    }//----------------------------------------







    public void componentResized(ComponentEvent e) {

        //System.out.println("componentResized [" +e.getSource() +"]");

        Object src = e.getSource();

        if (src == mainPanel) {


			/*
			mainComponent.setPreferredSize(
					new Dimension(
						Math.max(mainPanel.getWidth(), mainComponent.getWidth()),
						Math.max(mainPanel.getHeight(), mainComponent.getHeight())));

			*/



			//setBounds(mainPanel.getBounds());

		}
    }//----------------------------------------------------


    @Override public void componentMoved(ComponentEvent e) {}
    @Override public void componentShown(ComponentEvent e) {}
    @Override public void componentHidden(ComponentEvent e) {}


    ////////////////////////////////////////////////////////////




    public boolean createItem() {
        try {
            if (locator.getType() == RbLocator.LINE_TYPE) {

                Iterator itr = locatorList.iterator();
                RbLocator loc = null;
                if (itr.hasNext()) {

                    pointlist.clear();
                    while (itr.hasNext()) {
                        loc = (RbLocator)itr.next();
                        pointlist.add(loc.getAnchor());
                    }
                    if (loc != null)
                        pointlist.add(loc.getExtent());

					if (!Util.isSimplePolygon(pointlist))
						return false;

                    clearLocatorList();
                    locator.unsetLocator();

                    RbItem item = new RbItem(mainComponent);

					item.addCorners(pointlist);
                    mainComponent.add(item);
                    item.repaint();
                    pointlist.clear();

                    return true;
                }
            }
            else if (locator.getType() == RbLocator.RECT_TYPE) {
                Iterator itr = locatorList.iterator();
                RbLocator loc = null;
                if (itr.hasNext()) {
                    loc = (RbLocator)itr.next();
                    clearLocatorList();

                    Point anchor = loc.getAnchor();
                    Point extent = loc.getExtent();

                    Point p0 = new Point(Math.min(anchor.x, extent.x), Math.min(anchor.y, extent.y));
                    Point p1 = new Point(Math.min(anchor.x, extent.x), Math.max(anchor.y, extent.y));
                    Point p2 = new Point(Math.max(anchor.x, extent.x), Math.max(anchor.y, extent.y));
                    Point p3 = new Point(Math.max(anchor.x, extent.x), Math.min(anchor.y, extent.y));
                    pointlist.clear();
                    pointlist.add(p0);
                    pointlist.add(p1);
                    pointlist.add(p2);
                    pointlist.add(p3);

					if (!Util.isSimplePolygon(pointlist))
						return false;

                    RbItem item = new RbItem(mainComponent);
                    item.addCorners(pointlist);

                    mainComponent.add(item);
                    item.repaint();
                    pointlist.clear();

                    return true;
                }
            }
        } catch(Exception x) {}

        return false;

    }//---------------------------------------------


/*
    public void deleteItem(RbItem item) {
        try {
            if (item != null) {

				item.setVisible(false);

                Container cnt = item.getParent();
                if (cnt != null) {
                    cnt.remove(item);
                }

                item = null;
            }
        } catch(Exception x) {}
    }//---------------------------------------------
*/

/*
    
    public void addItemToRoom(RbItem item) {
        try {
            if (item != null) {

                Iterator itr = floorList.iterator();
                if (itr.hasNext()) {

                    RbFloor floor = (RbFloor)itr.next();
                    if (floor != null) {

                        Container cnt = item.getParent();
                        if (cnt != null) {

                            if (item.getWidth() < floor.getWidth() && item.getHeight() < floor.getHeight()) {

                                cnt.remove(item);
                                floor.add(item);

                                Point p = new Point(0, floor.getHeight()/2);
                                while (p.x < floor.getWidth() -1) {
                                    if (floor.isHovering(item)) {
                                        item.setLocation(p);
                                        break;
                                    }
                                    p.x++;
                                }

                                MouseListener ml[];
                                MouseMotionListener mm[];
                                RbWall wall;
                                Iterator itr2 = floor.getExternalWallItr();
                                while (itr2.hasNext()) {
                                    
                                    wall = (RbWall)itr2.next();

                                    ml = wall.getMouseListeners();
                                    for (int index = 0; index < ml.length; index++)
                                        wall.removeMouseListener(ml[index]);

                                    mm = wall.getMouseMotionListeners();
                                    for (int index = 0; index < ml.length; index++)
                                        wall.removeMouseMotionListener(mm[index]);

									



                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception x) {}
    }//---------------------------------------------
  */


	public RbFloor isHoveringOverFloor(RbItem item) {
		try {
            RbFloor floor;
            Iterator floorItr = floorList.iterator();
			while (floorItr.hasNext()) {

                floor = (RbFloor)floorItr.next();

                if (floor.isHovering(item))
                    return floor;
			}
		} catch(Exception x) {}

		return (RbFloor)null;

	}//------------------------------------------------------







    public boolean createFloor() {
        try {
            if (locator.getType() == RbLocator.LINE_TYPE) {

                Iterator itr = locatorList.iterator();
                RbLocator loc = null;
                if (itr.hasNext()) {

                    pointlist.clear();
                    while (itr.hasNext()) {
                        loc = (RbLocator)itr.next();
                        pointlist.add(loc.getAnchor());
                    }

					if (loc != null)
                        pointlist.add(loc.getExtent());

                    clearLocatorList();
                    locator.unsetLocator();

					if (!Util.isSimplePolygon(pointlist))
						return false;

                    RbFloor floor = new RbFloor(mainComponent);

					floor.addCorners(pointlist);
                    floor.addExteriorWalls();
                    floorList.add(floor);
                    mainComponent.add(floor);
                    floor.repaint();
                    pointlist.clear();

                    return true;
                }
            }
            else if (locator.getType() == RbLocator.RECT_TYPE) {
                Iterator itr = locatorList.iterator();
                RbLocator loc = null;
                if (itr.hasNext()) {
                    loc = (RbLocator)itr.next();
                    clearLocatorList();

                    Point anchor = loc.getAnchor();
                    Point extent = loc.getExtent();

                    Point p0 = new Point(Math.min(anchor.x, extent.x), Math.min(anchor.y, extent.y));
                    Point p1 = new Point(Math.min(anchor.x, extent.x), Math.max(anchor.y, extent.y));
                    Point p2 = new Point(Math.max(anchor.x, extent.x), Math.max(anchor.y, extent.y));
                    Point p3 = new Point(Math.max(anchor.x, extent.x), Math.min(anchor.y, extent.y));
                    pointlist.clear();
                    pointlist.add(p0);
                    pointlist.add(p1);
                    pointlist.add(p2);
                    pointlist.add(p3);

					if (!Util.isSimplePolygon(pointlist))
						return false;

                    RbFloor floor = new RbFloor(mainComponent);

					floor.addCorners(pointlist);
                    floor.addExteriorWalls();
                    floorList.add(floor);
                    mainComponent.add(floor);
                    floor.repaint();
                    pointlist.clear();

                    return true;
                }

            }
        } catch(Exception x) {}
        
        return false;

    }//---------------------------------------------



    public void deleteFloor(RbFloor floor) {
        try {
            if (floor != null) {
                floorList.remove((Object)floor);
                floor.setVisible(false);

                floor.clearFloor();

                mainComponent.remove(floor);

                floor = null;
            }
        } catch(Exception x) {}
    }//---------------------------------------------




    /*
    private ArrayList orFloors(RbFloor floor1, RbFloor floor2) {

        ArrayList list = new ArrayList();
        try {

            ArrayList l = floor1.getScreenCornerList();
            Point tl_1 = Util.getLeftTop(l.iterator());
            l = floor2.getScreenCornerList();
            Point tl_2 = Util.getLeftTop(l.iterator());

            RbFloor left = floor1;
            RbFloor right = floor2;
            if (tl_2.x <= tl_1.x) {
                left = floor1;
                right = floor2;
            }

            Polygon leftPoly = left.getScreenPolygon();
            Polygon rightPoly = right.getScreenPolygon();

            Line2D leftEdges[] = left.getScreenEgdes();
            Line2D rightEdges[] = right.getScreenEgdes();

            Line2D leftLine;
            Line2D rightLine;

            Point2D leftPt1;
            Point2D leftPt2;

            Point2D pt2Dx[] = new Point2D[2];
            pt2Dx[0] = null;
            pt2Dx[1] = null;

            for (int leftIndex = 0; leftIndex < leftEdges.length; leftIndex++) {

                leftLine = leftEdges[leftIndex];

                for (int rightIndex = 0; rightIndex < rightEdges.length; rightIndex++) {

                    rightLine = rightEdges[rightIndex];

                    if (!Util.intersection(leftLine, rightLine, pt2Dx)) {
                        list.add(leftLine.getP1());
                    }
                    else {
                        if (leftLine.getP1() != pt2Dx[0]) {
                            list.add(leftLine.getP1());
                        }

                        list.add(pt2Dx[0]);

                        if (pt2Dx[1] != null) {

                            if (rightPoly.contains(pt2Dx[1])) {


                            }
                            else {
                                //ok for left oly
                            }



                        }

                    }

                }
            }

        } catch(Exception x) {}

        return list;

    }//------------------------------------------------------------



    public void orInFloor(RbFloor floor0, Point ptCommon) {
        try {
            if (floor0 == null)
                return;

            Polygon poly0 = floor0.getScreenPolygon();
            if (!poly0.contains(ptCommon)) 
                return;

            Polygon poly;


            ArrayList newPoints = new ArrayList();

            Line2D edges0[];// = floor0.getEgdes();
            Line2D edges[];

            Line2D line0;
            Line2D line;

            RbFloor floor;
            Iterator floorItr = floorList.iterator();
            while (floorItr.hasNext()) {
                floor = (RbFloor)floorItr.next();
                if (!floor.equals(floor0)) {

                    poly = floor.getScreenPolygon();
                    if (poly.contains(ptCommon)) {

                        newPoints = orFloors(floor0, floor);

                    }
                }
            }
        } catch(Exception x) {}

    }//------------------------------------------
    */







    //////////////////////////////////////////////////////////////////


/*
    public JRadioButtonMenuItem getLineLocatorMI() {
        
        if (lineLocatorMI == null) {
            lineLocatorMI = new JRadioButtonMenuItem("Line Locator");

            lineLocatorMI.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (locator != null)
                        clearLocatorList();
                        locator.unsetLocator();
                        locator.setType(RbLocator.LINE_TYPE);
                }//..............................................
            });

        }
        lineLocatorMI.setSelected(locator.type == RbLocator.LINE_TYPE);
        
        return lineLocatorMI;
    }//-------------------------------------------

    public JRadioButtonMenuItem getRectLocatorMI() {
        
        if (rectLocatorMI == null) {
            rectLocatorMI = new JRadioButtonMenuItem("Rectangle Locator");

            rectLocatorMI.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (locator != null)
                        clearLocatorList();
                        locator.unsetLocator();
                        locator.setType(RbLocator.RECT_TYPE);
                }//..............................................
            });
        }
        rectLocatorMI.setSelected(locator.type == RbLocator.RECT_TYPE);
        
        return rectLocatorMI;
    }//-------------------------------------------



    public JMenu getChooseLocatorMenu() {

        
        if (chooseLocatorMenu == null)
            chooseLocatorMenu = new JMenu("Choose Locator");

        chooseLocatorMenu.removeAll();

        ButtonGroup group = new ButtonGroup();
        group.add(getLineLocatorMI());
        group.add(getRectLocatorMI());

        chooseLocatorMenu.add(getLineLocatorMI());
        chooseLocatorMenu.add(getRectLocatorMI());
        
        return chooseLocatorMenu;
    }//-------------------------------------------







    private JMenuItem getCreateFloorMI() {

        if (createFloorMI == null) {
            createFloorMI = new JMenuItem("Create Floor");

            createFloorMI.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    createFloor();
                }
            });
        }

        return createFloorMI;
    }//-------------------------------------------






    private JMenuItem getCreateItemMI() {

        if (createItemMI == null) {
            createItemMI = new JMenuItem("Create Item");

            createItemMI.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    createItem();

                }
            });
        }

        return createItemMI;
    }//-------------------------------------------

*/




    /*
    public RbPopupMenu getPopupMenu() {

        if (popupMenu == null)
            popupMenu = new RbPopupMenu(mainComponent);

        popupMenu.removeAll();

        return popupMenu;
    }//-----------------------------------
    */





	public void zoomIn() {

		try {
			double oldScale = mainScale;

            mainScale = Math.min(mainScale, 10.0);

			if (oldScale != mainScale) {

                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				int w = (int)dim.getWidth();
				int h = (int)dim.getHeight();
				w = w +(int)(Math.abs(mainScale -1.0)*(double)w);
				h = h +(int)(Math.abs(mainScale -1.0)*(double)h);
				dim.setSize(w, h);

				setPreferredSize(dim);

                RbFloor floor;
				Iterator itr = floorList.iterator();
				while (itr.hasNext()) {

					floor = (RbFloor)itr.next();

                    if (floor != null) {
                        floor.zoomIn();
                    }
				}
			}
		} catch(Exception x) {}

	}//----------------------------------


	public void zoomOut() {

        /*
		try {
			double oldScale = view.mainScale;

            view.mainScale = Math.max(1.0, view.mainScale -1.0);

			if (oldScale != view.mainScale) {
				Point p;
				ArrayList corners = getCornersOffset();
				Iterator itr = corners.iterator();
				while (itr.hasNext()) {

					p = (Point)itr.next();

					p.x /= oldScale;
					p.x *= view.mainScale;

					p.y /= oldScale;
					p.y *= view.mainScale;
				}

				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				int w = (int)dim.getWidth();
				int h = (int)dim.getHeight();
				w = w +(int)(Math.abs(view.mainScale -1.0)*(double)w);
				h = h +(int)(Math.abs(view.mainScale -1.0)*(double)h);
				dim.setSize(w, h);

				view.setPreferredSize(dim);

				refactor(corners);

				refactor(corners);
			}

		} catch(Exception x) {}
    */
    
	}//----------------------------------







    //////////////////////////////////////////////////



    public Iterator getLocatorListItr() {
        return locatorList.iterator();
    }//-----------------------------------



    public void setLocatorType(int type) {
        if (locator != null) {
            clearLocatorList();
            locator.unsetLocator();
            locator.setType(type);
        }
    }//---------------------------------------



    public int getLocatorType() {
        return locator.type;
    }//---------------------------------






}//end class (RbMainComponent)
