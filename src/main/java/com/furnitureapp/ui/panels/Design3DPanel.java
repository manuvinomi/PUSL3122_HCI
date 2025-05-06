package com.furnitureapp.ui.panels;

import com.furnitureapp.model.DesignModel;
import com.furnitureapp.model.FurnitureItem;
import com.furnitureapp.util.AppConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Panel for 3D furniture design visualization
 */
public class Design3DPanel extends JPanel implements DesignModel.DesignModelListener {
    private DesignModel model;
    
    // Camera and view parameters
    private double cameraX = 0;
    private double cameraY = 200;
    private double cameraZ = 500;
    private double rotationX = 30; // Rotation around X axis (pitch)
    private double rotationY = -30; // Rotation around Y axis (yaw)
    private double zoom = 1.0;
    
    // Mouse interaction
    private Point lastMousePos;
    private boolean isRotating = false;
    
    // Rendering settings
    private boolean showWireframe = false;
    private boolean showShadows = true;
    private boolean showReflections = true;
    
    /**
     * Creates a new 3D design panel
     * @param model the design model
     */
    public Design3DPanel(DesignModel model) {
        this.model = model;
        model.addListener(this);
        
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add mouse listeners for rotation and zooming
        setupMouseListeners();
    }
    
    /**
     * Sets up mouse listeners for interaction
     */
    private void setupMouseListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMousePos = e.getPoint();
                if (SwingUtilities.isLeftMouseButton(e)) {
                    isRotating = true;
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    isRotating = false;
                }
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isRotating) {
                    // Calculate rotation delta
                    int dx = e.getX() - lastMousePos.x;
                    int dy = e.getY() - lastMousePos.y;
                    
                    // Update rotation angles
                    rotationY += dx * 0.5;
                    rotationX += dy * 0.5;
                    
                    // Limit pitch rotation to avoid gimbal lock
                    if (rotationX > 89) rotationX = 89;
                    if (rotationX < -89) rotationX = -89;
                    
                    lastMousePos = e.getPoint();
                    repaint();
                }
            }
            
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                // Zoom in/out
                double zoomFactor = e.getWheelRotation() > 0 ? 0.9 : 1.1;
                zoom *= zoomFactor;
                
                // Limit zoom range
                if (zoom < 0.1) zoom = 0.1;
                if (zoom > 5.0) zoom = 5.0;
                
                repaint();
            }
        };
        
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Save the original transform
        AffineTransform originalTransform = g2d.getTransform();
        
        // Set up the camera view
        setupCamera(g2d);
        
        // Draw the room
        drawRoom(g2d);
        
        // Draw furniture items
        drawFurnitureItems(g2d);
        
        // Restore the original transform
        g2d.setTransform(originalTransform);
        
        // Draw UI overlays
        drawOverlays(g2d);
    }
    
    /**
     * Sets up the camera view
     * @param g2d the graphics context
     */
    private void setupCamera(Graphics2D g2d) {
        // Center the view
        g2d.translate(getWidth() / 2, getHeight() / 2);
        
        // Apply zoom
        g2d.scale(zoom, zoom);
        
        // Apply rotations
        g2d.rotate(Math.toRadians(rotationY), 0, 0);
        g2d.rotate(Math.toRadians(rotationX), 1, 0);
    }
    
    /**
     * Draws the room
     * @param g2d the graphics context
     */
    private void drawRoom(Graphics2D g2d) {
        int roomWidth = model.getRoomWidth();
        int roomLength = model.getRoomLength();
        int roomHeight = model.getRoomHeight();
        
        // Calculate room position (centered)
        int roomX = -roomWidth / 2;
        int roomY = -roomHeight;
        int roomZ = -roomLength / 2;
        
        // Draw floor
        g2d.setColor(model.getRoomFloorColor());
        drawRectangle3D(g2d, roomX, 0, roomZ, roomWidth, 0, roomLength, false);
        
        // Draw walls with semi-transparency
        g2d.setColor(new Color(
                model.getRoomWallColor().getRed(),
                model.getRoomWallColor().getGreen(),
                model.getRoomWallColor().getBlue(),
                180)); // Semi-transparent
        
        // Back wall
        drawRectangle3D(g2d, roomX, roomY, roomZ, roomWidth, roomHeight, 0, false);
        
        // Left wall
        drawRectangle3D(g2d, roomX, roomY, roomZ, 0, roomHeight, roomLength, false);
        
        // Draw ceiling
        g2d.setColor(model.getRoomCeilingColor());
        drawRectangle3D(g2d, roomX, roomY, roomZ, roomWidth, 0, roomLength, false);
        
        // Draw grid on floor
        drawFloorGrid(g2d, roomX, 0, roomZ, roomWidth, roomLength);
    }
    
    /**
     * Draws a grid on the floor
     * @param g2d the graphics context
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param width the width
     * @param length the length
     */
    private void drawFloorGrid(Graphics2D g2d, int x, int y, int z, int width, int length) {
        g2d.setColor(new Color(200, 200, 200, 100));
        g2d.setStroke(new BasicStroke(0.5f));
        
        // Draw grid lines along X axis
        for (int i = 0; i <= width; i += 50) {
            Point p1 = project3Dto2D(x + i, y, z);
            Point p2 = project3Dto2D(x + i, y, z + length);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
        
        // Draw grid lines along Z axis
        for (int i = 0; i <= length; i += 50) {
            Point p1 = project3Dto2D(x, y, z + i);
            Point p2 = project3Dto2D(x + width, y, z + i);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }
    
    /**
     * Draws the furniture items
     * @param g2d the graphics context
     */
    private void drawFurnitureItems(Graphics2D g2d) {
        List<FurnitureItem> items = model.getFurnitureItems();
        
        // Sort items by distance from camera (painter's algorithm)
        items.sort((a, b) -> {
            double distA = distance3D(a.getX(), a.getY(), a.getZ(), cameraX, cameraY, cameraZ);
            double distB = distance3D(b.getX(), b.getY(), b.getZ(), cameraX, cameraY, cameraZ);
            return Double.compare(distB, distA); // Draw furthest first
        });
        
        // Draw each furniture item
        for (FurnitureItem item : items) {
            drawFurnitureItem(g2d, item);
        }
    }
    
    /**
     * Draws a single furniture item
     * @param g2d the graphics context
     * @param item the furniture item
     */
    private void drawFurnitureItem(Graphics2D g2d, FurnitureItem item) {
        // Get item properties
        int x = item.getX();
        int y = item.getY();
        int z = item.getZ();
        int width = item.getWidth();
        int height = item.getHeight();
        int depth = item.getDepth();
        Color color = item.getColor();
        
        // Apply lighting effect
        Color shadedColor = applyLighting(color, item);
        g2d.setColor(shadedColor);
        
        // Draw the 3D box
        drawBox3D(g2d, x, y, z, width, height, depth, item == model.getSelectedItem());
        
        // Draw shadow on the floor if enabled
        if (showShadows) {
            drawShadow(g2d, x, 0, z, width, depth);
        }
    }
    
    /**
     * Applies lighting effects to a color
     * @param color the base color
     * @param item the furniture item
     * @return the shaded color
     */
    private Color applyLighting(Color color, FurnitureItem item) {
        // Apply ambient lighting
        float lightIntensity = model.getLightIntensity();
        float shadowIntensity = model.getShadowIntensity();
        float contrast = model.getContrast();
        
        // Calculate lighting factor based on position
        float lightFactor = 0.7f + (item.getY() / 500.0f) * 0.3f;
        lightFactor *= lightIntensity;
        
        // Apply contrast
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        
        r = Math.max(0, Math.min(1, (r - 0.5f) * contrast + 0.5f)) * lightFactor;
        g = Math.max(0, Math.min(1, (g - 0.5f) * contrast + 0.5f)) * lightFactor;
        b = Math.max(0, Math.min(1, (b - 0.5f) * contrast + 0.5f)) * lightFactor;
        
        return new Color(r, g, b);
    }
    
    /**
     * Draws a shadow on the floor
     * @param g2d the graphics context
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param width the width
     * @param depth the depth
     */
    private void drawShadow(Graphics2D g2d, int x, int y, int z, int width, int depth) {
        // Create a semi-transparent black for the shadow
        g2d.setColor(new Color(0, 0, 0, 50));
        
        // Draw a slightly offset and stretched shadow
        int shadowOffsetX = 10;
        int shadowOffsetZ = 10;
        
        // Project the shadow points
        // Note: y is already inverted in the calling method, so we use -y here
        // to place the shadow on the floor
        Point p1 = project3Dto2D(x - shadowOffsetX, -y + 1, z - shadowOffsetZ);
        Point p2 = project3Dto2D(x + width + shadowOffsetX, -y + 1, z - shadowOffsetZ);
        Point p3 = project3Dto2D(x + width + shadowOffsetX, -y + 1, z + depth + shadowOffsetZ);
        Point p4 = project3Dto2D(x - shadowOffsetX, -y + 1, z + depth + shadowOffsetZ);
        
        // Draw the shadow
        Path2D path = new Path2D.Double();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.lineTo(p4.x, p4.y);
        path.closePath();
        
        g2d.fill(path);
    }
    
    /**
     * Draws a 3D box
     * @param g2d the graphics context
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param width the width
     * @param height the height
     * @param depth the depth
     * @param isSelected whether the box is selected
     */
    private void drawBox3D(Graphics2D g2d, int x, int y, int z, int width, int height, int depth, boolean isSelected) {
        Color originalColor = g2d.getColor();
        
        // Invert the Y-coordinate so items appear above the floor instead of below
        // In 3D graphics, Y typically points up, but in our coordinate system it points down
        // So we need to negate the Y and height values
        int yTop = -y; // Top of the object (floor level)
        int yBottom = -(y + height); // Bottom of the object
        
        // Top face
        g2d.setColor(brighten(originalColor, 1.2f));
        drawRectangle3D(g2d, x, yTop, z, width, 0, depth, isSelected);
        
        // Front face
        g2d.setColor(originalColor);
        drawRectangle3D(g2d, x, yTop, z + depth, width, -height, 0, isSelected);
        
        // Right face
        g2d.setColor(darken(originalColor, 0.8f));
        drawRectangle3D(g2d, x + width, yTop, z, 0, -height, depth, isSelected);
        
        // If selected, draw with a highlight
        if (isSelected) {
            Stroke originalStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.setColor(Color.BLUE);
            
            // Draw wireframe
            drawWireframeBox(g2d, x, yTop, z, width, -height, depth);
            
            g2d.setStroke(originalStroke);
        }
    }
    
    /**
     * Draws a wireframe box
     * @param g2d the graphics context
     * @param x the x coordinate
     * @param y the y coordinate (top of the box)
     * @param z the z coordinate
     * @param width the width
     * @param height the height (negative value since we're drawing downward)
     * @param depth the depth
     */
    private void drawWireframeBox(Graphics2D g2d, int x, int y, int z, int width, int height, int depth) {
        // Note: height is negative, so y + height is the bottom of the box
        
        // Bottom rectangle (y + height is lower than y because height is negative)
        Point p1 = project3Dto2D(x, y + height, z);
        Point p2 = project3Dto2D(x + width, y + height, z);
        Point p3 = project3Dto2D(x + width, y + height, z + depth);
        Point p4 = project3Dto2D(x, y + height, z + depth);
        
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        g2d.drawLine(p2.x, p2.y, p3.x, p3.y);
        g2d.drawLine(p3.x, p3.y, p4.x, p4.y);
        g2d.drawLine(p4.x, p4.y, p1.x, p1.y);
        
        // Top rectangle (at y)
        Point p5 = project3Dto2D(x, y, z);
        Point p6 = project3Dto2D(x + width, y, z);
        Point p7 = project3Dto2D(x + width, y, z + depth);
        Point p8 = project3Dto2D(x, y, z + depth);
        
        g2d.drawLine(p5.x, p5.y, p6.x, p6.y);
        g2d.drawLine(p6.x, p6.y, p7.x, p7.y);
        g2d.drawLine(p7.x, p7.y, p8.x, p8.y);
        g2d.drawLine(p8.x, p8.y, p5.x, p5.y);
        
        // Connecting lines
        g2d.drawLine(p1.x, p1.y, p5.x, p5.y);
        g2d.drawLine(p2.x, p2.y, p6.x, p6.y);
        g2d.drawLine(p3.x, p3.y, p7.x, p7.y);
        g2d.drawLine(p4.x, p4.y, p8.x, p8.y);
    }
    
    /**
     * Draws a 3D rectangle
     * @param g2d the graphics context
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param width the width (along x axis)
     * @param height the height (along y axis)
     * @param depth the depth (along z axis)
     * @param isSelected whether the rectangle is selected
     */
    private void drawRectangle3D(Graphics2D g2d, int x, int y, int z, int width, int height, int depth, boolean isSelected) {
        // Calculate the four corners of the rectangle
        Point p1, p2, p3, p4;
        
        if (width == 0) { // YZ plane
            p1 = project3Dto2D(x, y, z);
            p2 = project3Dto2D(x, y + height, z);
            p3 = project3Dto2D(x, y + height, z + depth);
            p4 = project3Dto2D(x, y, z + depth);
        } else if (height == 0) { // XZ plane
            p1 = project3Dto2D(x, y, z);
            p2 = project3Dto2D(x + width, y, z);
            p3 = project3Dto2D(x + width, y, z + depth);
            p4 = project3Dto2D(x, y, z + depth);
        } else if (depth == 0) { // XY plane
            p1 = project3Dto2D(x, y, z);
            p2 = project3Dto2D(x + width, y, z);
            p3 = project3Dto2D(x + width, y + height, z);
            p4 = project3Dto2D(x, y + height, z);
        } else {
            // Not a rectangle on a principal plane
            return;
        }
        
        // Draw the filled rectangle
        Path2D path = new Path2D.Double();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.lineTo(p4.x, p4.y);
        path.closePath();
        
        g2d.fill(path);
        
        // Draw the outline
        Color originalColor = g2d.getColor();
        g2d.setColor(isSelected ? Color.BLUE : darken(originalColor, 0.7f));
        g2d.draw(path);
        g2d.setColor(originalColor);
    }
    
    /**
     * Projects a 3D point to 2D screen coordinates
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the projected 2D point
     */
    private Point project3Dto2D(double x, double y, double z) {
        // Simple perspective projection
        double scale = 1.0;
        double distance = 1000;
        
        // Apply camera transformations
        double dx = x - cameraX;
        double dy = y - cameraY;
        double dz = z - cameraZ;
        
        // Apply rotation around Y axis (yaw)
        double rotY = Math.toRadians(rotationY);
        double newX = dx * Math.cos(rotY) - dz * Math.sin(rotY);
        double newZ = dx * Math.sin(rotY) + dz * Math.cos(rotY);
        dx = newX;
        dz = newZ;
        
        // Apply rotation around X axis (pitch)
        double rotX = Math.toRadians(rotationX);
        double newY = dy * Math.cos(rotX) - dz * Math.sin(rotX);
        newZ = dy * Math.sin(rotX) + dz * Math.cos(rotX);
        dy = newY;
        dz = newZ;
        
        // Perspective projection
        double factor = distance / (distance + dz);
        int screenX = (int) (dx * factor * scale);
        int screenY = (int) (dy * factor * scale);
        
        return new Point(screenX, screenY);
    }
    
    /**
     * Calculates the 3D distance between two points
     * @param x1 the x coordinate of the first point
     * @param y1 the y coordinate of the first point
     * @param z1 the z coordinate of the first point
     * @param x2 the x coordinate of the second point
     * @param y2 the y coordinate of the second point
     * @param z2 the z coordinate of the second point
     * @return the distance
     */
    private double distance3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    /**
     * Brightens a color
     * @param color the color to brighten
     * @param factor the brightening factor
     * @return the brightened color
     */
    private Color brighten(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[1] = Math.max(0, Math.min(1, hsb[1] * 0.9f)); // Reduce saturation slightly
        hsb[2] = Math.max(0, Math.min(1, hsb[2] * factor)); // Increase brightness
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
    
    /**
     * Darkens a color
     * @param color the color to darken
     * @param factor the darkening factor
     * @return the darkened color
     */
    private Color darken(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[2] = Math.max(0, Math.min(1, hsb[2] * factor)); // Decrease brightness
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
    
    /**
     * Draws UI overlays
     * @param g2d the graphics context
     */
    private void drawOverlays(Graphics2D g2d) {
        // Draw view controls
        drawViewControls(g2d);
        
        // Draw selected item info if any
        FurnitureItem selectedItem = model.getSelectedItem();
        if (selectedItem != null) {
            drawSelectedItemInfo(g2d, selectedItem);
        }
    }
    
    /**
     * Draws view controls
     * @param g2d the graphics context
     */
    private void drawViewControls(Graphics2D g2d) {
        // Draw in the bottom-right corner
        int x = getWidth() - 150;
        int y = getHeight() - 80;
        
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(x, y, 140, 70, 10, 10);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2d.drawString("Left drag: Rotate", x + 10, y + 20);
        g2d.drawString("Scroll: Zoom", x + 10, y + 40);
        g2d.drawString("Zoom: " + String.format("%.1f", zoom) + "x", x + 10, y + 60);
    }
    
    /**
     * Draws information about the selected item
     * @param g2d the graphics context
     * @param item the selected item
     */
    private void drawSelectedItemInfo(Graphics2D g2d, FurnitureItem item) {
        // Draw in the top-right corner
        int x = getWidth() - 200;
        int y = 10;
        
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(x, y, 190, 100, 10, 10);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2d.drawString(item.getName(), x + 10, y + 20);
        
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2d.drawString("Size: " + item.getWidth() + " x " + item.getHeight() + " x " + item.getDepth(), x + 10, y + 40);
        g2d.drawString("Position: (" + item.getX() + ", " + item.getY() + ", " + item.getZ() + ")", x + 10, y + 60);
        
        // Draw color sample
        g2d.setColor(item.getColor());
        g2d.fillRect(x + 10, y + 70, 20, 20);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x + 10, y + 70, 20, 20);
        g2d.drawString("Material: " + item.getMaterial(), x + 40, y + 85);
    }
    
    /**
     * Sets the rendering options
     * @param showWireframe whether to show wireframe
     * @param showShadows whether to show shadows
     * @param showReflections whether to show reflections
     */
    public void setRenderingOptions(boolean showWireframe, boolean showShadows, boolean showReflections) {
        this.showWireframe = showWireframe;
        this.showShadows = showShadows;
        this.showReflections = showReflections;
        repaint();
    }
    
    /**
     * Resets the camera view
     */
    public void resetView() {
        cameraX = 0;
        cameraY = 200;
        cameraZ = 500;
        rotationX = 30;
        rotationY = -30;
        zoom = 1.0;
        repaint();
    }
    
    @Override
    public void onModelChanged(String changeType) {
        repaint();
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
}
