package plm.core.lang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import plm.core.PLMCompilerException;
import plm.core.model.Game;
import plm.core.model.LogWriter;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.session.SourceFile;
import plm.core.ui.ResourcesCache;
import plm.universe.Entity;

public class LangC extends ProgrammingLanguage {
  public LangC() { super("C", "c", ResourcesCache.getIcon("img/lang_c.png")); }

  /* Language detection logic */
  private static String brokenLanguageMessage;
  @Override public String getBrokenLanguageMessage() { return brokenLanguageMessage; }
  private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;
  @Override public boolean isBrokenLanguage()
  {
    Runtime runtime = Runtime.getRuntime();

    if (brokenLanguageState == BrokenLanguageState.Unitialized) {
      try {
        runtime.exec("gcc --version");
        brokenLanguageState = BrokenLanguageState.Usable;
      } catch (IOException e) {
        brokenLanguageMessage = e.getLocalizedMessage();
        e.printStackTrace();
        brokenLanguageState = BrokenLanguageState.NotUsable;
      }
    }
    return brokenLanguageState != BrokenLanguageState.Usable;
  }
  @Override public boolean isC() { return true; }

  @Override
  public void compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {

    List<SourceFile> sfs = exo.getSourceFilesList(this);
    if (sfs == null || sfs.isEmpty()) {
      String msg = exo.getName() + ": No source to compile";
      System.err.println(msg);
      exo.lastResult = ExecutionProgress.newCompilationError(msg);
      throw new PLMCompilerException(msg, null, null);
    }

    for (SourceFile sf : sfs) {
      String code = sf.getCompilableContent(runtimePatterns, whatToCompile);
      compile(code, exo.getId(), exo);
    }
  }

