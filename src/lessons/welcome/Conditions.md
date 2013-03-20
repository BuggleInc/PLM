## Conditional instructions ##
Programs made of simple suite of instructions similar to previous exercise
are quite boring. They always do the same thing, and cannot react to
external conditions. A *conditional* let the program adapt by doing
something like .

The Java syntax is the following:

The Python syntax is the following:

*if (condition* *) {whatToDo();*     }
*ifcondition* *:whatToDo()*     

If the condition is true, any code enclosed between the { and the
corresponding } will be executed. If not, it will be ignored. Of course, it
is possible to write more than one instruction between the curly brackets
(even another test).

If the condition is true, any code in the block following the
colon symbol will be executed. If not, it will be ignored. Of course, it
is possible to write more than one instruction in the sub-block
(even another test).

Python uses indentation to define code blocks. The standard Python indentation
is 4 spaces. Notice that code blocks do not need any termination. Indenting starts
a block and unindenting ends it. In the
following code the instructions *whatToDo()* and *whatToDoNext()* will be exectuded if the condition is true, then the instruction *whatToDoAnyway()* will be executed anyway. *ifcondition* *:whatToDo()* *whatToDoNext()* *whatToDoAnyway()*     

Python functions have no explicit begin or end, and
no curly braces to mark where the function code starts and stops. The
only delimiter is a colon (:) and the indentation of the code
itself.

Example 2.5. Indenting the buildConnectionString
Function

    def buildConnectionString(params):
    """Build a connection string from a dictionary of parameters.
    
    Returns string."""
    return ";".join(["%s=%s" % (k, v) for k, v in params.items()])

Code blocks are defined by their indentation. By
"code block", I mean functions, if statements, for loops, while loops,
and so forth. Indenting starts a block and unindenting ends it. There
are no explicit braces, brackets, or keywords. This means that
whitespace is significant, and must be consistent. In this example,
the function code (including the doc string) is indented four spaces.
It doesn't need to be four spaces, it just needs to be consistent. The
first line that is not indented is outside the function.

It is important that the indentations of all the
instructions of a block are consistent, and it is not possible to cut
a block. The two following codes are incorrect and will raise
errors.

*ifcondition* *:whatToDo()* *whatToDoNext()* *whatToDoAnyway()*     
*ifcondition* *:whatToDo()* *whatToDoAnyway()* *whatToDoNext()*     

A condition can be a variable of type . The code between
curly braces will get executed if the variable is and it will
be ignored if it is .

A condition can be a variable of type . The code in the inner bloc will get executed if the
variable is and it will be ignored if it is .

The condition can also be an arithmetic test, such as *==* , which checks whether the current value of is 5, or
such as *!=* (checking inequality), ** (smaller than), ** (larger than), *=* (smaller or equal to), *=* (larger or equal to).

Beware of the classical trap, which consists in testing the equality of a
variable using = instead of ==. Hopefully, the compiler detects this
problem most of the time, but not always. If the variable is of type
boolean, it can get trapped, so you have to be careful...

The condition can also be a call to some perticular methods returning a
boolean. For example, the method of the buggle
returns true if the buggle is facing a wall, and false in the other case.

Finally, a condition can be composed of several sub-conditions connected by
boolean operations. *and* *    is not even evaluated).
*or* *    is not even evaluated).
*    is false.
*    It is possible to force the order of evaluation by adding parenthesis. In
ambigous cases, do not hesitate to add more parenthesis to remove any
ambiguities on evaluation order.
*and* *    is not even evaluated).
*or* *    is not even evaluated).
*    is false.
*    It is possible to force the order of evaluation by adding parenthesis. In
ambigous cases, do not hesitate to add more parenthesis to remove any
ambiguities on evaluation order.

Finally, it is possible to specify what to do when the condition is false
using the following syntax: *if (condition* *) {whatToDoIfTheConditionIsTrue();* *} else {whatToDoIfItsFalse();*     }
*if (condition* *):whatToDoIfTheConditionIsTrue()* *else:whatToDoIfItsFalse()*     

Don't forget the colon (:) after the else, it is indicating
that a new block is beginning.

### Exercise goal ###

This exercise is a bit different: your code has to work for several buggles,
each of them being in a specific initial condition. The same code will be
executed for each of them.

When your program works, move forward to the next exercise.

