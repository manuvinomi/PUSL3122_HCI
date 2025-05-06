package com.furnitureapp.ui.components;

import com.furnitureapp.ui.panels.DesignerDashboard;
import com.furnitureapp.util.AppConstants;
import com.furnitureapp.util.ThemeManager;
import com.furnitureapp.util.UIUtils;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * A modern and visually appealing login frame for the furniture design application
 */
public class LoginFrame extends JFrame {
    // UI Components
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginBtn;
    private JLabel statusLabel;
    private JPanel mainPanel;
    
    public LoginFrame() {
        // Set up the frame
        setTitle("Furniture Designer Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true); // Remove window decorations for modern look
        
        // Create main panel with gradient background
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
        
        // Create header panel with logo/title
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        setContentPane(mainPanel);
        
        // Set up action listeners
        setupActionListeners();
        
        // Set window shape to rounded rectangle
        setShape(new RoundRectangle2D.Double(0, 0, 450, 550, 15, 15));
        
        // Make the frame visible
        setVisible(true);
    }
    
    /**
     * Creates the header panel with logo and title
     */
    private JPanel createHeaderPanel() {
        // Create gradient panel for header
        JPanel headerPanel = UIUtils.createGradientPanel(
            AppConstants.PRIMARY_COLOR,
            AppConstants.PRIMARY_DARK_COLOR,
            true
        );
        headerPanel.setLayout(new MigLayout("fill, insets 30 20 30 20"));
        
        // Create logo panel
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw furniture icon
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                
                // Draw a chair icon
                g2d.setColor(Color.WHITE);
                
                // Chair back
                g2d.fillRoundRect(centerX - 15, centerY - 30, 30, 10, 5, 5);
                g2d.fillRect(centerX - 15, centerY - 20, 5, 25);
                g2d.fillRect(centerX + 10, centerY - 20, 5, 25);
                
                // Chair seat
                g2d.fillRoundRect(centerX - 20, centerY + 5, 40, 10, 5, 5);
                
                // Chair legs
                g2d.fillRect(centerX - 15, centerY + 15, 5, 15);
                g2d.fillRect(centerX + 10, centerY + 15, 5, 15);
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(80, 80));
        
        // App title
        JLabel titleLabel = UIUtils.createStyledLabel(AppConstants.APP_NAME, 24, true, Color.WHITE);
        
        // Subtitle
        JLabel subtitleLabel = UIUtils.createStyledLabel("Design your space in 2D and 3D", 14, false, new Color(220, 220, 255));
        
        // Add components to header panel
        headerPanel.add(logoPanel, "align center, wrap");
        headerPanel.add(titleLabel, "align center, wrap");
        headerPanel.add(subtitleLabel, "align center");
        
        // Add window control buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        controlPanel.setOpaque(false);
        
        // Minimize button
        JButton minimizeBtn = new JButton("_");
        minimizeBtn.setForeground(Color.WHITE);
        minimizeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        minimizeBtn.setBorderPainted(false);
        minimizeBtn.setContentAreaFilled(false);
        minimizeBtn.setFocusPainted(false);
        minimizeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        minimizeBtn.addActionListener(e -> setState(JFrame.ICONIFIED));
        
        // Close button
        JButton closeBtn = new JButton("×");
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Arial", Font.BOLD, 18));
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));
        
        controlPanel.add(minimizeBtn);
        controlPanel.add(closeBtn);
        
        headerPanel.add(controlPanel, "pos 0 0 100% 30");
        
        return headerPanel;
    }
    
    /**
     * Creates the form panel with login fields
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new MigLayout("fillx, insets 30", "[grow]"));
        formPanel.setBackground(UIManager.getColor("Panel.background"));
        
        // Username field
        JLabel userLabel = UIUtils.createStyledLabel("Username", 14, true, UIManager.getColor("Label.foreground"));
        userField = UIUtils.createStyledTextField(20);
        userField.setToolTipText("Enter your username (admin)");
        
        // Password field
        JLabel passLabel = UIUtils.createStyledLabel("Password", 14, true, UIManager.getColor("Label.foreground"));
        passField = UIUtils.createStyledPasswordField(20);
        passField.setToolTipText("Enter your password (Test@123)");
        
        // Login button
        loginBtn = UIUtils.createStyledButton("LOGIN", null, AppConstants.PRIMARY_COLOR, Color.WHITE);
        
        // Status label for error messages
        statusLabel = UIUtils.createStyledLabel("", 12, false, Color.RED);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Theme toggle
        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        themePanel.setOpaque(false);
        
        JLabel themeLabel = UIUtils.createStyledLabel("Theme:", 12, false, UIManager.getColor("Label.foreground"));
        
        JToggleButton themeToggle = new JToggleButton();
        themeToggle.setText(ThemeManager.getCurrentTheme() == ThemeManager.Theme.LIGHT ? "Dark" : "Light");
        themeToggle.setFocusPainted(false);
        themeToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeToggle.addActionListener(e -> {
            if (themeToggle.isSelected()) {
                ThemeManager.changeTheme(ThemeManager.Theme.LIGHT);
                themeToggle.setText("Dark");
            } else {
                ThemeManager.changeTheme(ThemeManager.Theme.ARC_DARK);
                themeToggle.setText("Light");
            }
        });
        
        themePanel.add(themeLabel);
        themePanel.add(themeToggle);
        
        // Add hint text
        JLabel hintLabel = UIUtils.createStyledLabel("Hint: Use admin/Test@123", 12, false, UIManager.getColor("Label.disabledForeground"));
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add components to form panel
        formPanel.add(userLabel, "wrap");
        formPanel.add(userField, "growx, wrap 20");
        formPanel.add(passLabel, "wrap");
        formPanel.add(passField, "growx, wrap 30");
        formPanel.add(loginBtn, "growx, h 40, wrap 10");
        formPanel.add(statusLabel, "growx, wrap 20");
        formPanel.add(hintLabel, "align center, wrap 30");
        formPanel.add(themePanel, "align center");
        
        return formPanel;
    }
    
    /**
     * Sets up action listeners for interactive components
     */
    private void setupActionListeners() {
        // Make the frame draggable
        setupDraggableFrame();
        
        // Button hover effect
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginBtn.setBackground(AppConstants.PRIMARY_LIGHT_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                loginBtn.setBackground(AppConstants.PRIMARY_COLOR);
            }
        });
        
        // Login button action
        loginBtn.addActionListener(e -> attemptLogin());
        
        // Enter key in password field triggers login
        passField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    attemptLogin();
                }
            }
        });
    }
    
    /**
     * Makes the frame draggable (for the undecorated window)
     */
    private void setupDraggableFrame() {
        final Point[] dragPoint = {new Point(0, 0)};
        
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragPoint[0] = e.getPoint();
            }
        });
        
        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getLocationOnScreen();
                setLocation(currentPoint.x - dragPoint[0].x, currentPoint.y - dragPoint[0].y);
            }
        });
    }
    
    /**
     * Attempts to log in with the provided credentials
     */
    private void attemptLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password");
            shakeWindow(); // Visual feedback
            return;
        }
        
        // Simulate login process
        loginBtn.setEnabled(false);
        loginBtn.setText("Logging in...");
        
        // Use a timer to simulate network delay
        Timer timer = new Timer(800, e -> {
            // Check credentials
            if (username.equals(AppConstants.VALID_USERNAME) && 
                password.equals(AppConstants.VALID_PASSWORD)) {
                // Login successful
                dispose();
                SwingUtilities.invokeLater(() -> new DesignerDashboard());
            } else {
                // Login failed
                statusLabel.setText("Invalid username or password");
                loginBtn.setEnabled(true);
                loginBtn.setText("LOGIN");
                shakeWindow(); // Visual feedback for error
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * Shakes the window to indicate an error
     */
    private void shakeWindow() {
        final int originalX = getLocation().x;
        final int[] time = {0};
        
        Timer shakeTimer = new Timer(30, e -> {
            int offset = 10;
            if (time[0] % 2 == 0) {
                offset = -offset;
            }
            
            setLocation(originalX + offset, getLocation().y);
            time[0]++;
            
            if (time[0] >= 6) {
                ((Timer) e.getSource()).stop();
                setLocation(originalX, getLocation().y);
            }
        });
        
        shakeTimer.start();
    }
}
