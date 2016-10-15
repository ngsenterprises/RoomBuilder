/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.claymg;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JLabel;


/**
 *
 * @author woody
 */
public class RbWallHeadUpDisplay extends JPanel {

    RbWallHeadUpDisplay display = this;
    RbFloor floor;
	RbWall wall;

    JLabel angleLabel = null;
    JLabel lengthLabel = null;

	private Point anchor = new Point();
	private Point extent = new Point();

    private double angle = 0.0;

	int x,y, width, height;
    double feet, inches;

	final public int labelWidth = 50;
	final public int labelHeight = 20;

    Color fg = Color.red;


    public RbWallHeadUpDisplay(RbFloor floor, RbWall wall) {

        super(null, true);

        this.floor = floor;
		this.wall = wall;
        setOpaque(false);

        angleLabel = new JLabel("");
        angleLabel.setOpaque(false);
        add(angleLabel);
        angleLabel.setBounds(0, 0, labelWidth, labelHeight);
        angleLabel.setForeground(Color.red);
        angleLabel.setVisible(false);

        lengthLabel = new JLabel("");
        lengthLabel.setOpaque(false);
        add(lengthLabel);

		x = Math.max(0, Math.min(Math.abs((anchor.x +extent.x)/2 -labelWidth/2), floor.getWidth() -labelWidth));
		y = Math.max(0, Math.min(Math.abs((anchor.y +extent.y)/2 -labelHeight/2), floor.getHeight() -2*labelHeight));
		width = Math.max(0, Math.min(labelWidth, Math.abs(floor.getWidth() -x)));
		height = Math.max(0, Math.min(2*labelHeight, Math.abs(floor.getHeight() -y)));

		lengthLabel.setBounds(x, y, width, height);
        lengthLabel.setForeground(Color.red);
        lengthLabel.setVisible(false);

        setVisible(false);

    }//-----------------------------


	public void showDisplay(boolean show) {
		try {
			if (!show) {
				angleLabel.setVisible(false);
                lengthLabel.setVisible(false);
				setVisible(false);

				floor.repaint();
			}
			else {
				anchor = wall.getAnchor();
				extent = wall.getExtent();

				angle = Util.getAngle(anchor, extent)*180.0/Math.PI % 180.0;

				setVisible(true);
				angleLabel.setVisible(true);
                lengthLabel.setVisible(true);

				x = Math.max(0, Math.min(Math.abs((anchor.x +extent.x)/2 -labelWidth/2), floor.getWidth() -labelWidth));
				y = Math.max(0, Math.min(Math.abs((anchor.y +extent.y)/2 -labelHeight/2), floor.getHeight() -2*labelHeight));
				width = Math.max(0, Math.min(labelWidth, Math.abs(floor.getWidth() -x)));
				height = Math.max(0, Math.min(2*labelHeight, Math.abs(floor.getHeight() -y)));

				setBounds(x, y, width, height);
                angleLabel.setBounds(0, 0, labelWidth, labelHeight);
                lengthLabel.setBounds(0, labelHeight, labelWidth, labelHeight);
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

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

		if (angleLabel.isVisible())
            angleLabel.setText("\u00B0" +Double.toString(Util.getAngle(wall.getAnchor(), wall.getExtent())*180.0/Math.PI % 180.0));

		if (lengthLabel.isVisible()) {

            feet = wall.getRbLength()/12.0;
            inches = wall.getRbLength()%12.0;
                        
            lengthLabel.setText(Integer.toString((int)feet) +"\u0027 " +Integer.toString((int)inches) +"\u0027\u0027");
        }
    }//-------------------------------



}//end class (RbWallHeadUpDisplay)
