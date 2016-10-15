/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.claymg;

import java.lang.Integer;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 *
 * @author woody
 */
public class RbAngleDisplay extends JPanel {

    JPanel display = this;
    RbFloor floor;
	RbWall wall;

    JLabel label = null;

	private Point anchor = new Point();
	private Point extent = new Point();

    private double angle = 0.0;
    
    //private Point anchor = new Point();

	int x,y, width, height;

	final public int displayWidth = 50;
	final public int displayHeight = 20;


    public RbAngleDisplay(RbFloor floor, RbWall wall) {

        super(null, true);

        this.floor = floor;
		this.wall = wall;
        setOpaque(false);

        label = new JLabel("");
        label.setOpaque(false);
        add(label);
        label.setBounds(0, 0, 50, 20);
        label.setForeground(Color.red);
        label.setVisible(false);

        setVisible(false);

    }//-----------------------------


	public void showAngle(boolean show) {
		try {
			if (!show) {
				label.setVisible(false);
				setVisible(false);

				floor.repaint();
			}
			else {

				anchor = wall.getAnchor();
				extent = wall.getExtent();

				angle = Util.getAngle(anchor, extent)*180.0/Math.PI;

				/*
				if (anchor.y < extent.y)
					angle = Util.getAngle(extent, anchor);
				else
					angle = Util.getAngle(anchor, extent);
				*/
				setVisible(true);
				label.setVisible(true);

				x = Math.max(0, Math.min(Math.abs((anchor.x +extent.x)/2 -displayWidth/2), floor.getWidth() -displayWidth));
				y = Math.max(0, Math.min(Math.abs((anchor.y +extent.y)/2 -displayHeight/2), floor.getHeight() -displayHeight));
				width = Math.max(0, Math.min(displayWidth, Math.abs(floor.getWidth() -x)));
				height = Math.max(0, Math.min(displayHeight, Math.abs(floor.getHeight() -y)));

				setBounds(x, y, width, height);
				label.setBounds(0, 0, getWidth(), getHeight());
			}
		} catch(Exception x) {}
	}//-----------------------------------------------------------



/*
    public void setPoints(Point anchor, Point extent) {
        angle = (int)Util.getAngle(anchor, extent);
        label.setText(Integer.toString(angle));
        repaint();
    }//-------------------------------------

*/

    public void paintComponent(Graphics g) {
    
        super.paintComponent(g);

		
		if (label.isVisible()) {
            label.setText("\u00B0" +Double.toString(Util.getAngle(wall.getExtent(), wall.getAnchor())*180.0/Math.PI));
        }
		

    }//-------------------------------






}//end classs (RbAngleDisplay)
