/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.claymg;

import java.util.ArrayList;
import java.util.Iterator;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Dimension;
import java.awt.Container;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;






/**
 *
 * @author woody
 */
public class RbWall extends JPanel implements ComponentListener {

	RbWall wall = this;
    RbMainComponent view;
	RbFloor floor;
    RbWall prev;
    RbWall next;

	Point anchor = new Point();
	Point extent = new Point();

	private Color clrUnlocked = Color.blue;
	private Color clrHighLight = Color.magenta;
	private Color clrLocked = Color.black;

    RbWallHeadUpDisplay metricDisplay = null;

	private boolean lockDimensions = false;

    public double rbWidth = 0;
    public double rbHeight = 0;
    public double rbLength = 0;

    private int wallAccess = 0;


	private int style = -1;
	public final static int STYLE_TOP = 0;
	public final static int STYLE_RIGHT = 1;
	public final static int STYLE_BOTTOM = 2;
	public final static int STYLE_LEFT = 3;
	public final static int STYLE_VCENTER = 4;
	public final static int STYLE_HCENTER = 5;

	private final int minWidthLength = 10;



    public RbWall(RbMainComponent view, RbFloor floor) {

        super(null, true);

        this.view = view;
        this.floor = floor;

        setOpaque(false);
		setVisible(true);
		addMouseListener((MouseListener)floor);
		addMouseMotionListener((MouseMotionListener)floor);
        addComponentListener(this);
        floor.add(wall);

		metricDisplay = new RbWallHeadUpDisplay(floor, wall);
		floor.add(metricDisplay);

		//setBorder(BorderFactory.createLineBorder(Color.magenta));

    }//-----------------------------


    public int getAccess() {

        return wallAccess;
    }//--------------------------


	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Color org = g.getColor();
		g.setColor((!lockDimensions) ? clrUnlocked : clrLocked);

		if (style == STYLE_LEFT)
			g.drawLine(0, 0, 0, getHeight() -1);
		else if (style == STYLE_TOP)
			g.drawLine(0, 0, getWidth() -1, 0);
		else if (style == STYLE_RIGHT)
			g.drawLine(getWidth() -1, 0, getWidth() -1, getHeight() -1);
		else if (style == STYLE_BOTTOM)
			g.drawLine(0, getHeight() -1, getWidth() -1, getHeight() -1);
		else if (style == STYLE_VCENTER)
			g.drawLine(getWidth()/2, 0, getWidth()/2, getHeight() -1);
		else if (style == STYLE_HCENTER)
			g.drawLine(0, getHeight()/2, getWidth() -1, getHeight()/2);
		else {
			Point loc = getLocation();
			g.drawLine(anchor.x -loc.x, anchor.y -loc.y, extent.x - loc.x, extent.y -loc.y);
		}

