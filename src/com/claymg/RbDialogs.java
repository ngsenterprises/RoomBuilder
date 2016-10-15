/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.claymg;


import java.awt.Frame;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Point;


import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import javax.swing.JDialog;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.BorderFactory;




/**
 *
 * @author woody
 */
public class RbDialogs {



    public static RbWallAngleDialog getWallAngleDialog() {

        if (RbWallAngleDialog.wallAngleDialog == null)
            RbWallAngleDialog.wallAngleDialog = new RbWallAngleDialog();

        return RbWallAngleDialog.wallAngleDialog;
    }//-----------------------------------------------------

    static public class RbWallAngleDialog extends JDialog implements ActionListener {

        static RbWallAngleDialog wallAngleDialog = null;

        RbWall wall;

        private final int fieldwidth = 75;
        private final int fieldheight = 20;
        private final int btnwidth = 75;
        private final int btnheight = 20;
        private final int vstrut = 10;
        private final int hstrut = 5;


        JLabel label;
        JSpinner angleSpin;

        JButton btnCancel;
        JButton btnApply;


        public RbWallAngleDialog() {

            super((Frame)null, "Set Wall Angle", false);

            getContentPane().add(getMainBox());
            pack();

            setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        }//-----------------------------------------


        private Box getMainBox() {

            Box mainBox = Box.createVerticalBox();

            mainBox.add(getLabelBox());
            mainBox.add(Box.createVerticalStrut(vstrut));

            mainBox.add(getAngleFieldBox());
            mainBox.add(Box.createVerticalStrut(vstrut));

            mainBox.add(getButtonBox());
            mainBox.add(Box.createVerticalStrut(vstrut));

            mainBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            return mainBox;

        }//------------------------------




        private Box getButtonBox() {

            Box buttonBox = Box.createHorizontalBox();

            btnCancel = new JButton("Cancel");
            btnCancel.setSize(new Dimension(btnwidth, btnheight));
            btnCancel.setPreferredSize(new Dimension(btnwidth, btnheight));
            btnCancel.setMinimumSize(new Dimension(btnwidth, btnheight));
            btnCancel.setMaximumSize(new Dimension(btnwidth, btnheight));
            btnCancel.addActionListener(this);

            buttonBox.add(btnCancel);
            buttonBox.add(Box.createHorizontalGlue());
            buttonBox.add(Box.createHorizontalStrut(btnwidth));

            btnApply = new JButton("Apply");
            btnApply.setSize(new Dimension(btnwidth, btnheight));
            btnApply.setPreferredSize(new Dimension(btnwidth, btnheight));
            btnApply.setMaximumSize(new Dimension(btnwidth, btnheight));
            btnApply.setMinimumSize(new Dimension(btnwidth, btnheight));
            btnApply.addActionListener(this);
            buttonBox.add(btnApply);
            buttonBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

            return buttonBox;

        }//---------------------------------



        private Box getAngleFieldBox() {

            Box angleBox = Box.createHorizontalBox();

            SpinnerNumberModel model = new SpinnerNumberModel(Integer.valueOf(0),
                                                              Integer.valueOf(0),
                                                              Integer.valueOf(0),
                                                              Integer.valueOf(0));
            angleSpin = new JSpinner(model);
            angleSpin.setPreferredSize(new Dimension(fieldwidth, fieldheight));
            angleSpin.setSize(new Dimension(fieldwidth, fieldheight));
            angleSpin.setMinimumSize(new Dimension(fieldwidth, fieldheight));
            angleSpin.setMaximumSize(new Dimension(fieldwidth, fieldheight));
            angleBox.add(angleSpin);
            //angleBox.add(Box.createHorizontalGlue());

            return angleBox;

        }//---------------------------------

        private Box getLabelBox() {

            Box labelBox = Box.createHorizontalBox();

            label = new JLabel();
//            Dimension dim = new Dimension(fieldwidth, fieldheight);
//            label.setPreferredSize(dim);
//            label.setMaximumSize(dim);
//            label.setMinimumSize(dim);
//            label.setSize(dim);

            labelBox.add(label);
            //labelBox.add(Box.createHorizontalGlue());

            return labelBox;

        }//---------------------------------



