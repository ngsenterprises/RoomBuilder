/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.claymg;


import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.BorderFactory;


/**
 *
 * @author woody
 */
public class RbLocator extends JPanel {

    public static int LINE_TYPE = 0;
    public static int RECT_TYPE = 1;


    private int access = 0;


    RbLocator locator  = this;
    JPanel parent = null;

    public Point anchor = new Point();
    public Point extent = new Point();

    public Point lt = new Point();
    public Point rb = new Point();

    public int type = LINE_TYPE;
    public Color locatorActiveFg = Color.red;
    public Color locatorListedFg = Color.green;

    private Color tcolor = Color.GREEN;




    public boolean bLocatorAdded = false;
	public boolean bLocatorListed = false;

    ///////////////////////////////////////////

    public RbLocator(JPanel parent) {

        this(parent, LINE_TYPE);

    }//-------------------------------



    public int getAccess() {

        return access;

    }



    public RbLocator(JPanel parent, int type) {

        super(null, true);

        this.parent = parent;

        setType(type);
        setOpaque(false);
        setVisible(true);

        //setBorder(BorderFactory.createLineBorder(Color.yellow));

    }//-------------------------------


    public int getType() {

        return type;

    }//---------------------------------


    public void setType(int type) {

        this.type = type;

        repaint();

    }//---------------------------------



    public void test(RbLocator l) {

        System.out.println("test -------------------------");

        System.out.println("color = " +l.tcolor);
        l.tcolor = Color.BLUE;
        System.out.println("color = " +l.tcolor);

        System.out.println("this.color = " +this.tcolor);
    }



    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Color orgColor = g.getColor();

        if (type == LINE_TYPE) {

			if (bLocatorListed)
				g.setColor(locatorListedFg);
			else
				g.setColor(locatorActiveFg);


            /*
            if (parent instanceof RbMainComponent) {

                RbMainComponent p = ((RbMainComponent)parent);

                Color fg = p.fg;

                p.fg = Color.GREEN;

                System.out.println("-----------------------------");
                System.out.println("fg = " +fg);

                fg = Color.BLUE;
                System.out.println("fg = " +fg);

                System.out.println("((RbMainComponent)parent).getFG() = " +p.getFG());

            }
            */


            if (anchor.x < extent.x) {
                if (anchor.y < extent.y)
                    g.drawLine(0, 0, getWidth() -1, getHeight() -1);
                else
                    g.drawLine(0, getHeight() -1, getWidth() -1, 0);
            }
            else {
                if (anchor.y < extent.y)
                    g.drawLine(getWidth() -1, 0, 0, getHeight() -1);
                else
                    g.drawLine(0, 0, getWidth() -1, getHeight() -1);
            }
        } 
		else if (type == RECT_TYPE ) {
			
			if (bLocatorListed)
				g.setColor(locatorListedFg);
			else
				g.setColor(locatorActiveFg);
			
            g.drawLine(0, 0, getWidth() -1, 0);
            g.drawLine(getWidth() -1, 0, getWidth() -1, getHeight() -1);
            g.drawLine(0, 0, 0, getHeight() -1);
            g.drawLine(0, getHeight() -1, getWidth() -1, getHeight() -1);
		}

        if (orgColor != null)
            g.setColor(orgColor);
    }//-----------------------------------------


	public void setAsListed(boolean listed) {

		bLocatorListed = listed;

	}//------------------------------------------



    public boolean isLocatorSet() {

        return bLocatorAdded;

    }//------------------------------------



    public void unsetLocator() {

        if (bLocatorAdded) {
            locator.setVisible(false);
            parent.remove(locator);
            bLocatorAdded = false;
        }

    }//------------------------------------


    public void setLocator(Point extent) {

        this.extent.x = Math.max(0, Math.min(extent.x, parent.getWidth() -1));
        this.extent.y = Math.max(0, Math.min(extent.y, parent.getHeight() -1));

        lt.x = Math.min(anchor.x, extent.x);
        lt.y = Math.min(anchor.y, extent.y);
        rb.x = Math.max(anchor.x, extent.x);
        rb.y = Math.max(anchor.y, extent.y);

        this.setBounds(lt.x, lt.y, rb.x -lt.x +1, rb.y -lt.y +1);
        this.setVisible(true);

        if (!bLocatorAdded) {
            parent.add(locator);
            bLocatorAdded = true;
        }
        
    }//------------------------------------



    public void setAnchor(Point anchor) {

        this.anchor = (Point)anchor.clone();
        this.anchor.x = Math.max(0, Math.min(anchor.x, parent.getWidth() -1));
        this.anchor.y = Math.max(0, Math.min(anchor.y, parent.getHeight() -1));

    }//-----------------------------



    public Point getAnchor() {

        return (Point)anchor.clone();
    }//-----------------------------

    public Point getExtent() {

        return (Point)extent.clone();
    }//-----------------------------



}//end class (RbLocator)
