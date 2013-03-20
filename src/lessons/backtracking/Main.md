# Backtracking lesson #

This *experimental* lesson aims at teaching students about
backtracking. It is not ready for consumtion yet, not even the first
exercises. It will feature several exercices on the topic allowing to
progressively build the relevant notions and mental representation in
the students mind.

For now, it contains a Backtracking exercise which lacks a graphical
representation to be usable. It is based on an entity KnapsackSolver
which works on a KnapsackPartialSolution. This latter is not a World,
but a passive world component. It is used by the solver to store the
currently best solution and the working solution. This design should
be rather generic and other backtracking problems, such as the ones
used in my TOP teaching (pyramid, recipients) should be implmentable
following this design. That is why Backtracking classes derive from
generic ones: BacktrackingEntity and BacktrackingPartialSolution.
There is also a BacktrackingWorld, that specific universes should need
to not override, and BacktrackingExercise, dealing with this
specificity where the world does not only contain solving entities,
but also two partial solutions (best know and current).

This exercise should be introduced by an interactive discovery
activity such as the one used here:
http://interstices.info/jcms/c_19213/le-probleme-du-sac-a-dos

Although this exercise seems almost usable, there is a fundamental
difficulty to solve in the visualization. It is envisionned that the
call graph is used here, to help the students building their mental
representation of recursion. The actual representation of the graph
should be quite easy, thanks to the jung library. But I fail to see
how to get the needed info so far. Adding sensors to the stepUI()
method is probably the best way to go, but I would need to inspect the
complete call stack where Thread.currentThread().getStack() only give
the static stack (method called, file location, etc). I think I need
to inspect the parameters passed to each method call to rebuild the
proper call tree.

The first thing that I tried to use was dtrace, but it seems
non-portable and somehow linked to solaris so I didn't dig any
further.

Then, I tried to use the debugging infrastructure (JPDA), but never
managed to get it working. It seems to me that the original
com.sun.jdi package is somehow deprecated since
com.sun.jdi.BootStrap.virtualMachineManager() returns null while
org.eclipse.jdi.Bootstrap.virtualMachineManager() does not. But when
I'm using the eclipse version, it seem to take an endless amount of
dependencies, which I'm not inclined to do. The root of my problem may
be that I was using a jdi.jar comming from eclipse, but I didn' find
any other. I just realised that the com.sun.jdi package is also
implemented in /usr/lib/jvm/java-6-sun-1.6.0.26/lib/tools.jar on my
disk. It seem to be functional, I should give it another spin.

Afterward, I tried to reuse the debugging infrastructure of DrJava. It
looks like a good idea because they have several helper interfaces for
debugging and compiling. Also, they have a good editor that we could
reuse. Finally, they have a strong testing infrastructure with junit
ensuring that their tool still work after modifications (that's
something we are seriously missing in JLM). I'm really thinking that
the two tools should converge to something stronger. The only argument
for not doing so (beside the amount of work it'll take) is that each
of us get the credit for each tool where things would be more fuzzy on
a mixed tool. But I don't care, the mixed tool would be so much cooler
that I'd like to find the time to ensure this convergence.

But the debugging feature of DrJava seems to suffer from an issue:
http://sourceforge.net/tracker/index.php?func=detail =3004294 _id=44253 =438935
I was suspecting a permission error (something related to
com.sun.jdi.JDIPermission), but even with a
java.security.AllPermission as permission file, I still have the issue.

Another lead to get it working it to use lib ASM to modify the student
code so that their recursive method gets traced. With the static
backtrace and the tracing information, I guess I could rebuild the
actual call tree. That seem to be possible: 2 sec of googling gave me
something like http://rejeev.blogspot.com/2009/04/method-tracing.html

So, here I am. The lesson would be very interesting to students, and
quite easy to finish once I manage to get the information I need, but
I didn't manage to do so so far, despite my efforts. If you have any
hint (or patch!), please email martin.quinson#loria.fr.

This gives me the following todo actions:
* Check whether tools.jar gives a working com.sun.jdi package
* Find the DrJava bug around debugging to help them, and so that I can
steal parts of their code about it for JLM (it's BSD'ed while JLM is
mainly GPL'ed for now -- I'll have to ask them for an exception). That
would be better since their helper interface seem to be able to deal
with several debuggers (eclipse, sun or openjdk). That's quite a large
amount of work I'd like to avoid dupplicating.
* Check wheter I can get tracing info from ASM. It may be more
robust to JVM variants than the debugging approach. On the other hand,
debugging is a neat feature for JLM as is.

* Work on the convergence of JLM and DrJava. Beside of the licencing
issue, it will also complicate the ongoing integration of JLM within
Debian, since DrJava is composed of [[5 separated
modules|http://drjava.org/docs/developer/ch02s03.html]] that can only
be integrated as separated source packages. As every java package, no
source archive is distributed, and they must be retrieved directly
from the svn. Finally, they are quite huge, with sloccount reporting

