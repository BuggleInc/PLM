
## Buggle Dance Revolution 2 ##

### (BDR2) ###
BDR is cool, but it's a bit chaotic. First, the buggles giggle in any
directions, and then the code you had to write to let them move is rather
difficult to read. Here is a new BDR world where the buggle will dance a
gentle circle. We will benefit this tranquillity to clean up a bit our code
thanks to the new constructs we will introduce. 
### switch conditionals ###
The hardest part of previous code is certainly the conditional
cascading. Somewhere in your code, you certainly had something similar to: 
<pre> if (getIndication() == 'R') {
turnRight();
forward();
} else if (getIndication() == 'L') {
turnLeft();
forward();
} else if (getIndication() == 'I') {
turnBack();
forward();
/* other else if */
} else {
return;
}</pre>
When you review this code, it may not be clear at the first glance that it
is simply a choice with 4 branches depending on the value of
getIndication(). To improve this, we will use a ` switch` construct, which Java syntax is the following: 
<pre> **switch (expression** **) { casefirstValue** **:whatToDoIfExpressionEqualsFirstValue();** **break; casesecondValue** **:whatToDoIfExpressionEqualsSecondValue();** **break; casethirdValue** **:whatToDoIfExpressionEqualsThirdValue();** **break; /* as much similar cases as you want */ default:whatToDoIfExpressionDoesNotEqualsAnySeenValues();** }</pre>
Observe that each branch of a switch must be ended by a ` break` . If you forget this, the machine keeps going and execute
the next branch in the list after the branch it jumped to. There is even
some **rare** cases where this behavior reveals helpful.

It is then possible to rewrite previous BDR code in a cleaner way using the switch construct:


<pre> switch (getIndication()) {
case 'R':
turnRight();
forward();
break;
case 'L':
turnLeft();
forward();
break;
case 'U':
turnBack();
forward();
break;
default:
return;
}</pre>

## Variables shared between methods ##
Another issue in your code is that it begins to be a bit long to be written
as a single method. We would like to split it up in two methods:   
  
*  ` danceOneStep()` would take care of achieving a single dance step  
*  ` run()` would take care of the dance as a whole. It would do the
steps while we didn't encounter a cell not asking any further move.  

The difficulty is to make sure that danceOneStep() keeps run() informed that there is no further dance step to achieve. The
simpler solution is to have a boolean function visible from both methods
indicating whether there is more steps to do or if we're done. For that, we
have to write out the following of any method: 
<pre> boolean moreMusic = true;</pre>

Note that it is possible to write variable declarations out of any methods,
but that instructions must be in a method. In Java such *global* variables are called **fields** .

Then, the danceOneStep() must be changed to update this variable to false when there is nothing more to do. For that, simply add ` moreMusic = false;` before any return .

It is then possible to use the following run() method: 
<pre> public void run() {
while (moreMusic)
danseOneStep();
}</pre>

### Exercise goal ###

You don't have to write the Apply the improvement we just saw to rewrite your buggle
code.run() method since the buggle already
know it. If you put it anyway, the compiler will complain about this
multiple definition without noticing that both declarations match. Simply
declare the variable moreMusic and the danceOneStep() method.

Here are the ground indications to use for BRD2. Note that we can now move a
bugle up to 6 cells in one dance step. 

<table border=1>
	<tr>
		<td > Message </td>
		<td > What to do </td>
	</tr>
	<tr>
		<td > R </td>
		<td > Turn right and move one step forward </td>
	</tr>
	<tr>
		<td > L </td>
		<td > Turn left and move one step forward </td>
	</tr>
	<tr>
		<td > I </td>
		<td > Turn back and move one step forward </td>
	</tr>
	<tr>
		<td > A </td>
		<td > Move one step forward </td>
	</tr>
	<tr>
		<td > B </td>
		<td > Move two steps forward </td>
	</tr>
	<tr>
		<td > C </td>
		<td > Move three steps forward </td>
	</tr>
	<tr>
		<td > D </td>
		<td > Move four cells forward </td>
	</tr>
	<tr>
		<td > E </td>
		<td > Move five cells forward </td>
	</tr>
	<tr>
		<td > F </td>
		<td > Move six cells forward </td>
	</tr>
	<tr>
		<td > Z </td>
		<td > Move one step backward </td>
	</tr>
	<tr>
		<td > Y </td>
		<td > Move two steps backward </td>
	</tr>
	<tr>
		<td > X </td>
		<td > Move three steps backward </td>
	</tr>
	<tr>
		<td > W </td>
		<td > Move four cells backward </td>
	</tr>
	<tr>
		<td > V </td>
		<td > Move five cells backward </td>
	</tr>
	<tr>
		<td > U </td>
		<td > Move six cells backward </td>
	</tr>
</table>

In any other case, you should stop

When you program works again, proceed to next exercise.

