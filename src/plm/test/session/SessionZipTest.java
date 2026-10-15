package plm.test.session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import plm.core.model.session.SessionZip;

/** A session directory survives the trip through a zip file, and only a session is accepted back */
public class SessionZipTest {
  @TempDir Path path;

  private static final String UUID = "123e4567-e89b-12d3-a456-426614174000";

  @Test public void testUnzipRestoresTheSessionThatWasZipped() throws IOException
  {
    Path dir = Files.createDirectories(path.resolve("machine").resolve(UUID));
    Files.writeString(dir.resolve("exo.java.code"), "code");
    Files.createDirectories(dir.resolve(".git/refs/tags")); // an empty directory that git needs
    Path zip = path.resolve("session.zip");

    SessionZip.zip(dir.toFile(), zip.toFile());
    SessionZip.unzip(zip.toFile(), path.resolve("other").toFile());

    Assertions.assertEquals(UUID, SessionZip.userUUID(zip.toFile()));
    Assertions.assertEquals("code", Files.readString(path.resolve("other").resolve(UUID).resolve("exo.java.code")));
    Assertions.assertTrue(Files.isDirectory(path.resolve("other").resolve(UUID).resolve(".git/refs/tags")));
  }

  @Test public void testUserUUIDRefusesAZipThatIsNotASession() throws IOException
  {
    Path dir = Files.createDirectories(path.resolve("machine").resolve("not-a-uuid"));
    Files.writeString(dir.resolve("exo.java.code"), "code");
    Path zip = path.resolve("session.zip");

    SessionZip.zip(dir.toFile(), zip.toFile());

    Assertions.assertThrows(IOException.class, () -> SessionZip.userUUID(zip.toFile()));
  }

  @Test public void testUnzipRefusesTheEntriesLeavingTheTargetDirectory() throws IOException
  {
    Path zip = path.resolve("evil.zip");
    try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zip))) {
      zos.putNextEntry(new ZipEntry("../evil"));
      zos.write(1);
      zos.closeEntry();
    }

    Assertions.assertThrows(IOException.class, () -> SessionZip.unzip(zip.toFile(), path.resolve("out").toFile()));
    Assertions.assertFalse(Files.exists(path.resolve("evil")));
  }
}
