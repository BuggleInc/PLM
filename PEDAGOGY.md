# Pedagogical considerations

The Programmer’s Learning Machine (PLM) is an interactive exerciser for learning programming and algorithms. Using an integrated
and graphical environment that provides a short feedback loop, it allows students to learn in a (semi-)autonomous way. The PLM
builds upon the idea of [microworlds](https://edutechwiki.unige.ch/en/microworld), to provide playful learning environments
where the student can freely experiment toward a clear and managable objective (i.e., to provide adidactic learning situations).
The learner programs some **entities** that evolve in their **microworld** using the entity primitives: a buggle can walk, paint
and pick up objects; a turtle draws on the ground as it moves, etc. The design rationale is given in the following paper: [The
Programmer's Learning Machine: A Teaching System To Learn Programming](https://hal.inria.fr/hal-01243646).

## Design goals

The **pedagogical stance** of the PLM is to teach concepts of Computer Science to the users. Other projects are more focused on
the fun of the experience while others provide difficult challenges that can only be solved by the best players. The PLM
provides a set of progressive exercises carefully built to take novice programmers through their learning path. We try to make
that path pleasant and to reinforce the learner's motivation, but the main goal remains to teach computer science rather than
doing a game. On the other side, we try to keep the learning path *progressive* so that nobody gets stuck on an
almost-impossible challenge. To that extend, the exercises are not isolated, but grouped in lessons that are meant to be
progressive and challenging but doable. I used many of these lessons with my students. Some lessons are even calibrated to be
doable in one class session (1h30 or 2h). The lesson's documentation (accessible through the interface) documents the expected
learning progression, for the teachers.

The PLM serves **three kinds of users**. For the **students**, it provides a consistent environment in which they progress at
their own pace, through exercises that provide a clear and specific goals. The environment provides an informative feedback
about how the student code differs from the exercise's objectives, enabling open experimentation. Every run gives an immediate
graphical and textual feedback, and the quick succession of small successes sustains the student's motivation. The exercises are
solved in general-purpose languages (Java, Python3, Scala3 or C) to ease the later transition to standard programming
environments, but the templating hides the syntactic boilerplate (such as `public static void main` in Java) so that beginners
focus on the algorithmic concepts from the start.

For the **content authors**, the PLM isolates the non-functional code (user interactions, compilation, session persistence,
sandboxed execution, translation in other natural language) so that they can focus on the pedagogical situations. The outcome of
each run is saved, and if the student opted in, the anonymized history is exported to a public repository. These learning
analytics help the authors to polish their content.

For the **teachers**, the PLM is an animated textbook: a set of ready-to-use exercises, among which they select the ones that
fit their class. The provided pedagogical material provides a rich progression from the very bases of programming (instruction,
variable, loop) to more advanced algorithmic concepts (sorting, recursion). Be warned that it's sometimes difficult to
understand how to help the students if you don't know the rich situations provided by the PLM material. Don't get fooled into
thinking that tutoring a session on the PLM is easier, and carefuly prepare the material in advance to fully understand it!

The PLM is best used in a semi-autonomous setting, where the learner advances at her own pace but with someone who can answer
her questions (teaching assistant or friend). We'd love if it could be used in a fully autonomous setting with all needed help
provided by the PLM itself, but we are not quite there yet. The mission texts and hints are thus written for students who work
on their own most of the time, relying on both the automated feedback and occasionally on a human.

## Technical concepts

- **World + Entity**: a `World` is a micro-world instance, i.e. the simulated environment encoding a pedagogical problem
  situation. It contains one or more `Entity`, which are the actors that execute the student's code against the world's
  primitives. 
- **Universe**: a micro-world *kind*, i.e. a family of worlds/entities sharing a theme and a set of primitives (e.g. "a buggle
  can walk, paint, pick up objects" or "a turtle draws on the ground as it moves").

- **Exercise**: the unit of work a student solves. An `Exercise` is a programming challenge aiming at teaching or exercising a
  given concept. A given exercise uses exactly *one* universe. 
- **Lesson**: a `Lesson` groups `Exercise`s in a pedagogical sequence. Each of them constitute a progressive set of challenges
  taking the users through their learning path.

See the CONTRIBUTING.md document for more technical details.

## Universes

The PLM provides several **universes** that are *kinds* of microworlds, each adapted to a specific set of pedagogical
challenges. Some universes are generic and adapted to a large set of situations while others are specific to a single exercise.
Some are classical in CS classes (e.g. the sorting universe), others are more playful but still adapted to programming exercises
(e.g. the buggles or the turtles) and others are only recreative (e.g. lightbot and lander). Almost all of the universes can be
solved in either Java, Scala3, Python3 or C. Only the brain teaser `lightbot` is solved through a graphical interface with no
programming language.

Here are the provided generic universes. The specific ones are introduced below, along with the lesson they serve.

  - `bugglequest`: generic grid actor (buggles), with the richest primitive set. It is the main PLM microworld and its
    implementation is the reference. It is used to teach the basics about instructions, conditionals, variables and loops, and
    to introduce functions, problem decomposition, but also maze algorithms and other concepts.
  - `turtles`: LOGO-style turtle graphics, used to draw polygons, spirals and trees, and to teach recursion through the drawing
    of fractals. This is probably the ancestor of all microworlds as it builds upon the ideas of Seymour Papert in the late
    sixties.
  - `bat`: unit-testing style world with no graphics but a textual output, inspired by [CodingBat](https://codingbat.com/). A
    method prototype is filled in and tested against many parameter values. These exercises are often more scolar and less
    playful than the others, but they remain useful in many pedagogical progressions.
    
The Buggle universe is more used in our exercises than the turtles, because they offer richer interactions between the entity
and its world (more primitives), allowing diverse learning situations. The buggle universe was invented by Lyn Turbak, at
Wellesley College. It draws some ideas of the Karel microworld that is classical in the literature.

## Pedagogical tools

The PLM provides many pedagogical tools to ease the writing of "good" exercises, beyond the ideas of graphical feedback and
adidactic situations based on microwords that are the core of the PLM.

### Testing the student code

An exercise comes with a set of worlds and entities that act as **test cases** for the student code.  Each provided world must
pass for the exercise to be validated. This is thus comparable to parametrized unit tests: Adding another world instance to an
exercise, without touching the student-facing code, is the standard way to catch a wider range of incorrect solutions. Depending
on the situation, some exercises provide several entities in a single world so that the student can easily compare graphically
the situations to understand their errors (see for example the welcome.Moria or welcome.Variables exercises). Other exercises
provide several worlds for a better separation between the test cases, and sometimes also for a surprise effect where the other
worlds force the student to take more cases into consideration. In the `welcome.Poucet1` exercise for example, two buggles are
lost in similar but different mazes and must find their way our. The `welcome.SlugSnail` is another example of such exercise.

The **winning condition** is defined in the universe. Most universes enforce that the final state of the microworld is the same
when running the student code or the correction. Some universes also try to enforce that the same algorithm was used by
exploring the history of used primitives during the execution. But the recreative universe "Lunar lander" only checks that the
lander successfuly landed somewhere, without enforcing where it landed nor how it got there. Some exercises add extra checks to
the universe's winning condition by extending the provided entity template.

**API gating**: Some universe's primitives can be forbidden in a given exercise, to enforce that the student take a specific
approach. For example, the exercises on loops forbid the use of the `forward(n)` primitive that does `n` steps, to ensure that
the student use `n` times the primitive `step()` that does a single step. This gating can be done at the source level for very
specific situations. The `MethodsDogHouse` exercise uses this to enforce that the student wrote `left()` only once in their code
(typically in a loop), rather than unrolling the loop to "cheat the exercise".

### Scafolding tools

The **templating mechanism hides the chore boilerplate** (e.g. `public static void main(String[] args)` in Java) from the
students. That is to say that the students are asked to write a chunk of code that is then injected into an hidden templating
text to form a compilable source code (TODO: add a link to CONTRIBUTING).

**Exercise-specific primitives** can be added to help guide the student toward a given situation. If your problem can be
decomposed in several functions, you can ask on of them while providing the others in a given exercise, and then ask for the
other functions in subsequent exercises.

## Why should I learn programming when I can vibe-code?

I teach programming in various languages since 1999. It's now over 25 years that I'm told that programmers are soon obsolete and
replaced by machines. The approach based on natural languages promised to obsolete the classical programing languages, and
replace the act of programming with a simple discussion in plain English with the machine. The no-code approach promised to
replace textual programming with graphical interfaces that would be accessible to novices. None of this did happen yet.

The LLM systems are truly impressive and you can now produce much more software through vibe-coding than you could with no-code
tools back then. I do use these tools as an assistant to maintain the PLM itself, so I think I'm well aware of their potential
and limits (pull requests that are vibecoded but not understood by their contributor will be rejected. I'm interested in the
long-term maintainability of the project). But this technology is still very young and I'm usure of what will remain in 10
years. Some say that the energy bill of an LLM is so much higer than the pay check of an engineer that the bubble may not last.
Some say that the hype about vibe coding may decrease after some catastrophic failures involving vibe-coded softwares made in
autonomy by LLMs without the surveillance of a qualified human. Adding the potential legal costs to the energy bill is another
argument to say that the future of programmers as a profession is maybe not as gloomy as it currently seems.

That is not to say that programming must be reserved to an elite, or that LLM are simply an hype that will fade. The PLM is
intended to teach the basics of programming in an attractive way, to ensure that the users become eventually advanced
programmers, able of fruitful interactions with any programming tools (LLM and others).

The PLM does not replace a degree in computer science either, but rather come as a support for some practical sessions. A
curriculum in informatics must expose the students to some projects don't fit well in the PLM, because they are larger and less
guided than our teaching material. The PLM does not aim at being the silver bullet of the computer science education.

Finally, I hope that some users will actually find the PLM fun, just like a sudoku game with little thinking challenges, but
eventually teaching some things along the way.

# Learning material

The material is decomposed in about 15 lessons, each providing a coherent learning path. There is four groups of lessons,
depending on their pedagogical objectives.

## Tactical programming

### The welcome lesson

This lesson is long: it takes an absolute beginner about 10 to 20 hours. It covers what we call *tactical programming*: the
imperative kernel of the language, mastered well enough for the syntax to stop being an obstacle. It goes from the first
instructions to conditionals, loops, variables and functions. Some open-ended applications complement this introduction, so that
the learner can get used to the tactical programming while thinking about various problems on booleans  and arrays. Most of
these exercises build upon the Buggle world, with some exercises built upon the Bat world.

We used this material quite extensively around 2010 as a support for students entering an engineering school of informatics with
little to no previous exposure to programming. They were given one week of tutored work to solve the whole lesson, with two
teachers answering the questions of about 100 students (and fixing the PLM bugs that emerged during the sessions).

From a technical point of view, the `welcome` lesson is composed of 96 exercises, requiring the students to write 1277 Java
lines, 1037 Python lines, 1112 Scala lines or 1263 C lines. These counts are the amount of lines that are removed from the
correction by the templating mechanism and that the student is expected to write. Some languages are more verbose than others,
and no specific effort were made to ensure the concisness of the solutions.

The lesson is split in several chapters, as follows:

  * *Instructions, conditionals and while loops (6 exercises, 48/41/48/51 lines in Java/Python/Scala/C):* from a single
    `forward()` in `Environment`, that introduces the World and Objective views, to drawing a letter, testing the ground, and
    looking for a baggle.
  * *Variables (5 exercises, 131/116/78/112 lines):* counting steps in races, then the Buggle Dance Revolution, where the buggle
    reads its instructions on the ground. It introduces the `switch` statement, or pattern matching in Scala.
  * *For and do/while loops (7 exercises, 77/78/82/84 lines):* `LoopFor` forbids `forward(n)` so that the loop is written
    explicitly, and the Tom Thumb exercises (`Poucet`) use the do/while construct (or its idiomatic equivalent) to find the exit
    of a maze following hints on the ground.
  * *Methods (15 exercises, 497/390/460/495 lines):* functional decomposition through drawings. `MethodsDogHouse` refuses the
    code that does not factor its repetitions in a method, the `*Picture*` exercises request patterns of increasing size, so that
    the student see the point of decomposing the problem to factorize their code. Then come the methods with parameters and the
    ones returning a value (slug tracking and hunting) and the methods with parameters, used to draw even larger pictures.
  * *Boolean fun (40 exercises, 153/143/147/160 lines):* two series of short exercises adapted from CodingBat, in the bat
    universe, to train the writing of boolean expressions and conditionals. To be honnest, these exercises are not really fun
    but rather repetitive.
  * *Arrays (17 exercises, 191/136/129/193 lines):* the knitting exercises introduce arrays and modulos in the buggle world,
    then come classical traversals in the bat universe (search, count, average, extrema). Two application problems conclude this
    chapter: the Golomb sequence and the Island problem.
  * *Traversals (5 exercises, 159/116/149/149 lines):* traversing the whole world by columns, lines, zig-zag and diagonals. The
    moving primitives are forbidden, so that the students compute the coordinates of each cell and teleport with `setPos()`.
  * *Moria (1 exercise, 21/17/19/19 lines):* a summative exercise where the buggle clears its way out of a mine.


### The maze lesson

This lesson is seens as an application of tactical programming, introducing simple maze algorithm. The learning objectives are
less specific and we just aim at giving some programming experience to the learners.

This lesson, based on the Buggle world, provides 6 exercises, requiring the students to write 244/190/218/232 lines of either
Java/Python/Scala/C. These exercises cover the following  maze algorithms: the random mouse, the wall follower, the Pledge
algorithm, the islands that defeat the wall follower and the shortest path. Teleportation is forbidden in every exercise.

### The turmite lesson

This lesson is about a computer recreations of the same name. [Turmites](https://en.wikipedia.org/wiki/Turmite) are 2D Turing
machines, which is nice and graphical. Again, the learning objectives are only about getting some programming experience while
increasing the general knowledge of the students through this recreative example. A simple application of loops and arrays that
opens on a fascinating world, the last exercise letting the students build their own turmites.

This lesson is based on a specific subclass of the Buggle world, specificaly tailored to this usage. It is composed of 4
exercises, requesting the student to write 85/59/55/104 lines of either Java/Python/Scala/C. The Langton's ant is first
introduced before its multicolor generalization, then turmites with an internal state, that are 2D Turing machines.

TODO:
* The turmite creator exercise is a bit harsh, and the patterns could be introduced in a friendlier way. 
* An exercise on the busy beaver would be interesting, but I fail to think of any adidactic situation about this.

### The string lesson

This short lesson (9 exercises, requiring 84/55/53/91 lines) is a set of Bat exercises about string concatenations and
manipulation adapted from CodingBat. It is not very fun to play and its learning objectives are not very clear. It should
probably be redone in another way.

## Sort algorithms

This topic is served by 4 lessons, each coming with a specific microworld. 

### Basic sorting

This is the main lesson on this algorithmic topic. It is composed of 12 exercises, requesting the students to write
162/131/160/173 lines in either Java/Python/Scala/C. 

It is served by a specific `Sort` universe. The primitives (`isSmaller`, `copy`, `swap`) observe the data access patterns so the
student must reproduce the *expected algorithm*, not just produce a correctly sorted array. To make this efficient, the
student-facing API is instrumented to count the amount of reads and writes to the data array, rather than to record the complete
list of operations. This is not perfect as a student may manage to get the data sorted with the exact same amount of operations
without following the expected algorithm, but it's rather unlikely and a perfect verification of the operations history would
probably be too computationally intensive. Besides the array itself, the universe displays a time-space diagram (in the style of
Aldo Cortesi's sorting visualizations) where each line follows a value of the array and crossings denote the swaps, which helps
the students to understand and debug the behavior of their algorithm. A demo mode runs the algorithms side by side.

The lesson covers several in-place algorithms: bubble sort and its optimizations, cocktail, comb (and comb11), gnome, insertion,
Shell and selection sorts. The number of reads and writes must match the expected algorithm. Even without coding, the demo mode
lets the students race the algorithms to experiment what the asymptotic complexity means in practice.

This lesson was used extensively during practical sessions. It is not perfect, as it may well happen that the students implement
correctly the expected algorithm, but for the initialization or finalization phase of the algorithm, introducing test failures
that are very hard to debug. These false negative are very frustrating for the students. A more precise detection algorithm
would be good, as we did for the Turtle world used in the recursion lesson, but it is still to be devised.

TODO:
* Other graphical representations, such as the ones of [sorting.at](http://sorting.at/).
* Exercises on the recursive sorting algorithms (QuickSort, MergeSort).
* Maybe other microworlds applying the sorting algorithms to other contexts.

### The baseball lesson

This lesson implements the Rainbow Baseball, an unplugged activity turned into a [pebble motion
problem](https://en.wikipedia.org/wiki/Pebble_motion_problems) where the players move one at a time to the free position. This
lesson thus constitutes a bridge between the unplugged activities that I use in my teachings and programming on computers. It
proposes 4 exercises (requesting 133/94/123/148 lines in either Java/Python/Scala/C): The naive algorithm, then selection,
insertion and bubble sorts applied to this setting. The initial situations of the first exercise are generated to avoid the
cases where the naive algorithm would loop.

TODO: 
* other graphical representations, linear for the existing exercises, or a grid or a tree if exercises on such graphs prove
  interesting. 
* Other algorithms on this variant variant of the pebble motion problem, or other variants such as the 15-puzzle.


### The pancake lesson

This lesson implements the [pancake sorting](https://en.wikipedia.org/wiki/Pancake_sorting), also known as prefix reversal
sorting problem. It consists in sorting a stack of elements by flipping the top of the stack. This is another problem that I
extensively use in my unplugged activities. The lesson is composed of 5 exercises, requesting 347/289/322/353 lines of either
Java/Python/Scala/C: the naive algorithm to Gates' and to Cohen's algorithm on burnt pancakes. It comes with its own specific
universe.

TODO:
* This universe does not provide a temporal view similar to the one of the sorting universe yet
* An exercise on the [identical pancake stack](https://en.wikipedia.org/wiki/Pancake_sorting#The_identical_pancake_stack) (Roti
  sorting), where each Roti must touch the grill on both faces, could be interesting.

### The Dutch flag lesson

This short lesson is composed of a single exercise (requesting 21/16/19/20 lines of code). It introduces the [Dutch national
flag problem](https://en.wikipedia.org/wiki/Dutch_national_flag_problem), a classical example (first introduced by Dijkstra)
whose complexity and proof are neither trivial nor too hard. It is a linear sort of an array containing three distinct values.
It comes with its own specific universe.

TODO: 
* The corresponding universe could be given a temporal view
* More exercises could be added to cover the variants with two or more than three colors, and a recursive solution.

## Recursion

Three lessons serve this algorithmic topic, through recursive lists, turtle fractals and the variations of the Towers of Hanoi.

### Recursive lists

This lesson contains 17 classical exercises (86/77/75/86 lines) on recursive lists (using the
[cons](https://en.wikipedia.org/wiki/Cons), [car and cdr](https://en.wikipedia.org/wiki/CAR_and_CDR) constructs of LISP):
length, membership, nth element, reverse, concatenation, etc. It is not the most pleasant lesson, but probably the easiest
introduction to recursion. We used to use it as a first practical on the topic (I don't teach recursion anymore).

TODO:
* New exercises, such as the ones of [this sheet](http://webloria.loria.fr/~quinson/Teaching/TOP/02-td-recursivite-enonce.pdf).
* Checking the asymptotic complexity of the student code would be valuable but difficult: counting the `cons` is possible with
  the hand-made `RecList` of Java and Python but hard to relate to the right test case. This is not possible  with the native
  `::` of Scala. Bounding the depth of the call stack is another lead.

### Turtle fractals

The `recursion/lego` lesson provides 13 exercises (159/114/150/161 lines), kinda suited for a practical session (2h).

The main learning objective of the initial exercises (from squares and spirals to trees) is about recursive decomposition of the
problem. To that extend, the tree exercise makes the recursive depth visible: lines drawn at depth 0 are depicted in cyan while
the lines drawn at depth 1 are in blue, etc. This helps the student understanding the right decomposition.

The subsequent exercises are classical applications: Koch snowflakes, Sierpinski triangles and dragon curves. This section is a
bit long to fit in the practical section, but it's ok to have some optional exercises, isn't it? The `SpiralUse` exercise has
nothing to write: it is a playground to explore the function written just before.

The code ensuring the winning condition of these exercises tries to normalize the drawings to reduce false positives, where the
student's solution is classified as wrong just because of rounding errors due to the order of drawings. Aligned or overlaping
segments are merged, while dupplicated ones are removed.

TODO:
* the Sierpinski triangle with a simpler L-system (http://jpvallon.free.fr/recursivite.html)
* The Sierpinski curve (http://www.mathcurve.com/fractals/sierpinski/sierpinskitriangle.shtml)
* (http://ecademy.agnesscott.edu/~lriddle/ifs/heighway/heighway.htm)
* The arrowhead curve
* the dragon curve as an L-system (http://ecademy.agnesscott.edu/~lriddle/ifs/heighway/heighway.htm)
* the dragon2 exercise is called "twin dragon" (http://ecademy.agnesscott.edu/~lriddle/ifs/heighway/twindragon.htm)
* the space-filling curves (Peano, Hilbert -- http://teachout1.net/village/fill.html)
* nested polygons (http://www.inrialpes.fr/helix/people/genoud/ENSJAVA/tds/sujets/dessinsReccursifs/actionsrecursives.html).

### Hanoi variations

The `recursion/hanoi` lesson (12 exercises, 301/188/228/265 lines) comes with a rich set of exercises on recursive problem
decomposition. In addition of the  classical exercise, it uses variations from the literature (interleaved, split, linear,
linear twin, tricolor and cyclic towers) to let the students decompose each new problem into calls to the solutions of the
previous ones. The movement restrictions of the linear and cyclic variants are enforced by API gating. The lesson ends with the
iterative algorithm.

## Recreative lessons

* *`turtleart` (24 exercises, 378/345/368/360 lines):* a gallery of classical LOGO figures. The syntax of the language is not
  presented, so this lesson suits beginners guided by someone, or students who already know a bit of a language. TODOs: more
  figures could be taken from collections of LOGO challenges such as [neoparaiso](http://neoparaiso.com/logo/#sect4) or [Bill
  Kerr's 40 shapes](http://billkerr2.blogspot.fr/2009/08/40-maths-shapes-challenges.html). The `arc()` and `arcTo()` primitives
  are missing.
* *`lightbot` (12 exercises):* a brain teaser where the program is a list of blocks, with functions and recursion. No
  programming language is involved: that's definitively more of a brain teaser than a formal assignment.
* *`lander` (3 exercises, 33/34/37/40 lines):* programming challenge where the learner must pilot a lunar lander. This is first
  done by controlling the thrust only, then by finding a flat landing zone, and finally by controlling both the thrust and the
  angle. TODO: further exercises with a difficult initial angle and speed, then with a difficult ground, and maybe with wind.

## Prospective lessons

* *`backtracking`:* not offered yet. It is meant to progressively build the notions and the mental representation of
  backtracking, starting with the knapsack problem, that could be introduced by an unplugged discovery activity such as [this
  one](http://interstices.info/jcms/c_19213/le-probleme-du-sac-a-dos). A generic solving entity (`BacktrackingEntity`) works on
  passive partial solutions (`BacktrackingPartialSolution`, for the best known solution and the current one), so that other
  problems (pyramid, recipients, Euler's knight tour) should be easy to add. The main missing part is the visualization of the
  call graph, that would help the students to build their mental representation of recursion. Drawing the graph is easy, but
  rebuilding the call tree requires the parameters of each call, which the static stack trace does not give. The debugging
  interface (JDI) and the instrumentation of the student code (e.g. with ASM) are the leads to explore.

# Extending the learning material

## Designing exercises

New exercises should build upon the concepts introduced by the previous ones, and the lessons should remain doable within a few
class sessions. An exercise should be provided in every supported programming language, so that the students are not forced to
switch languages in the middle of their learning path. The technical aspects of writing a lesson, an exercise or a universe are
covered in the [contributor's guide](CONTRIBUTING.md#Extending_the_PLM).

Some exercises hide or alter a usual primitive so that the students practice the target concept instead of taking a shortcut,
which we call *API gating*. For example:
- In `LoopFor`, `forward(n)` is refused, so that the student writes the loop that calls `step()` n times.
- In the maze exercises, `setX()`, `setY()` and `setPos()` are forbidden, so that the student has to walk to the goal.
- In `CyclicHanoi`, `move()` is replaced by a stricter version that only accepts the cyclic moves.
- In `MethodsDogHouse`, the code must call `left()` exactly once, so that the student factors the repeated code instead of
  copying it.

The [contributor's guide](CONTRIBUTING.md#API_gating_restricting_or_adapting_the_primitives) explains how to implement it.