        public void setVisible(boolean vis) {

            System.out.println("setVisible " +vis +" wall " +wall);

            try {
                if (wall != null)
                    wall.highLightWall(vis);
            } catch(Exception x) {}

            super.setVisible(vis);

        }//--------------------------------------



        public boolean showDialog(RbWall wall) {
            try {
                if (wall != null) {

                    this.wall = wall;
                    init(wall);

                    Point loc = wall.getLocationOnScreen();
                    setLocation(loc.x, loc.y);

                    setVisible(true);
                }
            } catch(Exception x) {}

            return false;
        }//---------------------------------------------




        private void init(RbWall wall) {
            try {
                if (wall != null) {

                    Point anchor = wall.getAnchor();
                    Point extent = wall.getExtent();

                    Point base = (anchor.y < extent.y) ? anchor : extent;
                    Point rotate = (anchor.y < extent.y) ? extent : anchor;
                    double angle = Util.getAngle(base, rotate);

                    if (label != null) {

                        label.setText("0.0 <= angle <= 180 ");

                        /*
                        if (angle < 90.0)
                            label.setText("0.0 <= angle < 180 ");
                        else if (angle == 90.0)
                            label.setText("0.0 <= angle <= 180 ");
                        else if (90.0 < angle)
                            label.setText("0.0 < angle <= 180 ");
                         */
                    }

                    if (angleSpin != null) {
                        SpinnerNumberModel model = new SpinnerNumberModel(Double.valueOf(angle),
                                                                          Double.valueOf(0.0),
                                                                          Double.valueOf(180.0),
                                                                          Double.valueOf(1.0));
                        angleSpin.setModel(model);
                    }
                }
            } catch(Exception x) {}
        }//--------------------------------


        @Override
        public void actionPerformed(ActionEvent e) {

            System.out.println("actionPerformed " +e.getSource() +"-------------");

            if (e.getSource() == btnApply) {

                Double angle = (Double)angleSpin.getModel().getValue();
                //System.out.println("current angle = " +angle);

                if (wall != null) 
                    wall.setAngle(angle.doubleValue());
            }

            setVisible(false);
        }//-----------------------------------------------


        public void dispose() {

            wall = null;
            super.dispose();
        }//---------------------------




    }//end class (RbWallDialog)






    static public RbWallDialog getRbWallDialog() {

        if (RbWallDialog.rbWallDialog == null)
            RbWallDialog.rbWallDialog = new RbWallDialog();

        return RbWallDialog.rbWallDialog;
    }//---------------------------------------------------


    static public class RbWallDialog extends JDialog implements ActionListener {

        static RbWallDialog rbWallDialog = null;

        Frame owner = null;
        RbWall wall = null;

        JSpinner lengthFeetSpin;
        JSpinner lengthInchSpin;
        JSpinner widthFeetSpin;
        JSpinner widthInchSpin;
        JSpinner heightFeetSpin;
        JSpinner heightInchSpin;
        JSpinner angleSpin;

        JButton btnCancel;
        JButton btnApply;

        private final int fieldwidth = 75;
        private final int fieldheight = 20;
        private final int btnwidth = 75;
        private final int btnheight = 20;
        private final int vstrut = 10;
        private final int hstrut = 5;


        public RbWallDialog() {

            super((Frame)null, "Set Wall Dimensions", true);

            getContentPane().add(getMainBox());
            pack();

            setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        }//-----------------------------------------



        /*
        public void setVisible(boolean vis) {

            System.out.println("setVisible " +vis);

            wall.highLightWall(vis);

            super.setVisible(vis);

        }//--------------------------------------




        public boolean showDialog(RbWall wall) {
            try {
                if (wall != null) {

                    this.wall = wall;

                    init(wall);

                    Point loc = wall.getLocationOnScreen();
                    setLocation(loc.x, loc.y);

                    setVisible(true);
                }
            } catch(Exception x) {}

            return false;
        }//---------------------------------------------
        */



