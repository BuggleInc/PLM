
## Pledge algorithm ##

Once again, you thought that your algorithm were good enough to
escape the maze, and once again, you buggle is now in a maze where
your previous algorithm fails. Just give it a try: hit the "Run"
button and see your creation fail. The trap is shaped like an upper
case "G". The buggle enters the trap and follows the inner border. At
some point, it finds the north direction free, run into that
direction, and falls again in the trap.

The Pledge's algorithm (named after Jon Pledge of Exeter) can solve
this maze.

This algorithm is a modification of the previous one thought to avoid
obstacles. It randomly picks a heading and let the buggle move in that
direction. When it encounters an obstacle, a paw (for example the left one)
is kept on the wall following the obstacle while counting the turns. When
the buggle is back to its original heading and when the sum of the turns is
0, the buggle leaves the obstacle and continues keeping its original
heading.

Note that the use of "total turning" rather than just the "current
direction" allows the algorithm to avoid G-shapped traps. If one
proceeds left into the trap, one gets turned around a full 360
degrees by the walls. As we said before, the naive "current direction"
algorithm gets into a limit cycle as it leaves the lower rightmost
wall heading left and runs into the curved section on the left again.

The Pledge's algorithm does not leave the rightmost wall due to the total
turning not being zero at that point. It follows the wall all the way
around, finally leaving it heading left on the bottom outside


### Exercise goal ###

[]() You now have to modify your solution to implement the Pledge
algorithm to escape this maze.

Change your ` void keepHandOnSideWall()` method to count
the amount of turns done by the buggle (+1 when it turns left, and -1
when it turns right). This counting may require the addition of an ` angleSum` integer value in your program.

Write a ` boolean isChosenDirectionFree()` method indicating if
the arbitrary direction you picked up is free, ie, if you can move in
that direction. (Note that the demo uses the NORTH direction for
that).  You can retrieve the current direction of the
buggle using the ` Direction getDirection()` method. You can
change your direction (without moving) using ` void
setDirection(Direction d)` . Don't forget to store the previous
direction of your buggle (in a variable of type Direction) before checking if your favorite
direction is free in order to restore your state afterward.

You may have to change the rest of your code also, but these
changes should remain limited.

  
  
` Thevoid run()` ` method should move in your favorite
direction (NORTH is advised). Then, you should write the algorithm main
loop. In other words, while your buggle did not find its biscuit, you have
to move forward until next obstacle in the favorite direction. Then, put a
paw on a wall (using (void keepHandOnSideWall()` ) while the sum
of turns is not null and the favorite direction is not free. Do that until
you find your baggle.

