## Methods returning a result ##
Writing a method returning a result is not really more work than writing a
method without any result. You simply have to specify the data type of
expected results before the method name, and then write a instruction in your method body to specify the actual
value to return.     double pi() {return 3.14159;
    }
    boolean isNumberTwoEven() {return true;
    }

It is possible to have several instructions in several
branches of a conditional. It is even forbiden to have one execution path of
your body without any , or to write some code after the instruction.

Indeed, if the machine reaches the end of the method without finding any , it cannot know what actual value to give back to the
method caller. Moreover, interrupts immediately the
method execution (why bother looking further when you know the method
result?). So, if there is some code after a , it must be
an error and the compiler warns you.

    }
### Exercise goal ###
You will once again write a method that the buggle will use. Its name must
be , and it returns a boolean value indicating
whether the row in front of the buggle contains a baggle or not. The buggle
will use it to search the first row containing a baggle, and stop here.

The easier for this method is to use a boolean variable called indicating whether or not we saw a baggle so far. It
initial value is 'false'.

Then, move 6 steps forward (the world contains 7 cells and we already are
one one of them). For each cell, if it contains a baggle, we store true in (and we don't do anything but moving forward if not).

At the end, we move back by 6 steps, and we return the value of to the caller.

This exercise is a bit different since there is two initial worlds, each
with a specific objective. Your code must work for each of them. Observe
that the world selection scrolling menu (right below the speed slider)
allows to switch the observed world.

When your method works, proceed to next exercise.

