/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.claymg;

import java.util.Iterator;


import java.awt.Component;
import java.awt.Container;

import javax.swing.JPopupMenu;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.ButtonGroup;


import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;



/**
 *
 * @author woody
 */
public class RbPopupMenu extends JPopupMenu
                         implements PopupMenuListener
{

    static RbPopupMenu rbPopupMenu = null;

    //RbMainComponent view = null;
	Component invoker = null;

    private JMenu chooseLocatorMenu;
    private JRadioButtonMenuItem lineLocatorMI;
    private JRadioButtonMenuItem rectLocatorMI;
    private JMenuItem createFloorMI;
    private JMenuItem createItemMI;

    private JMenuItem deleteFloorMI = null;
    private JMenuItem addExteriorWallsMI = null;
	private JMenuItem setWallValuesMI = null;

	private JMenuItem setWallAngleMI = null;
	private JMenuItem deleteItemMI = null;
	private JMenuItem addToRoomMI = null;


	private JCheckBoxMenuItem lockWallDimensionsMI = null;

    private JMenu		scaleMenu;
	private JMenuItem	zoomInMI = null;
	private JMenuItem	zoomOutMI = null;


	public RbPopupMenu() {

		super();

		//this.view = view;

		addPopupMenuListener(this);

	}//-------------------------


    static public RbPopupMenu getPopupMenu() {

        if (rbPopupMenu == null)
            rbPopupMenu = new RbPopupMenu();

        rbPopupMenu.removeAll();

        return rbPopupMenu;
    }//--------------------------------------





    @Override
    public void show(Component invoker, int x, int y) {
        try {
            if (invoker != null) {

                this.invoker = invoker;

                if (invoker instanceof RbMainComponent) {

                    RbMainComponent view = (RbMainComponent)invoker;

                    add(getChooseLocatorMenu());

                    Iterator itrLocator = view.getLocatorListItr();
                    if (itrLocator.hasNext()) {
                        Iterator itrFloor = view.getFloorListItr();
                        if (!itrFloor.hasNext())
                            add(getCreateFloorMI());
                        else
                            add(getCreateItemMI());
                    }
					add(getScaleMenu(view));
                }
                else if (invoker instanceof RbFloor) {

                    RbMainComponent view = ((RbFloor)invoker).getView();

					add(getScaleMenu(view));
					add(getDeleteFloorMI());
                    //add(getAddExteriorWallsMI());
                }
                else if (invoker instanceof RbWall) {


					add(getLockWallDimensionsMI());

                    //add(getWallValuesMI());
                    //add(getWallAngleMI());
                }
                else if (invoker instanceof RbItem) {
                    add(getDeleteItemMI());
                }

                super.show(invoker, x, y);
            }
        } catch(Exception ex) {}

	}//--------------------------------------------------



    ///////////////////////////////////////////////////////


    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

		Object obj = e.getSource();

		if (invoker instanceof RbWall) {
			((RbWall)invoker).wall.highLightWall(true);
		}


	}//--------------------------------------------------


    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		Object obj = e.getSource();

		if (invoker instanceof RbWall) {
			((RbWall)invoker).highLightWall(false);
		}

	}//-----------------------------------------------------

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {

	}//--------------------------------------------



    /////////////////////////////////////////////////////////////




    public JRadioButtonMenuItem getLineLocatorMI() {

        if (lineLocatorMI == null) {
            lineLocatorMI = new JRadioButtonMenuItem("Line Locator");

            lineLocatorMI.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (invoker != null && invoker instanceof RbMainComponent) {
                        RbMainComponent view = (RbMainComponent)invoker;
                        view.setLocatorType(RbLocator.LINE_TYPE);
                        lineLocatorMI.setSelected(view.getLocatorType() == RbLocator.LINE_TYPE);
                    }
                }//..............................................
            });

        }

        return lineLocatorMI;
    }//-------------------------------------------

    public JRadioButtonMenuItem getRectLocatorMI() {

        if (rectLocatorMI == null) {
            rectLocatorMI = new JRadioButtonMenuItem("Rectangle Locator");

            rectLocatorMI.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (invoker != null && invoker instanceof RbMainComponent) {
                        RbMainComponent view = (RbMainComponent)invoker;
                        view.setLocatorType(RbLocator.RECT_TYPE);
                        rectLocatorMI.setSelected(view.getLocatorType() == RbLocator.RECT_TYPE);
                    }
                }//..............................................
            });
        }

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
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (invoker != null && invoker instanceof RbMainComponent) {
                        RbMainComponent view = (RbMainComponent)invoker;
                        view.createFloor();
                    }
                }
            });
        }

        return createFloorMI;
    }//-------------------------------------------






    private JMenuItem getCreateItemMI() {

        if (createItemMI == null) {
            createItemMI = new JMenuItem("Create Item");

            createItemMI.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (invoker != null && invoker instanceof RbMainComponent) {
                        RbMainComponent view = (RbMainComponent)invoker;
                        view.createItem();
                    }
                }
            });
        }

        return createItemMI;
    }//-------------------------------------------






    private JMenuItem getDeleteFloorMI() {

        if (deleteFloorMI == null) {
            deleteFloorMI = new JMenuItem("Delete Floor");

            deleteFloorMI.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (invoker != null && invoker instanceof RbFloor) {
                        RbFloor floor = (RbFloor)invoker;
                        RbMainComponent view = floor.getView();
                        if (view != null) {
                            view.deleteFloor(floor);
                        }
                    }
                }//..............................................
            });
        }

        return deleteFloorMI;
    }//------------------------------------------


	/*
    private JMenuItem getAddExteriorWallsMI() {
        if (addExteriorWallsMI == null) {
            addExteriorWallsMI = new JMenuItem("Add Exterior Walls");

            addExteriorWallsMI.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (invoker instanceof RbFloor) {
                        //view.addExteriorWalls((RbFloor)invoker);
                    }
                }//..............................................
            });
        }

        return addExteriorWallsMI;
    }//------------------------------------------
	*/


    private JMenuItem getWallValuesMI() {

		if (setWallValuesMI == null) {
			setWallValuesMI = new JMenuItem("Set Wall Dimensins");

			setWallValuesMI.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (invoker instanceof RbWall) {

						RbWall wall = (RbWall)invoker;

						RbDialogs.RbWallDialog dlg = RbDialogs.getRbWallDialog();
						dlg.showDialog(wall);

						//((RbWall)invoker).getWallValues();
					}
				}//..............................................
			});
		}

        return setWallValuesMI;
    }//------------------------------------------


    private JMenuItem getWallAngleMI() {

		if (setWallAngleMI == null) {
			setWallAngleMI = new JMenuItem("Set Wall Angle");

			setWallAngleMI.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (invoker instanceof RbWall) {
						RbWall wall = (RbWall)invoker;



						//((RbWall)invoker).getWallValues();
					}
				}//..............................................
			});
		}

        return setWallAngleMI;
    }//------------------------------------------



    private JMenuItem getDeleteItemMI() {

        if (deleteItemMI == null) {
            deleteItemMI = new JMenuItem("Delete Item");

            deleteItemMI.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (invoker instanceof RbItem) {

						RbItem item = (RbItem)invoker;
						Container parent = item.getParent();

						if (parent != null) {
							if (parent instanceof RbFloor) {
								((RbFloor)parent).deleteItem(item);
							}
							else {
								parent.remove(item);
								parent.repaint();
							}
							invoker = null;
						}
                    }
                }//..............................................
            });
        }

        return deleteItemMI;
    }//------------------------------------------



    private JMenuItem getAddToRoomMI() {

        if (addToRoomMI == null) {
            addToRoomMI = new JMenuItem("Add To Room");

            addToRoomMI.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (invoker instanceof RbItem) {
                       // view.addItemToRoom((RbItem)invoker);
                       // view.repaint();
                    }
                }//..............................................
            });
        }
        return addToRoomMI;
    }//------------------------------------------





    private JCheckBoxMenuItem getLockWallDimensionsMI() {

        if (lockWallDimensionsMI == null) {
			lockWallDimensionsMI = new JCheckBoxMenuItem();

			lockWallDimensionsMI.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (invoker instanceof RbWall) {

						RbWall wall = (RbWall)invoker;
						boolean locked = wall.getWallLock();

						wall.setWallLock(!locked);
					}
				}//..............................................
			});
		}

		if (invoker instanceof RbWall) {
			RbWall wall = (RbWall)invoker;
			boolean locked = wall.getWallLock();
			String s = (locked) ? "Unlock Wall Dimensions" : "Lock Wall Dimsensions";

			lockWallDimensionsMI.setText(s);
			lockWallDimensionsMI.setSelected(locked);
		}

        return lockWallDimensionsMI;
    }//------------------------------------------



    public JMenu getScaleMenu(RbMainComponent view) {

        if (scaleMenu == null)
            scaleMenu = new JMenu("Change Floor Scale (pixels/inch)");

        scaleMenu.removeAll();

		if (view != null)
			scaleMenu.setText("Change Scale (" +view.mainScale +" pixels/inch)");
		else
			scaleMenu.setText("Change Scale (pixels/inch)");

        scaleMenu.add(getZoomInMI());
        scaleMenu.add(getZoomOutMI());

        return scaleMenu;
    }//-------------------------------------------



    private JMenuItem getZoomInMI() {

        if (zoomInMI == null) {
            zoomInMI = new JMenuItem("Zoom In");

            zoomInMI.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (invoker instanceof RbFloor) {

						((RbFloor)invoker).zoomIn();
                    }
                }//..............................................
            });
        }
        return zoomInMI;
    }//------------------------------------------


    private JMenuItem getZoomOutMI() {

        if (zoomOutMI == null) {
            zoomOutMI = new JMenuItem("Zoom Out");

            zoomOutMI.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (invoker instanceof RbFloor) {

						((RbFloor)invoker).zoomOut();
                    }
                }//..............................................
            });
        }
        return zoomOutMI;
    }//------------------------------------------





}//end class (RbPopupMenu)
