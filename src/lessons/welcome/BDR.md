
## Buggle Dance Revolution (BDR) ##

Today is a great day: we will learn the buggles to play Dance Revolution,
this game beloved of some students where the player has to move its feet on
the carpet according to the instructions presented on the screen, and following
the music. But before that, we have some details to study first.


<java> 
### Conditionals without curly braces ###
</java> 
<java>

There is one detail we omitted about the conditional syntax: if a branch
contains only one instruction, then the curly braces become optional. So,
the two chunk of code are equivalent:

</java> 
<java> 
<pre> **if (condition** **) {whatToDoIfTheConditionIsTrue();** **} else {whatToDoElse();** }</pre>
</java> 
<java> 
<pre> **if (condition** **)whatToDoIfTheConditionIsTrue();** **elsewhatToDoElse();** </pre>
</java> 
<java>

But beware, this becomes dangerous if you chain the if instructions
like this:

</java> 
<java> 
<pre> if (isOverBaggle())
if (x == 5)
turnLeft();
else
turnRight();
forward();</pre>
</java> 
<java>

In fact, it does not turn right when there is no baggle on the ground AND x
equals 5, but when the buggle found a baggle on the ground and x equals
anything but 5. Putting this otherwise, the buggle understands the previous
code as if it were written the following way (note that the else were moved to the right):

</java> 
<java> 
<pre> if (isOverBaggle())
if (x == 5)
turnLeft();
else
turnRight();
forward();</pre>
</java> 
<java>

The first lesson of this is that the indentation is very helpful to help
humans understanding, but it's of no importance for the actual meaning of
the code. We could have written the following code and obtain the same
result. But beware, if you want a human to read and review your code, you
really want to indent it correctly. That's for example the case if you want
a professor to read it (to grade it or to answer a question about it), or if
you want to reuse your code later.

</java> 
<java> 
<pre> if (isOverBaggle()) if (x == 5) turnLeft(); else turnRight(); forward();</pre>
</java> 
<java>

The second lesson is that a else branch always connects to the
closest if . This may be a bit troublesome in some case, and it may
be easier to add more braces than strictly needed to remove any ambiguity.

</java> 
### Chaining conditionals ###

You sometimes want to ask the buggle something similar to:


<pre> if it's raining, take an umbrella;
if not, and if it's a hot day, take a bottle of water;
if not and if it's July 4th, take an american flag</pre>

The trap is that we want at most one of these actions to be taken. That is
to say, if it's raining a very hot July 4th, we don't want the buggle to get
outside with an umbrella, some water and a flag, but simply with an
umbrella. The following code is thus WRONG.


<java> 
<pre> if (rainy()) {takeUmbrella();
}
if (hot()) {takeWater();
}
if (todayIsJuly4th()) {takeFlag();
}</pre>
</java> 
<python> 
<pre> if rainy():takeUmbrella()
if hot():takeWater()
if todayIsJuly4th():takeFlag()</pre>
</python> 

Indeed, since the conditions are evaluated one after the other, there is a
risk that you go to the July 4th march on a rainy day. Instead, we should
use something like this:


<java> 
<pre> if (rainy()) {takeUmbrella();
} else {if (hotDay()) {takeWater();} else {if (todayIsJuly4th()) {takeFlag();}}
}</pre>
</java> 
<python> 
<pre> if rainy():takeUmbrella()
else:if hotDay():takeWater()else:if todayIsJuly4th():takeFlag()</pre>
</python> 
<java>

Such a cascade of conditionals are quite difficult to read, and it is better
to omit the curly braces for the else statements. Some languages
even introduce a specific construct for these else if (but Java
doesn't).

</java> 
<java> 
<pre> if (rainy()) {takeUmbrella();
} else if (hotDay()) {takeWater();
} else if (todayIsJuly4th()) {takeFlag();
}</pre>
</java> 
<python>

Such a cascade of conditionals are quite difficult to read, and it is better
to omit extra indentation for the else statements. In Python, there is a specific
construct for this: elif . 
<python> 
<pre> if rainy():takeUmbrella()
elif hotDay():takeWater()
elif todayIsJuly4th():takeFlag()
}</pre>
</python> 
### Graffitis in the Buggle World ###
Buggles can write graffitis on the ground of their world. For that, they use
the four following methods:   
  
` boolean isOverMessage()` : returns true if and only if there is a
message on the ground.  
` String readMessage()` : returns the message written on the ground
(or an empty string if nothing is written).  
` void writeMessage(String msg)` : writes the specified message
down on the ground. If there is already a message on the ground, the new
content is added at the end of the existing message.  
` void clearMessage()` : clears what is written on the ground.  

### Exercise goal ###


<table border=1>
	<tr>
		<td > Message The goal is then to organize a BDR game between the
buggles by learning them to move according to the instructions written on
the ground. These instructions are messages written on the ground, with the
following signification:</td>
		<td > What to do </td>
		<td > Mnemonic </td>
	</tr>
	<tr>
		<td > R </td>
		<td > Turn right and move one step forward </td>
		<td > Right </td>
	</tr>
	<tr>
		<td > L </td>
		<td > Turn left and move one step forward </td>
		<td > Left </td>
	</tr>
	<tr>
		<td > I </td>
		<td > Turn back (U-turn) and move one step forward </td>
		<td > Inverse </td>
	</tr>
	<tr>
		<td > A </td>
		<td > Move one step forward </td>
		<td > First letter of the alphabet </td>
	</tr>
	<tr>
		<td > B </td>
		<td > Move two steps forward </td>
		<td > Second letter of the alphabet </td>
	</tr>
	<tr>
		<td > C </td>
		<td > Move three steps forward </td>
		<td > Third letter of the alphabet </td>
	</tr>
	<tr>
		<td > Z </td>
		<td > Move one step backward </td>
		<td > One letter before the end of the alphabet </td>
	</tr>
	<tr>
		<td > Y </td>
		<td > Move two steps backward </td>
		<td > Two letters before the end of the alphabet </td>
	</tr>
	<tr>
		<td > X </td>
		<td > Move three steps backward </td>
		<td > Three letters before the end of the alphabet </td>
	</tr>
</table>

In any other case, you should stop

Write the code of the dance in the ` run()` method which prototype
is already in the editor.


### Indications ###

The first subtlety is that we use the ` This exercise may seem a bit complex at the first glance, but it comes down
to summarizing the information above in a sequence of conditionals.char getIndication()` instead of ` String readMessage()` . This method, only known by the
buggles of this exercise, return the first char of the message written on
the ground (or ' ' if nothing is written down).

The other subtlety is to keep working as long as there is some work to do,
i.e., as long as we did not find a cell which content is not described in
the table. The easier for that is to use an infinite loop ( ` while
(true)` ) with all the tests in the loop body. If we find a cell not
described in the table, we stop everything using a simple ` return;` .

The functions having ` void` as return type can contain some return without any associated value. It interrupts immediately
their execution.


### Tips and Hints ###
` If you fail understanding why the buggle does not execute the expected
steps, try addingbrushDown()`

When your program finally works, move on to the next exercise.

