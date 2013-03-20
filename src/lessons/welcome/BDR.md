## Buggle Dance Revolution (BDR) ##

Today is a great day: we will learn the buggles to play Dance Revolution,
this game beloved of some students where the player has to move its feet on
the carpet according to the instructions presented on the screen, and following
the music. But before that, we have some details to study first.

### Conditionals without curly braces ###

There is one detail we omitted about the conditional syntax: if a branch
contains only one instruction, then the curly braces become optional. So,
the two chunk of code are equivalent:

*if (condition* *) {whatToDoIfTheConditionIsTrue();* *} else {whatToDoElse();*     }
*if (condition* *)whatToDoIfTheConditionIsTrue();* *elsewhatToDoElse();*     

But beware, this becomes dangerous if you chain the instructions
like this:

    if (isOverBaggle())
    if (x == 5)
    turnLeft();
    else
    turnRight();
    forward();

In fact, it does not turn right when there is no baggle on the ground AND x
equals 5, but when the buggle found a baggle on the ground and x equals
anything but 5. Putting this otherwise, the buggle understands the previous
code as if it were written the following way (note that the were moved to the right):

    if (isOverBaggle())
    if (x == 5)
    turnLeft();
    else
    turnRight();
    forward();

The first lesson of this is that the indentation is very helpful to help
humans understanding, but it's of no importance for the actual meaning of
the code. We could have written the following code and obtain the same
result. But beware, if you want a human to read and review your code, you
really want to indent it correctly. That's for example the case if you want
a professor to read it (to grade it or to answer a question about it), or if
you want to reuse your code later.

    if (isOverBaggle()) if (x == 5) turnLeft(); else turnRight(); forward();

The second lesson is that a branch always connects to the
closest . This may be a bit troublesome in some case, and it may
be easier to add more braces than strictly needed to remove any ambiguity.

### Chaining conditionals ###

You sometimes want to ask the buggle something similar to:

    if it's raining, take an umbrella;
    if not, and if it's a hot day, take a bottle of water;
    if not and if it's July 4th, take an american flag

The trap is that we want at most one of these actions to be taken. That is
to say, if it's raining a very hot July 4th, we don't want the buggle to get
outside with an umbrella, some water and a flag, but simply with an
umbrella. The following code is thus WRONG.

    if (rainy()) {takeUmbrella();
    }
    if (hot()) {takeWater();
    }
    if (todayIsJuly4th()) {takeFlag();
    }
    if rainy():takeUmbrella()
    if hot():takeWater()
    if todayIsJuly4th():takeFlag()

Indeed, since the conditions are evaluated one after the other, there is a
risk that you go to the July 4th march on a rainy day. Instead, we should
use something like this:

    if (rainy()) {takeUmbrella();
    } else {if (hotDay()) {takeWater();} else {if (todayIsJuly4th()) {takeFlag();}}
    }
    if rainy():takeUmbrella()
    else:if hotDay():takeWater()else:if todayIsJuly4th():takeFlag()

Such a cascade of conditionals are quite difficult to read, and it is better
to omit the curly braces for the statements. Some languages
even introduce a specific construct for these (but Java
doesn't).

    if (rainy()) {takeUmbrella();
    } else if (hotDay()) {takeWater();
    } else if (todayIsJuly4th()) {takeFlag();
    }

Such a cascade of conditionals are quite difficult to read, and it is better
to omit extra indentation for the statements. In Python, there is a specific
construct for this: .     if rainy():takeUmbrella()
    elif hotDay():takeWater()
    elif todayIsJuly4th():takeFlag()
    }
### Graffitis in the Buggle World ###
Buggles can write graffitis on the ground of their world. For that, they use
the four following methods: *    : returns true if and only if there is a
message on the ground.
*    : returns the message written on the ground
(or an empty string if nothing is written).
*    : writes the specified message
down on the ground. If there is already a message on the ground, the new
content is added at the end of the existing message.
*    : clears what is written on the ground.
### Exercise goal ###

In any other case, you should stop

Write the code of the dance in the method which prototype
is already in the editor.

### Indications ###

The first subtlety is that we use the instead of . This method, only known by the
buggles of this exercise, return the first char of the message written on
the ground (or ' ' if nothing is written down).

The other subtlety is to keep working as long as there is some work to do,
i.e., as long as we did not find a cell which content is not described in
the table. The easier for that is to use an infinite loop ( ) with all the tests in the loop body. If we find a cell not
described in the table, we stop everything using a simple .

The functions having as return type can contain some without any associated value. It interrupts immediately
their execution.

### Tips and Hints ###

When your program finally works, move on to the next exercise.

