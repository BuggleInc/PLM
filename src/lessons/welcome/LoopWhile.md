
## While loops ##

In addition to conditionals, another handy construction is the ability to
repeat an action while a specific condition does not appear. A while loop is
used for that, with the following syntax: 
<java> 
<pre> **while (condition** **) {action()** ;
}</pre>
</java> 
<python> 
<pre> **whilecondition** **:action()** </pre>
</python> 

Naturally, if the chosen action does not modify the value of the condition,
the buggle will do the action endlessly. The **stop** button of the
interface becomes then handy. To test this, you can try to type the
following code in the editor: 
<java> 
<pre> while (true) {turnLeft();
}</pre>
</java> 
<java> 
<pre> while True:turnLeft()</pre>
</java> The buggle will turn left while true is true (ie, endlessly),
or until you stop it manually using the stop button.


### Exercise goal ###

<pre> You now have to write some code so that your buggles
move forward until they encounter a wall. The idea is thus to do something
like:while we are not facing a wall, do:moveForward()</pre>

When your program works, move forward to the next exercise.

