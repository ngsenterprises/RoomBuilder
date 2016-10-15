/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.claymg;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Iterator;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Dimension;

import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

/**
 *
 * @author woody
 */
public class Util {

	static public final int AREA0 = 0;
	static public final int AREA1 = 1;
	static public final int AREA2 = 2;
	static public final int AREA3 = 3;
	static public final int AREA4 = 4;
	static public final int AREA5 = 5;
	static public final int AREA6 = 6;
	static public final int AREA7 = 7;
	static public final int AREA8 = 8;



	static public boolean isSimplePolygon(ArrayList pts) {
		try {
			Point2D p1, p2, q1, q2;
			Line2D[] edges = getEdges(pts);
			if (0 < edges.length) {
				for (int pindex = 0; pindex < edges.length; pindex++) {

					p1 = edges[pindex].getP1();
					p2 = edges[pindex].getP2();

					for (int qindex = 0; qindex < edges.length; qindex++) {

						q1 = edges[qindex].getP1();
						q2 = edges[qindex].getP2();

						if (edges[pindex] == edges[qindex])
							continue;
						else if (p1.equals(q1) && p2.equals(q2))
							return false;
						else if (p1.equals(q2) || p2.equals(q1))
							continue;
						else if (edges[pindex].intersectsLine(edges[qindex]))
							return false;
					}
				}
			}
			return true;

		} catch(Exception x) {}

		return false;
	}//---------------------------------------------------



    static public Line2D[] getEdges(ArrayList pts) {

		return getEdgesOffset(pts, new Point());
	}//-------------------------------------------

    static public Line2D[] getEdgesOffset(ArrayList pts, Point off) {
        try {
            Point p0;
            Point p1;
            Point p2;

            Line2D edges[] = new Line2D[pts.size()];

            Iterator itr = pts.iterator();
            if (itr.hasNext()) {
                p0 = (Point)itr.next();
                p1 = p0;
                int index = 0;
                while (itr.hasNext()) {
                    p2 = (Point)itr.next();
                    edges[index] = new Line2D.Double((double)p1.x, (double)p1.y, (double)p2.x, (double)p2.y);
                    index++;
                    p1 = p2;
                }
                edges[index] = new Line2D.Double((double)p1.x, (double)p1.y, (double)p0.x, (double)p0.y);
            }

            return edges;
        } catch(Exception x) {}

        return new Line2D[0];

    }//----------------------------------------------





	static public double dotProduct(Point p1, Point p2, Point p3) {

		Point a = new Point();
		Point b = new Point();

		a.x = p1.x -p2.x;
		a.y = p1.y -p2.y;
		b.x = p3.x -p2.x;
		b.y = p3.y -p2.y;

		return (double)(a.x*b.x +a.y*b.y);

	}//--------------------------------------------------


	static public double crossProductLength(Point p1, Point p2, Point p3) {

		Point a = new Point();
		Point b = new Point();

		a.x = p1.x -p2.x;
		a.y = p1.y -p2.y;
		b.x = p3.x -p2.x;
		b.y = p3.y -p2.y;

		return (double)(a.x*b.y -a.y*b.x);

	}//--------------------------------------------------


	static public double aTan(double opp, double adj) {

		double angle = 0.0;


		if (Math.abs(adj) == 0.0)
			angle = Math.PI/2.0;
		else
			angle = Math.abs(Math.atan(opp/adj));

		// See if we are in quadrant 2 or 3.
		if (adj < 0.0) 
			// angle > PI/2 or angle < -PI/2.
			angle = Math.PI -angle;

		//' See if we are in quadrant 3 or 4.
		if (opp < 0.0)
			angle = -angle;

		return angle;

	}//---------------------------------------------------


    static public int getArea(Point a, Point e) {
		try {
			int ret = AREA0;
			int dx = e.x -a.x;
			int dy = e.y -a.y;
			if (dx < 0) {
				if (dy < 0)
					ret = AREA6;
				else if (dy == 0)
					ret = AREA5;
				else
					ret = AREA4;
			}
			else if (dx == 0) {
				if (dy < 0)
					ret = AREA7;
				else if (dy == 0)
					ret = AREA7;
				else
					ret = AREA3;
			}
			//0 < dx
			else {
				if (dy < 0)
					ret = AREA8;
				else if (dy == 0)
					ret = AREA1;
				else
					ret = AREA2;
			}
			return ret;
		} catch(Exception x) {}
		return AREA0;
	}//-----------------------------------------------------------




