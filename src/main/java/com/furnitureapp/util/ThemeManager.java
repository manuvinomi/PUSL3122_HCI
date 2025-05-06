package com.furnitureapp.util;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages application themes and UI customization
 */
public class ThemeManager {
    private static final Logger LOGGER = Logger.getLogger(ThemeManager.class.getName());
    
    // Theme options
    public enum Theme {
        LIGHT, DARK, ARC_LIGHT, ARC_DARK
    }
    
    private static Theme currentTheme = Theme.ARC_DARK;
    
    /**
     * Sets up UI defaults for custom components
     */
    public static void setupUIDefaults() {
        // Set custom UI properties
        UIManager.put("Button.arc", 10);
        UIManager.put("Component.arc", 10);
        UIManager.put("ProgressBar.arc", 10);
        UIManager.put("TextComponent.arc", 10);
        
        // Custom fonts
        Font defaultFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font boldFont = new Font("Segoe UI", Font.BOLD, 13);
        Font smallFont = new Font("Segoe UI", Font.PLAIN, 11);
        
        UIManager.put("defaultFont", defaultFont);
        UIManager.put("Button.font", boldFont);
        UIManager.put("ToolTip.font", smallFont);
        
        // Custom borders
        Border textFieldBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIManager.getColor("TextField.borderColor"), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        );
        UIManager.put("TextField.border", textFieldBorder);
        UIManager.put("PasswordField.border", textFieldBorder);
    }
    
    /**
     * Changes the application theme
     * @param theme the theme to apply
     * @return true if theme was changed successfully
     */
    public static boolean changeTheme(Theme theme) {
        try {
            switch (theme) {
                case LIGHT:
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    break;
                case DARK:
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    break;
                case ARC_LIGHT:
                    UIManager.setLookAndFeel(new FlatArcIJTheme());
                    break;
                case ARC_DARK:
                    UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
                    break;
                default:
                    UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
                    break;
            }
            
            // Update UI defaults after theme change
            setupUIDefaults();
            
            // Update all open windows
            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }
            
            currentTheme = theme;
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to change theme: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Gets the current theme
     * @return the current theme
     */
    public static Theme getCurrentTheme() {
        return currentTheme;
    }
    
    /**
     * Creates a rounded border with the specified color
     * @param color the border color
     * @param radius the corner radius
     * @return a rounded border
     */
    public static Border createRoundedBorder(Color color, int radius) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1, true),
            BorderFactory.createEmptyBorder(radius, radius, radius, radius)
        );
    }
}
