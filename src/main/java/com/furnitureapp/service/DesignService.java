package com.furnitureapp.service;

import com.furnitureapp.model.Design;
import com.furnitureapp.util.AppConstants;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for managing furniture designs
 */
public class DesignService {
    private static final Logger LOGGER = Logger.getLogger(DesignService.class.getName());
    
    /**
     * Creates a new design service
     */
    public DesignService() {
        // Create the designs directory if it doesn't exist
        ensureDesignsDirectoryExists();
    }
    
    /**
     * Ensures that the designs directory exists
     * @return true if the directory exists or was created successfully
     */
    private boolean ensureDesignsDirectoryExists() {
        File dir = new File(AppConstants.DESIGNS_DIRECTORY);
        if (dir.exists()) {
            LOGGER.info("Designs directory already exists: " + dir.getAbsolutePath());
            return true;
        }
        
        boolean created = dir.mkdirs();
        if (created) {
            LOGGER.info("Created designs directory: " + dir.getAbsolutePath());
            return true;
        } else {
            LOGGER.warning("Failed to create designs directory: " + dir.getAbsolutePath());
            return false;
        }
    }
    
    /**
     * Saves a design to disk
     * @param design the design to save
     * @throws IOException if an I/O error occurs
     */
    public void saveDesign(Design design) throws IOException {
        // Ensure the designs directory exists
        if (!ensureDesignsDirectoryExists()) {
            throw new IOException("Could not create designs directory");
        }
        
        // Create the file path
        File file = new File(AppConstants.DESIGNS_DIRECTORY, design.getId() + AppConstants.DESIGN_FILE_EXTENSION);
        LOGGER.info("Saving design to: " + file.getAbsolutePath());
        
        // Create parent directories if they don't exist
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                throw new IOException("Could not create parent directories for design file");
            }
        }
        
        // Save the design
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(design);
            LOGGER.info("Successfully saved design: " + design.getName() + " to " + file.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving design: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Loads a design from disk
     * @param designId the ID of the design to load
     * @return the loaded design
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if the class of the serialized object cannot be found
     */
    public Design loadDesign(String designId) throws IOException, ClassNotFoundException {
        File file = new File(AppConstants.DESIGNS_DIRECTORY, designId + AppConstants.DESIGN_FILE_EXTENSION);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Design design = (Design) ois.readObject();
            LOGGER.info("Loaded design: " + design.getName());
            return design;
        }
    }
    
    /**
     * Deletes a design from disk
     * @param designId the ID of the design to delete
     * @return true if the design was deleted, false otherwise
     */
    public boolean deleteDesign(String designId) {
        File file = new File(AppConstants.DESIGNS_DIRECTORY, designId + AppConstants.DESIGN_FILE_EXTENSION);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                LOGGER.info("Deleted design: " + designId);
            } else {
                LOGGER.warning("Failed to delete design: " + designId);
            }
            return deleted;
        }
        return false;
    }
    
    /**
     * Gets all designs from disk
     * @return a list of all designs
     */
    public List<Design> getAllDesigns() {
        List<Design> designs = new ArrayList<>();
        File dir = new File(AppConstants.DESIGNS_DIRECTORY);
        
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((dir1, name) -> name.endsWith(AppConstants.DESIGN_FILE_EXTENSION));
            
            if (files != null) {
                for (File file : files) {
                    try {
                        designs.add(loadDesign(file.getName().replace(AppConstants.DESIGN_FILE_EXTENSION, "")));
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Failed to load design: " + file.getName(), e);
                    }
                }
            }
        }
        
        // Sort designs by last modified time (newest first)
        designs.sort(Comparator.comparing(Design::getLastModifiedTime).reversed());
        
        return designs;
    }
    
    /**
     * Exports a design as an image
     * @param design the design to export
     * @param imageFile the file to save the image to
     * @param image the image data
     * @throws IOException if an I/O error occurs
     */
    public void exportDesignImage(Design design, File imageFile, byte[] image) throws IOException {
        // Ensure the exports directory exists
        File exportsDir = new File(AppConstants.EXPORTS_DIRECTORY);
        if (!exportsDir.exists()) {
            boolean created = exportsDir.mkdirs();
            if (!created) {
                LOGGER.warning("Failed to create exports directory");
            }
        }
        
        // Write the image file
        Files.write(imageFile.toPath(), image);
        LOGGER.info("Exported design image: " + imageFile.getName());
    }
    
    /**
     * Creates a backup of all designs
     * @param backupDir the directory to save the backup to
     * @throws IOException if an I/O error occurs
     */
    public void createBackup(File backupDir) throws IOException {
        if (!backupDir.exists()) {
            boolean created = backupDir.mkdirs();
            if (!created) {
                throw new IOException("Failed to create backup directory");
            }
        }
        
        File designsDir = new File(AppConstants.DESIGNS_DIRECTORY);
        if (designsDir.exists() && designsDir.isDirectory()) {
            File[] files = designsDir.listFiles((dir, name) -> name.endsWith(AppConstants.DESIGN_FILE_EXTENSION));
            
            if (files != null) {
                for (File file : files) {
                    Path source = file.toPath();
                    Path destination = Paths.get(backupDir.getPath(), file.getName());
                    Files.copy(source, destination);
                }
            }
        }
        
        LOGGER.info("Created backup in: " + backupDir.getPath());
    }
    
    /**
     * Restores designs from a backup
     * @param backupDir the directory containing the backup
     * @throws IOException if an I/O error occurs
     */
    public void restoreFromBackup(File backupDir) throws IOException {
        if (!backupDir.exists() || !backupDir.isDirectory()) {
            throw new IOException("Backup directory does not exist or is not a directory");
        }
        
        File designsDir = new File(AppConstants.DESIGNS_DIRECTORY);
        if (!designsDir.exists()) {
            boolean created = designsDir.mkdirs();
            if (!created) {
                throw new IOException("Failed to create designs directory");
            }
        }
        
        File[] files = backupDir.listFiles((dir, name) -> name.endsWith(AppConstants.DESIGN_FILE_EXTENSION));
        
        if (files != null) {
            for (File file : files) {
                Path source = file.toPath();
                Path destination = Paths.get(designsDir.getPath(), file.getName());
                Files.copy(source, destination);
            }
        }
        
        LOGGER.info("Restored backup from: " + backupDir.getPath());
    }
}