        public void setVisible(boolean vis) {

            //System.out.println("setVisible " +vis +" wall " +wall);

            try {
                if (wall != null)
                    wall.highLightWall(vis);
            } catch(Exception x) {}

            super.setVisible(vis);

        }//--------------------------------------



        public boolean showDialog(RbWall wall) {
            try {
                if (wall != null) {

                    this.wall = wall;
                    init(wall);

                    Point loc = wall.getLocationOnScreen();
                    setLocation(loc.x, loc.y);

                    setVisible(true);
                }
            } catch(Exception x) {}

            return false;
        }//---------------------------------------------







        private void init(RbWall wall) {

            if (wall != null) {
                if (heightFeetSpin != null) {
                    SpinnerNumberModel model = new SpinnerNumberModel(Double.valueOf(wall.getRbHeight()/12.0),
                                                                      Double.valueOf(0.0),
                                                                      Double.valueOf(20.0),
                                                                      Double.valueOf(1.0));

                    heightFeetSpin.setModel(model);
                }

                if (heightInchSpin != null) {
                    SpinnerNumberModel model = new SpinnerNumberModel(Double.valueOf(wall.getRbHeight()%12),
                                                                      Double.valueOf(0.0),
                                                                      Double.valueOf(11.0),
                                                                      Double.valueOf(1.0));
                    heightInchSpin = new JSpinner(model);
                }

                if (widthFeetSpin != null) {
                    SpinnerNumberModel model = new SpinnerNumberModel(Double.valueOf(0.0),
                                                                      Double.valueOf(0.0),
                                                                      Double.valueOf(0.0),
                                                                      Double.valueOf(1.0));
                    widthFeetSpin.setModel(model);
                }

                if (widthInchSpin != null) {
                    SpinnerNumberModel model = new SpinnerNumberModel(Double.valueOf(1.0),
                                                                      Double.valueOf(1.0),
                                                                      Double.valueOf(1.0),
                                                                      Double.valueOf(1.0));
                    widthInchSpin = new JSpinner(model);
                }



                if (lengthFeetSpin != null) {
                    SpinnerNumberModel model = new SpinnerNumberModel(Double.valueOf(wall.getRbLength()/12.0),
                                                                      Double.valueOf(0.0),
                                                                      Double.valueOf(5000.0),
                                                                      Double.valueOf(1.0));
                    lengthFeetSpin.setModel(model);
                }

                if (lengthInchSpin != null) {
                    SpinnerNumberModel model = new SpinnerNumberModel(Double.valueOf(wall.getRbLength()%12.0),
                                                                      Double.valueOf(0.0),
                                                                      Double.valueOf(11.0),
                                                                      Double.valueOf(1.0));
                    lengthInchSpin.setModel(model);
                }


                /*
                if (angleSpin != null) {
                    SpinnerNumberModel model = new SpinnerNumberModel(Integer.valueOf((int)Util.getAngle(wall)),
                                                                      Integer.valueOf(0),
                                                                      Integer.valueOf(180),
                                                                      Integer.valueOf(1));
                    angleSpin.setModel(model);
                }
                */


                if (btnCancel != null) {
                    btnCancel.addActionListener(this);
                }

                if (btnApply != null) {
                    btnApply.addActionListener(this);
                }
            }
        }//--------------------------------




        private Box getMainBox() {

            Box mainBox = Box.createVerticalBox();

            //mainBox.add(getAngleBox());
            //mainBox.add(Box.createVerticalStrut(vstrut));

            mainBox.add(getDimensionBox());
            mainBox.add(Box.createVerticalStrut(vstrut));

            mainBox.add(getButtonBox());
            mainBox.add(Box.createVerticalStrut(vstrut));

            mainBox.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

            return mainBox;

        }//------------------------------




