package com.furnitureapp.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Shared model class for the furniture design application
 * This class acts as a bridge between the 2D and 3D panels
 * to ensure that changes in one panel are reflected in the other
 */
public class DesignModel {
    /**
     * Interface for listening to changes in the design model
     */
    public interface DesignModelListener {
        void onModelChanged(String changeType);
    }
    
    // Room properties
    private int roomWidth = 500;
    private int roomLength = 400;
    private int roomHeight = 250;
    private Color roomFloorColor = new Color(240, 240, 240);
    private Color roomWallColor = Color.WHITE;
    private Color roomCeilingColor = Color.WHITE;
    private String roomShape = "Rectangle";
    
    // Furniture items
    private List<FurnitureItem> furnitureItems = new ArrayList<>();
    
    // Listeners for model changes
    private List<DesignModelListener> listeners = new CopyOnWriteArrayList<>();
    
    // Lighting and shadow settings
    private float lightIntensity = 0.8f;
    private float shadowIntensity = 0.5f;
    private float contrast = 1.0f;
    private Color ambientLightColor = new Color(255, 255, 220); // Warm light
    
    // Selection state
    private FurnitureItem selectedItem = null;
    
    /**
     * Creates a new design model with default settings
     */
    public DesignModel() {
        // Default constructor
    }
    
