package com.furnitureapp.ui.panels;

import com.furnitureapp.model.DesignModel;
import com.furnitureapp.model.FurnitureItem;
import com.furnitureapp.service.DesignService;
import com.furnitureapp.util.AppConstants;
import com.furnitureapp.util.ThemeManager;
import com.furnitureapp.util.UIUtils;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Main dashboard for the furniture designer application
 */
public class DesignerDashboard extends JFrame {
    private DesignModel designModel;
    private Design2DPanel design2DPanel;
    private Design3DPanel design3DPanel;
    private RoomConfigPanel roomConfigPanel;
    private DesignService designService;
    private JTabbedPane tabbedPane;
    
    /**
     * Creates a new designer dashboard
     */
    public DesignerDashboard() {
        setTitle("Furniture Designer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Create the design service
        designService = new DesignService();
        
        // Create the shared design model
        designModel = new DesignModel();
        
        // Initialize UI
        initializeUI();
        
        // Make the frame visible
        setVisible(true);
    }
    
    /**
     * Initializes the UI components
     */
    private void initializeUI() {
        // Create the main toolbar
        JToolBar toolBar = createToolbar();
        add(toolBar, BorderLayout.NORTH);
        
        // Create the side panel for room configuration
        roomConfigPanel = new RoomConfigPanel(designModel);
        JScrollPane configScrollPane = new JScrollPane(roomConfigPanel);
        configScrollPane.setBorder(null);
        configScrollPane.setPreferredSize(new Dimension(250, 600));
        
        // Create the main content panels
        design2DPanel = new Design2DPanel(designModel);
        design3DPanel = new Design3DPanel(designModel);
        
        // Create tabbed pane for 2D/3D views
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("2D Design", new ImageIcon(), design2DPanel, "Edit in 2D view");
        tabbedPane.addTab("3D Preview", new ImageIcon(), design3DPanel, "View in 3D");
        
        // Create a split pane for the config panel and design views
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, configScrollPane, tabbedPane);
        splitPane.setDividerLocation(250);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerSize(5);
        add(splitPane, BorderLayout.CENTER);
        
        // Create status bar
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the main toolbar
     * @return the toolbar
     */
    private JToolBar createToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(new EmptyBorder(5, 5, 5, 5));
        toolBar.setLayout(new MigLayout("insets 0, gap 5", "[left][center, grow][right]"));
        
        // File operations panel
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filePanel.setOpaque(false);
        
        JButton newButton = UIUtils.createStyledButton("New", null, AppConstants.PRIMARY_COLOR, Color.WHITE);
        JButton saveButton = UIUtils.createStyledButton("Save", null, AppConstants.PRIMARY_COLOR, Color.WHITE);
        
        newButton.addActionListener(this::newDesign);
        saveButton.addActionListener(this::saveDesign);
        
        filePanel.add(newButton);
        filePanel.add(saveButton);
        
        // Edit operations panel
        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        editPanel.setOpaque(false);
        
        JButton deleteButton = UIUtils.createStyledButton("Delete", null, AppConstants.PRIMARY_COLOR, Color.WHITE);
        JButton colorButton = UIUtils.createStyledButton("Color", null, AppConstants.PRIMARY_COLOR, Color.WHITE);
        JButton scaleButton = UIUtils.createStyledButton("Scale", null, AppConstants.PRIMARY_COLOR, Color.WHITE);
        
        deleteButton.addActionListener(e -> deleteSelectedItem());
        colorButton.addActionListener(e -> changeItemColor());
        scaleButton.addActionListener(e -> scaleSelectedItem());
        
        editPanel.add(deleteButton);
        editPanel.add(colorButton);
        editPanel.add(scaleButton);
        
        // View operations panel
        JPanel viewPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        viewPanel.setOpaque(false);
        
        JToggleButton themeToggle = new JToggleButton("Dark Mode");
        themeToggle.setSelected(ThemeManager.getCurrentTheme() == ThemeManager.Theme.ARC_DARK);
        
        themeToggle.addActionListener(e -> {
            if (themeToggle.isSelected()) {
                ThemeManager.changeTheme(ThemeManager.Theme.ARC_DARK);
            } else {
                ThemeManager.changeTheme(ThemeManager.Theme.LIGHT);
            }
        });
        
        viewPanel.add(themeToggle);
        
        // Add panels to toolbar
        toolBar.add(filePanel, "left");
        toolBar.add(editPanel, "center");
        toolBar.add(viewPanel, "right");
        
        return toolBar;
    }
    
