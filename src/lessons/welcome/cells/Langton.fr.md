## La fourmi de Langton ##

In this exercise, you will turn your buggle into a . These artificial little animals are very interesting because they
are given simple rules that depend only on their local environment, and
after a period of apparent chaotic behavior, a general pattern .

The rules are absolutely trivial: to compute what the next step should be,
you should check the current color of the ground (using ). If it's white, change it to black, turn
right and move forward by one cell. If the ground is currently black, change
it to white, turn left and move forward by one cell.

It's hard to come up with simpler rules isn't it? Well, let's go and code it
now. You have to complete the method, which encodes the
behavior of the ant at each step. You will probably use the method to retrieve the color of the cell on
which the ant is currently. To compare colors, you cannot use the equal sign
(=), because these things are not scalar values but objects. Instead, you
need to write something like the following:

    Color c /* = some initialization */;
    if (c.equals(Color.black)) {
    /* that's equal */
    } else {
    /* that was not equal */
    }

Changing the ground color is not difficult, but a bit long: you have to
change the brush color of your buggle, set the brush down (to mark the
current cell -- with ), and set the brush back up
(with ) to avoid further issues when the buggle will
move. You are naturally free of organizing your code the way you want, but
you may want to write a method to
factorize things a bit.

## More information on Langton's ant ##

As you can see from the execution of this exercise, the interest in this
algorithm is that after about 10000 steps of relative chaotic behavior, the
ant start building a regular pattern. This emergence of a regular pattern
from the chaos is rather fascinating, isn't it?

This mechanism were invented in 1986 by Chris Langton, and later generalized
in several ways (as we shall see in the next exercises). It was proven in
2000 that the ant's trajectory can be used to compute any boolean circuit,
and thus that the ant is capable of universal computation (ie, any possible
computation can be achieved using the ant as a computing device). Yet
another subject of fascination...

Check the corresponding wikipedia web page, of which this exercise is
inspired, for further details.

