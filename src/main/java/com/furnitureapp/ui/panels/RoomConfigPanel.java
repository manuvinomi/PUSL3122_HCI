package com.furnitureapp.ui.panels;

import com.furnitureapp.model.DesignModel;
import com.furnitureapp.model.FurnitureItem;
import com.furnitureapp.util.AppConstants;
import com.furnitureapp.util.UIUtils;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

/**
 * Panel for configuring room properties and adding furniture
 */
public class RoomConfigPanel extends JPanel implements DesignModel.DesignModelListener {
    private DesignModel model;
    
    // Room dimension components
    private JSpinner widthSpinner;
    private JSpinner lengthSpinner;
    private JSpinner heightSpinner;
    
    // Room color components
    private JButton floorColorButton;
    private JButton wallColorButton;
    private JButton ceilingColorButton;
    
    // Lighting components
    private JSlider lightIntensitySlider;
    private JSlider shadowIntensitySlider;
    private JSlider contrastSlider;
    
    // Furniture buttons
    private List<String> furnitureTypes = Arrays.asList(
            "Dining Table", "Chair", "Sofa", "Coffee Table", 
            "Bed", "Wardrobe", "Bookshelf", "Desk", "Cabinet", "Lamp"
    );
    
    /**
     * Creates a new room configuration panel
     * @param model the design model
     */
    public RoomConfigPanel(DesignModel model) {
        this.model = model;
        model.addListener(this);
        
        setLayout(new MigLayout("fillx, wrap 1, insets 10", "[grow]", "[]10[]10[]10[]"));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create the room dimension panel
        JPanel dimensionPanel = createDimensionPanel();
        add(dimensionPanel, "grow");
        
        // Create the room color panel
        JPanel colorPanel = createColorPanel();
        add(colorPanel, "grow");
        
        // Create the lighting panel
        JPanel lightingPanel = createLightingPanel();
        add(lightingPanel, "grow");
        
        // Create the furniture panel
        JPanel furniturePanel = createFurniturePanel();
        add(furniturePanel, "grow");
    }
    
    /**
     * Creates the room dimension panel
     * @return the dimension panel
     */
    private JPanel createDimensionPanel() {
        JPanel panel = new JPanel(new MigLayout("fillx, wrap 2", "[30%][70%]", "[]5[]5[]"));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Room Dimensions",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));
        
        // Width spinner
        JLabel widthLabel = new JLabel("Width:");
        widthSpinner = new JSpinner(new SpinnerNumberModel(model.getRoomWidth(), 200, 1000, 10));
        widthSpinner.addChangeListener(e -> {
            int width = (int) widthSpinner.getValue();
            model.setRoomDimensions(width, model.getRoomLength(), model.getRoomHeight());
        });
        
        // Length spinner
        JLabel lengthLabel = new JLabel("Length:");
        lengthSpinner = new JSpinner(new SpinnerNumberModel(model.getRoomLength(), 200, 1000, 10));
        lengthSpinner.addChangeListener(e -> {
            int length = (int) lengthSpinner.getValue();
            model.setRoomDimensions(model.getRoomWidth(), length, model.getRoomHeight());
        });
        
        // Height spinner
        JLabel heightLabel = new JLabel("Height:");
        heightSpinner = new JSpinner(new SpinnerNumberModel(model.getRoomHeight(), 100, 500, 10));
        heightSpinner.addChangeListener(e -> {
            int height = (int) heightSpinner.getValue();
            model.setRoomDimensions(model.getRoomWidth(), model.getRoomLength(), height);
        });
        
        // Room shape combo box
        JLabel shapeLabel = new JLabel("Shape:");
        String[] shapes = {"Rectangle", "L-Shape", "Square"};
        JComboBox<String> shapeCombo = new JComboBox<>(shapes);
        shapeCombo.setSelectedItem(model.getRoomShape());
        shapeCombo.addActionListener(e -> {
            String shape = (String) shapeCombo.getSelectedItem();
            model.setRoomShape(shape);
        });
        
        // Add components to panel
        panel.add(widthLabel);
        panel.add(widthSpinner);
        panel.add(lengthLabel);
        panel.add(lengthSpinner);
        panel.add(heightLabel);
        panel.add(heightSpinner);
        panel.add(shapeLabel);
        panel.add(shapeCombo);
        
        return panel;
    }
    