  private void compile(String code, String executable, Exercise exo) throws PLMCompilerException
  {

    Runtime runtime = Runtime.getRuntime();

    final StringBuffer resCompilationErr = new StringBuffer();
    try {
      String tempdir = System.getProperty("java.io.tmpdir");

      File plmDirTmp = new File(tempdir + "/plmTmp");
      if (!plmDirTmp.exists()) {
        plmDirTmp.mkdir();
      }

      File saveDirBin = new File(plmDirTmp.getAbsolutePath() + "/bin");
      if (!saveDirBin.exists()) {
        saveDirBin.mkdir();
      }
      String saveDirPathBin = saveDirBin.getAbsolutePath();

      String extension = "";
      String os        = System.getProperty("os.name").toLowerCase();
      if (os.indexOf("win") >= 0) {
        extension = ".exe";
      }

      File exec = new File(saveDirPathBin + "/" + executable + extension);
      if (exec.exists()) {
        exec.delete();
      }

      String remote = "";

      if (code.contains("RemoteBuggle")) {
        remote = "RemoteBuggle";
      } else if (code.contains("RemoteTurtle")) {
        remote = "RemoteTurtle";
      } else if (code.contains("RemoteSort")) {
        remote = "RemoteSort";
      } else if (code.contains("RemoteFlag")) {
        remote = "RemoteFlag";
      } else if (code.contains("RemoteBaseball")) {
        remote = "RemoteBaseball";
      } else if (code.contains("RemotePancake")) {
        remote = "RemotePancake";
      } else if (code.contains("RemoteHanoi")) {
        remote = "RemoteHanoi";
      } else {
        PLMCompilerException e = new PLMCompilerException("This universe is not implemented in C.", null, null);
        exo.lastResult         = ExecutionProgress.newCompilationError(e.getMessage());
        throw e;
      }

      String line;
      StringBuffer compiled_code = new StringBuffer();

      BufferedReader hRemote = new BufferedReader(new InputStreamReader(
          getClass().getClassLoader().getResourceAsStream("resources/langages/c/include/Remote.h")));
      compiled_code.append("/************/");
      compiled_code.append("/* Remote.h */");
      compiled_code.append("/************/");
      while ((line = hRemote.readLine()) != null)
        if (!line.startsWith("#include \".."))
          compiled_code.append(line + "\n");
      hRemote.close();

      BufferedReader cRemote = new BufferedReader(
          new InputStreamReader(getClass().getClassLoader().getResourceAsStream("resources/langages/c/src/Remote.c")));
      compiled_code.append("/************/");
      compiled_code.append("/* Remote.c */");
      compiled_code.append("/************/");
      while ((line = cRemote.readLine()) != null)
        if (!line.startsWith("#include \".."))
          compiled_code.append(line + "\n");
      cRemote.close();

      BufferedReader hRemoteWorld = new BufferedReader(new InputStreamReader(
          getClass().getClassLoader().getResourceAsStream("resources/langages/c/include/" + remote + ".h")));
      compiled_code.append("/****************/");
      compiled_code.append("/* " + remote + ".h */");
      compiled_code.append("/****************/");
      while ((line = hRemoteWorld.readLine()) != null)
        if (!line.equals("#include \"Remote.h\""))
          compiled_code.append(line + "\n");
      hRemoteWorld.close();

      BufferedReader cRemoteWorld = new BufferedReader(new InputStreamReader(
          getClass().getClassLoader().getResourceAsStream("resources/langages/c/src/" + remote + ".c")));
      compiled_code.append("/****************/");
      compiled_code.append("/* " + remote + ".c */");
      compiled_code.append("/****************/");
      while ((line = cRemoteWorld.readLine()) != null)
        if (!line.startsWith("#include \".."))
          compiled_code.append(line + "\n");
      cRemoteWorld.close();

      compiled_code.append("/****************/");
      compiled_code.append("/* Student code */");
      compiled_code.append("/****************/");
      for (String li : code.split("\n"))
        if (!li.startsWith("#include \".."))
          compiled_code.append(li + "\n");

      String[] arg1;
      if (os.indexOf("win") >= 0) {
        arg1    = new String[3];
        arg1[0] = "cmd.exe";
        arg1[1] = "/c";
        arg1[2] = "gcc -g -Wall -lm -lpthread -fsanitize=address -o \"" + exec + "\" - ";
      } else {
        arg1    = new String[3];
        arg1[0] = "/bin/sh";
        arg1[1] = "-c";
        arg1[2] = "gcc -g -x c -Wall -lm -lpthread -fsanitize=address -o \"" + exec + "\" - ";
      }

      final Process process        = runtime.exec(arg1);
      final BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
      bwriter.write(compiled_code.toString());
      bwriter.close();

      Thread reader = new Thread() {
        public void run()
        {
          try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line           = "";
            try {
              while ((line = reader.readLine()) != null) {
                resCompilationErr.append(line + "\n");
              }
            } finally {
              reader.close();
            }
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        }
      };

      Thread error = new Thread() {
        public void run()
        {
          try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line           = "";
            try {
              while ((line = reader.readLine()) != null) {
                resCompilationErr.append(line + "\n");
              }
            } finally {
              reader.close();
            }
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        }
      };
      reader.start();
      error.start();
      process.waitFor();
      reader.join();
      error.join();

      if (resCompilationErr.length() > 0) {
        PLMCompilerException e = new PLMCompilerException(resCompilationErr.toString(), null, null);
        System.err.println(Game.i18n.tr("Compilation error:"));
        System.err.println(e.getMessage());
        exo.lastResult = ExecutionProgress.newCompilationError(e.getMessage());

        throw e;
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override public List<Entity> mutateEntities(Exercise exercise, List<Entity> old, StudentOrCorrection whatToMutate)
  {

    return old; /* Nothing to do, actually */
  }

  @Override public void runEntity(final Entity ent, final ExecutionProgress progress)
  {
    final StringBuffer resCompilationErr = new StringBuffer();

    try {

      String tempdir = System.getProperty("java.io.tmpdir") + "/plmTmp";
      File saveDir   = new File(tempdir + "/bin");

      String extension = "";
      String os = System.getProperty("os.name").toLowerCase();
      String executable;
      if (ent.getScript(this) != null) {
        executable = ent.getScript(this);
      } else {
        executable = Game.getInstance().getCurrentLesson().getCurrentExercise().getId();
      }

      if (os.indexOf("win") >= 0)
        extension = ".exe";

      String cmd = saveDir.getAbsolutePath() + "/" + executable + "" + extension;
      File exec  = new File(cmd);
      if (!exec.exists() || !exec.canExecute() || !exec.isFile()) {
        System.err.println(Game.i18n.tr("Error, please recompile the exercise: {0} does not exist", exec.getName()));
        return;
      }

      String asan_report = tempdir + "/asan_report.txt";
      ProcessBuilder pb  = new ProcessBuilder(cmd);
      // log_path=/tmp/plmTmp/asan_report.txt.$PID ~~> don't report to stderr but to that file
      // to_syslog=0   ~~> Prevent ASan from writing also to stderr
      pb.environment().put("ASAN_OPTIONS", "log_path=" + asan_report + ":to_syslog=0");
      final Process process        = pb.start();
      long pid                     = process.pid();
      final BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

      Thread reader = new Thread() {
        public void run()
        {
          try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            try {
              String str;
              while ((str = reader.readLine()) != null)
                System.out.println(str);
            } finally {
              reader.close();
            }
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        }
      };

      Thread error = new Thread() {
        public void run()
        {
          BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
          Exception parseError  = null;
          String str            = "";
          try {
            while ((str = reader.readLine()) != null)
              ent.command(str, bwriter);
          } catch (Exception e) {
            parseError = e;
          }
          if (parseError != null) {
            StringBuffer sb = new StringBuffer(str + "\n");
            try {
              while ((str = reader.readLine()) != null)
                sb.append(str + "\n");
            } catch (IOException ioe) {
              System.err.println("Exception while handling the exception. Bailing out");
              parseError.printStackTrace();
              ioe.printStackTrace();
            }
            throw new RuntimeException("Parse error while reading the command: " + sb.toString(), parseError);
          }
        }
      };

      reader.start();
      error.start();

      process.waitFor();

      reader.join();
      error.join();

      bwriter.close();

      File asan_report_file = new File(asan_report + "." + pid);
      if (asan_report_file.exists()) {
        System.err.println(Game.i18n.tr(
            "The Address Sanitizer detected an issue with the execution of your entity. You probably want to fix "
            + "it.\nThe exact error message contains hints about the problem. Good luck in debugging this.\n"));
        try (BufferedReader br = new BufferedReader(new FileReader(asan_report_file))) {
          String line;
          while ((line = br.readLine()) != null) {
            System.err.println(line);
          }
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
        asan_report_file.delete();
      }

      if (resCompilationErr.length() > 0) {
        System.err.println(resCompilationErr.toString());
        progress.setCompilationError(resCompilationErr.toString());
      }

    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