    /**
     * Adds a listener to the model
     * @param listener the listener to add
     */
    public void addListener(DesignModelListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Removes a listener from the model
     * @param listener the listener to remove
     */
    public void removeListener(DesignModelListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Notifies all listeners of a change in the model
     * @param changeType the type of change that occurred
     */
    private void notifyListeners(String changeType) {
        for (DesignModelListener listener : listeners) {
            listener.onModelChanged(changeType);
        }
    }
    
    /**
     * Adds a furniture item to the model
     * @param item the furniture item to add
     */
    public void addFurnitureItem(FurnitureItem item) {
        furnitureItems.add(item);
        notifyListeners("ITEM_ADDED");
    }
    
    /**
     * Removes a furniture item from the model
     * @param item the furniture item to remove
     */
    public void removeFurnitureItem(FurnitureItem item) {
        furnitureItems.remove(item);
        if (selectedItem == item) {
            selectedItem = null;
        }
        notifyListeners("ITEM_REMOVED");
    }
    
    /**
     * Updates a furniture item in the model
     * @param item the furniture item to update
     */
    public void updateFurnitureItem(FurnitureItem item) {
        notifyListeners("ITEM_UPDATED");
    }
    
    /**
     * Gets all furniture items in the model
     * @return a list of all furniture items
     */
    public List<FurnitureItem> getFurnitureItems() {
        return new ArrayList<>(furnitureItems);
    }
    
    /**
     * Sets the room dimensions
     * @param width the room width
     * @param length the room length
     * @param height the room height
     */
    public void setRoomDimensions(int width, int length, int height) {
        this.roomWidth = width;
        this.roomLength = length;
        this.roomHeight = height;
        notifyListeners("ROOM_DIMENSIONS_CHANGED");
    }
    
    /**
     * Sets the room floor color
     * @param color the room floor color
     */
    public void setRoomFloorColor(Color color) {
        this.roomFloorColor = color;
        notifyListeners("ROOM_COLOR_CHANGED");
    }
    
    /**
     * Sets the room wall color
     * @param color the room wall color
     */
    public void setRoomWallColor(Color color) {
        this.roomWallColor = color;
        notifyListeners("ROOM_COLOR_CHANGED");
    }
    
    /**
     * Sets the room ceiling color
     * @param color the room ceiling color
     */
    public void setRoomCeilingColor(Color color) {
        this.roomCeilingColor = color;
        notifyListeners("ROOM_COLOR_CHANGED");
    }
    
    /**
     * Gets the room width
     * @return the room width
     */
    public int getRoomWidth() {
        return roomWidth;
    }
    
    /**
     * Gets the room length
     * @return the room length
     */
    public int getRoomLength() {
        return roomLength;
    }
    
    /**
     * Gets the room height
     * @return the room height
     */
    public int getRoomHeight() {
        return roomHeight;
    }
    
    /**
     * Gets the room floor color
     * @return the room floor color
     */
    public Color getRoomFloorColor() {
        return roomFloorColor;
    }
    
    /**
     * Gets the room wall color
     * @return the room wall color
     */
    public Color getRoomWallColor() {
        return roomWallColor;
    }
    
    /**
     * Gets the room ceiling color
     * @return the room ceiling color
     */
    public Color getRoomCeilingColor() {
        return roomCeilingColor;
    }
    
    /**
     * Gets the room shape
     * @return the room shape (Rectangle, L-Shape, etc.)
     */
    public String getRoomShape() {
        return roomShape;
    }
    
    /**
     * Sets the room shape
     * @param shape the new room shape
     */
    public void setRoomShape(String shape) {
        this.roomShape = shape;
        notifyListeners("ROOM_SHAPE_CHANGED");
    }
    
    /**
     * Gets the light intensity
     * @return the light intensity (0.0-1.0)
     */
    public float getLightIntensity() {
        return lightIntensity;
    }
    
    /**
     * Sets the light intensity
     * @param intensity the new light intensity (0.0-1.0)
     */
    public void setLightIntensity(float intensity) {
        this.lightIntensity = Math.max(0.0f, Math.min(1.0f, intensity));
        notifyListeners("LIGHTING_CHANGED");
    }
    
    /**
     * Gets the shadow intensity
     * @return the shadow intensity (0.0-1.0)
     */
    public float getShadowIntensity() {
        return shadowIntensity;
    }
    
    /**
     * Sets the shadow intensity
     * @param intensity the new shadow intensity (0.0-1.0)
     */
    public void setShadowIntensity(float intensity) {
        this.shadowIntensity = Math.max(0.0f, Math.min(1.0f, intensity));
        notifyListeners("LIGHTING_CHANGED");
    }
    
    /**
     * Gets the contrast level
     * @return the contrast level (0.5-1.5)
     */
    public float getContrast() {
        return contrast;
    }
    
    /**
     * Sets the contrast level
     * @param contrast the new contrast level (0.5-1.5)
     */
    public void setContrast(float contrast) {
        this.contrast = Math.max(0.5f, Math.min(1.5f, contrast));
        notifyListeners("LIGHTING_CHANGED");
    }
    
    /**
     * Gets the ambient light color
     * @return the ambient light color
     */
    public Color getAmbientLightColor() {
        return ambientLightColor;
    }
    
    /**
     * Sets the ambient light color
     * @param color the new ambient light color
     */
    public void setAmbientLightColor(Color color) {
        this.ambientLightColor = color;
        notifyListeners("LIGHTING_CHANGED");
    }
    
    /**
     * Gets the currently selected furniture item
     * @return the selected item, or null if none is selected
     */
    public FurnitureItem getSelectedItem() {
        return selectedItem;
    }
    
    /**
     * Sets the currently selected furniture item
     * @param item the item to select, or null to clear selection
     */
    public void setSelectedItem(FurnitureItem item) {
        this.selectedItem = item;
        notifyListeners("SELECTION_CHANGED");
    }
    
    /**
     * Clears the selection
     */
    public void clearSelection() {
        this.selectedItem = null;
        notifyListeners("SELECTION_CHANGED");
    }
    
    /**
     * Converts the model to a Design object for saving
     * @param name the name of the design
     * @param designerId the ID of the designer
     * @return a Design object representing the current state
     */
    public Design toDesign(String name, String designerId) {
        Design design = new Design(name, designerId);
        design.setRoomWidth(roomWidth);
        design.setRoomLength(roomLength);
        design.setRoomHeight(roomHeight);
        design.setRoomShape(roomShape);
        design.setRoomFloorColor(roomFloorColor);
        design.setRoomWallColor(roomWallColor);
        design.setRoomCeilingColor(roomCeilingColor);
        design.setLightIntensity(lightIntensity);
        design.setShadowIntensity(shadowIntensity);
        design.setContrast(contrast);
        
        // Add all furniture items
        for (FurnitureItem item : furnitureItems) {
            design.addFurnitureItem(item.copy());
        }
        
        return design;
    }
    
    /**
     * Loads a Design object into this model
     * @param design the design to load
     */
    public void loadFromDesign(Design design) {
        roomWidth = design.getRoomWidth();
        roomLength = design.getRoomLength();
        roomHeight = design.getRoomHeight();
        roomShape = design.getRoomShape();
        roomFloorColor = design.getRoomFloorColor();
        roomWallColor = design.getRoomWallColor();
        roomCeilingColor = design.getRoomCeilingColor();
        lightIntensity = design.getLightIntensity();
        shadowIntensity = design.getShadowIntensity();
        contrast = design.getContrast();
        
        // Clear existing items and add new ones
        furnitureItems.clear();
        selectedItem = null;
        
        for (FurnitureItem item : design.getFurnitureItems()) {
            furnitureItems.add(item.copy());
        }
        
        // Notify listeners of the complete model change
        notifyListeners("MODEL_LOADED");
    }
    
    /**
     * Resets the model to default values
     */
    public void reset() {
        roomWidth = 500;
        roomLength = 400;
        roomHeight = 250;
        roomShape = "Rectangle";
        roomFloorColor = new Color(240, 240, 240);
        roomWallColor = Color.WHITE;
        roomCeilingColor = Color.WHITE;
        lightIntensity = 0.8f;
        shadowIntensity = 0.5f;
        contrast = 1.0f;
        ambientLightColor = new Color(255, 255, 220);
        
        furnitureItems.clear();
        selectedItem = null;
        
        notifyListeners("MODEL_RESET");
    }
}