    /**
     * Creates the room color panel
     * @return the color panel
     */
    private JPanel createColorPanel() {
        JPanel panel = new JPanel(new MigLayout("fillx, wrap 2", "[30%][70%]", "[]5[]5[]"));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Room Colors",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));
        
        // Floor color button
        JLabel floorLabel = new JLabel("Floor:");
        floorColorButton = new JButton();
        floorColorButton.setBackground(model.getRoomFloorColor());
        floorColorButton.setPreferredSize(new Dimension(80, 25));
        floorColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Floor Color", model.getRoomFloorColor());
            if (newColor != null) {
                model.setRoomFloorColor(newColor);
                floorColorButton.setBackground(newColor);
            }
        });
        
        // Wall color button
        JLabel wallLabel = new JLabel("Walls:");
        wallColorButton = new JButton();
        wallColorButton.setBackground(model.getRoomWallColor());
        wallColorButton.setPreferredSize(new Dimension(80, 25));
        wallColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Wall Color", model.getRoomWallColor());
            if (newColor != null) {
                model.setRoomWallColor(newColor);
                wallColorButton.setBackground(newColor);
            }
        });
        
        // Ceiling color button
        JLabel ceilingLabel = new JLabel("Ceiling:");
        ceilingColorButton = new JButton();
        ceilingColorButton.setBackground(model.getRoomCeilingColor());
        ceilingColorButton.setPreferredSize(new Dimension(80, 25));
        ceilingColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Ceiling Color", model.getRoomCeilingColor());
            if (newColor != null) {
                model.setRoomCeilingColor(newColor);
                ceilingColorButton.setBackground(newColor);
            }
        });
        
        // Add components to panel
        panel.add(floorLabel);
        panel.add(floorColorButton);
        panel.add(wallLabel);
        panel.add(wallColorButton);
        panel.add(ceilingLabel);
        panel.add(ceilingColorButton);
        
        return panel;
    }
    
    /**
     * Creates the lighting panel
     * @return the lighting panel
     */
    private JPanel createLightingPanel() {
        JPanel panel = new JPanel(new MigLayout("fillx, wrap 2", "[30%][70%]", "[]5[]5[]"));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Lighting",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));
        
        // Light intensity slider
        JLabel lightLabel = new JLabel("Light:");
        lightIntensitySlider = new JSlider(0, 100, (int)(model.getLightIntensity() * 100));
        lightIntensitySlider.setMajorTickSpacing(25);
        lightIntensitySlider.setPaintTicks(true);
        lightIntensitySlider.setPaintLabels(true);
        lightIntensitySlider.addChangeListener(e -> {
            float value = lightIntensitySlider.getValue() / 100.0f;
            model.setLightIntensity(value);
        });
        
        // Shadow intensity slider
        JLabel shadowLabel = new JLabel("Shadow:");
        shadowIntensitySlider = new JSlider(0, 100, (int)(model.getShadowIntensity() * 100));
        shadowIntensitySlider.setMajorTickSpacing(25);
        shadowIntensitySlider.setPaintTicks(true);
        shadowIntensitySlider.setPaintLabels(true);
        shadowIntensitySlider.addChangeListener(e -> {
            float value = shadowIntensitySlider.getValue() / 100.0f;
            model.setShadowIntensity(value);
        });
        
        // Contrast slider
        JLabel contrastLabel = new JLabel("Contrast:");
        contrastSlider = new JSlider(50, 150, (int)(model.getContrast() * 100));
        contrastSlider.setMajorTickSpacing(25);
        contrastSlider.setPaintTicks(true);
        contrastSlider.setPaintLabels(true);
        contrastSlider.addChangeListener(e -> {
            float value = contrastSlider.getValue() / 100.0f;
            model.setContrast(value);
        });
        
        // Add components to panel
        panel.add(lightLabel);
        panel.add(lightIntensitySlider);
        panel.add(shadowLabel);
        panel.add(shadowIntensitySlider);
        panel.add(contrastLabel);
        panel.add(contrastSlider);
        
        return panel;
    }
    
    /**
     * Creates the furniture panel
     * @return the furniture panel
     */
    private JPanel createFurniturePanel() {
        JPanel panel = new JPanel(new MigLayout("fillx, wrap 2", "[50%][50%]", "[]5[]"));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Add Furniture",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));
        
        // Create buttons for each furniture type
        for (String type : furnitureTypes) {
            JButton button = UIUtils.createStyledButton(type, null, AppConstants.PRIMARY_COLOR, Color.WHITE);
            button.addActionListener(e -> {
                // Directly create a new furniture item in the design model
                System.out.println("Creating furniture item: " + type);
                
                // Create a new furniture item with default dimensions based on type
                int width = 0;
                int height = 0;
                int depth = 0;
                
                switch (type) {
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
                        width = 150;
                        height = 40;
                        depth = 60;
                        break;
                    case "Coffee Table":
                        width = 80;
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
                        height = 180;
                        depth = 50;
                        break;
                    case "Bookshelf":
                        width = 90;
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
                        height = 90;
                        depth = 40;
                        break;
                    case "Lamp":
                        width = 30;
                        height = 120;
                        depth = 30;
                        break;
                }
                
                if (width > 0 && height > 0 && depth > 0) {
                    // Create a new furniture item and add it to the model
                    // Position it in the center of the room
                    int x = model.getRoomWidth() / 2 - width / 2;
                    int y = 0; // On the floor
                    int z = model.getRoomLength() / 2 - depth / 2;
                    
                    // Create and add the furniture item to the model
                    Color woodColor = new Color(139, 69, 19); // Brown color
                    FurnitureItem item = new FurnitureItem(type, x, y, z, width, height, depth, woodColor);
                    item.setMaterial("Wood");
                    item.setRotation(0);
                    
                    model.addFurnitureItem(item);
                    System.out.println("Added furniture item: " + type + " at position " + x + ", " + y + ", " + z);
                }
            });
            panel.add(button, "growx");
        }
        
        return panel;
    }
    
    @Override
    public void onModelChanged(String changeType) {
        // Update UI components when the model changes
        if (changeType.equals("ROOM_DIMENSIONS_CHANGED")) {
            widthSpinner.setValue(model.getRoomWidth());
            lengthSpinner.setValue(model.getRoomLength());
            heightSpinner.setValue(model.getRoomHeight());
        } else if (changeType.equals("ROOM_COLOR_CHANGED")) {
            floorColorButton.setBackground(model.getRoomFloorColor());
            wallColorButton.setBackground(model.getRoomWallColor());
            ceilingColorButton.setBackground(model.getRoomCeilingColor());
        } else if (changeType.equals("LIGHTING_CHANGED")) {
            lightIntensitySlider.setValue((int)(model.getLightIntensity() * 100));
            shadowIntensitySlider.setValue((int)(model.getShadowIntensity() * 100));
            contrastSlider.setValue((int)(model.getContrast() * 100));
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(250, super.getPreferredSize().height);
    }
}