		if (org != null)
			g.setColor(org);

		
        //if (lblAngle.isVisible())
        //    lblAngle.setText(Double.toString(Util.getAngle(wall)));
        
		
	}//-----------------------------------------



    ////////////////////////////////////////////////////


    @Override public void componentShown(ComponentEvent e) {}
    @Override public void componentHidden(ComponentEvent e) {}

    @Override
    public void componentMoved(ComponentEvent e) {
        //System.out.println("componentMoved");
    }//-----------------------------------------------


    @Override
    public void componentResized(ComponentEvent e) {
        //System.out.println("componentResized");
        try {
			if (floor.getButton1Dragged())
				showSpecs(true);
        } catch(Exception x) {}

    }//------------------------------------------




    /////////////////////////////////////////////////////


	public boolean getWallLock() {
		return lockDimensions;
	}//---------------------------------


	public void setWallLock(boolean lock) {
		lockDimensions = lock;
	}//---------------------------------



    public void setPrev(RbWall wall) {
        prev = wall;
    }//------------------------------------

    public void setNext(RbWall wall) {
        next = wall;
    }//------------------------------------


    public int getAngle() {

        return (int)Util.getAngle(wall);

    }//------------------------------



    public void setAngle(double angle) {

        //System.out.println("setAngle " +angle +"----------------------");
        //System.out.println("anchor (" +anchor.getX() +", " +anchor.getY() +")");
        //System.out.println("extent (" +extent.getX() +", " +extent.getY() +")");

        double curAngle = Util.getAngle(wall);
        double delta = angle -curAngle;

		Container parent = getParent();
		if (parent instanceof RbFloor && !lockDimensions) {

			RbFloor floor = (RbFloor)parent;
			ArrayList mainCornerList = floor.getCornersOffset(floor.getLocation());

			Point2D vpSrc = new Point2D.Double();
			if ((anchor.x < extent.x) == (anchor.y < extent.y))
				vpSrc.setLocation(-(double)getWidth(), (double)getHeight());
			else
				vpSrc.setLocation((double)getWidth(), (double)getHeight());

			System.out.println("vpSrc (" +vpSrc.getX() +", " +vpSrc.getY() +")");

			AffineTransform tx = new AffineTransform();
			tx.setToRotation(delta*Math.PI/180.0, 0.0, 0.0);

			Point2D vpDest = new Point2D.Double();
			tx.transform(vpSrc, vpDest);
			System.out.println("vpDest (" +vpDest.getX() +", " +vpDest.getY() +")");

			int dx = (int)(vpDest.getX() -vpSrc.getX());
			int dy = -(int)(vpDest.getY() -vpSrc.getY());
			System.out.println("(dx, dy) (" +dx +", " +dy +")");

            Point bPoint = new Point();
			Point rPoint = new Point();
			if (anchor.y < extent.y) {
				rPoint.setLocation(anchor);
                bPoint.setLocation(extent);
            }
			else {
				rPoint.setLocation(extent);
                bPoint.setLocation(anchor);
            }
			Point base = floor.getLocation();
            rPoint.x += base.x;
			rPoint.y += base.y;
            bPoint.x += base.x;
            bPoint.y += base.y;

			Point p;
			Iterator itr = mainCornerList.iterator();
			while (itr.hasNext()) {
				p = (Point)itr.next();
                if (p.x == rPoint.x && p.y == rPoint.y) {
                    //p.x = Math.max(0, Math.min(p.x +dx, view.getWidth() -1));
                    //p.y = Math.max(0, Math.min(p.y +dy, view.getHeight() -1));
                    p.x = (angle == 90) ? p.x = bPoint.x : Math.max(0, Math.min(p.x +dx, view.getWidth() -1));
                    p.y = (angle == 0 || angle == 180) ?  p.y = bPoint.y : Math.max(0, Math.min(p.y +dy, view.getHeight() -1));
                }
			}
            floor.refactor(mainCornerList);
		}

    }//---------------------------------




    public Point getAnchor() {
        return (Point)anchor.clone();
    }//-------------------------



    public Point getExtent() {
        return (Point)extent.clone();
    }//-------------------------



    public void setRbWidth(int width) {
        rbWidth = Math.max(5, Math.max(width, 12000));
    }//-----------------------------------------

    public void setRbHeight(int height) {
        rbHeight = Math.max(5, Math.max(height, 12000));
    }//-----------------------------------------


    public void setRbLength(int length) {
        rbLength = Math.max(5, Math.max(length, 12000));
    }//------------------------------------




    public double getRbWidth() {
        return rbWidth;
    }//-------------------------

    public double getRbHeight() {
        return rbHeight;
    }//-------------------------


    public double getRbLength() {
        return rbLength;
    }//-------------------------


    public int getStyle() {
        return style;
    }//-------------------------



	public void highLightWall(boolean hilite) {

		if (hilite)
			setBorder(BorderFactory.createLineBorder(clrHighLight));
		else
			setBorder(BorderFactory.createEmptyBorder());

		repaint();
	}//----------------------------------------


    
	public void showSpecs(boolean show) {

        metricDisplay.showDisplay(show);

		repaint();

	}//----------------------------------------



	public void setAnchorExtent(Point anchor, Point extent) {
		try {
			//System.out.println("setAnchorExtent (" +anchor.x +", " +anchor.y +") (" +extent.x +", " +extent.y +")");
			Polygon poly = null;

            Container parent = getParent();
			if (parent instanceof RbFloor)
				poly = ((RbFloor)parent).getPolygonOffset(new Point());

			Rectangle r = parent.getBounds();

			if (anchor.x < r.width && anchor.y < r.height &&
					                  extent.x < r.width &&
									  extent.y < r.height)
			{
				this.anchor = anchor;
				this.extent = extent;
                double angle = Util.getAngle(anchor, extent);

				//System.out.println("angle " +angle );

				int x,y, width, height;
				if (anchor.x == extent.x || angle == 90) {

					y = Math.min(anchor.y, extent.y);
					width = minWidthLength;
					height = Math.max(minWidthLength, Math.abs(anchor.y -extent.y) +1);
					if (poly.contains(anchor.x -1, (anchor.y +extent.y)/2)) {
						style = STYLE_RIGHT;
						x = anchor.x -minWidthLength;
					}
					else if (poly.contains(anchor.x +1, (anchor.y +extent.y)/2)) {
						style = STYLE_LEFT;
						x = anchor.x;
					}
					else {
						style = STYLE_VCENTER;
						x = anchor.x -minWidthLength/2;
					}

                    rbLength = (Math.abs(extent.y -anchor.y) +1)/view.mainScale;
				}
				else if (anchor.y == extent.y || angle == 0 || angle == 180) {
					x = Math.min(anchor.x, extent.x);
					width = Math.max(minWidthLength, Math.abs(anchor.x -extent.x) +1);
					height = minWidthLength;
					if (poly.contains((anchor.x +extent.x)/2, anchor.y -1)) {
						style = STYLE_BOTTOM;
						y = anchor.y -minWidthLength;
					}
					else if (poly.contains((anchor.x +extent.x)/2, anchor.y +1)) {
						style = STYLE_TOP;
						y = anchor.y;
					}
					else {
						style = STYLE_HCENTER;
						y = anchor.y -minWidthLength/2;
					}
                    
                    rbLength = (Math.abs(extent.x -anchor.x) +1)/view.mainScale;
				}
				else {
					style = -1;

					//System.out.println("-1");
					x = Math.max(0, Math.min(anchor.x, extent.x));
					y = Math.max(0, Math.min(anchor.y, extent.y));
					width = Math.max(minWidthLength, Math.abs(anchor.x -extent.x) +1);
					height = Math.max(minWidthLength, Math.abs(anchor.y -extent.y) +1);

                    double w =  (double)Math.abs(extent.x -anchor.x) +1.0;
                    double h =  (double)Math.abs(extent.y -anchor.y) +1.0;
                    w = w*w;
                    h = h*h;

                    rbLength = Math.sqrt(w +h)/view.mainScale;
				}

                rbWidth = 1;
                rbHeight = 96;
				
				setBounds(x, y, width, height);

				setVisible(true);
				repaint();
			}
		} catch(Exception x) {}
	}//---------------------------------------------------------




















}//end class (RbWall)
