package com.furnitureapp.model;

import java.awt.Color;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a furniture design with room properties and furniture items
 */
public class Design implements Serializable {
    private String id;
    private String name;
    private String designerId;
    private int roomWidth;
    private int roomLength;
    private int roomHeight;
    private String roomShape; // "Rectangle", "Square", "L-Shape"
    private Color roomFloorColor;
    private Color roomWallColor;
    private Color roomCeilingColor;
    private List<FurnitureItem> furnitureItems;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
    private String description;
    private float lightIntensity;
    private float shadowIntensity;
    private float contrast;
    
    /**
     * Creates a new design with the specified name and designer ID
     * @param name the design name
     * @param designerId the designer ID
     */
    public Design(String name, String designerId) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.designerId = designerId;
        this.roomWidth = 500;
        this.roomLength = 400;
        this.roomHeight = 250;
        this.roomShape = "Rectangle";
        this.roomFloorColor = new Color(240, 240, 240);
        this.roomWallColor = Color.WHITE;
        this.roomCeilingColor = Color.WHITE;
        this.furnitureItems = new ArrayList<>();
        this.createdTime = LocalDateTime.now();
        this.lastModifiedTime = this.createdTime;
        this.description = "";
        this.lightIntensity = 0.7f;
        this.shadowIntensity = 0.5f;
        this.contrast = 1.0f;
    }
    
    /**
     * Creates a new design with default values
     */
    public Design() {
        this("Untitled Design", "admin");
    }
    
    // Getters and setters
    public String getId() { return id; }
    
    public String getName() { return name; }
    public void setName(String name) { 
        this.name = name; 
        updateModifiedTime();
    }
    
    public String getDesignerId() { return designerId; }
    
    public int getRoomWidth() { return roomWidth; }
    public void setRoomWidth(int roomWidth) { 
        this.roomWidth = roomWidth; 
        updateModifiedTime();
    }
    
    public int getRoomLength() { return roomLength; }
    public void setRoomLength(int roomLength) { 
        this.roomLength = roomLength; 
        updateModifiedTime();
    }
    
    public int getRoomHeight() { return roomHeight; }
    public void setRoomHeight(int roomHeight) { 
        this.roomHeight = roomHeight; 
        updateModifiedTime();
    }
    
    public String getRoomShape() { return roomShape; }
    public void setRoomShape(String roomShape) { 
        this.roomShape = roomShape; 
        updateModifiedTime();
    }
    
    public Color getRoomFloorColor() { return roomFloorColor; }
    public void setRoomFloorColor(Color roomFloorColor) { 
        this.roomFloorColor = roomFloorColor; 
        updateModifiedTime();
    }
    
    public Color getRoomWallColor() { return roomWallColor; }
    public void setRoomWallColor(Color roomWallColor) { 
        this.roomWallColor = roomWallColor; 
        updateModifiedTime();
    }
    
    public Color getRoomCeilingColor() { return roomCeilingColor; }
    public void setRoomCeilingColor(Color roomCeilingColor) { 
        this.roomCeilingColor = roomCeilingColor; 
        updateModifiedTime();
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
        updateModifiedTime();
    }
    
    public float getLightIntensity() { return lightIntensity; }
    public void setLightIntensity(float lightIntensity) {
        this.lightIntensity = lightIntensity;
        updateModifiedTime();
    }
    
    public float getShadowIntensity() { return shadowIntensity; }
    public void setShadowIntensity(float shadowIntensity) {
        this.shadowIntensity = shadowIntensity;
        updateModifiedTime();
    }
    
    public float getContrast() { return contrast; }
    public void setContrast(float contrast) {
        this.contrast = contrast;
        updateModifiedTime();
    }
    
    public List<FurnitureItem> getFurnitureItems() { return furnitureItems; }
    
    /**
     * Adds a furniture item to the design
     * @param item the furniture item to add
     */
    public void addFurnitureItem(FurnitureItem item) {
        furnitureItems.add(item);
        updateModifiedTime();
    }
    
    /**
     * Removes a furniture item from the design
     * @param item the furniture item to remove
     * @return true if the item was removed, false otherwise
     */
    public boolean removeFurnitureItem(FurnitureItem item) {
        boolean removed = furnitureItems.remove(item);
        if (removed) {
            updateModifiedTime();
        }
        return removed;
    }
    
    /**
     * Removes a furniture item by its ID
     * @param itemId the ID of the furniture item to remove
     * @return true if the item was removed, false otherwise
     */
    public boolean removeFurnitureItemById(String itemId) {
        for (int i = 0; i < furnitureItems.size(); i++) {
            if (furnitureItems.get(i).getId().equals(itemId)) {
                furnitureItems.remove(i);
                updateModifiedTime();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Updates a furniture item in the design
     * @param updatedItem the updated furniture item
     * @return true if the item was updated, false otherwise
     */
    public boolean updateFurnitureItem(FurnitureItem updatedItem) {
        for (int i = 0; i < furnitureItems.size(); i++) {
            if (furnitureItems.get(i).getId().equals(updatedItem.getId())) {
                furnitureItems.set(i, updatedItem);
                updateModifiedTime();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets a furniture item by its ID
     * @param itemId the ID of the furniture item to get
     * @return the furniture item, or null if not found
     */
    public FurnitureItem getFurnitureItemById(String itemId) {
        for (FurnitureItem item : furnitureItems) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }
    
    public LocalDateTime getCreatedTime() { return createdTime; }
    
    public LocalDateTime getLastModifiedTime() { return lastModifiedTime; }
    
    /**
     * Updates the last modified time to now
     */
    private void updateModifiedTime() {
        this.lastModifiedTime = LocalDateTime.now();
    }
    
    /**
     * Creates a deep copy of this design
     * @return a new design with the same properties
     */
    public Design copy() {
        Design copy = new Design(name, designerId);
        copy.roomWidth = roomWidth;
        copy.roomLength = roomLength;
        copy.roomHeight = roomHeight;
        copy.roomShape = roomShape;
        copy.roomFloorColor = roomFloorColor;
        copy.roomWallColor = roomWallColor;
        copy.roomCeilingColor = roomCeilingColor;
        copy.description = description;
        copy.lightIntensity = lightIntensity;
        copy.shadowIntensity = shadowIntensity;
        copy.contrast = contrast;
        
        // Deep copy of furniture items
        for (FurnitureItem item : furnitureItems) {
            copy.addFurnitureItem(item.copy());
        }
        
        return copy;
    }
    
    @Override
    public String toString() {
        return name + " (" + furnitureItems.size() + " items)";
    }
}
