# Pedagogical considerations

The Programmer’s Learning Machine (PLM) is an interactive exerciser for learning programming and algorithms. Using an integrated
and graphical environment that provides a short feedback loop, it allows students to learn in a (semi)-autonomous way. The PLM
builds upon the idea of [microworlds](https://edutechwiki.unige.ch/en/microworld), that are playful learning environments. The
learner program some **entities** that evolve in their **microworld** using the entities primitives: a buggle can walk, paint,
pick up objects; a turtle can draw on the ground as it moves, etc.

## Universes

The PLM provide several **universes** that are *kind* of microwords, each adapted to a specific set of pedagogical challenges.
Some universes are generic and adapted to a large set of situations while some other are specific to a given exercise. Some are
classical in CS classes (e.g. the sorting universe), others are more playful but still adapted to programing exercises (e.g. the
buggles or the turtles) and others are only recreatives (e.g. lightbot and lander). Almost all of the universes can be solved in
either Java, Scala3, Python3 or C. Only the brain teaser `lightbot` is solved through a graphical interface with no programming
language.

  - `bugglequest`: generic grid actor (buggles), richest primitive set. It is the main PLM microworld and its implementation is
    the reference. It is used to teach the basics about variablesand loops, and to introduce functions and problem
    decomposition. This micro-world is also used to present various maze algorithms in a specific lesson.
    - `turmites` is a subclass of the buggle microworld introducing [2D turing machines](https://en.wikipedia.org/wiki/Turmite).
  - `turtles`: LOGO-style turtle graphics, used to teach recursion through the drawing of fractals.
  - `sort`: sorting algorithms; primitives (`isSmaller`, `copy`, `swap`) observe the data accesses patterns so the student must
    reproduce the *expected algorithm*, not just a correctly sorted array. To make this efficient, the student-facing API is
    instrumented to count the amount of data access in read and write. We don't observe the complete operation list but only the
    amount of reads and writes to the data array. This is not perfect as a student may manage to get the data sorted with the
    exact same amount of operations without following the exact expected algorithm, but it's rather unlikely and a perfect
    verification of the operations history would probably be too computationally intensive.
  - `bat`: unit-testing style no graphical world but a textual output; a method prototype is filled in and tested against many
    parameter values.
  - Specific sorting microwords: `sort/baseball` ([pebble-motion](https://en.wikipedia.org/wiki/Pebble_motion_problems)),
   `sort/pancake` ([pancake sorting](https://en.wikipedia.org/wiki/Pancake_sorting)), `sort/dutchflag` ([Dutch national flag
    sorting](https://en.wikipedia.org/wiki/Dutch_national_flag_problem)).
  - Specific recusion microwords: `recursion/hanoi` (comes with a rich set of exercises on recursive problem decomposition),
    `recursion/cons` (recursive strings using the [cons](https://en.wikipedia.org/wiki/Cons) [car and
    CDR](https://en.wikipedia.org/wiki/CAR_and_CDR) constructs of LISP). The cons micro-world is subclassed from the bat one.
  - Recreative microworlds: `lightbot` a brain teaser for programmers, `lander` a lunar lander programming challenge. 

## Pedagogical stance

The goal of the PLM is to teach concepts of Computer Science to the user. Other projects are more focused on the experience fun
while others provide difficult challenges that can only be solved by the best players. The PLM provides a set of progressive
exercises carefuly built to take novice programmers through their learning path. We try to make that path pleasant and to
reinforce the learner's motivation, but we are not yet at the point where we could say that using the PLM is a fun game. On the
other side, we try to keep the learning path *progressive* so that nobody gets stuck on an almost-impossible challenge.

The exercises are not isolated, but grouped in lessons that are meant to be progressive and challenging but doable. I used many
of these lessons with my students. Some lessons are even calibrated to be doable in one class session. The lesson's
documentation (accessible through Ctrl-L in the interface) documents the expected learning progression.

## (Semi-)autonomous settings

The PLM is best used in a semi-autonomous setting, where the learner advance at her own pace but with someone who can answer her
question (teaching assistant or friend). We'd love if it could be used in a fully autonomous setting with all needed help
provided by the PLM itself, but we are not quite there yet.