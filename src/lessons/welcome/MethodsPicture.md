## Methodically drawing ##
In this exercise, we will reproduce the geometric drawing that you can see
in the "Objective" tab.

Your goal (here and in any well written program) is to write the simplest
possible . For that, you have to decompose your work in
sub-steps, and write a specific method for each sub-step.

If you observe carefully the picture to draw, it is constituted of four
parts depicting a sort of V using a different color. A possible
decomposition is to write a method in charge of drawing a V of the specified
color from the current position. Its prototype can be:     void makeV(Color c)

The data type naturally describes a particular
color. Your method should probably call with the following arguments (a different color for each
call): *    Color.yellow
*    Color.red
*    Color.blue
*    Color.green

In , you should use the method (predefined in the buggle) to change the color of the buggle's brush,
as well as and to change the
brush position.

It may be wise to write the so that it places directly
the buggle in position for the next V.

Your turn. The method should not be longer than 4
lines...

