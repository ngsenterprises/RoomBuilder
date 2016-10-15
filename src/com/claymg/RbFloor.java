/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.claymg;


import java.util.ArrayList;
import java.util.Iterator;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.Polygon;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.Dimension;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;


import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;




import javax.swing.JPanel;


/**
 *
 * @author woody
 */
public class RbFloor extends JPanel
                     implements MouseListener,
								MouseMotionListener
{

    RbFloor floor = this;
    RbMainComponent view = null;

    private ArrayList cornerList = new ArrayList();
    private ArrayList markerList = new ArrayList();
	private ArrayList exteriorWallList = new ArrayList();
    public ArrayList itemList = new ArrayList();

    //double scale = 2.0; //pixels/inch

    Polygon polygon = null;
    Color clrEdge = Color.black;

    boolean bButton_1_down = false;
    boolean bButton_1_dragged = false;
	Point btn1PressedLOS = new Point();

	static final int WINDING_COUNTERCLOCKWISE = -1;
	static final int WINDING_CLOCKWISE = 1;

	private int winding = 0;


	//Point ptScreen = new Point();

    /////////////////////////////////////////////


    public RbFloor(RbMainComponent view) {

        super(null, true);

        this.view = view;

        setOpaque(false);
        addMouseListener(this);
		addMouseMotionListener(this);
        setVisible(true);

    }//-----------------------------


    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

		Color org = g.getColor();
		g.setColor(clrEdge);

        try {
			if (exteriorWallList.isEmpty()) {

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
        }
        catch(Exception x) {}

		if (org != null)
			g.setColor(org);

    }//-----------------------------------------

	





	////////////////////////////////




	public void zoomIn() {
	
		try {
			double oldScale = view.mainScale;

            view.mainScale = Math.min(view.mainScale, 10.0);

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
			}

		} catch(Exception x) {}

	}//----------------------------------


	public void zoomOut() {

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

	}//----------------------------------

















    public RbMainComponent getView() {
        return view;
    }//-----------------------------------


	public int getWinding() {
		return winding;
	}//---------------------------




    public boolean getButton1Dragged() {
        return bButton_1_dragged;
    }//----------------------------------------





    public boolean refactorPoint(Point org, int dx, int dy) {
		//System.out.println("refactorPoint (" +org.x +", " +org.y +") dx dy " +dx +", " +dy);
		try {
			ArrayList list = getCornersOffset();
            Point base = getLocation();

			Point p;
			Iterator itr = list.iterator();
			while (itr.hasNext()) {

				p = (Point)itr.next();
        		//System.out.println("p (" +p.x +", " +p.y +")");

				if (p.x == org.x && p.y == org.y) {
					p.x = Math.max(0, Math.min(base.x +p.x +dx, view.getWidth() -1));
					p.y = Math.max(0, Math.min(base.y +p.y +dy, view.getHeight() -1));
				}
                else {
                    p.x += base.x;
                    p.y += base.y;
                }
			}
    		return refactor(list);

		} catch(Exception x) {}

		return false;

	}//----------------------------




    public boolean refactor(ArrayList clist) {
		try {
			if (!Util.isSimplePolygon(clist))
				return false;

			addCorners(clist);

			Point p0, p1, p2;
			RbWall wall;
			CornerMarker marker;
			Iterator witr = exteriorWallList.iterator();
			Iterator mitr = markerList.iterator();
			Iterator citr = cornerList.iterator();
			if (citr.hasNext()) {

				p0 = (Point)citr.next();
				p1 = p0;
				while (citr.hasNext()) {

					p2 = (Point)citr.next();

					if (witr.hasNext()) {
						wall = (RbWall)witr.next();
						wall.setAnchorExtent(p1, p2);
					}

					if (mitr.hasNext()) {
						marker = (CornerMarker)mitr.next();
						marker.setCorner(p1);
					}

					p1 = p2;
				}

				if (witr.hasNext()) {
					wall = (RbWall)witr.next();
					wall.setAnchorExtent(p1, p0);
				}

				if (mitr.hasNext()) {
					marker = (CornerMarker)mitr.next();
                    marker.setCorner(p1);
				}
			}

			return true;

		} catch(Exception x) {}

		return false;

    }//----------------------------













    public void clearFloor() {

        cornerList.clear();
        exteriorWallList.clear();
        itemList.clear();

    }//-------------------------------



	public void clearCornerMarkers() {
		try {
			CornerMarker marker;
			Iterator itr = markerList.iterator();
			while (itr.hasNext()) {
				marker = (CornerMarker)itr.next();
				floor.remove(marker);
			}
			markerList.clear();

			floor.repaint();

		} catch(Exception x) {}
	}//---------------------------------------


	public void addCornerMarkers() {
		try {
			clearCornerMarkers();

            CornerMarker marker;
            Point corner;
            Iterator itr = cornerList.iterator();


            while (itr.hasNext()) {

                corner = (Point)itr.next();

                marker = new CornerMarker(view, floor, corner);
                add(marker);
                markerList.add(marker);
            }

            repaint();
		} catch(Exception x) {}
	}//---------------------------------------



	public void deleteItem(RbItem item) {
		try{
			itemList.remove(item);
			remove(item);
		} catch(Exception x) {}
	}//---------------------------------------




    public void addItem(RbItem item) {
        try {
            Container itemParent = item.getParent();
            if (itemParent == getParent()) {

                Point location = getLocation();
                Point itemLocation = item.getLocation();

                itemLocation.x -= location.x;
                itemLocation.y -= location.y;
                
                itemParent.remove(item);
                itemList.add(item);
                add(item);
                item.setLocation(itemLocation.x, itemLocation.y);

                MouseListener ml[];
                MouseMotionListener mm[];
                RbWall wall;
                Iterator itr2 = getExternalWallItr();
                while (itr2.hasNext()) {

                    wall = (RbWall)itr2.next();

                    ml = wall.getMouseListeners();
                    for (int index = 0; index < ml.length; index++)
                        wall.removeMouseListener(ml[index]);

                    mm = wall.getMouseMotionListeners();
                    for (int index = 0; index < ml.length; index++)
                        wall.removeMouseMotionListener(mm[index]);
                }
				clearCornerMarkers();
            }
        } catch (Exception x) {}
    }//--------------------------------------






    public Iterator getExternalWallItr() {

        return exteriorWallList.iterator();
        
    }//--------------------------------------------



    public boolean isHovering(RbItem item) {

		System.out.println("isHovering");

		try {
			if (getParent() == item.getParent()) {

				Point floorLocation = getLocation();
                Point itemLocation = item.getLocation();
                
				//in RbFloor coordinates
				Polygon plg = getPolygonOffset(floorLocation);

				Point p;
				Point pts[] = item.getCornersOffset(itemLocation);
				for (int index = 0; index < pts.length; index++) {
					p = pts[index];
					if (!plg.contains(p.x, p.y))
						return false;
				}

				Line2D edges[] = getEdgesOffset(floorLocation);
				Line2D itemEdges[] = item.getEdgesOffset(itemLocation);
				for (int itemIndex = 0; itemIndex < itemEdges.length; itemIndex++) {
					for (int index = 0; index < edges.length; index++) {
						if (edges[index].intersectsLine(itemEdges[itemIndex]))
							return false;
					}
				}

				
                RbItem itm;
                Iterator itr = itemList.iterator();
                while (itr.hasNext()) {
                    itm = (RbItem)itr.next();
                    edges = itm.getEdgesOffset(floorLocation);

                    for (int i = 0; i < itemEdges.length; i++) {
                        for (int j = 0; j < edges.length; j++) {
                            if (itemEdges[i].intersectsLine(edges[j]))
                                return false;
                        }
                    }
                }

                return true;
    		}
        } catch(Exception X) {}

        return false;
    }//------------------------------------------------



    public boolean isValidLocation(int dx, int dy, RbItem item0) {

		//System.out.println("isValidLocation");

        try {
			if (item0.getParent() == floor) {

				Polygon plg = getPolygonOffset();
					
				Point itemLocationOffset = item0.getLocation();
				itemLocationOffset.x += dx;
				itemLocationOffset.y += dy;

				Point p;
				Point item0Pts[] = item0.getCornersOffset(itemLocationOffset);
				for (int index =0; index < item0Pts.length; index++) {
					p = item0Pts[index];
                    if (!plg.contains(p.x, p.y))
						return false;
				}
                
				Line2D edges[] = getEdgesOffset();
				Line2D item0Edges[] = item0.getEdgesOffset(itemLocationOffset);
				for (int itemIndex = 0; itemIndex < item0Edges.length; itemIndex++) {
					for (int index = 0; index < edges.length; index++) {
						if (edges[index].intersectsLine(item0Edges[itemIndex]))
							return false;
					}
				}
				
                RbItem item;
                Iterator itr = itemList.iterator();
                while (itr.hasNext()) {
                    item = (RbItem)itr.next();
                    if (item != item0) {
                        edges = item.getEdgesOffset(item.getLocation());

                        for (int i = 0; i < item0Edges.length; i++) {
                            for (int j = 0; j < edges.length; j++) {
                                if (item0Edges[i].intersectsLine(edges[j]))
                                    return false;
                            }
                        }
                    }
                }

				return true;
			}
        } catch(Exception X) {}

        return false;

    }//------------------------------------------------


    public Polygon getPolygonOffset() {
		return getPolygonOffset(new Point());
	}//--------------------------------------



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



	public ArrayList getCornersOffset() {

        return getCornersOffset(new Point());
    }//---------------------------------------


	public ArrayList getCornersOffset(Point offset) {

		ArrayList list = new ArrayList();

		Point p;
		Iterator itr = cornerList.iterator();
		while (itr.hasNext()) {
			p = (Point)((Point)itr.next()).clone();

			p.x += offset.x;
			p.y += offset.y;

			list.add(p);
		}

		return list;

	}//---------------------------------------------------



    public RbWall[] getExteriorWalls(Point p) {
        try {
            RbWall walls[] = new RbWall[2];
            RbWall wall;
            Iterator itr = exteriorWallList.iterator();
            while (itr.hasNext()) {

                wall = (RbWall)itr.next();

                if (wall.getExtent().equals(p))
                    walls[0] = wall;
                else if (wall.getAnchor().equals(p))
                    walls[1] = wall;
            }
            return walls;
        } catch(Exception x) {}

        return new RbWall[0];
    }//----------------------------------------------




    public Line2D[] getEdgesOffset() {
		return getEdgesOffset(new Point());
	}//------------------------------------------------



    public Line2D[] getEdgesOffset(Point2D offset) {
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
                    edges[index] = new Line2D.Double((double)p1.x +offset.getX(), (double)p1.y +offset.getY(),
													 (double)p2.x +offset.getX(), (double)p2.y +offset.getY());
                    index++;
                    p1 = p2;
                }
                edges[index] = new Line2D.Double((double)p1.x +offset.getX(), (double)p1.y +offset.getY(),
												 (double)p0.x +offset.getX(), (double)p0.y +offset.getY());
            }

            return edges;
        } catch(Exception x) {}

        return new Line2D[0];

    }//----------------------------------------------


	//////////////////////////////////////////////
	// Mouse Motion Listener routines
	//

	@Override  public void mouseMoved(MouseEvent e) {
		//System.out.println("RbFloor.mouseMoved (" +e.getX() +", " +e.getY());
	}//-----------------------------------------------------

	@Override
    public void mouseDragged(MouseEvent e) {
		
		//System.out.println("RbFloor.mouseDragged --------------(");// +e.getX() +", " +e.getY());
		//System.out.println("SRC " +e.getSource());

		if (bButton_1_down == true) {
            bButton_1_dragged = true;

			//Point p = e.getPoint();
			//System.out.println("e.getPoint() (" +p.x +", " +p.y +")");

			Point draggedLOS = e.getLocationOnScreen();
			int dx = draggedLOS.x -btn1PressedLOS.x;
			int dy = draggedLOS.y -btn1PressedLOS.y;
			//System.out.println("delta (" +dx +", " +dy +")");
            
			Iterator itr = markerList.iterator();
			if (!markerList.isEmpty()) {

				Point LOS = getLocationOnScreen();
				Point ptBtn1Pressed = new Point(btn1PressedLOS.x -LOS.x, btn1PressedLOS.y -LOS.y);
				//System.out.println("ptBtn1Pressed (" +ptBtn1Pressed.x +", " +ptBtn1Pressed.y +")");

				CornerMarker marker;
				while (itr.hasNext()) {

					marker = (CornerMarker)itr.next();

					if (marker.containsPoint(ptBtn1Pressed)) {

						//System.out.println("ptBtn1Pressed (" +(ptBtn1Pressed.x) +", " +(ptBtn1Pressed.y) +")");
						//Point loc = getLocation();
						Point ptMarker = marker.getCorner();

						RbWall walls[] = getExteriorWalls(ptMarker);

						if (walls != null && walls.length == 2 &&
											 !walls[0].getWallLock() &&
											 !walls[1].getWallLock())
						{
							if (floor.refactorPoint(ptMarker, dx, dy)) {
								btn1PressedLOS = draggedLOS;
								return;
							}
						}
					}
				}
			}
			
			Point loc = getLocation();
			loc.x = Math.max(0, Math.min(loc.x +dx, view.getWidth() -getWidth() -1));
			loc.y = Math.max(0, Math.min(loc.y +dy, view.getHeight() -getHeight() -1));
			setLocation(loc.x, loc.y);

			btn1PressedLOS = draggedLOS;
		}
	}//------------------------------------------






    
    /////////////////////////////////////////////////////
    // Mouse Listener routines


    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {

		Object src = e.getSource();

		if (e.getButton() == MouseEvent.BUTTON1) {
			
			bButton_1_down = true;
	        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

			Point pressedLOS = e.getLocationOnScreen();
			btn1PressedLOS.x = pressedLOS.x;
			btn1PressedLOS.y = pressedLOS.y;

			Iterator itr = markerList.iterator();
			if (!markerList.isEmpty()) {
				Point LOS = getLocationOnScreen();
				Point ptBtn1Pressed = new Point(btn1PressedLOS.x -LOS.x, btn1PressedLOS.y -LOS.y);
				CornerMarker marker;
				while (itr.hasNext()) {
					marker = (CornerMarker)itr.next();
					if (marker.containsPoint(ptBtn1Pressed)) {
						Point ptMarker = marker.getCorner();

						RbWall walls[] = getExteriorWalls(ptMarker);
						if (walls != null && walls.length == 2) {
							walls[0].showSpecs(true);
							walls[1].showSpecs(true);
						}
					}
				}
			}

			if (src instanceof RbWall) {
			
				((RbWall)src).showSpecs(true);

			}
		}
		else if (e.getButton() == MouseEvent.BUTTON3) {

			if (src instanceof RbWall) {
				RbWall wall = (RbWall)src;
                if (itemList.isEmpty()) {
    				JPopupMenu menu = RbPopupMenu.getPopupMenu();
                    menu.show(wall, e.getX(), e.getY());
    				return;
                }
			}
		
            JPopupMenu menu = RbPopupMenu.getPopupMenu();
            menu.show(floor, e.getX(), e.getY());
        }

    }//------------------------------------------

    @Override
    public void mouseReleased(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON1) {
            bButton_1_down = false;
            bButton_1_dragged = false;
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            clearExteriorWallAngleDisplay();
        }
	}//--------------------------------------------


	///////////////////////////////////////////////////////


    public void clearExteriorWallAngleDisplay() {
        try{
            RbWall wall;
            Iterator itr = exteriorWallList.iterator();
            while (itr.hasNext()) {
                wall = (RbWall)itr.next();
                wall.showSpecs(false);
            }
            
        } catch(Exception x) {}
    }//----------------------------------------------



    private void clearExteriorWallList() {
        try {
            RbWall wall;
            Iterator itr = exteriorWallList.iterator();
            while (itr.hasNext()) {
                
                wall = (RbWall)itr.next();
                
                floor.remove(wall);
                wall.setVisible(false);
            }
            exteriorWallList.clear();
            
        } catch(Exception x) {}
    }//--------------------------



    public void addExteriorWalls() {
		//System.out.println("addExteriorWalls ");
        try {
            clearExteriorWallList();

            Point p0;
            Point p1;
            Point p2;

			RbWall wall;
			Iterator itr = cornerList.iterator();
			if (itr.hasNext()) {

				p0 = (Point)itr.next();
				p1 = p0;

				while (itr.hasNext()) {

					p2 = (Point)itr.next();

					wall = new RbWall(view, floor);
                    wall.setAnchorExtent(p1, p2);
                    exteriorWallList.add(wall);

					p1 = p2;
				}

				wall = new RbWall(view, floor);
				wall.setAnchorExtent(p1, p0);
				exteriorWallList.add(wall);
			}

            RbWall wall0;
            RbWall wall1;
            RbWall wall2;

            itr = exteriorWallList.iterator();
            if (itr.hasNext()) {

                wall0 = (RbWall)itr.next();
                wall1 = wall0;

                while (itr.hasNext()) {

                    wall2 = (RbWall)itr.next();

                    wall1.setNext(wall2);
                    wall2.setPrev(wall1);

                    wall1 = wall2;
                }

                wall1.setNext(wall0);
                wall0.setPrev(wall1);
            }

            addCornerMarkers();
			repaint();

		} catch(Exception x) {
			x.printStackTrace();
		}
    }//---------------------------------





    //input : (Point)ArrayList list.
    //
    // Points are in mainComponent coordinates,
    // Points are stored in Floor coordites.

    public void addCorners(ArrayList list) {
        //System.out.println("addCorners ----");
        try {
            cornerList.clear();

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

            polygon = new Polygon(x, y, len);

			Point p0, p1, p2;
			double area = 0.0;
			itr = cornerList.iterator();
			if (itr.hasNext()) {
				p0 = (Point)itr.next();
				p1 = p0;
				while (itr.hasNext()) {
					p2 = (Point)itr.next();
					area += (double)(p1.x*p2.y -p2.x*p1.y);
					p1 = p2;
				}
				area += (double)(p1.x*p0.y -p0.x*p1.y);
				area /= 2.0;
			}

			winding = (area < 0.0) ? WINDING_COUNTERCLOCKWISE : WINDING_CLOCKWISE;

            setBounds(lt.x, lt.y, rb.x -lt.x +1, rb.y -lt.y +1);
            setVisible(true);

            repaint();

        } catch(Exception x) {}
    }//---------------------------------



}//end class (RbFloor)
