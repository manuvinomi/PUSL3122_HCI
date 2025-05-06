package com.furnitureapp.ui.components;

import com.furnitureapp.util.AppConstants;
import com.furnitureapp.util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern splash screen with progress animation
 */
public class SplashScreen extends JWindow {
    
    private JProgressBar progressBar;
    private Timer progressTimer;
    private Runnable onComplete;
    
    /**
     * Creates a new splash screen
     * @param onComplete callback to run when splash screen completes
     */
    public SplashScreen(Runnable onComplete) {
        this.onComplete = onComplete;
        
        // Set up the window
        setSize(500, 350);
        setLocationRelativeTo(null);
        
        // Create main panel with gradient background
        JPanel mainPanel = UIUtils.createGradientPanel(
            new Color(48, 63, 159), // Dark indigo
            new Color(63, 81, 181), // Indigo
            true
        );
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add logo panel at the top
        JPanel logoPanel = createLogoPanel();
        mainPanel.add(logoPanel, BorderLayout.CENTER);
        
        // Add title panel
        JPanel titlePanel = createTitlePanel();
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Add progress panel at the bottom
        JPanel progressPanel = createProgressPanel();
        mainPanel.add(progressPanel, BorderLayout.SOUTH);
        
        // Set content pane
        setContentPane(mainPanel);
        
        // Make the window shape rounded
        setShape(new RoundRectangle2D.Double(0, 0, 500, 350, 20, 20));
        
        // Start progress animation
        startProgressAnimation();
        
        // Show the window
        setVisible(true);
    }
    
    /**
     * Creates the logo panel with furniture icon
     */
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw a 3D room with furniture
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                
                // Room floor (3D perspective)
                Polygon floor = new Polygon();
                floor.addPoint(centerX - 100, centerY + 50);
                floor.addPoint(centerX + 100, centerY + 50);
                floor.addPoint(centerX + 150, centerY);
                floor.addPoint(centerX - 50, centerY);
                g2d.setColor(new Color(200, 200, 200));
                g2d.fillPolygon(floor);
                
                // Room left wall
                Polygon leftWall = new Polygon();
                leftWall.addPoint(centerX - 100, centerY + 50);
                leftWall.addPoint(centerX - 50, centerY);
                leftWall.addPoint(centerX - 50, centerY - 100);
                leftWall.addPoint(centerX - 100, centerY - 50);
                g2d.setColor(new Color(180, 180, 220));
                g2d.fillPolygon(leftWall);
                
                // Room back wall
                Polygon backWall = new Polygon();
                backWall.addPoint(centerX - 50, centerY);
                backWall.addPoint(centerX + 150, centerY);
                backWall.addPoint(centerX + 150, centerY - 100);
                backWall.addPoint(centerX - 50, centerY - 100);
                g2d.setColor(new Color(160, 160, 200));
                g2d.fillPolygon(backWall);
                
                // Draw a table (3D)
                g2d.setColor(new Color(139, 69, 19));
                // Table top
                Polygon tableTop = new Polygon();
                tableTop.addPoint(centerX - 30, centerY);
                tableTop.addPoint(centerX + 70, centerY);
                tableTop.addPoint(centerX + 50, centerY - 10);
                tableTop.addPoint(centerX - 50, centerY - 10);
                g2d.fillPolygon(tableTop);
                
                // Table legs
                g2d.fillRect(centerX - 25, centerY, 10, 30);
                g2d.fillRect(centerX + 65, centerY, 10, 30);
                
                // Draw a chair
                g2d.setColor(new Color(160, 82, 45));
                // Chair seat
                Polygon chairSeat = new Polygon();
                chairSeat.addPoint(centerX - 80, centerY + 10);
                chairSeat.addPoint(centerX - 50, centerY + 10);
                chairSeat.addPoint(centerX - 60, centerY);
                chairSeat.addPoint(centerX - 90, centerY);
                g2d.fillPolygon(chairSeat);
                
                // Chair back
                Polygon chairBack = new Polygon();
                chairBack.addPoint(centerX - 90, centerY);
                chairBack.addPoint(centerX - 90, centerY - 30);
                chairBack.addPoint(centerX - 85, centerY - 30);
                chairBack.addPoint(centerX - 85, centerY);
                g2d.fillPolygon(chairBack);
                
                // Chair legs
                g2d.fillRect(centerX - 80, centerY + 10, 5, 20);
                g2d.fillRect(centerX - 55, centerY + 10, 5, 20);
                
                // Draw a lamp
                g2d.setColor(new Color(100, 100, 100));
                g2d.fillRect(centerX + 40, centerY - 10, 5, 5);
                g2d.fillRect(centerX + 42, centerY - 40, 1, 30);
                
                // Lamp shade
                g2d.setColor(new Color(255, 215, 0, 200));
                Polygon lampShade = new Polygon();
                lampShade.addPoint(centerX + 32, centerY - 40);
                lampShade.addPoint(centerX + 52, centerY - 40);
                lampShade.addPoint(centerX + 47, centerY - 60);
                lampShade.addPoint(centerX + 37, centerY - 60);
                g2d.fillPolygon(lampShade);
                
                // Draw light effect
                g2d.setColor(new Color(255, 255, 200, 50));
                g2d.fillOval(centerX + 17, centerY - 80, 60, 60);
            }
        };
        logoPanel.setOpaque(false);
        return logoPanel;
    }
    
    /**
     * Creates the title panel with app name and subtitle
     */
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout(0, 10));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // App title
        JLabel titleLabel = UIUtils.createStyledLabel(AppConstants.APP_NAME, 28, true, Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        
        // Subtitle
        JLabel subtitleLabel = UIUtils.createStyledLabel("3D Furniture Design Tool", 14, false, new Color(220, 220, 255));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);
        
        // Version
        JLabel versionLabel = UIUtils.createStyledLabel("Version " + AppConstants.APP_VERSION, 12, false, new Color(200, 200, 255));
        versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(versionLabel, BorderLayout.SOUTH);
        
        return titlePanel;
    }
    
    /**
     * Creates the progress panel with progress bar and status
     */
    private JPanel createProgressPanel() {
        JPanel progressPanel = new JPanel(new BorderLayout(0, 5));
        progressPanel.setOpaque(false);
        progressPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setForeground(new Color(255, 64, 129)); // Pink accent color
        progressBar.setBackground(new Color(255, 255, 255, 50));
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(progressBar.getPreferredSize().width, 5));
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        // Status label
        JLabel statusLabel = UIUtils.createStyledLabel("Loading application...", 12, false, Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        progressPanel.add(statusLabel, BorderLayout.SOUTH);
        
        return progressPanel;
    }
    
    /**
     * Starts the progress animation
     */
    private void startProgressAnimation() {
        progressTimer = new Timer(30, new ActionListener() {
            private int progress = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 1;
                progressBar.setValue(progress);
                
                if (progress >= 100) {
                    progressTimer.stop();
                    dispose();
                    
                    // Run the completion callback
                    if (onComplete != null) {
                        SwingUtilities.invokeLater(onComplete);
                    }
                }
            }
        });
        progressTimer.start();
    }
}
