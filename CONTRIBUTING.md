The Programmer's Learning Machine (PLM) is a free cross-platform programming exerciser. It lets you explore various concepts of
programming through interactive challenges in differing micro-worlds, that you can solve in either Java, Python, Scala or C. The
design rational is given in the following paper: [The Programmer's Learning Machine: A Teaching System To Learn
Programming](https://hal.inria.fr/hal-01243646). On this page, you will find the following sections:

* [Overall architecture](#Architecture) and concepts, to help the onboarding of prospective contributors
* [How to translate the project](#Translating_the_PLM)
* [How to add new exercises and design new universes](#Extending_the_PLM)
* [Maintainer's notes](#Maintainers_notes): how to merge in new translation and how to release a new version of the PLM.

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
    reproduce the *expected algorithm*, not just a correctly sorted array. To make this efficient, the student-facing API is instrumented to count the amount of data access in read and write. We don't observe the complete operation list but only the amount of reads and writes to the data array. This is not perfect as a student may manage to get the data sorted with the exact same amount of operations without following the exact expected algorithm, but it's rather unlikely and a perfect verification of the operations history would probably be too computationally intensive.
  - `bat`: unit-testing style no graphical world but a textual output; a method prototype is filled in and tested against many
    parameter values.
  - Specific sorting microwords: `sort/baseball` ([pebble-motion](https://en.wikipedia.org/wiki/Pebble_motion_problems)),
   `sort/pancake` ([pancake sorting](https://en.wikipedia.org/wiki/Pancake_sorting)), `sort/dutchflag` ([Dutch national flag
    sorting](https://en.wikipedia.org/wiki/Dutch_national_flag_problem)).
  - Specific recusion microwords: `recursion/hanoi` (comes with a rich set of exercises on recursive problem decomposition),
    `recursion/cons` (recursive strings using the [cons](https://en.wikipedia.org/wiki/Cons) [car and
    CDR](https://en.wikipedia.org/wiki/CAR_and_CDR) constructs of LISP). The cons micro-world is subclassed from the bat one.
  - Recreative microworlds: `lightbot` a brain teaser for programmers, `lander` a lunar lander programming challenge. 
- **Correction entity**: for each exercise/language pair, a source file (e.g. `MoriaEntity.java`, `MoriaEntity.py`,
  `MoriaEntity.scala`, `MoriaEntity.c`) contains both the teacher's reference solution and the template shown to the
  student. See "Adding a new exercise" below for the file layout and "From correction entity to compilable source: templating".

## How an exercise executes

* **Reset**: `currentWorld` is reset from `initialWorld` for each world instance.
* **Compile**: `Exercise.compile()` delegates to `ProgrammingLanguage.compileExo()` for the selected language.
  - A source code containing the student code and the execution harness is generated (see the section on templating below).
  - The code is then compiled to an external executable/jar if needed. Java compiles in-process to avoid the startup time of an
    external JVM, using the same API than javac. Scala and C are stating external compilers, and Python has nothing to compile.
    - TODO: Scala should be converted to compile in-process too, as Java. But it's a bit more difficult as its API is less
      stable than the Java counterpart, and may introduce thread safety issues.
  - `compileExo()` returns a textual reference to the result (the path to a jar, a binary or a script, or `null` for LightBoy
    that don't compile at all), which the caller then passes down as-is to `runEntity()`'s `executable` parameter below.
* **Run**: `World.runEntities()` spawns one thread per entity and calls `ProgrammingLanguage.runEntity()`:
   - Java/Scala/Python/C: all four inherit the same `RemoteExecutionLang.runEntity()`. It binds a UNIX domain socket, starts
     the student code as an external process, and relays primitive calls over that socket to `plm.universe.CommandExecutor`.
     The only thing each language still implements on its own is `buildProcess()`, which turns the compiled/written artifact
     into the right command line (`java -jar ...`, `python3 ...`, the compiled binary, etc).
   - LightBot: This brain teaser is an exception, as it can only be solved using the graphical block-list rather than a real
     programming language. Thus, `run()` *interprets* a student-authored program.
* **Check**: `Exercise.check()` compares each `currentWorld` to its `answerWorld` via `World.winning()`. On mismatch,
  `World.diffTo()` produces a human-readable diff shown to the student. All the universes but Lander use a structural equality
  between currentWorld and answerWorld to compute whether it's winning. Instead, Lander checks whether the lunar lander reached
  a pad or crashed.

* **Dealing with infinite loops in student code**. The "Stop" action calls `LessonRunner.stopAll()`, which cooperatively
  `Thread.interrupt()`s each per-entity runner thread. Since student code is an external process, interrupting the runner thread
  only unblocks it from `Process.waitFor()`; `RemoteExecutionLang.runEntity()` catches that `InterruptedException` and calls
  `destroyForcibly()` on the child process before returning, so a stopped infinite loop does not leave any orphaned process
  behind. This mechanism is not a hard sandbox either: the process is killed, but nothing prevents it from spawning its own
  children or from being heavy enough to matter for the second or so it takes to die.

## From correction entity to compilable source: templating

This section is about how the single `XxxEntity.<ext>` file described in "Adding a new exercise" below (which mixes the
teacher's solution and the student-facing template) becomes two different compilable programs: one for the CORRECTION which is
used to compute the demo and the `answerWorld`, and one for the student's current editor content when the "Run" button is hit in
the GUI. This process can be decomposed as follows:

- When the lesson is loaded, `ExerciseTemplated.setup()` only checks which languages have an entity file (`FileUtils.exists()`)
  but the files are not parsed nor even loaded in memory at this stage since we almost never need to parse all entities in all
  language for a given PLM usage session.
- When needed, the entity content is parsed (split into parts), and a `SourceFile` is created to store the result of this
  parsing. This step (detailed below) is language agnostic: it applies exactly the same for all entities of all languages.
- When the "Run" button is hit, a compilable file content is derived from the current content of the editor and along with the
  `SourceFile` that was previously parsed. This happens for each new exercice compilation, and also to compile the correction
  entity that computes the `answerWorld`.

### Entity content parsing: `EntityTemplateParser.parse()` (language agnostic -- cached)

The entity file is read and parsed the first time `Exercise.getSourceFilesList(lang)` is called for that language (see
`loadSourceFiles()` and `newSourceFromFile()`). The result is cached as a `SourceFile` in `Exercise.sourceFiles`.
`Exercise.getLoadedSourceFiles(lang)` returns what is already loaded without parsing anything: session saving and
`Game.revertExo()` use it.

`split()` cuts the file into segments, driven by marker comments. Markers are matched anywhere in a line, expected alone on it,
and removed from the segments:
- `BEGIN/END TEMPLATE`: the code initially shown to the student in the editor (at most one).
- `BEGIN/END SOLUTION`: code kept for the correction but hidden from the student. With a `TEMPLATE`, there can be any number of
  `SOLUTION`s, before, inside or after it. Without a `TEMPLATE`, there is exactly `SOLUTION`, and it plays the role of the
  templated region: the editor is initially empty in this case. An entity without any `SOLUTION` is rejected as a likely
  mistake: if the template is meant to be the answer already, say so with an empty `SOLUTION` section.
- `BEGIN/END IMPORT`: extra imports, kept out of the template.
- `BEGIN/END REMOTE` (optional): narrows what counts as head and tail. Everything before `BEGIN REMOTE` and after `END REMOTE`
  is dropped, which lets Java/Scala leave out their own package/class declaration (their wrapper provides it). It must fully
  enclose the templated region, and any `SOLUTION` out of the template region must be within the remote region.

Any invalid markup throws a RuntimeException (unmatched or nested markers, several `TEMPLATE`s, a `SOLUTION` straddling another
marker, ...).

What lies outside the templated region is the `head` and the `tail`. The resulting `SourceFile` holds:
- `body`: initially the `TEMPLATE` content minus its `SOLUTION` sections, what the student sees in the editor. It is dedented by
  `bodyIndent`, the indentation shared by the whole templated region (solutions included). For Python, leading tabs are first
  expanded to spaces, with tab stops every 8 columns.
- `template`: `head + "$body" + tail`, without any `SOLUTION` section. It is used to compile the student's code by substituting
  the string "$body" with the editor's content.
- `correctionTemplate`: same, but keeping the `SOLUTION` sections of head and tail. It is used to compile the correction.
- `correctionBody`: the raw span (markers included) from `BEGIN TEMPLATE` to `END TEMPLATE`, or from `BEGIN SOLUTION` to
  `END SOLUTION` without template. It is the `$body` value used to compile the correction.
- `imports`: the content of the `IMPORT` sections.
- `remote`: the name of the `RemoteXxx` universe, guessed by `guessRemote()` from the entity file content.
- `bodyIndent`: see above.

### Step 2: building a compilable source (language-specific in `compileExo()`, not cached)

Each `compileExo()` reads the pieces stored in every `SourceFile`, picks `correctionTemplate` for
`StudentOrCorrection.CORRECTION` or `template` otherwise, and wraps it the way its language needs:
- Java/Scala wrap it in their own class/object boilerplate: `package`, their imports (all on a single line, so that the line
  numbers of the generated code do not depend on how many there are), `class Entity {`, then the template, then a closing brace.
- Python only prepends its own imports to the template, with no further boilerplate. As indentation matters in Python, it
  indents the student's code by `bodyIndent` spaces (after removing its own common indentation) before substituting it. The tabs
  found in the leading whitespace of the whole source (entity and student) are then expanded as well, so that tabs and spaces
  never get mixed up.
- C inserts a `#line` preprocessor directive right before `$body`, so that compiler errors point at the entity's own file.

The body is retrieved from the `SourceFile` (either the source's `correctionBody` or the editor's current content).

Once the template is computed the actual `entityCode` is computed:
  - `offset` is computed if needed (not in C). It's the number of lines of the template before `$body`'s own first line, meant
    to fix the location of the compilation errors so that they point to the code written by the student (no caller uses it yet).
  - `$body` is substituted with the actual body.
  - Python fixes the indentation: change tabs to spaces in editor's content and reindent the body to fit its position in the
    template.
  - non-breaking spaces are stripped.
  - The method returns a `SourceFile.CompilableContent(content, offset)` record
- The `content` is written to a per-compile directory on disk, which name is given by `TemplatedRemoteLang.packageNameForExercise()`. 
  This name derived from the exercise id and `STUDENT`/`CORRECTION`, so unrelated concurrent compiles never collide) alongside
  the copied `RemoteXxx` glue file and any other support file the exercise needs, then compiled/run the usual way. `offset` is
  meant to translate a compiler error's line number back into the student's own editor location but it not wired to anything yet.

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
  - `executed()`: writes/rewrites 4 files per exercise (`<id>.<ext>.code`, `.error`, `.correction` (the entity file, read again
    from its source), `.mission`, via `createFiles()`), creates or deletes a `.DONE` marker file depending on pass/fail (`checkSuccess()`), then commits (JSON
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
- `SourceFile` (what `Exercise.newSource()` actually stores, see "From correction entity to compilable source" above) keeps the
  original `initialContent` from step 1 above so the "revert" action (`Game.worldHasChanged()`) can restore the editor to it; this is
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
  - **Scala**: `scala3-library_3`/`scala3-compiler_3` 3.9.0 (LTS); compiled by driving `dotty.tools.dotc.Main` as a
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

# Extending the PLM

## Adding a new exercise

### Defining an exercise

The Moria exercise is representative of what you want to do
https://github.com/BuggleInc/PLM/tree/javaUI/src/lessons/welcome/summative

**Everything works because the files have the right name.** 

- Moria.html: mission text in English, created either manually or with the editor (see below).
- Moria.map: initial map, created with the map editor (see below).
- Moria.java: creates the exercise. This code is generic and boring. Don't mess up your copy/paste :)

- MoriaEntity.java: Template and solution in Java. 
- MoriaEntity.py: Template and solution in python.
- MoriaEntity.scala:  Template and solution in Scala.
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

Since the entities are loaded lazily by the PLM, you need either to switch the programming languages in the interface (or to run
the maven tests) to ensure that your exercise entities are correctly formatted. If not, the student code may not compile
properly.

### World instance (map)

If you want to add an exercise for the Buggle universe, you can open the map editor with:
  java -cp /usr/share/java/plm-*.jar plm.core.ui.editor.buggleeditor.MapEditorApp

There is no graphical editors for the other universes, so you will have to create the world instances programatically, from your
exercise.

### Mission text

You can either write the text manually in html, or use the PLM mission editor, that make it easier to write mission texts that
work for more than one programming language. You can start it with:
  java -cp /usr/share/java/plm-*.jar plm.core.ui.editor.MissionEditorApp

### Connecting your exercise to the lesson

The lesson is defined as a Java file:
https://github.com/BuggleInc/PLM/blob/javaUI/src/lessons/welcome/Main.java#L226

## Designing a new universe

Extend, at minimum: `World` (state/data), an `Entity` subclass (ancestor of correction entities, exposes primitives), a
`WorldView` (graphical rendering), and usually a `WorldPanel`/`EntityControlPanel` for interactive controls. Document it with an
HTML file following the same convention as mission texts. Existing universes stay small (a few hundred to ~1500 lines including
the buggle map editor) because all non-functional plumbing (compilation, templating, session handling) is factored into
`plm.core`.

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

## TODOs

TODO: add to the exercice a verification of the source code, so that MethodDogHouse can verify that there is only one occurence of the left() method in the source code

TODO: create an Exercise.runAll(WorldKind), to come after Exercise.compile()
TODO: Kill Exercice.compile() as it does nothing more than delegating to ProgrammingLanguage

TODO: fix the compilation error messages to match the student code: `SourceFile.getCompilableContent()` now returns the
      `$body` offset alongside the compilable source, but no caller uses it yet to shift a compiler diagnostic's line number
      back to the student's own editor coordinates.
TODO: Port the SimpleExercise tests to LangC
TODO: Precompile the correction entities within the jar file so that they don't get generated and compiled every time we 
      load the lesson
TODO: split the UI from the compilation+exec services. The latter may be pure functions with no hidden globals. The former should include the Game singleton that encompasses the model part of the MVC thing.
TODO: Use the PLM's JVM to compile Scala too (Java's own compilation is now in-process).
TODO: benchmark the tests to understand where the time goes, and optimize this out

TODO: Primitive numbering should be automatic
TODO: Find a way for the exercise to specify which primitives should be forbidden to the student in this specific exercise 
