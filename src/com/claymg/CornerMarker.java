/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.claymg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import java.lang.Integer;

import java.awt.geom.AffineTransform;

import java.awt.Polygon;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Container;
import java.awt.Polygon;
import java.awt.Rectangle;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.Box;




/**
 *
 * @author woody
 */
public class CornerMarker extends JPanel {

	CornerMarker marker = this;

	RbMainComponent view;
	RbFloor floor;
    JLabel label = new JLabel();
    JPanel btnGrip = new JPanel();

	ArrayList list = new ArrayList();
    //RbWall wallPre;
    //RbWall wallPost;
    Point anchor;

    Point gripLoc = new Point();
    Point labelLoc = new Point();

    final int cornerSide = 50;
    final int markerSide = 30;
    final int labelWidth = 50;
    final int labelHeight = 20;

	//int orientation = SwingConstants.CENTER;

    public CornerMarker(RbMainComponent view, RbFloor floor, Point anchor) {

        super(null, true);

        try {
            this.view = view;
            this.floor = floor;

            setOpaque(false);
            this.anchor = (Point)anchor.clone();
    		//this.setBorder(BorderFactory.createLineBorder(Color.red));
            //System.out.println("anchor = (" +anchor.x +", " +anchor.y +")");

            btnGrip.setOpaque(false);
            btnGrip.setBorder(BorderFactory.createLineBorder(Color.red));
            add(btnGrip);

            label.setOpaque(false);
            //label.setBorder(BorderFactory.createLineBorder(Color.cyan));
            add(label);

            floor.add(this);
			setCorner(anchor);

        } catch(Exception x) {}

    }//-----------------------------


    public void paintComponent(Graphics g) {

        super.paintComponent(g);

		if (label.isVisible()) {
			label.setText("\u00B0" +Double.toString(getAngle()));
		}
    }//----------------------------------------


    double getAngle() {

        //System.out.println("CornerMarker.getAngle ----------------------------");

        try {
            double angle = 0.0;

            RbWall walls[] = floor.getExteriorWalls(anchor);

            if (walls.length == 2) {

                Point e0 = walls[0].getAnchor();
                Point a = walls[1].getAnchor();
                Point e1 = walls[1].getExtent();

				if (floor.getWinding() < 0)
					angle = Util.getAngle(e1, a, e0);
				else
					angle = Util.getAngle(e0, a, e1);

				return angle;
            }

        } catch(Exception x) {}

        return 0.0;
    }//---------------------------------------











    public int getOrientation(Point p, RbFloor f) {

		//System.out.println("getOrientation ");

        int orient = SwingConstants.CENTER;
        try {

			list.clear();
            Polygon plg = f.getPolygonOffset();

			int x, y;
			double rx, ry;
			double r;
			int dir =  SwingConstants.WEST;
			for (double rad = 0.0; rad < 2.0*Math.PI; rad += Math.PI/4.0) {

				//r = (rad +Math.PI/2.0)%(2*Math.PI);

				rx = Math.cos(rad);
				ry = Math.sin(rad);
				//System.out.println("(" +rx +", " +ry +")");

				x = (-.0001 < rx && rx < 0.0001) ? 0 : (rx < 0.0) ? -1 : 1;
				y = (-.0001 < ry && ry < 0.0001) ? 0 : (ry < 0.0) ? -1 : 1;


				if (plg.contains(p.x +x, p.y +y))
					list.add(Integer.valueOf(dir));

				dir = dir%SwingConstants.NORTH_WEST +1;
			}

			int len = list.size();
			Integer i = (Integer)list.get(Math.max(0, len/2));

			orient = i.intValue();
			list.clear();

			return orient;
						
			/*
			for (int index = SwingConstants.NORTH; index <= SwingConstants.NORTH_WEST; index++) {

				if (index == SwingConstants.NORTH) {
                    if (plg.contains(p.x, p.y +1))
						orient = index;
                }
                else if (index == SwingConstants.EAST) {
                    if (plg.contains(p.x -1, p.y))
						orient = index; 
                }
                else if (index == SwingConstants.SOUTH) {
                    if (plg.contains(p.x, p.y -1))
						orient = index; 
                }
                else if (index == SwingConstants.WEST) {
                    if (plg.contains(p.x +1, p.y))
						orient = index; 
                }
				else if (index == SwingConstants.NORTH_EAST) {
                    if (plg.contains(p.x -1, p.y +1))
						orient = index;
                }
                else if (index == SwingConstants.SOUTH_EAST) {
                    if (plg.contains(p.x -1, p.y -1))
						orient = index;
                }
                else if (index == SwingConstants.SOUTH_WEST) {
                    if (plg.contains(p.x +1, p.y -1))
						orient = index;
                }
                else if (index == SwingConstants.NORTH_WEST) {
                    if (plg.contains(p.x +1, p.y +1))
						orient = index;
                }
            }
			orientation = orient;
			return orient;
			*/
		} catch(Exception x) {}

        return SwingConstants.CENTER;

    }//--------------------------------------------------------

    public void setRbBounds() {

        setRbBounds(getOrientation(anchor, floor));

    }//------------------------------



