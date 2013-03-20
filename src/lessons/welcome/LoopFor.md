## For loops ##

Warning, this lesson is currently written in Java only. Adapt it yourself

While loops are well adapted to situations where you want to achieve an
action while a condition stays true, but it is less adapted to achieve a
given action a predetermined amount of time. For example, when we wanted to
move steps backward in previous exercise, you had to
create a new variable, initialize it, and move backward until the new
variable became equal to , incrementing the new
variable manually at the end of the loop.

In such situations, loops become handy. Their syntax is the
following: *for (initializing* *;condition* *;incrementing* *) {action*     ();
    }
This code is perfectly equivalent to the following: *initializing* *; while (condition* *) {action* *();incrementing*     ;
    }

In such situations, loops become handy. Their syntax is the
following: *for (initializing* *;condition* *;incrementing* *) {action*     ();
    }
This code is perfectly equivalent to the following: *initializing* *; while (condition* *) {action* *();incrementing*     ;
    }

For example, both following codes are equivalent. The latter is easier to
read, don't you think?     int done = 0;
    while (donestepAmount) {backward();done++;
    }
    for (int done = 0; donestepAmount; done++) {backward();
    }

It is possible to build more advanced loops since any valid
instruction can be used as initialization, condition and incrementation. The
following example is a bit extrem since it compute the gcd (greatest common
divisor) of two numbers without loop body and initialization (everything is
in the condition and incrementation).     

If you don't understand every details of this example, don't panic. That's
quite logic since it uses some syntax details that we did not introduce yet.

### Exercise goal ###

Once done, proceed to next exercise.

