The Programmer's Learning Machine (PLM) is a free cross-platform programming exerciser. It lets you explore various concepts of
programming through interactive challenges in differing micro-worlds, that you can solve in either Java, Python, Scala or C. The
design rational is given in the following paper: [The Programmer's Learning Machine: A Teaching System To Learn
Programming](https://hal.inria.fr/hal-01243646). On this page, you will find the following sections:

* [Overall architecture](#Architecture) and concepts, to help the onboarding of prospective contributors
* [How to translate the project](#Translating_the_PLM)
* [Adding a new exercise](#Adding_a_new_exercise)
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
* **Compile**: `Exercise.compileAll()` delegates to `ProgrammingLanguage.compileExo()` for the selected language. Java and C are
  compiled to an external files, Scala is compiled within the same JVM that runs the PLM (but shall be converted to exernal
  compilation at some point); Python and Ruby scripts are not compiled at all.
* **Mutate entities**: `Exercise.mutateEntities()` swaps in the student's (or the correction's) compiled code for each world.
  This is mandatory for the in-JVM execution as in Scala, but shall be removed once every languages execute remotely.
* **Run**: `World.runEntities()` spawns one thread per entity and calls `ProgrammingLanguage.runEntity()`, whose behavior
  depends on the language/universe combo (see `ProgrammingLanguage.runEntity`):
   - Java/C: an external process is started; primitives are relayed over pipes (`plm.universe.CommandExecutor`). In the future,
     this shall be the way to go for all languages.
   - Scala: the entity's `run()` method (student-authored) executes directly.
   - Python/Ruby: student code is injected into a scripting engine bound to the Java world/entity, using an in-JVM scripting
     engine for these languages. In the future, an external execution shall be used.
   - LightBot: This challenge is an exception, as it can only be solved using the graphical block-list rather than a real
     programming language. Thus, `run()` *interprets* a student-authored program.
* **Check**: `Exercise.check()` compares each `currentWorld` to its `answerWorld` via `World.winning()`. On mismatch,
  `World.diffTo()` produces a human-readable diff shown to the student. All the universes but Lander use a structural equality
  between currentWorld and answerWorld to compute whether it's winning. Instead, Lander checks whether the lunar lander reached
  a pad or crashed.

Runaway/infinite-loop student code is caught via `Thread.UncaughtExceptionHandler` + interruption, not a hard sandbox, keep
this in mind when touching `World.runEntities`. Changing this is the core motivation for the ongoing remote execution transition.

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
  - **Scala**: `scala-library`/`scala-compiler`/`scala-reflect` 2.12.20; compiled jointly with Java sources (scalac runs before
    javac in the Maven build) since PLM compiles user Scala in-process.
  - **Python**: via Jython 2.7.3 (`jython-standalone`), i.e. **Python 2 syntax**, not Python 3. TODO: this will change.
  - **Ruby** via JRuby 9.4.8.0 (`jruby-complete`).
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