        private Box getButtonBox() {

            Box buttonBox = Box.createHorizontalBox();

            btnCancel = new JButton("Cancel");
            btnCancel.setSize(new Dimension(btnwidth, btnheight));
            btnCancel.setPreferredSize(new Dimension(btnwidth, btnheight));
            btnCancel.setMinimumSize(new Dimension(btnwidth, btnheight));
            btnCancel.setMaximumSize(new Dimension(btnwidth, btnheight));
            //btnCancel.addActionListener(dialog);

            buttonBox.add(btnCancel);
            buttonBox.add(Box.createHorizontalGlue());

            btnApply = new JButton("Apply");
            btnApply.setSize(new Dimension(btnwidth, btnheight));
            btnApply.setPreferredSize(new Dimension(btnwidth, btnheight));
            btnApply.setMaximumSize(new Dimension(btnwidth, btnheight));
            btnApply.setMinimumSize(new Dimension(btnwidth, btnheight));
            //btnApply.addActionListener(dialog);
            buttonBox.add(btnApply);

            return buttonBox;

        }//---------------------------------





        private Box getDimensionBox() {

            Box dimensionBox = Box.createVerticalBox();

            dimensionBox.add(getDimensionLabelBox());
            dimensionBox.add(getDimensionLengthBox());
            dimensionBox.add(getDimensionWidthBox());
            dimensionBox.add(getDimensionHeightBox());

            return dimensionBox;

        }//---------------------------------



        private Box getDimensionHeightBox() {

            Box dimensionHeightBox = Box.createHorizontalBox();

            JLabel lbl = new JLabel("Height");
            lbl.setSize(new Dimension(fieldwidth, fieldheight));
            lbl.setPreferredSize(new Dimension(fieldwidth, fieldheight));
            lbl.setMinimumSize(new Dimension(fieldwidth, fieldheight));
            lbl.setMaximumSize(new Dimension(fieldwidth, fieldheight));
            dimensionHeightBox.add(lbl);

            SpinnerNumberModel model = new SpinnerNumberModel(Integer.valueOf(0),
                                                              Integer.valueOf(0),
                                                              Integer.valueOf(0),
                                                              Integer.valueOf(0));
            heightFeetSpin = new JSpinner(model);
            heightFeetSpin.setSize(new Dimension(fieldwidth, fieldheight));
            heightFeetSpin.setPreferredSize(new Dimension(fieldwidth, fieldheight));
            heightFeetSpin.setMaximumSize(new Dimension(fieldwidth, fieldheight));
            heightFeetSpin.setMinimumSize(new Dimension(fieldwidth, fieldheight));
            dimensionHeightBox.add(heightFeetSpin);
            dimensionHeightBox.add(Box.createHorizontalStrut(hstrut));

            model = new SpinnerNumberModel(Integer.valueOf(0),
                                           Integer.valueOf(0),
                                           Integer.valueOf(0),
                                           Integer.valueOf(0));
            heightInchSpin = new JSpinner(model);
            heightInchSpin.setSize(new Dimension(fieldwidth, fieldheight));
            heightInchSpin.setPreferredSize(new Dimension(fieldwidth, fieldheight));
            heightInchSpin.setMaximumSize(new Dimension(fieldwidth, fieldheight));
            heightInchSpin.setMinimumSize(new Dimension(fieldwidth, fieldheight));
            dimensionHeightBox.add(heightInchSpin);
            dimensionHeightBox.add(Box.createHorizontalStrut(hstrut));

            return dimensionHeightBox;

        }//---------------------------------





