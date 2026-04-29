package net.cozyvanilla.cozylib.services.files;

import net.cozyvanilla.cozylib.Enums;
import net.cozyvanilla.cozylib.modules.messages.Console;
import net.cozyvanilla.cozylib.utilities.time.InstantUtils;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.file.Files;
import java.time.Instant;

public class LogFileManager {
    private final File parentDirectory;
    private final int ageThreshold;

    private File outputDirectory;

    public LogFileManager(File parentDirectory, int ageThreshold) {
        this.ageThreshold = ageThreshold;
        this.parentDirectory = parentDirectory;
        this.outputDirectory = createOutputDir(InstantUtils.toReadable(Instant.now(), Enums.DateFormat.ISO_DATE));
        // purge files on startup
        purge();
    }

    public LogFileManager(Plugin plugin, String outputPath, int ageThreshold) {
        this.ageThreshold = ageThreshold;
        this.parentDirectory = new File(plugin.getDataFolder(), outputPath);
        this.outputDirectory = createOutputDir(InstantUtils.toReadable(Instant.now(), Enums.DateFormat.ISO_DATE));
        // purge files on startup
        purge();
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private File createOutputDir(String directoryName) {
        File directory = new File(parentDirectory, directoryName);
        directory.mkdirs();
        return directory;
    }

    private void refreshOutputDirectory() {
        String currentTime = InstantUtils.toReadable(Instant.now(), Enums.DateFormat.ISO_DATE);
        if (!outputDirectory.getName().equals(currentTime)) {
            this.outputDirectory = createOutputDir(currentTime);
        }
    }

    private void logDirectory(File directory, String fileName) {
        File archive = new File(outputDirectory, fileName + ".tar.gz");
        try (FileOutputStream fos = new FileOutputStream(archive)) {
            compress(directory, directory.getName(), fos);
        } catch (IOException e) {
            Console.severe("Failed to compress directory: " + directory.getPath());
        }
    }

    private void compress(File source, String entryName, OutputStream sink) throws IOException {
        // Use a raw TAR-over-GZ implementation to avoid external dependencies.
        // Walk the source tree, writing each file as a TAR entry.
        try (java.util.zip.GZIPOutputStream gzip = new java.util.zip.GZIPOutputStream(sink)) {
            writeTar(source, entryName, gzip);
        }
    }

    private void writeTar(File file, String entryPath, OutputStream out) throws IOException {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    writeTar(child, entryPath + "/" + child.getName(), out);
                }
            }
            return;
        }
        byte[] header = buildTarHeader(entryPath, file.length());
        out.write(header);

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.transferTo(out);
        }

        // TAR blocks must be padded to a 512-byte boundary.
        int remainder = (int) (file.length() % 512);
        if (remainder != 0) {
            out.write(new byte[512 - remainder]);
        }
    }

    /** Builds a 512-byte POSIX TAR header for the given path and file size. */
    private byte[] buildTarHeader(String name, long size) {
        byte[] header = new byte[512];
        putString(header, 0, 100, name);
        putString(header, 100, 8, "0000755 ");
        putString(header, 108, 8, "0000000 ");
        putString(header, 116, 8, "0000000 ");
        putString(header, 124, 12, String.format("%011o ", size));
        putString(header, 136, 12, String.format("%011o ", Instant.now().getEpochSecond()));
        putString(header, 148, 8, "        ");
        header[156] = '0';                                   //
        putString(header, 257, 6, "ustar ");
        putString(header, 263, 2, " ");

        // Compute and write checksum.
        int checksum = 0;
        for (byte b : header) checksum += (b & 0xFF);
        putString(header, 148, 8, String.format("%06o\0 ", checksum));
        return header;
    }

    private void putString(byte[] buf, int offset, int length, String value) {
        byte[] bytes = value.getBytes(java.nio.charset.StandardCharsets.US_ASCII);
        int copy = Math.min(bytes.length, length);
        System.arraycopy(bytes, 0, buf, offset, copy);
    }

    private void logFile(File file, String fileName) {
        File logFile = new File(outputDirectory, fileName + ".log");
        try {
            Files.copy(file.toPath(), logFile.toPath());
        } catch (IOException e) {
            Console.severe("Failed to copy log file: " + file.getPath());
        }
    }

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

            // Strip extension to recover the timestamp token written by InstantUtils#toString.
            String token = name.endsWith(".tar.gz")
                    ? name.substring(0, name.length() - 7)
                    : name.substring(0, name.length() - 4);

            try {
                Instant fileInstant = InstantUtils.toInstant(token);
                if (fileInstant.isBefore(cutoff)) {
                    entry.delete();
                }
            } catch (Exception e) {
                Console.severe("Skipping unparseable filename: " + name);
            }
        }
    }

    private void deleteEmptyDirs(File dir) {
        File[] entries = dir.listFiles();
        if (entries == null) return;

        for (File entry : entries) {
            if (entry.isDirectory()) {
                deleteEmptyDirs(entry);
                // Re-check after recursion: delete if now empty and not the root.
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

    public void log(File file) {
        String fileName = InstantUtils.toString(Instant.now());
        if (file.isDirectory()) {
            logDirectory(file, fileName);
        } else {
            logFile(file, fileName);
        }
    }

    public void purge() {
        purgeFiles(parentDirectory);
        deleteEmptyDirs(parentDirectory);
    }
}