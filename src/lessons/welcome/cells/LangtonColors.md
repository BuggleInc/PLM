## Multicolor Langton's ant ##

There is several ways to extend the concept of Langton's ant. In this exercise,
we explore first one, using more than two colors. It remains very similar to the
base case: the behavior at each step still depends on the ground color, but you
have more than 2 possibilities. It allows to have more than one kind of ant,
depending on what you decide to do for each color. For example, the ant LRL takes
3 colors. It turns left on the first color, right on the second one and left
on the third color. According to this definition, the basic ant is a RL (since it
turns right on white cells and left on black ones).

Some of these ants draw fascinating patterns (switch the world to see them):
LLRR build a symmetric figure resembling loosely to a ball, LRRRRRLLR draws a
square, LLRRRLRLRLLR draws a convoluted regular pattern after a period of
seemingly chaotic behavior, and RRLLLRLLLRRR seems to fill a hour glass...

Changing your buggle into a generic Longton's ant is not very complicated,
although it is not completely trivial. As previously, you have to write a function. But this time, it receives two arrays as parameters.
The first one defines the rules to follow depending on the ground color while the
second one gives the sequence of colors to use. For example, the basic ant would
have and as arguments.

At each step, you thus have to apply the following pseudo-code:

*    Find the position of the ground color in the color sequence;
*    Turn left or right depending on the content of the rule array at that position;
*    Mark the current ground with the next color in the sequence (the last color being followed by the first one);
*    Move forward by one step.

You now should have enough information to succeed.

## Bibliographical notes ##

According to wikipedia, multicolor Langton's ants were discovered in 1995 by Propp
et Al. Another funny fact is that the ants which name is a list of consecutive pair of
identical letters (LL and RR) produce symmetric patterns. This fact was even formally
proved.

