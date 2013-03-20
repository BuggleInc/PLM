## Dragon curve (1) ##

The dragon curve is a classical example of recursive method.

The definition of this curve is the following: the dragon curve of order 1 is a vector between to arbitrary points P and Q, the dragon curve of order n is the dragon curve of order n-1 between P and
R, followed by the same curve of order n-1 between R and Q (reverse side),
where PRQ is an isoscele triangle with angle R being a right angle, and R
being at the right of the PQ vector. Thus, if P and Q coordinates are (x, y)
and (z, t), the coordinate (u, v) of R are given by:     u = (x + z)/2 + (t - y)/2
    v = (y + t)/2 - (z - x)/2

The prototype of the method drawing the curve is the following:     void dragon(int ordre, double x, double y, double z, double t)

You should use the method to put your turtle at
coordinates (x,y) and the method to draw a line
between the turtle position and the point (z,t).

Have a look at each world's objective view to understand how to write the
function.