        private Box getDimensionWidthBox() {

            Box dimensionWidthBox = Box.createHorizontalBox();

            JLabel lbl = new JLabel("Width");
            lbl.setSize(new Dimension(fieldwidth, fieldheight));
            lbl.setPreferredSize(new Dimension(fieldwidth, fieldheight));
            lbl.setMinimumSize(new Dimension(fieldwidth, fieldheight));
            lbl.setMaximumSize(new Dimension(fieldwidth, fieldheight));
            dimensionWidthBox.add(lbl);

            SpinnerNumberModel model = new SpinnerNumberModel(Integer.valueOf(0),
                                                              Integer.valueOf(0),
                                                              Integer.valueOf(0),
                                                              Integer.valueOf(1));
            widthFeetSpin = new JSpinner(model);
            widthFeetSpin.setSize(new Dimension(fieldwidth, fieldheight));
            widthFeetSpin.setPreferredSize(new Dimension(fieldwidth, fieldheight));
            widthFeetSpin.setMinimumSize(new Dimension(fieldwidth, fieldheight));
            widthFeetSpin.setMaximumSize(new Dimension(fieldwidth, fieldheight));
            widthFeetSpin.setEnabled(false);
            dimensionWidthBox.add(widthFeetSpin);
            dimensionWidthBox.add(Box.createHorizontalStrut(hstrut));

            model = new SpinnerNumberModel(Integer.valueOf(1),
                                           Integer.valueOf(1),
                                           Integer.valueOf(1),
                                           Integer.valueOf(1));
            widthInchSpin = new JSpinner(model);
            widthInchSpin.setSize(new Dimension(fieldwidth, fieldheight));
            widthInchSpin.setPreferredSize(new Dimension(fieldwidth, fieldheight));
            widthInchSpin.setMaximumSize(new Dimension(fieldwidth, fieldheight));
            widthInchSpin.setMinimumSize(new Dimension(fieldwidth, fieldheight));
            widthInchSpin.setEnabled(false);
            dimensionWidthBox.add(widthInchSpin);
            dimensionWidthBox.add(Box.createHorizontalStrut(hstrut));

            //dimensionWidthBox.setAlignmentX(Component.LEFT_ALIGNMENT);

            return dimensionWidthBox;

        }//---------------------------------




        private Box getDimensionLengthBox() {

            Box dimensionLengthBox = Box.createHorizontalBox();

            JLabel lbl = new JLabel("Length");
            lbl.setSize(new Dimension(fieldwidth, fieldheight));
            lbl.setPreferredSize(new Dimension(fieldwidth, fieldheight));
            lbl.setMinimumSize(new Dimension(fieldwidth, fieldheight));
            lbl.setMaximumSize(new Dimension(fieldwidth, fieldheight));
            dimensionLengthBox.add(lbl);

            SpinnerNumberModel model = new SpinnerNumberModel(Integer.valueOf(0),
                                                              Integer.valueOf(0),
                                                              Integer.valueOf(0),
                                                              Integer.valueOf(0));
            lengthFeetSpin = new JSpinner(model);
            lengthFeetSpin.setSize(new Dimension(fieldwidth, fieldheight));
            lengthFeetSpin.setPreferredSize(new Dimension(fieldwidth, fieldheight));
            lengthFeetSpin.setMinimumSize(new Dimension(fieldwidth, fieldheight));
            lengthFeetSpin.setMaximumSize(new Dimension(fieldwidth, fieldheight));
            dimensionLengthBox.add(lengthFeetSpin);
            dimensionLengthBox.add(Box.createHorizontalStrut(hstrut));

            model = new SpinnerNumberModel(Integer.valueOf(0),
                                           Integer.valueOf(0),
                                           Integer.valueOf(0),
                                           Integer.valueOf(0));
            lengthInchSpin = new JSpinner(model);
            lengthInchSpin.setSize(new Dimension(fieldwidth, fieldheight));
            lengthInchSpin.setPreferredSize(new Dimension(fieldwidth, fieldheight));
            lengthInchSpin.setMaximumSize(new Dimension(fieldwidth, fieldheight));
            lengthInchSpin.setMinimumSize(new Dimension(fieldwidth, fieldheight));
            dimensionLengthBox.add(lengthInchSpin);
            dimensionLengthBox.add(Box.createHorizontalStrut(hstrut));

            //dimensionLengthBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            return dimensionLengthBox;

        }//---------------------------------


