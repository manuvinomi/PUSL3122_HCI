package com.furnitureapp.ui.panels;

import com.furnitureapp.model.DesignModel;
import com.furnitureapp.model.FurnitureItem;
import com.furnitureapp.util.AppConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Panel for 2D furniture design
 */
public class Design2DPanel extends JPanel implements DesignModel.DesignModelListener {
    private DesignModel model;
    private Point dragStart;
    private boolean isDragging = false;
    private FurnitureItem selectedItem = null;
    private FurnitureItem newItem = null;
    private String newItemType = null;
    
    /**
     * Creates a new 2D design panel
     * @param model the design model
     */
    public Design2DPanel(DesignModel model) {
        this.model = model;
        model.addListener(this);
        
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add mouse listeners for interaction
        setupMouseListeners();
    }
    
    /**
     * Sets up mouse listeners for interaction
     */
    private void setupMouseListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased(e);
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClicked(e);
            }
        };
        
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        
        // Add a debug message to show when the panel receives mouse events
        System.out.println("Mouse listeners added to Design2DPanel");
    }
    
    /**
     * Handles mouse pressed events
     * @param e the mouse event
     */
    private void handleMousePressed(MouseEvent e) {
        // Check if we're creating a new item
        if (newItemType != null) {
            // Create a new furniture item at the mouse position
            createNewFurnitureItem(e.getPoint());
            return;
        }
        
        // Check if we're clicking on an existing item
        FurnitureItem item = getFurnitureItemAt(e.getPoint());
        
        if (item != null) {
            // Select the item
            selectedItem = item;
            model.setSelectedItem(item);
            dragStart = e.getPoint();
            isDragging = true;
            repaint();
        } else {
            // Deselect if clicking on empty space
            selectedItem = null;
            model.clearSelection();
            repaint();
        }
    }
    
    /**
     * Handles mouse released events
     * @param e the mouse event
     */
    private void handleMouseReleased(MouseEvent e) {
        if (isDragging && selectedItem != null) {
            isDragging = false;
            // Update the model with the new position
            model.updateFurnitureItem(selectedItem);
        }
    }
    
    /**
     * Handles mouse dragged events
     * @param e the mouse event
     */
    private void handleMouseDragged(MouseEvent e) {
        if (isDragging && selectedItem != null) {
            // Calculate the drag distance in screen coordinates
            int dx = e.getX() - dragStart.x;
            int dy = e.getY() - dragStart.y;
            
            // Convert screen movement to room coordinates (1:1 ratio)
            int roomDx = dx;
            int roomDz = dy;
            
            // Update the item position
            int newX = selectedItem.getX() + roomDx;
            int newZ = selectedItem.getZ() + roomDz;
            
            // Keep the item within the room boundaries
            int roomWidth = model.getRoomWidth();
            int roomLength = model.getRoomLength();
            
            // Calculate room boundaries in room coordinates
            int minX = -roomWidth/2;
            int maxX = roomWidth/2 - selectedItem.getWidth();
            int minZ = -roomLength/2;
            int maxZ = roomLength/2 - selectedItem.getDepth();
            
            // Constrain to room boundaries
            if (newX < minX) newX = minX;
            if (newX > maxX) newX = maxX;
            if (newZ < minZ) newZ = minZ;
            if (newZ > maxZ) newZ = maxZ;
            
            // Update the item
            selectedItem.setX(newX);
            selectedItem.setZ(newZ);
            
            System.out.println("Moving item to: " + newX + ", " + newZ);
            
            // Update the drag start point
            dragStart = e.getPoint();
            
            repaint();
        }
    }
    
    /**
     * Handles mouse clicked events
     * @param e the mouse event
     */
    private void handleMouseClicked(MouseEvent e) {
        // Double-click to edit properties
        if (e.getClickCount() == 2) {
            FurnitureItem item = getFurnitureItemAt(e.getPoint());
            if (item != null) {
                // TODO: Show property editor dialog
            }
        }
    }
    
    /**
     * Creates a new furniture item at the specified position
     * @param point the position
     */
    private void createNewFurnitureItem(Point point) {
        // Default dimensions based on furniture type
        int width = 60;
        int height = 30;
        int depth = 60;
        
        switch (newItemType) {
            case "Dining Table":
                width = 120;
                height = 30;
                depth = 80;
                break;
            case "Chair":
                width = 40;
                height = 45;
                depth = 40;
                break;
            case "Sofa":
                width = 180;
                height = 40;
                depth = 80;
                break;
            case "Coffee Table":
                width = 100;
                height = 20;
                depth = 60;
                break;
            case "Bed":
                width = 160;
                height = 30;
                depth = 200;
                break;
            case "Wardrobe":
                width = 100;
                height = 200;
                depth = 60;
                break;
            case "Bookshelf":
                width = 80;
                height = 180;
                depth = 30;
                break;
            case "Desk":
                width = 120;
                height = 75;
                depth = 60;
                break;
            case "Cabinet":
                width = 80;
                height = 100;
                depth = 40;
                break;
            case "Lamp":
                width = 30;
                height = 100;
                depth = 30;
                break;
        }
        
        // Convert screen coordinates to room coordinates
        int roomX = point.x - (getWidth() - model.getRoomWidth()) / 2 - model.getRoomWidth()/2;
        int roomZ = point.y - (getHeight() - model.getRoomLength()) / 2 - model.getRoomLength()/2;
        
        // Create a new furniture item
        Color randomColor = AppConstants.FURNITURE_COLORS[(int)(Math.random() * AppConstants.FURNITURE_COLORS.length)];
        FurnitureItem item = new FurnitureItem(newItemType, roomX, 0, roomZ, width, height, depth, randomColor);
        
        // Add it to the model
        model.addFurnitureItem(item);
        
        // Select the new item
        selectedItem = item;
        model.setSelectedItem(item);
        
        // Reset the new item type
        newItemType = null;
        setCursor(Cursor.getDefaultCursor());
        
        repaint();
    }
    
    /**
     * Gets the furniture item at the specified point
     * @param point the point
     * @return the furniture item at the point, or null if none
     */
    private FurnitureItem getFurnitureItemAt(Point point) {
        List<FurnitureItem> items = model.getFurnitureItems();
        
        // Check items in reverse order (top to bottom)
        for (int i = items.size() - 1; i >= 0; i--) {
            FurnitureItem item = items.get(i);
            
            // Convert 3D coordinates to 2D screen coordinates
            int x = (getWidth() - model.getRoomWidth()) / 2 + item.getX() + model.getRoomWidth()/2;
            int y = (getHeight() - model.getRoomLength()) / 2 + item.getZ() + model.getRoomLength()/2;
            
            // Check if the point is within the item bounds
            if (point.x >= x && point.x <= x + item.getWidth() &&
                point.y >= y && point.y <= y + item.getDepth()) {
                System.out.println("Selected item: " + item.getName());
                return item;
            }
        }
        
        return null;
    }
    
    /**
     * Sets the new item type for creation
     * @param type the furniture type
     */
    public void setNewItemType(String type) {
        this.newItemType = type;
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        System.out.println("New item type set to: " + type);
        
        // Add the item at the center of the panel if the type is not null
        if (type != null) {
            Point center = new Point(getWidth() / 2, getHeight() / 2);
            createNewFurnitureItem(center);
        }
    }
    
    /**
     * Deletes the selected item
     */
    public void deleteSelectedItem() {
        if (selectedItem != null) {
            model.removeFurnitureItem(selectedItem);
            selectedItem = null;
        }
    }
    
    /**
     * Gets the selected item
     * @return the selected item
     */
    public FurnitureItem getSelectedItem() {
        return selectedItem;
    }
    
    /**
     * Gets the selected item model
     * @return the selected item model
     */
    public FurnitureItem getSelectedItemModel() {
        return selectedItem;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw room outline
        drawRoom(g2d);
        
        // Draw furniture items
        drawFurnitureItems(g2d);
        
        // Draw grid
        drawGrid(g2d);
    }
    
    /**
     * Draws the room outline
     * @param g2d the graphics context
     */
    private void drawRoom(Graphics2D g2d) {
        int roomWidth = model.getRoomWidth();
        int roomLength = model.getRoomLength();
        
        // Calculate room position (centered in the panel)
        int roomX = (getWidth() - roomWidth) / 2;
        int roomY = (getHeight() - roomLength) / 2;
        
        // Draw room floor
        g2d.setColor(model.getRoomFloorColor());
        g2d.fillRect(roomX, roomY, roomWidth, roomLength);
        
        // Draw room outline
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(roomX, roomY, roomWidth, roomLength);
    }
    
    /**
     * Draws the furniture items
     * @param g2d the graphics context
     */
    private void drawFurnitureItems(Graphics2D g2d) {
        List<FurnitureItem> items = model.getFurnitureItems();
        
        for (FurnitureItem item : items) {
            // Convert 3D coordinates to 2D screen coordinates
            int x = (getWidth() - model.getRoomWidth()) / 2 + item.getX() + model.getRoomWidth()/2;
            int y = (getHeight() - model.getRoomLength()) / 2 + item.getZ() + model.getRoomLength()/2;
            
            // Draw the furniture item
            g2d.setColor(item.getColor());
            g2d.fillRect(x, y, item.getWidth(), item.getDepth());
            
            // Draw outline
            if (item == selectedItem) {
                // Selected item has a thicker outline
                g2d.setColor(Color.BLUE);
                g2d.setStroke(new BasicStroke(2));
            } else {
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(1));
            }
            g2d.drawRect(x, y, item.getWidth(), item.getDepth());
            
            // Draw item name
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2d.drawString(item.getName(), x + 5, y + 15);
        }
    }
    
    /**
     * Draws a grid on the room floor
     * @param g2d the graphics context
     */
    private void drawGrid(Graphics2D g2d) {
        int roomWidth = model.getRoomWidth();
        int roomLength = model.getRoomLength();
        
        // Calculate room position (centered in the panel)
        int roomX = (getWidth() - roomWidth) / 2;
        int roomY = (getHeight() - roomLength) / 2;
        
        // Draw grid
        g2d.setColor(new Color(200, 200, 200, 100));
        g2d.setStroke(new BasicStroke(0.5f));
        
        // Vertical grid lines
        for (int x = 0; x <= roomWidth; x += 50) {
            g2d.drawLine(roomX + x, roomY, roomX + x, roomY + roomLength);
        }
        
        // Horizontal grid lines
        for (int y = 0; y <= roomLength; y += 50) {
            g2d.drawLine(roomX, roomY + y, roomX + roomWidth, roomY + y);
        }
    }
    
    @Override
    public void onModelChanged(String changeType) {
        // Update the selected item reference if the model selection changed
        if (changeType.equals("SELECTION_CHANGED")) {
            selectedItem = model.getSelectedItem();
        }
        
        // Repaint the panel
        repaint();
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
}
