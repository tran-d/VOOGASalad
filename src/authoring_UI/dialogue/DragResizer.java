package authoring_UI.dialogue;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public class DragResizer {

    /**
     * The margin around the control that a user can click in to start resizing
     * the region.
     */
    private static final int RESIZE_MARGIN = 2;
    
	private static double orgSceneX, orgSceneY;
	private static double orgTranslateX, orgTranslateY;
	private static double paneLeft, paneRight, paneTop, paneBottom;

    private final TextArea region;

    private double y, x;

    private boolean initMinHeight;

    private short dragging = 0;

    private final short NOTDRAGGING = 0;
    private final short NORTH = 1;
    private final short SOUTH = 2;
    private final short EAST = 3;
    private final short WEST = 4;

    
    protected DragResizer(TextArea aRegion) {
        region = aRegion;
        region.applyCss();
        
    }

    public void makeResizable() {
        final DragResizer resizer = new DragResizer(region);

        region.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            		resizer.mousePressed(event);
            }
        });
        region.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            		resizer.mouseDragged(event);
            }
        });
        region.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseOver(event);
            }
        });
        region.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseReleased(event);
            }
        });
    }

    protected void mouseReleased(MouseEvent event) {
        initMinHeight = false; //Reset each time
        dragging = NOTDRAGGING;
        region.setCursor(Cursor.DEFAULT);
    }

    protected void mouseOver(MouseEvent event) {
        if (isInDraggableZoneS(event) || dragging == SOUTH) {
            region.setCursor(Cursor.S_RESIZE);
        } else if (isInDraggableZoneE(event) || dragging == EAST) {
            region.setCursor(Cursor.E_RESIZE);
        } else if (isInDraggableZoneN(event) || dragging == NORTH) {
            region.setCursor(Cursor.N_RESIZE);
        } else if (isInDraggableZoneW(event) || dragging == WEST) {
            region.setCursor(Cursor.W_RESIZE);
        } else {
            region.setCursor(Cursor.DEFAULT);
        }
    }

    private boolean isInDraggableZoneN(MouseEvent event) {
        return event.getY() < RESIZE_MARGIN;
    }

    private boolean isInDraggableZoneW(MouseEvent event) {
        return event.getX() < RESIZE_MARGIN;
    }

    private boolean isInDraggableZoneS(MouseEvent event) { return event.getY() > (region.getHeight() - RESIZE_MARGIN); }

    private boolean isInDraggableZoneE(MouseEvent event) { return event.getX() > (region.getWidth() - RESIZE_MARGIN); }


    private void mouseDragged(MouseEvent event) {
    		if (dragging == SOUTH) {
            region.setMinHeight(event.getY());
        } else if (dragging == EAST) {
            region.setMinWidth(event.getX());
        } else if (dragging == NORTH) {
            double prevMin = region.getMinHeight();
            region.setMinHeight(region.getMinHeight() - event.getY());
            if (region.getMinHeight() < region.getPrefHeight()) {
                region.setMinHeight(region.getPrefHeight());
                region.setTranslateY(region.getTranslateY() - (region.getPrefHeight() - prevMin));
                return;
            }
            if (region.getMinHeight() > region.getPrefHeight() || event.getY() < 0)
                region.setTranslateY(region.getTranslateY() + event.getY());
        } else if (dragging == WEST) {
            double prevMin = region.getMinWidth();
            region.setMinWidth(region.getMinWidth() - event.getX());
            if (region.getMinWidth() < region.getPrefWidth()) {
                region.setMinWidth(region.getPrefWidth());
                region.setTranslateX(region.getTranslateX() - (region.getPrefWidth() - prevMin));
                return;
            }
            if (region.getMinWidth() > region.getPrefWidth() || event.getX() < 0)
                region.setTranslateX(region.getTranslateX() + event.getX());
        }


    }

    private void mousePressed(MouseEvent event) {
        // ignore clicks outside of the draggable margin
        if (isInDraggableZoneE(event)) {
            dragging = EAST;
        } else if (isInDraggableZoneS(event)) {
            dragging = SOUTH;
        } else if (isInDraggableZoneN(event)) {
            dragging = NORTH;
        } else if (isInDraggableZoneW(event)) {
            dragging = WEST;
        } else
            return;


        // make sure that the minimum height is set to the current height once,
        // setting a min height that is smaller than the current height will
        // have no effect
        if (!initMinHeight) {
            region.setMinHeight(region.getHeight());
            region.setMinWidth(region.getWidth());
            initMinHeight = true;
        }

    }
    
    protected void makeDraggable() {
    	 
//    	Node textAreaContent = region.lookup(".content");
//
//           textAreaContent.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
//
//           		System.out.println("is clicked");
//
//             	orgSceneX = e.getSceneX();
//             	orgSceneY = e.getSceneY();
//             	orgTranslateX = region.getTranslateX();
//             	orgTranslateY = region.getTranslateY();
//
//             	region.toFront();
//           });
//
//           textAreaContent.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
//
//           		System.out.println("is dragged");
//
//           		double offsetX = e.getSceneX() - orgSceneX;
//           		double offsetY = e.getSceneY() - orgSceneY;
//           		double newTranslateX = orgTranslateX + offsetX;
//           		double newTranslateY = orgTranslateY + offsetY;
//           		if (newTranslateX != paneLeft && newTranslateX != paneRight) {
//               		region.setTranslateX(newTranslateX);
//           		}
//           		if (newTranslateY != paneTop && newTranslateY != paneBottom) {
//               		region.setTranslateY(newTranslateY);
//           		}
//           });
           
       	Node textAreaContent = region.lookup(".content");
        Pane pane = (Pane) region.getParent();

        	textAreaContent.setOnMouseDragged(event -> {

    	        Point2D currentPointer = new Point2D(event.getSceneX(), event.getSceneY());
    		    // set the clip boundary
    		    Rectangle bound = new Rectangle(pane.getWidth(), pane.getHeight());
    		    pane.setClip(bound);

    	        if(bound.getBoundsInLocal().contains(currentPointer)){

    	            if(currentPointer.getX() > 0 &&
    	                    (currentPointer.getX() + region.getWidth()) < pane.getWidth()){
    	                region.setTranslateX(currentPointer.getX());
    	            }
    	            if(currentPointer.getY() > 0 &&
    	                    (currentPointer.getY() + region.getHeight()) < pane.getHeight()){
    	                region.setTranslateY(currentPointer.getY());
    	            }
    	        }
    	    });
    }
}