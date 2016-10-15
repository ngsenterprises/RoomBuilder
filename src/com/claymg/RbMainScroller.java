/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.claymg;

import java.awt.Component;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;


/**
 *
 * @author woody
 */
public class RbMainScroller extends JScrollPane implements ComponentListener {

	JPanel mainPanel = null;

	public RbMainScroller(JPanel panel) {
		super(RbMainComponent.getRbInstance(panel), ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
													ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel = panel;

        mainPanel.addComponentListener(this);
        //addComponentListener(this);

	}//--------------------------------------------



	/////////////////////////////////////////////

	@Override
    public void componentResized(ComponentEvent e) {
        //System.out.println("componentResized [" +e.getSource() +"]");
		 Object src = e.getSource();

        if (src == mainPanel)
            setBounds(mainPanel.getBounds());
    }//----------------------------------------------------


    @Override public void componentMoved(ComponentEvent e) {}
    @Override public void componentShown(ComponentEvent e) {}
    @Override public void componentHidden(ComponentEvent e) {}


    ////////////////////////////////////////////////////////////





}//end class (RbMainScroller)
