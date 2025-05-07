package com.furnitureapp.core;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import com.furnitureapp.ui.components.SplashScreen;
import com.furnitureapp.ui.components.LoginFrame;
import com.furnitureapp.util.AppConstants;
import com.furnitureapp.util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for the Furniture Design Application
 * This application allows furniture designers to create and visualize
 * room layouts in both 2D and 3D views.
 */
public class Main {
    // Logger for application-wide logging
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    
    /**
     * Application entry point
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Configure logging
        configureLogging();
        
        LOGGER.info("Starting " + AppConstants.APP_NAME + " version " + AppConstants.APP_VERSION);
        
        // Set the look and feel to FlatLaf
        setupLookAndFeel();
        
        // Ensure application directories exist
        ensureApplicationDirectories();
        
        // Start the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Show splash screen with progress
            new SplashScreen(() -> {
                // After splash screen closes, show login frame
                new LoginFrame();
            });
        });
    }
    
    /**
     * Configure application logging
     */
    private static void configureLogging() {
        // Set default logging level
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO);
    }
    
    /**
     * Set up the application look and feel
     */
    private static void setupLookAndFeel() {
        try {
            // Set the default theme (can be changed by user later)
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
            
            // Register custom UI defaults
            ThemeManager.setupUIDefaults();
            
            LOGGER.info("Using FlatLaf look and feel");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not set look and feel: " + e.getMessage(), e);
            // Fallback to system look and feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Failed to set system look and feel", ex);
            }
        }
    }
    
    /**
     * Ensure that all required application directories exist
     */
    private static void ensureApplicationDirectories() {
        // Ensure designs directory exists
        File designsDir = new File("designs");
        if (!designsDir.exists()) {
            boolean created = designsDir.mkdirs();
            if (created) {
                LOGGER.info("Created designs directory");
            } else {
                LOGGER.warning("Failed to create designs directory");
            }
        }
        
        // Ensure exports directory exists
        File exportsDir = new File("exports");
        if (!exportsDir.exists()) {
            boolean created = exportsDir.mkdirs();
            if (created) {
                LOGGER.info("Created exports directory");
            } else {
                LOGGER.warning("Failed to create exports directory");
            }
        }
    }
}
