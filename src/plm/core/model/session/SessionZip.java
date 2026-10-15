package plm.core.model.session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
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
}
