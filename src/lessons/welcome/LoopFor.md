
## For loops ##

<python>

Warning, this lesson is currently written in Java only. Adapt it yourself

</python> 

While loops are well adapted to situations where you want to achieve an
action while a condition stays true, but it is less adapted to achieve a
given action a predetermined amount of time. For example, when we wanted to
move ` stepAmount` steps backward in previous exercise, you had to
create a new variable, initialize it, and move backward until the new
variable became equal to ` stepAmount` , incrementing the new
variable manually at the end of the loop.

In such situations, ` for` loops become handy. Their syntax is the
following: 
<pre> **for (initializing** **;condition** **;incrementing** **) {action** ();
}</pre>
This code is perfectly equivalent to the following: 
<pre> **initializing** **; while (condition** **) {action** **();incrementing** ;
}</pre>

In such situations, ` for` loops become handy. Their syntax is the
following: 
<pre> **for (initializing** **;condition** **;incrementing** **) {action** ();
}</pre>
This code is perfectly equivalent to the following: 
<pre> **initializing** **; while (condition** **) {action** **();incrementing** ;
}</pre>

For example, both following codes are equivalent. The latter is easier to
read, don't you think? 
<pre> int done = 0;
while (donestepAmount) {backward();done++;
}</pre>

<pre> for (int done = 0; donestepAmount; done++) {backward();
}</pre>

It is possible to build more advanced for loops since any valid
instruction can be used as initialization, condition and incrementation. The
following example is a bit extrem since it compute the gcd (greatest common
divisor) of two numbers without loop body and initialization (everything is
in the condition and incrementation). 
<pre> 
<comment> ( int x=20, y=3, tmp;
for (; y!=0 ; tmp=x, x=y, y=tmp%y) { }/* the gcd is stored in x */) </comment> </pre>

If you don't understand every details of this example, don't panic. That's
quite logic since it uses some syntax details that we did not introduce yet.


### Exercise goal ###
You now have to redo the same exercise than previously
(move forward until being over a baggle, pick it up, move back to your
original location, drop the baggle), but using afor loop instead
of awhile

Once done, proceed to next exercise.

