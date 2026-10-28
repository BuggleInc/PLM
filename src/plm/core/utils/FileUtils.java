package plm.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Stream;
import plm.core.model.lesson.ExerciseTemplated;

/**
 * This class is in charge of loading the resources from disk into memory
 *
 * 	It solves 2 main difficulties. The first one is to find the files in any case, be
 *  them in the distributed jar file, or on the disk (as it happens when we develop PLM:
 *  we don't build a jar for each run, we directly from our source tree). It also deals
 *  with the windows/unix incompatibilities about directory separators (/ or \).
 *
 *  The second problem it deals with is about translations. When looking for a help file,
 *  it first search for a suitable translated version. If not found, it fallbacks to the
 *  English version. This is done through the locale static variable. Yeah, that's not
 *  clean but it just works.
 */
public class FileUtils {
  private static Locale locale;

  private final static String[] directories = {"", "lib/", "src/"};

  /** Specifies the locale that we have to use when looking for translated files */
  public static void setLocale(Locale l) { locale = l; }
  public static Locale getLocale() { return locale == null ? new Locale("en") : locale; }

  public static BufferedReader newFileReader(String file, String extension, boolean translatable) throws FileNotFoundException, UnsupportedEncodingException
  {
    /* first check if we can find it unmodified */
    int i = 0;
    if (!translatable) { // extension is ignored in this case. That's useful to get it from a file chooser
      while (i < directories.length) {
        if ((new File(directories[i] + file)).exists()) {
          return new BufferedReader(new FileReader(directories[i] + file));
        } else {
          i++;
        }
      }
    }

    if (translatable && locale == null)
      throw new RuntimeException("locale is null: you cannot request for translated material (yet)");

    /* Build the list of filenames we will iterate (translated if any, and raw) */
    String[] fileNames;
    if (translatable && !locale.getLanguage().equals("en")) {
      String lang = locale.getLanguage();
      if (lang.equals("pt_br"))
        lang = "pt_BR"; // Damn stupid java. Could you please stick to ISO norms and put the country uppercase?

      fileNames = new String[] {file.replace('.', '/') + "." + lang, file.replace('.', '/')};
    } else { // not translatable, or currently in English: only search for non-translated form
      fileNames = new String[] {file.replace('.', '/')};
    }
    /* Do that iteration */
    for (String fileName : fileNames) {
      /* change class name to directories */
      fileName = fileName + (extension != null ? "." + extension : "");

      i = 0;
      while (i < directories.length) {
        if ((new File(directories[i] + fileName)).exists()) {
          return new BufferedReader(new FileReader(directories[i] + fileName));
        } else {
          i++;
        }
      }

      // external HTML file of this exercise not found on file system. Try as resource, in case we are in a jar file
      String resourceName = "/" + fileName;
      resourceName        = resourceName.replace('\\', '/'); /* just in case we were passed a windows path */

      InputStream s = ExerciseTemplated.class.getResourceAsStream(resourceName);
      if (s == null)
        continue; // test next name in the list

      BufferedReader br = null;
      try {
        br = new BufferedReader(new InputStreamReader(s, "UTF-8"));
      } catch (UnsupportedEncodingException e1) {
        e1.printStackTrace();
        System.err.println("File encoding of " + fileName + " is not supported on this platform (please report this bug)");
        // return null;
        throw e1;
        // throw new FileNotFoundException(file + "with extension " + extension + " is encoded in an unsupported encoding.");
      }
      if (br != null)
        return br;
    }
    // file not found, give up. No logs here, as it is ok that some entities do not exist in some languages
    if (extension == null)
      throw new FileNotFoundException(file + " (without extension) could not be found.");
    throw new FileNotFoundException(file + " with extension " + extension + " could not be found.");
  }

  /** Whether {@link #readContentAsText} would find this (non translatable) file, without opening it. */
  public static boolean exists(String file, String extension)
  {
    String fileName = file.replace('.', '/') + (extension != null ? "." + extension : "");
    for (String directory : directories)
      if (new File(directory + fileName).exists())
        return true;
    return ExerciseTemplated.class.getResource("/" + fileName.replace('\\', '/')) != null;
  }

  public static StringBuffer readContentAsText(String file, String extension, boolean translatable) throws FileNotFoundException, UnsupportedEncodingException
  {
    BufferedReader br = FileUtils.newFileReader(file, extension, translatable);
    String newLine    = System.getProperty("line.separator");

    StringBuffer sb = new StringBuffer();
    try {
      String s;
      s = br.readLine();
      while (s != null) {
        sb.append(s);
        sb.append(newLine);
        s = br.readLine();
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        br.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return sb;
  }

  /** Recursively removes a file or a directory, without following the symbolic links. Reports what cannot be removed. */
  public static void deleteRecursive(File path)
  {
    if (!path.exists())
      return;
    try (Stream<Path> files = Files.walk(path.toPath())) {
      files.sorted(Comparator.reverseOrder()).forEach(file -> {
        try {
          file.toFile().setWritable(true); // Windows refuses to delete the read-only files, as the objects written by git
          Files.delete(file);
        } catch (IOException e) {
          System.err.println("Cannot delete " + file + ": " + e);
        }
      });
    } catch (IOException e) {
      System.err.println("Cannot delete " + path + ": " + e);
    }
  }
}