    //compute angle between two vectros that share a common
    //origin.
    //
    // output in degrees.
    //
    static public double getAngle(Point e0, Point a, Point e1) {

		double angle = 0.0;
		try {
			double angle0 = getAngle(a, e0)*180.0/Math.PI;
            //System.out.println("angle0 " +angle0*180.0/Math.PI);
			double angle1 = getAngle(a, e1)*180.0/Math.PI;
            //System.out.println("angle1 " +angle1*180.0/Math.PI);

			angle = (360.0 +(angle1 -angle0)) % 360.0;
            //System.out.println("angle " +angle*180.0/Math.PI);

		} catch(Exception x) {}

		return angle;

	}//-----------------------------------------------------------





    static public double getAngle(RbWall wall) {
		try {
			return getAngle(wall.getAnchor(), wall.getExtent());
		} catch(Exception x) {}

		return 0.0;
	}//-------------------------------------------------


    // input : Point a, Point e
    //
    // y coordinates are reversed.
    //
    // return value in radians.
    //
    static public double getAngle(Point a, Point e) {

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		Point anchor = new Point(a.x, dim.height -a.y);
		Point extent = new Point(e.x, dim.height -e.y);
		
		//System.out.println("Util.getAngle -------------------");
		//System.out.println("anchor (" +anchor.x +", " +anchor.y +")");
		//System.out.println("extent (" +extent.x +", " +extent.y +")");

		double angle = 0.0;
		try {

			double dy = extent.y -anchor.y;
			double dx = extent.x -anchor.x;

			if (dx < 0.0) {
				if (dy < 0.0)
					angle = Math.PI +Math.atan(dy/dx);
				else if (dy == 0.0)
					angle = Math.PI;
				else
					angle = Math.PI +Math.atan(dy/dx);
			}

			else if (dx == 0.0) {
				if (dy < 0.0)
					angle = 3*Math.PI/2.0;
				else if (dy == 0.0)
					angle = 0.0;
				else
					angle = Math.PI/2.0;
			}

			//0.0 < dx
			else {
				if (dy < 0.0)
					angle = 2*Math.PI +Math.atan(dy/dx);
				else if (dy == 0.0)
					angle = 0.0;
				else 
					angle = Math.atan(dy/dx);
			}

			//System.out.println("dx dy(" +dx +", " +dy +")");
			//System.out.println("tan(theta) = " +(dy/dx));
			//System.out.println("theta = " +Math.atan(dy/dx));

			return angle;

		} catch(Exception x) {}

		return 0.0;
	}//-------------------------------------------------





	/*
    static public double getAngle(RbWall wall) {
        return getAngle(wall.anchor, wall.extent);
    }//-------------------------------------------------

    static public double getAngle(Point anchor, Point extent) {

        double x = Math.abs(extent.x -anchor.x) +1;
        double y = Math.abs(extent.y -anchor.y) +1;

        double angle = 0;
        if (anchor.x == extent.x)
            angle = 90.0;
        else if (anchor.y == extent.y)
            angle = 0.0;
        else {
            double rad = Math.atan(y/x);
            angle = (rad*180.0/Math.PI);

            if (anchor.x < extent.x) {
                if (anchor.y < extent.y)
                    angle  = 180.0 -angle;
            } else {
                if (extent.y < anchor.y)
                    angle = 180.0 -angle;
            }
        }

        return angle;
    }//-----------------------------------------------
	*/


