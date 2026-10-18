package plm.core.model.session;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.User;
import plm.core.model.UserAbortException;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;

public class GitSessionKit {

  private Game game;
  private User cachedUser = null;

  public GitSessionKit(Game game) { this.game = game; }

  /** The repository of the current user, in the given saving location */
  private File userRepo(File path) { return new File(path, game.getUsers().getCurrentUser().getUserUUIDasString()); }

  /**
   * Writes the summary of every opened lesson. The source code is already written by the tracker.
   *
   * @param path
   * @throws UserAbortException
   */
  public void storeAll(File path) throws UserAbortException
  {
    File repoDir = userRepo(path);

    Collection<Lesson> lessons = this.game.getLessons();
    for (Lesson lesson : lessons) {
      // Recompute the summaries (the bodies are written by the tracker)
      File summary = new File(repoDir, lesson.getId() + ".summary");
      try {
        Files.writeString(summary.toPath(), Game.getInstance().studentWork.lessonSummary(lesson.getId()));
      } catch (IOException ex) {
        System.out.println("Failed to write the lesson summary on disk: " + ex.getLocalizedMessage());
      }
    }
  }

  /**
   * Load the user source code of the lessons' exercises. Also get the per
   * lesson summaries
   *
   * @param path
   */
  public void loadAll(final File path)
  {
    // Load bodies
    for (Lesson lesson : this.game.getLessons()) {
      loadLesson(path, lesson);
    }

    // Load summary from the lastly saved files,
    // but don't trust the game.getLessons that is empty at startup, so search for existing files on disk
    String pattern            = "*.summary";
    FileSystem fs             = FileSystems.getDefault();
    final PathMatcher matcher = fs.getPathMatcher("glob:" + pattern); // to match file names ending with string "summary"

    FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
      @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attribs)
      {
        if (!matcher.matches(file.getFileName())) // Not a summary file. Who cares?
          return FileVisitResult.CONTINUE;

        String lessonId = file.getFileName().toString();
        lessonId        = lessonId.substring(0, lessonId.length() - ".summary".length());

        // Pass the content of the file to the sessionDB
        try {
          Game.getInstance().studentWork.lessonSummaryParse(lessonId, Files.readString(file));
        } catch (IOException e) {
          System.err.println("Error while reading the summary of lesson " + lessonId + ": " + e.getLocalizedMessage());
        }

        return FileVisitResult.CONTINUE;
      }
    };

    try {
      Files.walkFileTree(userRepo(path).toPath(), matcherVisitor);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Load the lesson's exercises user source code
   *
   * @param path
   * @param lesson
   */
  public void loadLesson(File path, Lesson lesson)
  {
    File repoDir = userRepo(path);
    for (Lecture lecture : lesson.exercises()) {
      if (lecture instanceof Exercise) {
        Exercise exercise = (Exercise)lecture;
        for (ProgrammingLanguage lang : exercise.getProgLanguages()) {
          // check if exercise already done correctly
          String prefix = exercise.getId() + "." + lang.getExt();
          Game.getInstance().studentWork.setPassed(exercise, lang, new File(repoDir, prefix + ".DONE").exists()); // the file exists if the exercise was correct
          // load source code
          try {
            exercise.setEditorContent(lang, Files.readString(new File(repoDir, prefix + ".code").toPath())); // does not parse the entity
          } catch (NoSuchFileException nsf) {
            /* that's fine, we never did that exercise */
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
      }
    }
  }
}
