## Traversal by column ##
The goal of this serie of exercises is to let the buggle traverse its
world. It must number the cells it walks on to show its traversal order.

The main loop of the method (that you must write) is
something like:     while we are not on the final position
    go to the next position
    label the cell with its number

In contrary to the exercises we saw so far, we won't use the , and similar
methods. Instead, we will compute the coordinate of the next buggle position
and use the method to the
buggle directly to this position. For example, teleports the buggle to the cell where x=3 and y=5.

Your first task is thus to write a boolean function indicating whether the
buggle the final position or not, ie if it reached the bottom right corner
of the world. For this, you can use and which return respectively the world's width
and height. Your test is about comparing the buggle's current position (that
you can access with and ) to the
world dimensions. Beware, the first line and column are numbered 0 and not 1...

Then, you have to write the code to reach the next position. In this
exercise, you have to traverse the world row after row. So, if you are at
the bottom of a row, you have to move to the top of next row, and you have
to move to the cell below else.

At this point, you can launch your program to check that the buggle
correctly traverse the world in the expected order, and that it stops when
it has to. Use the *stop* button if the buggle does not stop correctly.

It is now time to write done the cell numbers. For that, you will need a
counter initialiser to zero at the begining of your method, and incremented by one at each step (for example with ). Then, you have to write the value on the ground,
for example with .

You probably need to write the first or last value out of the main loop,
depending on whether you prefer to use a or a ...

Your turn...

