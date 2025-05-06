package com.furnitureapp.model;

import java.awt.Color;
import java.io.Serializable;
import java.util.UUID;

/**
 * Model class for furniture items
 * This class is used by both the 2D and 3D panels
 */
public class FurnitureItem implements Serializable {
    private String id;
    private String name;
    private int x, y, z;
    private int width, height, depth;
    private Color color;
    private float rotation;
    private String material;
    private float materialReflectivity;
    private float materialRoughness;
    
    /**
     * Creates a new furniture item model
     * @param name the name of the furniture item
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @param width the width
     * @param height the height
     * @param depth the depth
     * @param color the color
     */
    public FurnitureItem(String name, int x, int y, int z, int width, int height, int depth, Color color) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.color = color;
        this.rotation = 0.0f;
        this.material = "wood";
        this.materialReflectivity = 0.2f;
        this.materialRoughness = 0.7f;
    }
    
    /**
     * Creates a new furniture item model from 2D coordinates
     * @param name the name of the furniture item
     * @param x the x position in 2D
     * @param y the y position in 2D
     * @param width the width
     * @param height the height
     * @param color the color
     * @return a new furniture item
     */
    public static FurnitureItem from2D(String name, int x, int y, int width, int height, Color color) {
        // Convert 2D coordinates to 3D
        // Adjust the coordinates to maintain proper spacing in 3D
        // We'll use x as x, y as z, and a fixed y value for the floor level
        int depth = determineFurnitureDepth(name, width, height);
        return new FurnitureItem(name, x - 250, 0, y - 200, width, height, depth, color);
    }
    
    /**
     * Determines an appropriate depth for the furniture based on its type and dimensions
     * @param name the furniture type
     * @param width the width
     * @param height the height
     * @return an appropriate depth value
     */
    private static int determineFurnitureDepth(String name, int width, int height) {
        switch (name) {
            case "Dining Table":
                return width/2;
            case "Chair":
                return width;
            case "Sofa":
                return width/3;
            case "Coffee Table":
                return width/2;
            case "Bed":
                return height/4;
            case "Wardrobe":
                return width/2;
            case "Lamp":
                return width/2;
            case "Bookshelf":
                return width/3;
            case "Desk":
                return width/2;
            case "Cabinet":
                return width/2;
            default:
                return width/2;
        }
    }
    
    /**
     * Creates a copy of this furniture item
     * @return a new furniture item with the same properties
     */
    public FurnitureItem copy() {
        FurnitureItem copy = new FurnitureItem(name, x, y, z, width, height, depth, color);
        copy.setRotation(rotation);
        copy.setMaterial(material);
        copy.setMaterialReflectivity(materialReflectivity);
        copy.setMaterialRoughness(materialRoughness);
        return copy;
    }
    
    /**
     * Moves the furniture item by the specified delta values
     * @param dx change in x
     * @param dy change in y
     * @param dz change in z
     */
    public void move(int dx, int dy, int dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
    }
    
    /**
     * Scales the furniture item by the specified factor
     * @param factor the scale factor
     */
    public void scale(float factor) {
        this.width = (int)(this.width * factor);
        this.height = (int)(this.height * factor);
        this.depth = (int)(this.depth * factor);
    }
    
    // Getters and setters
    public String getId() { return id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    
    public int getZ() { return z; }
    public void setZ(int z) { this.z = z; }
    
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    
    public int getDepth() { return depth; }
    public void setDepth(int depth) { this.depth = depth; }
    
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
    
    public float getRotation() { return rotation; }
    public void setRotation(float rotation) { this.rotation = rotation; }
    
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    
    public float getMaterialReflectivity() { return materialReflectivity; }
    public void setMaterialReflectivity(float materialReflectivity) { this.materialReflectivity = materialReflectivity; }
    
    public float getMaterialRoughness() { return materialRoughness; }
    public void setMaterialRoughness(float materialRoughness) { this.materialRoughness = materialRoughness; }
    
    @Override
    public String toString() {
        return name + " [" + width + "x" + height + "x" + depth + "] at (" + x + "," + y + "," + z + ")";
    }
}
