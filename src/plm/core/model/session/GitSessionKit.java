package plm.core.model.session;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
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
  private String reponame;
  private User cachedUser = null;

  public GitSessionKit(Game game) { this.game = game; }

  /**
   * Writes the summary of every opened lesson. The source code is already written by the tracker.
   *
   * @param path
   * @throws UserAbortException
   */
  public void storeAll(File path) throws UserAbortException
  {
    reponame = game.getUsers().getCurrentUser().getUserUUIDasString();

    Collection<Lesson> lessons = this.game.getLessons();
    for (Lesson lesson : lessons) {
      File repoDir = new File(path.getAbsolutePath() + System.getProperty("file.separator") + reponame);

      // Recompute the summaries (the bodies are written by the tracker)
      File summary = new File(repoDir, lesson.getId() + ".summary");
      try {
        FileWriter fwSummary     = new FileWriter(summary.getAbsoluteFile());
        BufferedWriter bwSummary = new BufferedWriter(fwSummary);
        bwSummary.write(Game.getInstance().studentWork.lessonSummary(lesson.getId()));
        bwSummary.close();
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
    reponame = game.getUsers().getCurrentUser().getUserUUIDasString();

    if (!Game.getInstance().getUsers().getCurrentUser().equals(cachedUser)) {
      if (Game.getInstance().isDebugEnabled())
        System.out.println("The user changed! switch to the right branch");
      cachedUser = Game.getInstance().getUsers().getCurrentUser();

      File gitDir = new File(Game.getSavingLocation() + System.getProperty("file.separator") + cachedUser.getUserUUIDasString());
      if (!gitDir.exists()) {
        // It should never happen since the session content should be created by the git tracker module.
        System.out.println(Game.i18n.tr("Something weird happened. Your session was not created/reloaded properly. Please report this issue."));
      }
    }

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
      Files.walkFileTree(Paths.get(path.getAbsolutePath() + System.getProperty("file.separator") + reponame), matcherVisitor);
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
    for (Lecture lecture : lesson.exercises()) {
      if (lecture instanceof Exercise) {
        Exercise exercise = (Exercise)lecture;
        for (ProgrammingLanguage lang : exercise.getProgLanguages()) {
          // check if exercise already done correctly
          String doneFile = path.getAbsolutePath() + System.getProperty("file.separator") + reponame + System.getProperty("file.separator") + exercise.getId() +
                            "." + lang.getExt() + ".DONE";
          if (new File(doneFile).exists()) { // if the file exists, the exercise was correct
            Game.getInstance().studentWork.setPassed(exercise, lang, true);
          } else {
            Game.getInstance().studentWork.setPassed(exercise, lang, false);
          }
          // load source code
          String fileName = path.getAbsolutePath() + System.getProperty("file.separator") + reponame + System.getProperty("file.separator") + exercise.getId() +
                            "." + lang.getExt() + ".code";
          // System.out.println(fileName);
          try {
            exercise.setEditorContent(lang, Files.readString(Path.of(fileName))); // does not parse the entity
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
