
## The dragon curve (2) ##

Previous solution induce that the turtle teleports to other location, or at
the very least, that it moves its pen up during the drawing. Indeed, the end
of the drawing of the first recursive call does not match the begining of
the second recursive call. That is why we had to use the method ` setPos()`

In this lesson, you will write a recursive method allowing to draw the
dragon curve without taking the pen up. For that, we need another recursive
method drawing the mirror side of the curve.

The method ` dragon()` is then recursively defined using itself
and ` dragonReverse()` . Likewise, the method ` dragonReverse()` is defined recursively using itself and ` dragon()` . This is thus an example of *mutual recursion* .

The prototype of the ` dragon()` method remains unchanged from
previous exercise: 
<pre> void dragon(int ordre, double x, double y, double z, double t)</pre>
The new point's coordinate (u, v) introduced by the ` dragon()` are: 
<pre> u = (x + z)/2 + (t - y)/2
v = (y + t)/2 - (z - x)/2</pre>

The prototype of the method ` dragonReverse` is similar: 
<pre> void dragonReverse(int ordre, double x, double y, double z, double t)</pre>
The new point's coordinate (u, v) introduced by the ` dragonReverse()` are: 
<pre> u = (x + z)/2 - (t - y)/2
v = (y + t)/2 + (z - x)/2</pre>

To make the work of each method recursiv more visible, the line painted by
the ` dragon()` must be red ( ` Color.red` ) while the
line painted by the ` dragonReverse()` must be blue
( ` Color.blue` ).

Have a look at each world's objective view to understand how to write the
function.

