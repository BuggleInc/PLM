package plm.core.model.session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/** Packs a whole session directory (including its git repository) into a zip file, to carry it between disconnected machines */
public class SessionZip {

  /** Writes dir and its content into zipFile, under a top-level entry named after dir (that is the user's UUID) */
  public static void zip(File dir, File zipFile) throws IOException
  {
    Path parent = dir.toPath().getParent();
    try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile.toPath())); Stream<Path> paths = Files.walk(dir.toPath())) {
      for (Path path : (Iterable<Path>)paths::iterator) {
        String name = parent.relativize(path).toString().replace(File.separatorChar, '/');
        if (Files.isDirectory(path)) {
          zos.putNextEntry(new ZipEntry(name + "/"));
        } else {
          zos.putNextEntry(new ZipEntry(name));
          Files.copy(path, zos);
        }
        zos.closeEntry();
      }
    }
  }

  /** The UUID of the user whose session is in that zip file, that is the name of its top-level entry */
  public static String userUUID(File zipFile) throws IOException
  {
    try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile.toPath()))) {
      ZipEntry entry = zis.getNextEntry();
      if (entry == null)
        throw new IOException("Empty zip file");
      String uuid = entry.getName().split("/")[0];
      try {
        UUID.fromString(uuid);
      } catch (IllegalArgumentException e) {
        throw new IOException(zipFile + " is not a PLM session: its top-level entry is not a UUID", e);
      }
      return uuid;
    }
  }

  /** Extracts zipFile into targetDir */
  public static void unzip(File zipFile, File targetDir) throws IOException
  {
    Path target = targetDir.toPath().toAbsolutePath().normalize();
    try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile.toPath()))) {
      for (ZipEntry entry; (entry = zis.getNextEntry()) != null;) {
        Path path = target.resolve(entry.getName()).normalize();
        if (!path.startsWith(target))
          throw new IOException("Invalid entry in " + zipFile + ": " + entry.getName());
        if (entry.isDirectory()) {
          Files.createDirectories(path);
        } else {
          Files.createDirectories(path.getParent());
          Files.copy(zis, path);
        }
      }
    }
  }
}
