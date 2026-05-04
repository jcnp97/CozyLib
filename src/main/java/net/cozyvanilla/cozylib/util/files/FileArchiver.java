package net.cozyvanilla.cozylib.util.files;

import net.cozyvanilla.cozylib.Logger;
import net.cozyvanilla.cozylib.common.enums.DateFormat;
import net.cozyvanilla.cozylib.utilities.time.InstantUtils;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.file.Files;
import java.time.Instant;

public class FileArchiver {
    private final File parentDirectory;
    private final int ageThreshold;

    private File outputDirectory;

    /**
     * Creates a new archiver using a parent directory and age threshold.
     *
     * @param parentDirectory the base directory where archives will be stored
     * @param ageThreshold the maximum age (in seconds) before files are purged
     */
    public FileArchiver(File parentDirectory, int ageThreshold) {
        this.ageThreshold = ageThreshold;
        this.parentDirectory = parentDirectory;
        this.outputDirectory = createOutputDir(InstantUtils.toReadable(Instant.now(), DateFormat.ISO_DATE));
        // purge files on startup
        purge();
    }

    /**
     * Creates a new archiver using a plugin's data folder and output path.
     *
     * @param plugin the plugin instance
     * @param outputPath the relative path inside the plugin's data folder
     * @param ageThreshold the maximum age (in seconds) before files are purged
     */
    public FileArchiver(Plugin plugin, String outputPath, int ageThreshold) {
        this.ageThreshold = ageThreshold;
        this.parentDirectory = new File(plugin.getDataFolder(), outputPath);
        this.outputDirectory = createOutputDir(InstantUtils.toReadable(Instant.now(), DateFormat.ISO_DATE));
        // purge files on startup
        purge();
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Creates a new output directory with the given name.
     *
     * @param directoryName the name of the directory
     * @return the created directory
     */
    private File createOutputDir(String directoryName) {
        File directory = new File(parentDirectory, directoryName);
        directory.mkdirs();
        return directory;
    }

    /**
     * Updates the output directory if the current date has changed.
     */
    private void refreshOutputDirectory() {
        String currentTime = InstantUtils.toReadable(Instant.now(), DateFormat.ISO_DATE);
        if (!outputDirectory.getName().equals(currentTime)) {
            this.outputDirectory = createOutputDir(currentTime);
        }
    }

    /**
     * Compresses a directory into a .tar.gz archive.
     *
     * @param toCompress the directory to compress
     * @param fileName the output file name (without extension)
     */
    private void archiveDirectory(File toCompress, String fileName) {
        try {
            DirectoryUtils.compress(toCompress, outputDirectory, fileName);
        } catch (IOException e) {
            Logger.severe("Failed to compress directory: " + toCompress.getPath(), e);
        }
    }

    /**
     * Copies a file into the output directory as a .log file.
     *
     * @param file the file to copy
     * @param fileName the output file name (without extension)
     */
    private void archiveFile(File file, String fileName) {
        File logFile = new File(outputDirectory, fileName + ".log");
        try {
            Files.copy(file.toPath(), logFile.toPath());
        } catch (IOException e) {
            Logger.severe("Failed to copy log file: " + file.getPath(), e);
        }
    }

    /**
     * Recursively deletes archived files older than the configured threshold.
     *
     * @param dir the directory to scan
     */
    private void purgeFiles(File dir) {
        File[] entries = dir.listFiles();
        if (entries == null) return;

        Instant cutoff = Instant.now().minusSeconds(ageThreshold);

        for (File entry : entries) {
            if (entry.isDirectory()) {
                purgeFiles(entry);
                continue;
            }

            String name = entry.getName();
            if (!name.endsWith(".tar.gz") && !name.endsWith(".log")) continue;

            String token = name.endsWith(".tar.gz")
                    ? name.substring(0, name.length() - 7)
                    : name.substring(0, name.length() - 4);

            try {
                Instant fileInstant = InstantUtils.toInstant(token);
                if (fileInstant.isBefore(cutoff)) {
                    entry.delete();
                }
            } catch (Exception e) {
                Logger.severe("Skipping unparseable filename: " + name, e);
            }
        }
    }

    /**
     * Recursively deletes empty directories except the parent directory.
     *
     * @param dir the directory to scan
     */
    private void deleteEmptyDirs(File dir) {
        File[] entries = dir.listFiles();
        if (entries == null) return;

        for (File entry : entries) {
            if (entry.isDirectory()) {
                deleteEmptyDirs(entry);
                File[] remaining = entry.listFiles();
                if (remaining != null && remaining.length == 0 && !entry.equals(parentDirectory)) {
                    entry.delete();
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Archives a file or directory into the current output directory.
     *
     * @param file the file or directory to archive
     */
    public void archive(File file) {
        refreshOutputDirectory();
        String fileName = InstantUtils.toString(Instant.now());
        if (file.isDirectory()) {
            archiveDirectory(file, fileName);
        } else {
            archiveFile(file, fileName);
        }
    }

    /**
     * Purges old archived files and removes empty directories.
     */
    public void purge() {
        purgeFiles(parentDirectory);
        deleteEmptyDirs(parentDirectory);
    }
}