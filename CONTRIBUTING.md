The Programmer's Learning Machine (PLM) is a free cross-platform programming exerciser. It lets you explore various concepts of
programming through interactive challenges in differing micro-worlds, that you can solve in either Java, Python, Scala or C. The
design rational is given in the following paper: [The Programmer's Learning Machine: A Teaching System To Learn
Programming](https://hal.inria.fr/hal-01243646). On this page, you will find the following sections:

* [Overall architecture](#Architecture) and concepts, to help the onboarding of prospective contributors
* [How to translate the project](#Translating_the_PLM)
* [Adding a new exercise](#Adding_a_new_exercise)
* [Maintainer's notes](#Maintainers_notes): how to merge in new translation and how to release a new version of the PLM.

TODO: move the entity templating logic from sourceFileTemplated to TemplatedRemoteLanguage
TODO: create an Exercise.runAll(WorldKind), to come after Exercise.compile()
TODO: fix the compilation error messages to match the student code, fixing Entity.setScriptOffset and friends
TODO: Port the SimpleExercise tests to LangC
TODO: Precompile the correction entities so that they don't get generated/compiled/executed every time we load the lesson
TODO: Split Lightbot away from the other languages, by defining another subclass of Lecture that is not an Exercise but a Brainteaser. Exercises are the one you can do in any programming language; brain teasers are in a specific, probably dedicated, programming language. 
TODO: split the UI from the compilation+exec services. The latter may be pure functions with no hidden globals. The former should include the Game singleton that encompasses the model part of the MVC thing.
TODO: would it be possible to not generate a package name in Java/Scala now that it's a separated build directory? That would further simplify the templating code

# Architecture

## Core concepts

- **Lesson / Lecture**: a `Lesson` (e.g. `welcome`, `sort`, `recursion`) groups `Lecture`s in a pedagogical sequence. An
  `Exercise` is a leaf `Lecture`.
- **Exercise**: the unit of work a student solves (`plm.core.model.lesson.Exercise`). It owns three parallel sets of **worlds**:
  `initialWorld` (the starting state, reset before every run), `currentWorld` (the current state while executing the student's
  code), and `answerWorld` (the target state, produced by either loading a cached solution or by running the teacher's
  correction code once). Passing == every `currentWorld` "wins" against its matching `answerWorld`.  By default
  `World.winning()` uses equality but other winning conditions can be defined.
- **World + Entity**: a `World` (`plm.universe.World`) is the simulated environment encoding the pedagogical problem situation.
  This is a micro-world instance. It contains one or more `Entity` objects, which are the actors that execute the student's code
  (or the teacher's correction code) against the world's primitives. `Entity`/`World` are subclassed per universe.
- **Worlds as test cases**: an exercise typically ships **several world instances** and/or **several entities per world
  instance.** Each is compiled/run independently and must pass for the exercise to be validated, i.e. the set of worlds *is*
  the exercise's test suite (comparable to parametrized unit tests). Adding another world instance to an exercise, without
  touching the student-facing code, is the standard way to catch a wider range of incorrect solutions.
- **Universe**: a micro-world *kind*, i.e. a family of worlds/entities sharing a theme and a set of primitives (e.g. "the buggle
  can walk, paint, pick up objects"). A given exercise uses exactly **one** universe. Universes found in `src/plm/universe` for
  the generic ones and in `src/lessons/*/universe` for the ones specificaly tailored for a given lesson:
  - `bugglequest`: generic grid actor (buggles), richest primitive set. It is the main PLM microworld and its implementation is
    the reference. It is used to teach the basics about variablesand loops, and to introduce functions and problem
    decomposition. This micro-world is also used to present various maze algorithms in a specific lesson.
    - `turmites` is a subclass of the buggle microworld introducing [2D turing machines](https://en.wikipedia.org/wiki/Turmite).
  - `turtles`: LOGO-style turtle graphics, used to teach recursion through the drawing of fractals.
  - `sort`: sorting algorithms; primitives (`isSmaller`, `copy`, `swap`) observe the data accesses patterns so the student must
    reproduce the *expected algorithm*, not just a correctly sorted array.
  - `bat`: unit-testing style no graphical world but a textual output; a method prototype is filled in and tested against many
    parameter values.
  - Specific sorting microwords: `sort/baseball` ([pebble-motion](https://en.wikipedia.org/wiki/Pebble_motion_problems)),
   `sort/pancake` ([pancake sorting](https://en.wikipedia.org/wiki/Pancake_sorting)), `sort/dutchflag` ([Dutch national flag
    sorting](https://en.wikipedia.org/wiki/Dutch_national_flag_problem)).
  - Specific recusion microwords: `recursion/hanoi` (comes with a rich set of exercises on recursive problem decomposition),
    `recursion/cons` (recursive strings using the [cons](https://en.wikipedia.org/wiki/Cons) [car and
    CDR](https://en.wikipedia.org/wiki/CAR_and_CDR) constructs of LISP). The cons micro-world is subclassed from the bat one.
  - Recreative microworlds: `lightbot` a programming challenge using a graphical programming, `lander` a lunar lander
    programming challenge. 
  - Ongoing microworlds that do not work yet: `backtracking` should be completed or removed.
- **Correction entity**: for each exercise/language pair, a source file (e.g. `MoriaEntity.java`, `MoriaEntity.py`,
  `ScalaMoriaEntity.scala`, `MoriaEntity.c`) contains both the teacher's reference solution and the template shown to the
  student. See "Adding a new exercise" below for the file layout and the BEGIN/END TEMPLATE/SOLUTION markers.

## How an exercise executes

* **Reset**: `currentWorld` is reset from `initialWorld` for each world instance.
* **Compile**: `Exercise.compile()` delegates to `ProgrammingLanguage.compileExo()` for the selected language. Java, Scala
  and C are compiled to an external executable/jar; Python needs no compilation step, just the student's `.py` files written
  out to a workspace. Either way, `compileExo()` returns a textual reference to the result (a jar/binary path, a
  "jarPath|mainClass" pair, etc., or `null` for LightBoy that don't compile at all), which the caller then passes down as-is
  to `runEntity()`'s `executable` parameter below.
* **Run**: `World.runEntities()` spawns one thread per entity and calls `ProgrammingLanguage.runEntity()`:
   - Java/Scala/Python/C: all four inherit the same `RemoteExecutionLang.runEntity()`. It binds a UNIX domain socket, starts
     the student code as an external process, and relays primitive calls over that socket to `plm.universe.CommandExecutor`.
     The only thing each language still implements on its own is `buildProcess()`, which turns the compiled/written artifact
     into the right command line (`java -jar ...`, `python3 ...`, the compiled binary, etc).
   - LightBot: This challenge is an exception, as it can only be solved using the graphical block-list rather than a real
     programming language. Thus, `run()` *interprets* a student-authored program.
* **Check**: `Exercise.check()` compares each `currentWorld` to its `answerWorld` via `World.winning()`. On mismatch,
  `World.diffTo()` produces a human-readable diff shown to the student. All the universes but Lander use a structural equality
  between currentWorld and answerWorld to compute whether it's winning. Instead, Lander checks whether the lunar lander reached
  a pad or crashed.

Runaway/infinite-loop student code: the "Stop" action calls `LessonRunner.stopAll()`, which cooperatively `Thread.interrupt()`s
each per-entity runner thread (a deprecated `Thread.stop()` used to be used instead, back when Java exercises were compiled
in-process). Since student code is now an external process, interrupting the runner thread only unblocks it from
`Process.waitFor()`; `RemoteExecutionLang.runEntity()` catches that `InterruptedException` and calls `destroyForcibly()` on the
child process before returning, so a stopped infinite loop does not leave any orphaned process behind. This mechanism is not
a hard sandbox either: the process is killed, but nothing prevents it from spawning its own children or from being heavy
enough to matter for the second or so it takes to die.

## From correction entity to compilable source: templating

This section is about how the single `XxxEntity.<ext>` file described in "Adding a new exercise" below (which mixes the
teacher's solution and the student-facing template) becomes two different compilable programs: one for the CORRECTION,
one for the STUDENT's current editor content. It happens in two separate steps.

### Step 1 (load time): `ExerciseTemplated.newSourceFromFile()` parses the entity file once

Called from `ExerciseTemplated.setup()` for every `(exercise, language)` pair, once when the lesson is loaded. It reads the
raw `XxxEntity.<ext>` file and runs it character-by-character (line-by-line) through a hand-written state machine (states 0
to 6) driven by marker comments found in the file: `BEGIN/END TEMPLATE`, `BEGIN/END SOLUTION`, `BEGIN/END HIDDEN`.
`BEGIN/END HIDDEN` is stripped from what the student sees but kept in the correction (e.g. helper code the student shouldn't
have to read). Out of that pass, it builds several `StringBuffer`s:
- `head`/`tail`: the file content strictly outside the templated region (before `BEGIN TEMPLATE`/after `END TEMPLATE`,
  or the whole file if only `BEGIN/END SOLUTION` is used).
- `templateHead`/`templateTail`: inside the templated region but outside the solution -- concatenated together as
  `initialContent`, what the student sees in the editor the first time.
- `solution`: the reference implementation, discarded here (it goes into `correction` below, not into what the student sees).
- `correction`: the *entire* file content again, unchanged except for the class/package name rewrite -- this is stored
  as-is and re-parsed later, at every compile, by each language's own marker-extraction logic (see step 2).

It then does bookkeeping common to all languages: rewrites the `class`/`package` line to use the exercise's own class name,
inserts a `#line` C preprocessor directive so compiler errors point at the right file for C, collapses `initialContent`'s
leading whitespace to the smallest common indentation, and folds `head`+`tail` down to a single line for Java 
(the Python/Scala/C compilers/offset-tracking don't need that flattening the way javac's line-based error reporting does).
`head + "$body" + tail` becomes `template` (a string with one placeholder, `$body`), and `offset` is `head`'s line count
(used later to translate a compiler error's line number back into the student's own editor coordinates). An optional
`patternString` (`s/regex/replacement/;...`, only used by a couple of exercises) can further rewrite `template` and
`initialContent` at this point. `newSource()` then stores `(name, initialContent, template, offset,
correction)` as one `SourceFile` per `(exercise, language)` in `Exercise.sourceFiles`.

### Step 2 (upon the first compilation): each language re-parses `correction` and fills in `$body` and friends

`SourceFile` only knows about the single `$body` placeholder above; everything else is language-specific and lives in
`ProgrammingLanguage.compileExo()` (Java/Scala/Python/C, sharing common helpers through `TemplatedRemoteLang`):
- It re-extracts pieces out of the *raw* `correction` string using its own marker syntax (comment-delimited for Java/Scala/C,
  e.g. `/* BEGIN DEPENDENCY */.../* END DEPENDENCY */`, `/* BEGIN IMPORT */.../* END IMPORT */`): the `run()`
  method's own text (`extractRunFunction()`/`extractRunSpan()` -- brace-matching for Java/Scala/C, indentation-based for
  Python, see `LangPython`'s override), any extra dependency code, extra imports, and which `RemoteXxx` micro-world glue
  file to compile against (guessed from keywords found in the source, e.g. `"Buggle"` -> `RemoteBuggle`, see
  `TemplatedRemoteLang.getRemote()`).
- It builds a `runtimePatterns` map of regex->replacement (`$package`, `$run`, `$dependency`, `$imports`, ...) to be
  substituted into whatever template ends up being used.
- Java additionally computes its own, second, per-compile `template` string (`LangJava.getCorrectedTemplate()`), picking
  one of three class-body shapes depending on whether the file's templated region and `run()`'s own braces overlap, are
  nested, or are disjoint, and overwrites the `SourceFile`'s stored template with it via `sf.setTemplate()` -- so the
  `template`/`offset` computed once in step 1 are actually only the final answer for Python/Scala/C; Java rebuilds
  `template` from scratch on every compile and only keeps `offset` from step 1.
- Finally `SourceFile.getCompilableContent(runtimePatterns, whatToCompile)` does the actual substitution: for
  `StudentOrCorrection.CORRECTION` it slices the solution back out of `correction` (the text between the same
  `BEGIN/END TEMPLATE` or `BEGIN/END SOLUTION` markers step 1 already knew about) and substitutes it for `$body`;
  for `STUDENT` it substitutes the editor's current `body` instead; either way `runtimePatterns` is then applied on top
  of the result, and non-breaking spaces are stripped. The resulting string is written to a per-compile workspace
  (`TemplatedRemoteLang.packageNameForExercise()`: a name derived from the exercise id, `STUDENT`/`CORRECTION` and a hash
  of the source, so unrelated concurrent compiles never collide, see its Javadoc) alongside the copied `RemoteXxx` glue
  file and any other support file the exercise needs, then compiled/run the usual way.

Everything in step 2 above is a pure function of `correction`, which never changes between compiles of the same
`SourceFile`, so it's computed only the first time a given exercise is compiled, and the result is cached in the `SourceFile`.

So the same `XxxEntity` file is walked twice by two independent parsers using two different marker vocabularies: once by
`ExerciseTemplated` (TEMPLATE/SOLUTION/HIDDEN, to build the student-visible `initialContent` and the outer `$body`
template) and once per-language inside `compileExo()` (DEPENDENCY/IMPORT/run(), to fill in everything else). The `correction`
string is the only link between the two passes.

## Saving the student's work: GitSpy and friends

Two independent mechanisms persist what a student does, both driven by `Game.progressSpyListeners`
(`plm.core.model.tracking.ProgressSpyListener`, fired from `Game.fireProgressSpy()`/`fireCallForHelpSpy()`/
`fireCancelCallForHelpSpy()`/`fireReadTipSpy()` and from `Game.fireCurrentExerciseChanged()`/`worldHasChanged()`) whenever the
student runs an exercise (`LessonRunner`, after showing the pass/fail dialog), switches exercise, reverts their code, asks
for/cancels help, or reads a hint:
- `LocalFileSpy`: appends one human-readable line per `executed()` event to a local `progress.spy` text file. Every other
  callback is a no-op.
- `GitSpy` (+ `GitUtils`, a thin wrapper around JGit): the interesting one, one per running PLM instance, constructed once
  in `Game`'s constructor and registered as a `UserSwitchesListener` too. For each PLM user, it keeps one local git
  repository under `SAVE_DIR/<userUUID>`, on a branch named `"PLM" + sha1(userUUID)`, and mirrors it to a shared server repo
  (`plm.git.server.url`) -- this is both the per-student backup/sync mechanism and the anonymized activity log the PLM
  maintainers use to see how students actually work through the exercises. Pushing is entirely opt-in, gated by the
  `plm.git.track.user` property (the student is asked once, see `StartExecution`); everything still happens on the *local*
  repo either way.
  - `userHasChanged()` (called once at startup and on every user switch): creates/reuses `repoDir`, sets the git remote
    config, creates an empty initial commit the very first time, checks out (or creates) the user's branch, tries to
    `fetch`+`merge` the same branch from the server to resume a previous session (`GitUtils.mergeRemoteIntoLocalBranch()`
    resolves conflicts file-by-file by keeping whichever side's last commit on that path is more recent), commits a
    `"started"` marker, then asks for a (rate-limited, see below) push.
  - `executed()`: writes/rewrites 4 files per exercise (`<id>.<ext>.code`, `.error`, `.correction`, `.mission`, via
    `createFiles()`), creates or deletes a `.DONE` marker file depending on pass/fail (`checkSuccess()`), then commits (JSON
    commit message, see below) and asks for a push.
  - `switched()`/`reverted()`: same idea for "the student navigated away from an exercise that had a result" (re-writes its
    files before committing) and "the student clicked revert" (deletes that exercise's files instead).
  - `callForHelp()`/`cancelCallForHelp()`/`readTip()`: same write-then-commit-then-push pattern for those UI events.
  - `leave()` (PLM shutdown): does one final `addFiles()`+`commit()`+`forcefullyPushToUserBranch()` (bypassing the
    rate-limiting below, since this is the last chance to sync) then disposes the JGit handle.
  - Commit messages are hand-built JSON blobs (`writeCommitMessage()`/`writePLMStartedOrLeavedCommitMessage()`) carrying the
    exercise id, language, outcome, test counts, and optional feedback -- built by string surgery (dropping the JSON
    library's own leading `{`) so that a `"kind"` key always comes first and the commit list stays human-scannable
    from the GitHub UI.
  - Pushing is rate-limited (`GitUtils.maybePushToUserBranch()`, see its own comment for the incident that motivated it):
    at most one push in flight at a time (a static `currentlyPushing` flag) and a fixed delay (currently 1 minute) before
    it actually pushes, run off the Swing EDT via a `SwingWorker`; `forcefullyPushToUserBranch()` used by `leave()` skips
    that delay. A push that's rejected retries once after fetching and merging the remote branch first.
- `GitSessionKit` (`plm.core.model.session`, `ISessionKit`) is the read side of the same on-disk layout `GitSpy` writes:
  at startup, `loadLesson()` reads each exercise's `.code` file back into its `SourceFile.body` and its `.DONE` file into
  `studentWork`'s pass/fail state; `storeLesson()` is a deliberate no-op ("Everything's done by spy"), since `GitSpy`
  already keeps those files current after every relevant event. `GitSessionKit` additionally computes and reads back one
  `.summary` file per lesson (a `StudentWork`-produced digest, not something `GitSpy` writes).
- `SourceFileRevertable` (what `ExerciseTemplated.newSourceFromFile()` actually instantiates) keeps the original
  `initialContent` from step 1 above so the "revert" action (`Game.worldHasChanged()`) can restore the editor to it; this is
  unrelated to git and only concerns the in-memory `SourceFile`.

## How tests work

* `SimpleExercise` tests ensure that the compilation and templating work in every language without pulling a full universe. It
  also tests the error catching mechanism of each language is working properly (syntax error, exception raising, etc).
* Integration testing driven by `ExoTest`/`LessonTest` and living in `src/plm/test/integration` (`ExoTestJavaLang`,
   `ExoTestScalaLang`, `ExoTestPythonLang`, `ExoTestCLang`) run every exercise's own correction entity, in every language it
   supports, through the normal compile/run/check pipeline and assert it passes. This is a regression test suite over the
   pedagogical content itself: it catches broken exercises (e.g. a correction that no longer matches its `-answerN.map`) rather
   than testing application logic in isolation.
* `plm.test.git.*` tests the session persistence logic.
* `plm.test.gui.MainFrameSmokeTest` is a Swing smoke test (via AssertJ-Swing) that needs a display (`xvfb` in CI).

## Languages & runtime versions

- **Build/host**: Java 17 (`maven.compiler.release=17`), built with Maven (`pom.xml`.
- **Student languages** (each implemented as a `ProgrammingLanguage` subclass in `plm.core.lang`):
  - **Java**: compiled with the standard JVM javac, entry point is the correction/student class directly (no `public static
    void main` boilerplate exposed to the student).
  - **Scala**: `scala-library`/`scala-compiler`/`scala-reflect` 2.12.20; compiled by driving `scala.tools.nsc.Main` as a
    separate `java -cp <scala jars> ...` process, then run as its own `java -jar` process like Java.
  - **Python**: an external `python3` process is spawned per run.
  - **C** compiled externally and driven over pipes.
- Adding a new language: see
  `https://github.com/oster/PLM/wiki/Adding-a-new-programming-language`
  and extend `plm.core.lang.ProgrammingLanguage`.

## Build & run

```
mvn package -DskipTests && java -jar target/plm-*.jar          # build + run, no tests
mvn test                                                       # full test suite
mvn test -Dtest=TestName*                                      # a single test class
mvn test -Dtest=none -Dsurefire.failIfNoSpecifiedTests=false   # Only the C unit tests
```

Entry point: `plm.core.ui.ProgrammersLearningMachine` (`main.class` in `pom.xml`).

## Designing a new universe

Extend, at minimum: `World` (state/data), an `Entity` subclass (ancestor of correction entities, exposes primitives), a
`WorldView` (graphical rendering), and usually a `WorldPanel`/`EntityControlPanel` for interactive controls. Document it with an
HTML file following the same convention as mission texts. Existing universes stay small (a few hundred to ~1500 lines including
the buggle map editor) because all non-functional plumbing (compilation, templating, session handling) is factored into
`plm.core`.

# Translating the PLM

The easiest is to use
[weblate](https://hosted.weblate.org/projects/programmers-learning-machine/)
for that. You have two translation components: The 'PLM engine'
contains all texts that is displayed in the interface while the
'missions and exercises' contains the text of all exercises
implemented in this environment.  Even if all lessons are grouped
together, you probably want to translate the "welcome" lesson first.

Every change to the engine will automatically be included in the
[versions built on
appveyor](https://ci.appveyor.com/project/mquinson/plm) (once weblate
commits your work, which may take one or two day unless someone asks
the robots to do so earlier). The translations of the mission texts is
not integrated automatically, and someone should run po4a locally and
commit the modified files. po4a cannot be run automatically on
appveyor because of portability issues.

Everytime that you think you reached a milestone in your translation
(e.g., completion of the engine or of any given lesson), you should
drop an email to Martin Quinson so that he publishes your work in a
new release of the PLM.

# Adding a new exercise

## Defining an exercise

The Moria exercise is representative of what you want to do
https://github.com/BuggleInc/PLM/tree/javaUI/src/lessons/welcome/summative

**Everything works because the files have the right name.** 

- Moria.html: mission text in English, created either manually or with the editor (see below).
- Moria.map: initial map, created with the map editor (see below).
- Moria.java: creates the exercise. This code is generic and boring. Don't mess up your copy/paste :)

- MoriaEntity.java: Template and solution in Java. 
- MoriaEntity.py: Template and solution in python.
- ScalaMoriaEntity.scala:  Template and solution in Scala.
- MoriaEntity.c: Template and solution in C (experimental).

When the exercise is initialized, it will contain the TEMPLATE without
the SOLUTION. It means that PLM takes everything between BEGIN
TEMPLATE and END TEMPLATE as a template for the student, and kill the
lines between BEGIN SOLUTION and END SOLUTION.

When the student runs its code, the editor's content is placed in the
Entity file in place of the TEMPLATE before compiling. It is thus
expected that the student uses the TEMPLATE as a guide, and then write
an equivalent of the SOLUTION that was removed.

- Moria-answer0.map: expected world situation, computed by the java solution (do not edit)
- Moria.fr.html: French translation, written from translated content coming from weblate.org (do not edit)
- Moria.it.html: Italian translation, do not edit.
- Moria.pt_BR.html: Brazilian translation, do not edit.
- Moria.pt.html: Portuguese translation, do not edit.

To create the translation files, do not proceed manually. You need to
list your exercise in the `po4a.conf` file, in the main directory.
po4a will extract the strings of your mission text, to push them to
weblate. There, volunteers will translate your content to their
language. Next time that po4a is run, a new translated mission file
will be created (if over 80% of its content is translated) and added
to the git.

## World instance (map)

If you want to add an exercise for the Buggle universe, you can open
the map editor with:
  java -cp /usr/share/java/plm-*.jar plm.core.ui.editor.buggleeditor.MapEditorApp

There is no graphical editors for the other universes, so you will
have to create the world instances programatically, from your exercise.

## Mission text

You can either write the text manually in html, or use the PLM mission
editor, that make it easier to write mission texts that work for more
than one programming language. You can start it with:
  java -cp /usr/share/java/plm-*.jar plm.core.ui.editor.MissionEditorApp

## Connecting your exercise to the lesson

The lesson is defined as a Java file:
https://github.com/BuggleInc/PLM/blob/javaUI/src/lessons/welcome/Main.java#L226

# Maintainer's note

## Managing translations

Here are the commands to run to update the pot files on weblate, to
allow the translators to update their work. This should be done
regularly.

- po4a po4a.conf
- ant i18n-update
- git checkout l10n/*/*.po
- git commit -m "Update translation templates" l10n/*/*.pot
- git push && wlc pull

## Releasing the PLM

This is the check list to complete a new version of the PLM. It is
mostly for internal use.

Building the release:
- The content of weblate is correctly integrated.
  - wlc commit && wlc push && git pull --rebase
- All tests pass
  - ant test-all
- The mission texts are correctly translated:
  - po4a po4a.conf
  - Commit every translated file (eg, **/*.fr.html) in git
- ChangeLog
  - Ensure that all changes are documented
  - Update the release date and version number (even number for stable)
- Update the PLM_VERSION variable in .appveyor.yml to match the ChangeLog
  - Use an even number for the stable release.
- Update the version number in lib/resources/plm.configuration.properties
- Git: commit everything
  - git push && git tag v2.??.?? && git push tags
  
Publishing the release:
- Open the plm-src.jar, and repack it as a tgz file containing a directory
  - mv plm-src.2.9*.jar /tmp/ ; mkdir /tmp/plm-2.9.XXX; unzip ../plm-src.2.9* -d /tmp/plm-2.* 
  - cd /tmp ; tar plm-2.9.XXX ; gzip -9v plm-2*.tar
- Document the tag on github, and then upload all 4 artefacts from appveyor
- Modify the web page to point to the latest release, and publish it
- Announce it on Discord and Tweeter

Publishing the Debian package:
- gbp import-orig plm-src.*.tgz
- dch -i "New upstream release."
- Edit debian/changelog to include [some ideas of the] upstream changelog
- git commit -m "Package the new upstream release" debian/changelog
  git-pbuilder update
  gbp build-package --git-pbuilder --git-tag
- Install and test the built package
- debsign ../build-area/plm_*-1_source.changes
  dput ../build-area/plm_*-1_source.changes

Preparing the next release cycle
- Create a new entry in Changelog with an odd patch version
- Update the version number in .appveyor.yml and plm.configuration.properties