    /**
     * Creates the status bar
     * @return the status bar
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel statusLabel = new JLabel("Ready");
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        JLabel versionLabel = new JLabel("Version " + AppConstants.APP_VERSION);
        statusBar.add(versionLabel, BorderLayout.EAST);
        
        return statusBar;
    }
    
    /**
     * Creates a new design
     * @param e the action event
     */
    private void newDesign(ActionEvent e) {
        // Confirm if there are unsaved changes
        int result = JOptionPane.showConfirmDialog(this,
                "Create a new design? Any unsaved changes will be lost.",
                "New Design", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            designModel.reset();
        }
    }
    
    /**
     * Opens an existing design
     * @param e the action event
     */
    private void openDesign(ActionEvent e) {
        // TODO: Implement open design dialog
        JOptionPane.showMessageDialog(this, "Open design functionality will be implemented soon.", 
                "Coming Soon", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Saves the current design
     * @param e the action event
     */
    private void saveDesign(ActionEvent e) {
        String name = JOptionPane.showInputDialog(this, "Enter a name for this design:");
        
        if (name != null && !name.isEmpty()) {
            try {
                // Convert DesignModel to Design for saving
                designService.saveDesign(designModel.toDesign(name, "admin"));
                JOptionPane.showMessageDialog(this, "Design saved successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving design: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Exports the current design as an image
     */
    private void exportDesign() {
        // TODO: Implement export functionality
        JOptionPane.showMessageDialog(this, "Export functionality will be implemented soon.", 
                "Coming Soon", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Deletes the selected furniture item
     */
    private void deleteSelectedItem() {
        FurnitureItem selectedItem = designModel.getSelectedItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete", 
                    "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this item?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            designModel.removeFurnitureItem(selectedItem);
        }
    }
    
    /**
     * Changes the color of the selected furniture item
     */
    private void changeItemColor() {
        FurnitureItem selectedItem = designModel.getSelectedItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item to change color", 
                    "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Color newColor = JColorChooser.showDialog(this, "Choose Color", selectedItem.getColor());
        
        if (newColor != null) {
            selectedItem.setColor(newColor);
            designModel.updateFurnitureItem(selectedItem);
        }
    }
    
    /**
     * Rotates the selected furniture item
     */
    private void rotateSelectedItem() {
        FurnitureItem selectedItem = designModel.getSelectedItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item to rotate", 
                    "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Rotate by 90 degrees
        float currentRotation = selectedItem.getRotation();
        selectedItem.setRotation((currentRotation + 90) % 360);
        designModel.updateFurnitureItem(selectedItem);
    }
    
    /**
     * Scales the selected furniture item
     */
    private void scaleSelectedItem() {
        FurnitureItem selectedItem = designModel.getSelectedItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item to scale", 
                    "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Show dialog to get scale factor
        String input = JOptionPane.showInputDialog(this, 
                "Enter scale factor (0.5 to 2.0):", 
                "1.0");
        
        if (input != null && !input.isEmpty()) {
            try {
                float scaleFactor = Float.parseFloat(input);
                
                // Limit scale factor to reasonable range
                if (scaleFactor < 0.5f) scaleFactor = 0.5f;
                if (scaleFactor > 2.0f) scaleFactor = 2.0f;
                
                // Apply scaling
                selectedItem.scale(scaleFactor);
                designModel.updateFurnitureItem(selectedItem);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number", 
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