    public void setRbBounds(int orient) {

        //System.out.println("setRbBounds");

        try {
            //anchor = (Point)wallPost.getAnchor().clone();

            int x = anchor.x;// = Math.max(0, Math.min(anchor.x -labelWidth/2, floor.getWidth() -labelWidth -1));
            int y = anchor.y;// = Math.max(0, Math.min(anchor.y -gripSide/2, floor.getHeight() -gripSide -labelHeight -1));

            int gx = 0;
            int gy = 0;

            int lx = 0;
            int ly = markerSide;


            if (orient == SwingConstants.NORTH) {

                x = Math.max(0, Math.min(anchor.x -cornerSide/2, floor.getWidth() -cornerSide));
                y = Math.max(0, Math.min(anchor.y -cornerSide/2, floor.getHeight() -cornerSide));
                
                gx = Math.max(0, Math.min(anchor.x -x -markerSide/2, cornerSide -markerSide));
                gy = 0;
                
                lx = 0;
                ly = markerSide;
            }
            else if (orient == SwingConstants.NORTH_EAST) {
                x = Math.max(0, Math.min(anchor.x -cornerSide +markerSide/2, floor.getWidth() -cornerSide));
                y = Math.max(0, Math.min(anchor.y -markerSide/2, floor.getHeight() -cornerSide));
                
                gx = Math.max(0, Math.min(anchor.x -x -markerSide/2, cornerSide -markerSide));
                gy = 0;
                
                lx = 0;
                ly = markerSide;
            }
            else if (orient == SwingConstants.EAST) {
                x = Math.max(0, Math.min(anchor.x -cornerSide +markerSide/2, floor.getWidth() -cornerSide));
                y = Math.max(0, Math.min(anchor.y -markerSide/2, floor.getHeight() -cornerSide));

                gx = Math.max(0, Math.min(anchor.x -x -markerSide/2, cornerSide -markerSide));
                gy = 0;

                lx = 0;
                ly = markerSide;
            }
            else if (orient == SwingConstants.SOUTH_EAST) {
                x = Math.max(0, Math.min(anchor.x -cornerSide +markerSide/2, floor.getWidth() -cornerSide));
                y = Math.max(0, Math.min(anchor.y -cornerSide +markerSide/2, floor.getHeight() -cornerSide));
                
                gx = Math.max(0, Math.min(anchor.x -x -markerSide/2, cornerSide -markerSide));
                gy = cornerSide -markerSide;
                
                lx = 0;
                ly = 0;
            }
            else if (orient == SwingConstants.SOUTH) {
                x = Math.max(0, Math.min(anchor.x -cornerSide/2, floor.getWidth() -cornerSide));
                y = Math.max(0, Math.min(anchor.y -cornerSide +markerSide/2, floor.getHeight() -cornerSide));
                
                gx = Math.max(0, Math.min(anchor.x -x -markerSide/2, cornerSide -markerSide));
                gy = cornerSide -markerSide;
                
                lx = 0;
                ly = 0;
            }
            else if (orient == SwingConstants.SOUTH_WEST) {
                x = Math.max(0, Math.min(anchor.x -markerSide/2, floor.getWidth() -cornerSide));
                y = Math.max(0, Math.min(anchor.y -cornerSide +markerSide/2, floor.getHeight() -cornerSide));
                
                gx = Math.max(0, Math.min(anchor.x -x -markerSide/2, cornerSide -markerSide));
                gy = cornerSide -markerSide;
                
                lx = 0;
                ly = 0;
            }
            else if (orient == SwingConstants.WEST) {
                x = Math.max(0, Math.min(anchor.x -markerSide/2, floor.getWidth() -cornerSide));
                y = Math.max(0, Math.min(anchor.y -markerSide/2, floor.getHeight() -cornerSide));
                
                gx = Math.max(0, Math.min(anchor.x -x -markerSide/2, cornerSide -markerSide));
                gy = 0;
                
                lx = 0;
                ly = markerSide;
            }
            else if (orient == SwingConstants.NORTH_WEST) {
                x = Math.max(0, Math.min(anchor.x -markerSide/2, floor.getWidth() -cornerSide));
                y = Math.max(0, Math.min(anchor.y -markerSide/2, floor.getHeight() -cornerSide));

                gx = Math.max(0, Math.min(anchor.x -x -markerSide/2, cornerSide -markerSide));
                gy = 0;

                lx = 0;
                ly = markerSide;
            }

            setVisible(true);
            setBounds(x, y, cornerSide, cornerSide);
            //System.out.println("setBounds(" +x +", " +y +") (" +(x +cornerSide) +", " +(y +cornerSide) +")") ;
            
            btnGrip.setVisible(true);
            btnGrip.setBounds(gx, gy, markerSide, markerSide);

            label.setVisible(true);
            label.setBounds(lx, ly, labelWidth, labelHeight);

            repaint();

        } catch(Exception x) {}

    }//---------------------------------
    


    
	public void setCorner(Point p) {
		//System.out.println("setCorner (" +p.x +", " +p.y +")");

		this.anchor = (Point)p.clone();

        setRbBounds(getOrientation(p, floor));

	}//--------------------------------
    
    


	public Point getCorner() {
		return (Point)anchor.clone();
	}//--------------------------


	public boolean containsPoint(Point p) {
		//System.out.println("containsPoint (" +p.x +", " +p.y +")");
		try {
			Point loc = getLocation();
    		//System.out.println("location (" +loc.x +", " +loc.y +") " +(loc.x +getWidth() +", " +(loc.y +getWidth() +")")));

			if (loc.x <= p.x && p.x < loc.x +getWidth() &&
				loc.y <= p.y && p.y < loc.y +getWidth())
			{
				return true;
			}
		} catch(Exception X) {}

		return false;
	}//--------------------------------------







}//end class (CornerMarker)
