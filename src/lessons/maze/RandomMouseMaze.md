
## The crazy mouse ##

The day of your buggle starts badly. Out of luck, it got trapped into a
maze. Help it finding its path out of there.

Since the maze is so small, we can write the dumbest possible algorithm to
do so. It relies on randomness and proves quite inefficient.

While the buggle didn't find the path to the escape, it must proceed the
following way: if it is at an intersection, it must take a random decision
(moving forward if possible, turn left or turn right). If there is no
intersection, it must move forward if possible, and turn randomly if not.


### Exercise goal ###

The body of the ` This exercise's objective is to write an algorithm
allowing the buggle to find its path out of the maze. Do not forget to let
your buggle pick up the baggle once it found the way out.void run()` method is provided. You are asked to
implement the other methods that your buggle still miss.

Write the ` void turnRandomly()` method that your buggle uses to
turn randomly to the right or to the left. You can use the ` random2()` method that returns 0 or 1 randomly.

Write the ` void takeRandomDirection()` method so that your buggle
takes a random decision (forward, turn left or turn right). You can use the ` random3()` method which returns randomly either 0, 1 or 2.

Write the ` boolean atAJunction()` method, indicating whether your
buggle is at an intersection (ie, if it can turn right or left).

You're up.

