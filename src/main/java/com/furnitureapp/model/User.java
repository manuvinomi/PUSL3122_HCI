package com.furnitureapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a user of the furniture design application
 */
public class User implements Serializable {
    private String username;
    private String password;
    private String displayName;
    private String email;
    private LocalDateTime lastLogin;
    private UserPreferences preferences;

    /**
     * Creates a new user with the specified username and password
     * @param username the username
     * @param password the password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.displayName = username;
        this.preferences = new UserPreferences();
    }
    
    /**
     * Creates a new user with the specified details
     * @param username the username
     * @param password the password
     * @param displayName the display name
     * @param email the email address
     */
    public User(String username, String password, String displayName, String email) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.email = email;
        this.preferences = new UserPreferences();
    }

    // Getters and setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public UserPreferences getPreferences() { return preferences; }
    
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setEmail(String email) { this.email = email; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    public void setPreferences(UserPreferences preferences) { this.preferences = preferences; }
    
    /**
     * Updates the last login time to now
     */
    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }
    
    /**
     * Inner class for user preferences
     */
    public static class UserPreferences implements Serializable {
        private boolean darkMode = true;
        private boolean showWelcomeScreen = true;
        private boolean autoSave = true;
        private int autoSaveInterval = 5; // minutes
        
        // Getters and setters
        public boolean isDarkMode() { return darkMode; }
        public boolean isShowWelcomeScreen() { return showWelcomeScreen; }
        public boolean isAutoSave() { return autoSave; }
        public int getAutoSaveInterval() { return autoSaveInterval; }
        
        public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }
        public void setShowWelcomeScreen(boolean showWelcomeScreen) { this.showWelcomeScreen = showWelcomeScreen; }
        public void setAutoSave(boolean autoSave) { this.autoSave = autoSave; }
        public void setAutoSaveInterval(int autoSaveInterval) { this.autoSaveInterval = autoSaveInterval; }
    }
}
