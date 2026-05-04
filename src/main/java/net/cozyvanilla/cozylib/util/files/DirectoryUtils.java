package net.cozyvanilla.cozylib.util.files;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryUtils {

    private DirectoryUtils() {}

    /**
     * Compresses a file or directory into a .tar.gz archive.
     *
     * @param toCompress the file or directory to compress
     * @param outputDirectory the directory where the archive will be created
     * @param fileName the archive file name without the .tar.gz extension
     * @throws IOException if compression fails
     */
    public static void compress(File toCompress, File outputDirectory, String fileName) throws IOException {
        File outputFile = new File(outputDirectory, fileName + ".tar.gz");

        try (FileOutputStream fos = new FileOutputStream(outputFile);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             GzipCompressorOutputStream gzos = new GzipCompressorOutputStream(bos);
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(gzos)) {

            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);
            Path basePath = toCompress.toPath();
            Files.walk(basePath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        String entryName = basePath.relativize(path).toString();
                        try (InputStream is = Files.newInputStream(path)) {
                            TarArchiveEntry entry = new TarArchiveEntry(path.toFile(), entryName);
                            tarOut.putArchiveEntry(entry);

                            byte[] buffer = new byte[8192];
                            int len;
                            while ((len = is.read(buffer)) != -1) {
                                tarOut.write(buffer, 0, len);
                            }

                            tarOut.closeArchiveEntry();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });

            tarOut.finish();
        } catch (IOException | UncheckedIOException e) {
            throw new IOException("Failed to compress " + toCompress.getPath(), e);
        }
    }
}
