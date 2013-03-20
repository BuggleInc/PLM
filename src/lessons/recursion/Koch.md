## Snow flake ##
We will now draw snow flakes using the Koch fractal. A fractal is a
geometrical pattern repeated at every scale.

The general form is a triangle, with each side given by a serie of recursive
calls. The general form is given by something like this:     void snowFlake (int levels, double length) {
    snowSide(levels, length);
    turnRight(120);
    snowSide(levels, length);
    turnRight(120);
    snowSide(levels, length);
    turnRight(120);
    }

Observe the drawing in each world's objective to understand the pattern's
logic, and then reproduce it. You must write the method, which is recursive.

