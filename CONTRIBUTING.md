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

Releasing the PLM
=================

This is the check list to complete a new version of the PLM. It is
mostly for internal use.

Building the release:
- The content of weblate is correctly integrated.
  - wlc commit && wlc push && git pull --rebase
- The mission texts are correctly translated:
  - po4a po4a.conf
  - Commit every translated file (eg, **/*.fr.html) in git
- ChangeLog
  - Ensure that all changes are documented
  - Update the release date and version number
- Update the PLM_VERSION variable in .appveyor.yml to match the ChangeLog
- Update the version number in lib/resources/plm.configuration.properties
- Git: commit everything
  - git push && git tag v2.??.?? && git push tags
  
Publishing the release:
- Open the plm-src.jar, and repack it as a tgz file containing a directory
- Document the tag on github, and then upload all 4 artefacts from appveyor
- Modify the web page to point to the latest release, and publish it
- Announce it on Discord

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
