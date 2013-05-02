
## Turmites ##

This exercise explores a new way to extend the concept of Langton's ant. Now,
the behavior of the ant not only depends on the color on the ground, but also on
its internal state (represented by an integer value). The idea of changing the
ant into such an automata naturally comes from the Turing machine concept. This
explains the name of these new animals, which is a portemanteau of *Turing* and *Termite* (if you don't know what a Turing machine is, you should run
to wikipedia, because it is simply impossible to be a real computer scientist
before that).

Once again, you just have to write the ` step()` method, in charge
of doing one turmite's step. Once again, you should first find the rank of the
current's cell ground color in the color sequence. The ` colors` array
is not passed as parameter but declared as global variable. It shouldn't change
anything for us since this array was never modified during the execution. This
trick to pass constant parameters is not very clean, but it should help
performance a bit.

The second step is to use the ` rule` data depending on the current
color and the current state. ` rule` actually contains 3 information in
each situation: the color to write, the move to do, and the next state value. For
example, rule[1][0] contains the informations to use when ` state==1` and ` color==0` . In other worlds, you can retrieve the information relative
to your current situation by using ` rule[state][currentColor]` .

Each such information set contains 3 values. The first one is the rank of the
color to write on the ground. The second is the move to do, with the following
notation: 0=stop, 1=noturn, 2=right, 4=u-turn, 8=left. Note that if the command is stop,
you shouldn't even move forward on that step (but you shouldn't stop your program
either: the next steps can do something else). Finally, the third integer is the
next ` state` value to go into after this iteration.

Since these arbitrary notations are somehow difficult to remember, the template
code defines a set of constants that you should use instead of the direct numerical
values. Their names are NOTURN, LEFT, RIGHT and so on. The modifiers ` final
static` before their type is the way to mark variables as constant in Java
(sorry if the notation seems complex). Using such constants greatly help making the
code easier to read, since 
<pre> if (rule[state][currentColor][NEXT_MOVE] == LEFT) {
turnLeft();
}</pre>
is much more easier to read than 
<pre> if (rule[x][y][1] == 2) {
turnLeft();
}</pre>

You now should have enough information to succeed.


## Bibliographical notes ##

According to wikipedia, turmites were invented independently by the end of
the eighties. It has been shown that turmites in general are exactly equivalent
in power to one-dimensional Turing machines with an infinite tape, as either can
simulate the other. This means that absolutely any program that you can think of
could theoretically be computed on this device...

