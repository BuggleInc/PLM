
## Snake World ##
We will now teach the buggle to explore its world. Its initial position is
the bottom left corner, and it should visit any cells up to the top
(coloring the ground on its path. The main loop of the ` run()` method (that you should write) is something like: 
<pre> move brush down
while we did not reach the final position
move like a snake</pre>
The prototype of this method (its first line) must be: 
<pre> public void run()</pre>
(we will come back later on the meaning of ` public` ). We thus
have to write two methods in addition to ` run()` . The former
returns a boolean indicating whether we are on a final position while the
latter does not return any result and move one snake step forward.

We reached the final position if and only if:   
  
We are facing a wall  
` We can get the current heading of the buggle using thegetDirection()` ` , and we know whether it looks east usinggetDirection() == Direction.EAST` For the checking itself, nothing magical: you have to turn the buggle and
check whether it is facing a wall afterward.  

Then, a snake step can be achieved by moving one step forward if we are not
facing a wall, and moving to the upper line else (ie, if you look to the
west facing a wall, you have to turn right, forward and turn right).

Hint: the main loop of the ` run()` method must continue while the
testing function returns false. Their is thus two way of writing it: 
<pre> while (testingFunction() == false)</pre>
or 
<pre> while (! testingFunction())</pre>
It works because the exclamation mark (!) means in Java a boolean negation.

Your turn...