	/*
    static public double getAngleAlpha(Point a, Point e) {

        
        if (a.y == e.y)
            return 0.0;

        if (a.x == e.x)
            return 90.0;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Point a0 = (Point)a.clone();
        a0.y = screenSize.height -a0.y;
        Point e0 = (Point)e.clone();
        e0.y = screenSize.height -e0.y;

        double dy = e.y -a.y;
        double dx = e.x -a.x;

        double angle = Math.atan(dy/dx)*180.0/Math.PI;

        if (dy < 0.0) {
            if (0.0 < dx)
                angle += 180;
        }
        else if (dx < 0.0)
            angle += 180.0;

        return angle;

    }//-----------------------------------------------


    static public double getAngleBeta(Point p1, Point p2) {

        if (p1.y == p2.y)
            return 0.0;

        if (p1.x == p2.x)
            return 0.0;

        Point anchor = (p1.y < p2.y) ? (Point)p2.clone() : (Point)p1.clone();
        Point extent = (p1.y < p2.y) ? (Point)p1.clone() : (Point)p2.clone();

        double dx = extent.x -anchor.x;
        double dy = Math.abs(extent.y -anchor.y);

        double angle = Math.atan(dy/dx)*180.0/Math.PI;

        return (0 < dx) ? 180.0 -angle : 270.0 +angle;

    }//-----------------------------------------------
	*/






/*
    static public double getAngle(Point pre, Point anchor, Point post) {


        Point a = (Point)pre.clone();
        Point b = (Point)anchor.clone();
        Point c = (Point)post.clone();

        a.x -= b.x;
        a.y -= b.y;
        a.y *= -1;

        c.x -= b.x;
        c.y -= b.y;
        c.y *= -1;

        double dx = c.x -a.x;
        double dy = -1*(c.y -a.y);

        double angle = Math.atan(dy/dx)*180.0/Math.PI;

        return angle;
    }//-----------------------------------------------

*/


/*

    static public double getAngle2(Point anchor, Point extent) {

        double dx = extent.x -anchor.x;
        double dy = -1*(extent.y - anchor.y);

        System.out.println("getAngle2 (x, y) = (" +dx +", " +dy +")");

        double angle = Math.atan(dy/dx)*180.0/Math.PI;

        angle += 360.0;
        angle = angle % 360.0;


        return angle;
        //return Math.atan(dy/dx)*180.0/Math.PI;

    }//-----------------------------------------------


*/






    static public Point getLeftTop(Iterator itr) {
        try {
            int xmin = Integer.MAX_VALUE;
            int ymin = Integer.MAX_VALUE;
            Point p;

            while (itr.hasNext()) {

                p = (Point)itr.next();

                xmin = Math.min(p.x, xmin);
                ymin = Math.min(p.y, ymin);
            }

            return new Point(xmin, ymin);

        } catch(Exception x) {}

        return new Point(0, 0);
    }//-------------------------------------------------


    static public Point getRightBottom(Iterator itr) {
        try {
            int xmax = Integer.MIN_VALUE;
            int ymax = Integer.MIN_VALUE;
            Point p;

            while (itr.hasNext()) {

                p = (Point)itr.next();

                xmax = Math.max(xmax, p.x);
                ymax = Math.max(ymax, p.y);
            }

            return new Point(xmax, ymax);

        } catch(Exception x) {}

        return new Point(0, 0);
    }//-------------------------------------------------



    static public boolean intersection(Line2D line1, Line2D line2, Point2D pt[]) {

        if (pt == null || pt.length < 2)
            return false;

        if (!line1.intersectsLine(line2))
            return false;

        int signal;

        Point2D line1_p1 = line1.getP1();
        Point2D line1_p2 = line1.getP2();

        Point2D line2_p1 = line2.getP1();
        Point2D line2_p2 = line2.getP2();

        Point2D p1 = line1.getP1();
        Point2D p2 = line1.getP2();
        double m = (p2.getY() -p1.getY())/(p2.getX() -p1.getX());
        double b = p1.getX()*(p2.getY() -p1.getY())/(p2.getX() -p1.getX()) +p1.getY();

        double x = line1_p1.getX();
        double y = line1_p1.getY();

        int org = Line2D.relativeCCW(line2_p1.getX(), line2_p1.getY(), line2_p2.getX(), line2_p2.getY(), line1_p1.getX(), line1_p1.getY());

        double xterm = line1_p2.getX();
        if (org == 0) {
            pt[0] = new Point2D.Double(line1_p1.getX(), line1_p1.getY());

            x = x + 1.0;
            signal = org;
            while (signal == org && x < xterm) {

                y = m*x +b;

                signal = Line2D.relativeCCW(line2_p1.getX(), line2_p1.getY(), line2_p2.getX(), line2_p2.getY(), x, y);

                x = x +1.0;
            }

            if (signal != org)
                pt[1] = new Point2D.Double(x, y);

            return true;
        }

        x = x +1.0;
        signal = org;
        while ((signal != 0 || signal != org) && x < xterm) {

            y = m*x +b;

            signal = Line2D.relativeCCW(line2_p1.getX(), line2_p1.getY(), line2_p2.getX(), line2_p2.getY(), x, y);

            x = x +1.0;
        }

        if (signal == 0)
            pt[0] = new Point2D.Double(x, y);

        x = x +1.0;
        while (signal != org && x < xterm) {
            y = m*x +b;

            signal = Line2D.relativeCCW(line2_p1.getX(), line2_p1.getY(), line2_p2.getX(), line2_p2.getY(), x, y);

            x = x +1.0;
        }

        if (signal != org)
            pt[1] =  new Point2D.Double(x, y);

        return true;

    }//-----------------------------------------------------------------------------






}//end class (Util)