        private Box getDimensionLabelBox() {

            Box dimensionLabelBox = Box.createHorizontalBox();

            dimensionLabelBox.add(Box.createRigidArea(new Dimension(fieldwidth, fieldheight)));

            JLabel lbl = new JLabel("Feet");
            lbl.setPreferredSize(new Dimension(fieldwidth, fieldheight));
            lbl.setMaximumSize(new Dimension(fieldwidth, fieldheight));
            lbl.setMinimumSize(new Dimension(fieldwidth, fieldheight));
            lbl.setSize(new Dimension(fieldwidth, fieldheight));
            dimensionLabelBox.add(lbl);

            lbl = new JLabel("Inches");
            lbl.setPreferredSize(new Dimension(fieldwidth, fieldheight));
            lbl.setMaximumSize(new Dimension(fieldwidth, fieldheight));
            lbl.setMinimumSize(new Dimension(fieldwidth, fieldheight));
            lbl.setSize(new Dimension(fieldwidth, fieldheight));
            dimensionLabelBox.add(lbl);

            return dimensionLabelBox;

        }//---------------------------------














        /////////////////////////////////////////////////



        private Box getAngleBox() {

            Box angleBox = Box.createHorizontalBox();

            angleBox.add(new JLabel("Angle in degrees "));
            angleBox.add(new JLabel("0 <= "));

            SpinnerNumberModel model = new SpinnerNumberModel(Integer.valueOf(0),
                                                              Integer.valueOf(0),
                                                              Integer.valueOf(0),
                                                              Integer.valueOf(0));
            angleSpin = new JSpinner(model);
            angleSpin.setPreferredSize(new Dimension(fieldwidth, fieldheight));
            angleSpin.setSize(new Dimension(fieldwidth, fieldheight));
            angleSpin.setMinimumSize(new Dimension(fieldwidth, fieldheight));
            angleSpin.setMaximumSize(new Dimension(fieldwidth, fieldheight));
            angleBox.add(angleSpin);

            angleBox.add(new JLabel(" < 180"));
           // angleBox.setAlignmentX(Component.LEFT_ALIGNMENT);

            return angleBox;

        }//---------------------------------



        @Override
        public void actionPerformed(ActionEvent e) {

            System.out.println("actionPerformed " +e.getSource() +"-------------");

            if (e.getSource() == btnApply) {

                Integer feet = (Integer)widthFeetSpin.getModel().getValue();
                Integer inches = (Integer)widthInchSpin.getModel().getValue();

                int width = feet.intValue()*12 +inches.intValue();

                feet = (Integer)lengthFeetSpin.getModel().getValue();
                inches = (Integer)lengthInchSpin.getModel().getValue();

                int length = feet.intValue()*12 +inches.intValue();

                feet = (Integer)heightFeetSpin.getModel().getValue();
                inches = (Integer)heightInchSpin.getModel().getValue();

                int height = feet.intValue()*12 +inches.intValue();

                Integer angle = (Integer)angleSpin.getModel().getValue();
                //System.out.println("current angle = " +angle);

                if (wall != null) {

                    wall.setRbHeight(height);
                    wall.setRbWidth(width);
                    wall.setRbLength(length);

                    wall.setAngle(angle.intValue());
                }
            }
            dispose();

        }//-----------------------------------------------


        public void dispose() {

            System.out.println("dispose ----------------");

            wall.highLightWall(false);
            setVisible(false);
            rbWallDialog = null;

            super.dispose();

        }//--------------------------




        /*
        public void componentMoved(ComponentEvent e) {}
        public void componentResized(ComponentEvent e) {}


        public void componentHidden(ComponentEvent e) {

            
            if (wall != null)
                wall.highLightWall(false);
            
        }//-----------------------------------------------

        public void componentShown(ComponentEvent e) {
            
            if (wall != null)
                wall.highLightWall(true);
            
        }//----------------------------------------
        */






    }//end class (RbWallDialog)















}//end class (RbDialogs)
