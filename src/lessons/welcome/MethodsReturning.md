
## Methods returning a result ##
Writing a method returning a result is not really more work than writing a
method without any result. You simply have to specify the data type of
expected results before the method name, and then write a ` return` instruction in your method body to specify the actual
value to return. 
<pre> double pi() {return 3.14159;
}
boolean isNumberTwoEven() {return true;
}</pre>

It is possible to have several ` return` instructions in several
branches of a conditional. It is even forbiden to have one execution path of
your body without any ` return` , or to write some code after the ` return` instruction.

Indeed, if the machine reaches the end of the method without finding any ` return` , it cannot know what actual value to give back to the
method caller. Moreover, ` return` interrupts immediately the
method execution (why bother looking further when you know the method
result?). So, if there is some code after a ` return` , it must be
an error and the compiler warns you.


<pre> 
<comment> ( boolean negation(boolean cond) {if (cond == true) {return true;/* no code allowed here */) </comment> 
<comment> ( } else {return false;/* here neither */) </comment> 
<comment> ( }/* even here, forget it */) </comment> }</pre>

### Exercise goal ###
You will once again write a method that the buggle will use. Its name must
be ` haveBaggle` , and it returns a boolean value indicating
whether the row in front of the buggle contains a baggle or not. The buggle
will use it to search the first row containing a baggle, and stop here.

The easier for this method is to use a boolean variable called ` seenBaggle` indicating whether or not we saw a baggle so far. It
initial value is 'false'.

Then, move 6 steps forward (the world contains 7 cells and we already are
one one of them). For each cell, if it contains a baggle, we store true in ` sawBaggle` (and we don't do anything but moving forward if not).

At the end, we move back by 6 steps, and we return the value of ` seenBaggle` to the caller.

This exercise is a bit different since there is two initial worlds, each
with a specific objective. Your code must work for each of them. Observe
that the world selection scrolling menu (right below the speed slider)
allows to switch the observed world.

When your method ` haveBaggle` works, proceed to next exercise.

