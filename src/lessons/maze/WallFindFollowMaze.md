## Finding the walls to follow ##

This is exactly the same maze than before, but the buggle does not
start at the same location. In particular, it does not have any wall
to its left at the begining.

As a result, the method you wrote for the previous exercise
probably don't work for this one. If you click on the run button with
no modification, your buggle probably start looping over the four free
cells around its start position (if that's not the case, well, you
didn't really stick to the mission on previous exercise. Feel lucky
and move to the next one once you've read this text).

This is because your method has
an implicit *pre-condition* : it works well if and only if the
buggle has a wall to its left when you call it. Such pre-condition are
very heavily used when programming. Specifying them explicitely helps
understanding the code written by other, and they even allow sometimes
to prove that the code works correctly.

### Exercise goal ###
Fixing the problem should be very easy. Simply make sure that the
pre-condition of is verifyied before
calling it. For that, update your run() method to first look for a
wall on its left before the big loop.

