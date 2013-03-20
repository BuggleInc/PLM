## Slug Hunting ##
After all this excitation of dance revolution, we will move on to a slower
activity : the slug hunting. Your buggle just happened to found the trail of
a slug: a green dribbling tracks. If it manage to follow it to its end, it
will find a baggle representing an appetizing slug (from a buggle point of
view).

To reach that goal, the simpler would be to have a boolean method , which would help determining whether we are facing a
green cell or not. Of course, if we are facing a wall, it should return
false. It would be great if this method could in addition preserve the state
of the calling buggle and of the world. Such a method is said to have no *side effect* .

Then, the best would be if it could take the color of the trail we want to
follow as an argument. Slugs leave green trails, but other prey leave trails
of other colors. In Java, there is a to
indicate the colors. The green color is defined by .

In order to not mistake the part of the tracks to follow with the one your
buggle comes from, you should ask your buggle to leave a tracks behind
it. Don't forget to use the method to put your
brush down and to move it up again.

A buggle can know the color of the cell it is over using the method.

Finally, do not forget to capture your prey once you found it (using ).

### Exercise goal ###

