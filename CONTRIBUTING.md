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

- **World + Entity**: a `World` (`plm.universe.World`) is the simulated environment encoding a pedagogical problem situation.
  This is a micro-world instance. It contains one or more `Entity` objects, which are the actors that execute the student's code
  (or the teacher's correction code) against the world's primitives. 
- **Universe**: a micro-world *kind*, i.e. a family of worlds/entities sharing a theme and a set of primitives (e.g. "a buggle
  can walk, paint, pick up objects" or "a turtle draws on the ground as it moves"). Universes are found in `src/plm/universe`
  for the generic ones and in `src/lessons/*/universe` for the ones specificaly tailored for a given lesson. See the
  [pedagogical documentation](PEDAGOGICAL.md) for more information.

- **Exercise**: the unit of work a student solves (`plm.core.model.lesson.Exercise`). An `Exercise` is a programming challenge
  aiming at teaching or exercising a given concept. A given exercise uses exactly **one** universe. An exercise comes with a set
  of worlds and entities that act as **test cases** for the student code. Each provided world must pass for the exercise to be
  validated. This is thus comparable to parametrized unit tests: Adding another world instance to an exercise, without touching
  the student-facing code, is the standard way to catch a wider range of incorrect solutions.
- **Lesson**: a `Lesson` (e.g. `welcome`, `sort`, `recursion`) groups `Exercise`s in a pedagogical sequence. Each of them
  constitute a progressive set of challenges taking the users through their learning path. Again, see the [pedagogical
  documentation](PEDAGOGICAL.md) for details.
  
- **Templating entity**: for each exercise/language pair, a source file (e.g. `MoriaEntity.java`, `MoriaEntity.py`,
  `MoriaEntity.scala`, `MoriaEntity.c`) contains both the teacher's reference correction, the template shown to the student and
  the code harness to execute the student code. See "Code templating" below for the file layout.

## How an exercise executes: the 30,000 feet overview

* **Exercise setup**: Each exercise comes with a `setup()` method that creates the pedagogical settings and populates the
  microworlds with entities. The exercise owns **three parallel sets of worlds**:
  - `initialWorld`: the starting state, as built by `setup()`.
  - `answerWorld`: the target state, produced by either loading a cached solution from the disk, or by running the teacher's
    correction code on the corresponding `initialWorld`. 
  - `currentWorld`: the current state while executing the student's code, initially equal to `initialWorld`. If the
    `currentWorld` becomes semantically equals to `answerWorld` after executing the student code, the exercise is passed.

Here are the steps of the exercise execution:
* **Reset**: `currentWorld` is reset from `initialWorld` for each world instance.
* **Templating**: The templating entity of the current language is split in parts, and a new source code is generated from the
  execution harness and the current editor's content (see the section on templating below).
* **Compile**: The starting point is `ProgrammingLanguage.compileExo()`.
  - The code is then compiled to an external executable/jar if needed. Java/Scala compile in-process to avoid the startup time of an
    external JVM. C and Python use an external compilers.
  - `compileExo()` returns a textual reference to the result (the path to a jar, a binary, etc), which the caller then passes
    down as-is to `runEntity()`'s `executable` parameter below.
* **Remote execution**: `World.runEntities()` spawns one thread per entity and calls `ProgrammingLanguage.runEntity()`: that
  method binds a UNIX domain socket, starts the student code as an external process, and relays primitive calls over that socket
  to `plm.universe.CommandExecutor`. These primitives allow the student code to interact with the microworld that is located in
  the PLM process. This is transparent to both the student and the exercise authors.
* **Check**: when all entities are terminated, `Exercise.check()` compares each `currentWorld` to its `answerWorld` via
  `World.winning()`. On mismatch, `World.diffTo()` produces a human-readable diff shown to the student. All the universes but
  Lander use a structural equality between currentWorld and answerWorld to compute whether it's winning. Instead, Lander checks
  whether the lunar lander reached a pad or crashed.
* **Session saving**: each student attempt is saved in a local git repository along with the exercise outcome (compilation
  error, failed objective or passed). The goal is to enable learning analytics if the student allowed the export of her
  anonymized data to an online repository.

* **Lightbot specificities**: this brain teaser can only be solved using the graphical block-list rather than a real programming
     language. Thus, `run()` *interprets* a student-authored program and there is no templating nor remote execution.

* **Dealing with infinite loops in student code**. The "Stop" action calls `LessonRunner.stopAll()`, which cooperatively
  `Thread.interrupt()`s each per-entity runner thread. Since student code is an external process, interrupting the runner thread
  only unblocks it from `Process.waitFor()`; `RemoteExecutionLang.runEntity()` catches that `InterruptedException` and calls
  `destroyForcibly()` on the child process before returning, so a stopped infinite loop does not leave any orphaned process
  behind. This mechanism is not a hard sandbox either: the process is killed, but nothing prevents it from spawning its own
  children or from being heavy enough to matter for the second or so it takes to die.

## Code templating (preparing the source to compile)

This section is about how the single `XxxEntity.<ext>` file described in "Adding a new exercise" below (which mixes the
teacher's solution and the student-facing template) becomes two different compilable programs: one for the CORRECTION which is
used to compute the demo and the `answerWorld`, and one for the student's current editor content when the "Run" button is hit in
the GUI. This process can be decomposed as follows:

- When the lesson is loaded, `ExerciseTemplated.setup()` only checks which languages have an entity file (`FileUtils.exists()`)
  but the files are not parsed nor even loaded in memory at this stage since we almost never need to parse all entities in all
  language for a given PLM usage session. `LightBotExercise` has no entity file: it overrides `setup()` and creates its own
  `LightBotSourceFile`.
- When needed, the entity content is parsed (split into parts), and a `SourceFile` is created to store the result of this
  parsing. This step (detailed below) is language agnostic: it applies exactly the same for all entities of all languages.
- When the "Run" button is hit, a compilable file content is derived from the current content of the editor and along with the
  `SourceFile` that was previously parsed. This happens for each new exercice compilation, and also to compile the correction
  entity that computes the `answerWorld`.

### Entity content parsing: `EntityTemplateParser.parse()` (language agnostic code -- cached result)

The entity file is read and parsed the first time `Exercise.getSourceFilesList(lang)` is called for that language (see
`loadSourceFiles()` and `newSourceFromFile()`). The result is cached as a `SourceFile` in `Exercise.sourceFiles`.
`Exercise.getLoadedSourceFiles(lang)` returns what is already loaded without parsing anything: session saving and
`Game.revertExo()` use it.

`split()` cuts the file into segments, driven by marker comments. Markers are matched anywhere in a line, expected alone on it,
and removed from the segments:
- `BEGIN/END TEMPLATE`: the code initially shown to the student in the editor (at most one).
- `BEGIN/END SOLUTION`: code kept for the correction but hidden from the student. With a `TEMPLATE`, there can be any number of
  `SOLUTION`s, before, inside or after it. Without a `TEMPLATE`, there is exactly one `SOLUTION`, and it plays the role of the
  templated region: the editor is initially empty in this case. An entity without any `SOLUTION` is rejected as a likely
  mistake: if the template is meant to be the answer already, say so with an empty `SOLUTION` section.
- `BEGIN/END IMPORT`: extra imports, kept out of the template.
- `BEGIN/END REMOTE` (optional): narrows what counts as head and tail. Everything before `BEGIN REMOTE` and after `END REMOTE`
  is dropped, which lets Java/Scala leave out their own package/class declaration (their wrapper provides it). It must fully
  enclose the templated region, and any `SOLUTION` out of the template region must be within the remote region.

Any invalid markup throws a RuntimeException (unmatched or nested markers, several `TEMPLATE`s, a `SOLUTION` straddling another
marker, ...).

What lies within REMOTE but outside the templated region is the `head` and the `tail`. The tail starts with a `\n` so that
Python sees it as a new block, even if the student left indented blank lines at the end of the body. 

The resulting `SourceFile` holds:
- `name`: the exercise's `tabName`, i.e. the label of the editor tab and the key of the saved code. The generated class is always
  called `Entity`, whatever this name is.
- `remote`: the name of the `RemoteXxx` universe, guessed by `guessRemote()` from the entity file content.
- `imports`: the content of the `IMPORT` sections.
- `bodyIndent`: the indentation shared by the whole templated region. Python uses it to reindent the editor's content before
  injecting it in the generated source code.
- `student`: an `EntityFileSegments(pre, body, post)` record, where:
   - `student.pre` is the head and `student.post` the tail without any `SOLUTION` section.
   - `student.body` is the initial editor content: the lines between `BEGIN TEMPLATE` and `END TEMPLATE` without the `SOLUTION`
      sections, or an an empty string when there is no TEMPLATE marker (the SOLUTION is then the templated region). It is
      dedented by `bodyIndent`. Leading tabs are expanded in Python, with tab stops every 8 columns, and replaced by 4 spaces in
      the other languages.
- `correction`: same record, to generate a correction entity, but `correction.pre` and `correction.post` keep the `SOLUTION`
  sections of head and tail. `correction.body` is the raw span (markers included) from `BEGIN TEMPLATE` to `END TEMPLATE`, or
  from `BEGIN SOLUTION` to `END SOLUTION` when no template is given.
- `editorContent`: the editor's current content, initially the `student.body`.

After the SourceFile creation, only SourceFile.editorContent is mutable: it is synchronized with the editor's content. The rest of that
object is immutable, except for the listener that is notified of the changes of the editor content.

### Step 2: building a compilable source (language-specific code in `compileExo()`, not cached)

Each `compileExo()` gets the `EntityFileSegments` using `SourceFile.getSegments(whatToCompile)`, with whatToCompile being either
`StudentOrCorrection.CORRECTION` or `STUDENT`. It then concatenates `pre`, the body (either the editor's content or the
teacher's correction) and `post` with what its language needs:
- Java/Scala wrap them in their own class/object boilerplate: `package`, their imports (all on a single line, so that the line
  numbers of the generated code do not depend on how many there are), `class Entity {`, then `pre`, the body, `post` and a
  closing brace.
- Python only prepends its own imports, with no further boilerplate. As indentation matters in Python, it indents the student's
  code by `bodyIndent` spaces (after removing its own common indentation) before concatenating it. The tabs found in the leading
  whitespace of the whole source (entity and student) are then expanded as well, so that tabs and spaces never get mixed up.
- C inserts a `#line` preprocessor directive between `pre` and the body, so that compiler errors point at the entity's own file.

Each language does this concatenation inline in its own `compileExo()`, to keep a single linear flow instead of jumping to a
handful of one-off helper lines elsewhere:
  - Java/Scala first compute `offset` via `JvmTemplatedLang.countLinesBeforeBody()`: the number of lines of the generated source
    before the body's own first line, meant to fix the location of compilation errors so that they point to the code written by
    the student. Java subtracts it from the line numbers of the compiler diagnostics reported for `Entity.java` when compiling
    the student's code (diagnostics located before the body are left untouched, and so are all of them when debugging is
    enabled, to keep the path of the generated file). Java and Scala also subtract it from the `Entity.java:N` and `Entity.scala:N`
    locations found in the stack traces that the student process writes on its stderr, through the
    `RemoteExecutionLang.shiftLocations()` hook (`JvmTemplatedLang` keeps the offset of each compiled jar in `lineShifts`;
    locations before the body, and all of them when debugging is enabled, are left untouched).
  - Java/Scala/Python add a trailing space and newline after the body, so that a body ending right before the closing brace
    still parses.
  - Python fixes the indentation: change tabs to spaces in editor's content and reindent the body to fit its position in the
    source.
  - non-breaking spaces are stripped.
- The resulting source is written to a per-compile directory on disk which name is unique, so unrelated concurrent compiles
  never collide. A `Main` containing the execution harness is also generated in this directory, and both files are compiled.
- The glue that does not depend on the exercise (`Remote`, `RemoteXxx`, `ValueSerializer`, `Point`, `RecList`...) is built once
  and shared by all compilations:
  - Java/Scala: `CodeCreation` (run by maven at `process-classes` stage) generates the `RemoteXxx` sources, then compiles them
    in-process with the helper sources into `plm-entities-java.jar` and `plm-entities-scala.jar`. The staged sources are kept in
    `target/plm-entities/` for debugging but excluded from PLM.jar by a filter of the shade plugin. The jars are resources of
    PLM.jar, deployed to `/tmp/plm` upon compilation and put on the classpath of both the compilation and the run.
  - Python: the modules are deployed in `/tmp/plm/python-entities`, which is prepended to the `PYTHONPATH` of the run. Python caches
    their bytecode by itself.
  - C: `LangC.ensureCachedObject()` compiles each file once to an object cached in `/tmp/plm/C/objects`, which is linked with each
    exercise.

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
  at startup, `loadLesson()` reads each exercise's `.code` file back into its `SourceFile.editorContent` and its `.DONE` file into
  `studentWork`'s pass/fail state; `storeLesson()` is a deliberate no-op ("Everything's done by spy"), since `GitSpy`
  already keeps those files current after every relevant event. `GitSessionKit` additionally computes and reads back one
  `.summary` file per lesson (a `StudentWork`-produced digest, not something `GitSpy` writes).
- `SourceFile` (what `Exercise.newSource()` actually stores, see "From correction entity to compilable source" above) keeps the
  initial body from step 1 above (`student.body()`) so the "revert" action (`Game.worldHasChanged()`) can restore the editor to it; this is
  unrelated to git and only concerns the in-memory `SourceFile`.

## How tests work

* `SimpleExercise` tests ensure that the compilation and templating work in every language without pulling a full universe. It
  also tests the error catching mechanism of each language is working properly (syntax error, exception raising, etc). The Java
  ones also check that the line of a compilation error and of a stack trace frame is the one of the editor, with debugging off.
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

TODO: fix the compilation error messages of Scala, and the stack traces of Python and C runtime errors, to match the student
      code: only the Java compiler diagnostics and the Java/Scala stack traces shift their line numbers back to the student's own
      editor coordinates so far.
TODO: Port the SimpleExercise tests to LangC
TODO: Precompile the correction entities within the jar file so that they don't get generated and compiled every time we 
      load the lesson
TODO: split the UI from the compilation+exec services. The latter may be pure functions with no hidden globals. The former should include the Game singleton that encompasses the model part of the MVC thing.
TODO: Use the PLM's JVM to compile Scala too (Java's own compilation is now in-process).
TODO: benchmark the tests to understand where the time goes, and optimize this out

TODO: Primitive numbering should be automatic
TODO: Find a way for the exercise to specify which primitives should be forbidden to the student in this specific exercise 
