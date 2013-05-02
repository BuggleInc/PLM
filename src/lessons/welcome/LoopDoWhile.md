
## Do .. while loops ##

<python>

Warning, this lesson is currently written in Java only, and deals with a notion that does not exist in Python. Forget about it

</python> 

In a while loop, the condition is evaluated before anything else,
and if it's false, the loop body is never evaluated. Sometimes (although not
that often), you would prefer the
loop body to get evaluated at least once, even if the condition is initially
false. For that, a variation of the while loop gets used, using the
following syntax in Java: 
<pre> **do {action()** **; } while (condition** );</pre>

### Exercise goal ###

The general idea is to do something like: 
<pre> Some cells of the world are yellow, but your buggle
cannot stand being in such cells as it is right now. Write the code needed
to move forward until the ground gets white. You can use for that the
isGroundWhite() method, that only the buggle of this exercise knows.move forward until located in a white cell</pre>

*Remark:* it is also possible to solve this exercise with a classical while loop, but it's not the goal.

